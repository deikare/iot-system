<template>
  <base-shadowed-card class="modifier">
    <template v-slot:default>
      <header class="modifier-header header-margin">
        <h2 class="header-text">Update <slot name="entityType"></slot></h2>
        <button class="close-button" v-on:click="emitCloseComponent">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="close-icon"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            />
          </svg>
        </button>
      </header>

      <loading-spinner v-if="isPatcherLoadingVisible"></loading-spinner>

      <entities-error v-else-if="isError"></entities-error>
      <loading-spinner v-else-if="!isEntityLoaded"></loading-spinner>

      <div
        class="properties-modifier default-margin"
        v-else-if="isEntityLoaded"
      >
        <div
          class="property-container"
          v-for="(property, index) in getProperties"
          v-bind:key="property.label"
        >
          <div class="property-label">{{ property.label }}:</div>

          <input
            class="property-modifier text-modifier"
            type="text"
            v-if="property.type === `text`"
            v-model="newValues[index].value"
            size="15"
          />
          <select
            class="property-modifier select-modifier"
            v-else-if="property.type === `select`"
            v-model="newValues[index].value"
          >
            <option disabled value="">Please select one</option>
            <option
              v-for="option in property.options"
              v-bind:key="option.value"
              v-bind:value="option.value"
            >
              {{ option.label }}
            </option>
          </select>
        </div>

        <div class="control-buttons" v-if="areControlButtonsVisible">
          <button class="update-button" v-on:click="submit">Update</button>
        </div>
      </div>
    </template>
  </base-shadowed-card>
</template>

<script>
import BaseShadowedCard from "@/slots/abstract/BaseShadowedCard";
import LoadingSpinner from "@/components/LoadingSpinner";
import { mapActions, mapState } from "vuex";
import EntitiesError from "@/slots/abstract/EntitiesError";
export default {
  name: "EntityModifier",
  components: { EntitiesError, LoadingSpinner, BaseShadowedCard },

  data() {
    return {
      newValues: [],
      areEmptyErrorsVisible: false,
      hasEntityCreationStarted: false,
      hasEntityCreationEnded: false,
      isEntityLoaded: false,
      isError: false,
    };
  },

  props: {
    id: {
      type: String,
      required: true,
      default: "",
    },
    transactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          namespace: "",
          getters: {
            entity: "",
          },
          actions: {
            loader: "",
            patcher: "",
          },
        };
      },
    },
  },

  emits: ["entityUpdated", "closeComponent"],

  computed: {
    ...mapState({
      getProperties(state, getters) {
        return getters[
          `${this.transactionMappings.namespace}/${this.transactionMappings.getters.entity}`
        ];
      },
    }),

    areNewValuesEmpty() {
      return this.newValues.map((newValue) => newValue.value.length === 0);
    },

    isPatcherLoadingVisible() {
      return (
        this.hasEntityCreationStarted === true &&
        this.hasEntityCreationEnded === false
      );
    },

    areControlButtonsVisible() {
      let areNewValues = false;
      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (
          proxy.value !== undefined &&
          proxy.value !== null &&
          proxy.value !== this.getProperties[Number(key)].initialValue
        ) {
          areNewValues = true;
          break;
        }
      }
      return !this.isError && this.isEntityLoaded && areNewValues;
    },
  },

  methods: {
    ...mapActions({
      loadEntity(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.namespace}/${this.transactionMappings.actions.loader}`,
          payload
        );
      },

      patchEntity(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.namespace}/${this.transactionMappings.actions.patcher}`,
          payload
        );
      },
    }),

    submit() {
      let newData = {};

      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (proxy.value !== undefined && proxy.value !== null) {
          newData[proxy.label] = proxy.value;
        }
      }

      if (
        !(
          newData &&
          Object.keys(newData).length === 0 &&
          Object.getPrototypeOf(newData) === Object.prototype
        )
      )
        this.updateEntityAndShowLoadingBar(newData);
    },

    initializeNewValues() {
      this.newValues = this.getProperties.map((property) => {
        return { label: property.label, value: property.initialValue };
      });
    },

    fetchEntity() {
      this.isEntityLoaded = false;
      this.isError = false;

      this.loadEntity({
        id: this.id,
        size: 10,
        ifSuccessHandler: () => {
          this.initializeNewValues();
          this.isEntityLoaded = true;
          this.isError = false;
        },
        ifErrorHandler: () => {
          this.isEntityLoaded = true;
          this.isError = true;
        },
      });
    },

    updateEntityAndShowLoadingBar(entity) {
      this.hasEntityCreationStarted = true;
      this.hasEntityCreationEnded = false;

      console.log(entity);

      const payload = {
        data: entity,
        id: this.id,
        ifSuccessHandler: () => {
          this.hasEntityCreationStarted = false;
          this.hasEntityCreationEnded = true;
          this.emitEntityUpdated();
          this.emitCloseComponent();
        },
        ifErrorHandler: () => {
          this.hasEntityCreationStarted = true;
          this.hasEntityCreationEnded = true;
        },
      };

      this.patchEntity(payload);
    },

    emitCloseComponent() {
      this.$emit("closeComponent");
    },

    emitEntityUpdated() {
      this.$emit("entityUpdated");
    },
  },

  created() {
    this.$watch(
      () => this.id,
      () => {
        this.fetchEntity();
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.modifier {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.default-margin {
  margin: 0 1.2rem;
}

.modifier-header {
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px solid var(--underline-color);
  margin: 0.4rem 1.2rem 0;
}

.header-text {
  margin: 0;
}

.close-button {
  width: 3.6rem;
  height: 3.6rem;

  margin-left: auto;

  display: flex;
  align-items: center;
  justify-content: center;

  background: transparent;
  border: 2px solid transparent;
  border-radius: 50%;
}

.close-button:hover,
.close-button:active {
  border: 2px solid var(--main-color);
  cursor: pointer;
}

.close-icon {
  background: transparent;
  stroke: var(--main-color);

  width: 2.4rem;
  height: 2.4rem;
}

.properties-modifier {
  padding-left: 0.2rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  gap: 0.4rem;
  padding-bottom: 0.2rem;
}

.property-container {
  display: flex;
  gap: 0.8rem;
  align-items: center;
  justify-content: center;
}

.property-label {
  text-transform: capitalize;
}

.property-modifier {
  font-size: 1.6rem;
  color: var(--main-color);
}

.text-modifier {
  background: transparent;
  border: none;

  color: var(--main-color);
}

.text-modifier:focus {
  outline-width: 0;
  border-bottom: 2px solid var(--main-color);
}

.select-modifier {
  background: transparent;
  border: 2px solid var(--main-color);
}

.control-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  margin-bottom: 0.4rem;
  size: 1.6rem;
}

.update-button {
  font-size: 1.4rem;
  color: var(--main-color);
}
</style>
