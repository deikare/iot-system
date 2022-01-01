<template>
  <div class="creator">
    <header class="creator-header header-margin">
      <h2 class="header-text">Add new <slot name="entityType"></slot></h2>
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

    <div class="properties-modifier default-margin">
      <div
        class="property-container"
        v-for="(property, index) in entityProperties"
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
        <empty-field-error
          v-if="areNewValuesEmpty[index] && areEmptyErrorsVisible"
        ></empty-field-error>
      </div>
    </div>

    <div class="parents-selector-container">
      <div v-bind:class="parentsSelectorStyle">
        <div
          v-on:click="displayParents"
          v-bind:class="parentsSelectorHeaderStyle"
        >
          <span v-bind:class="parentsSelectorHeaderTextStyle">
            <slot name="parentType"></slot> id: {{ newParentId }}
          </span>
          <div class="arrow-container" v-if="!areParentsVisible">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="arrow-icon"
              v-bind:class="parentsSelectorHeaderIconStyle"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 9l-7 7-7-7"
              />
            </svg>
          </div>

          <div class="arrow-container" v-if="areParentsVisible">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="arrow-icon"
              v-bind:class="parentsSelectorHeaderIconStyle"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M5 15l7-7 7 7"
              />
            </svg>
          </div>
        </div>

        <entity-list-with-paginator
          class="parents-list"
          v-show="areParentsVisible"
          v-bind:buttons-properties="buttonsProperties"
          v-bind:entities-properties="getParentsProperties"
          v-bind:page="getParentsPage"
          v-bind:is-error="areParentsError"
          v-bind:is-loaded="areParentsLoaded"
          v-on:changePage="changeParentsPage"
          v-on:entityClicked="setNewParentId"
        ></entity-list-with-paginator>
      </div>
      <empty-field-error
        v-bind:class="parentsSelectorErrorStyle"
        v-if="isNewParentEmpty && areEmptyErrorsVisible"
      ></empty-field-error>
    </div>

    <div class="control-buttons">
      <button class="create-button" v-on:click="submit">Create</button>
    </div>
  </div>
</template>

<script>
import EntityListWithPaginator from "@/components/entity-list/EntityListWithPaginator";
import { mapState, mapActions } from "vuex";
import EmptyFieldError from "@/slots/entities-creator/EmptyFieldError";

export default {
  name: "EntityCreator",
  components: { EmptyFieldError, EntityListWithPaginator },
  data() {
    return {
      newValues: this.entityProperties.map((property) => {
        return { label: property.label, value: property.initialValue };
      }),
      buttonsProperties: {
        isEditVisible: false,
        isDeleteVisible: false,
      },
      areParentsLoaded: false,
      areParentsError: false,
      areEmptyErrorsVisible: false,
      newParentId: this.initialParentId,
      areParentsVisible: this.initialParentId === "",
    };
  },

  props: {
    entityProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
    initialParentId: {
      type: String,
      required: false,
      default: "",
    },
    parentsTransactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          namespace: "",
          getters: {
            entities: "",
            page: "",
          },
          actions: {
            loader: "",
          },
        };
      },
    },
  },

  emits: ["newEntity", "closeComponent"],

  computed: {
    ...mapState({
      getParentsProperties(state, getters) {
        return getters[
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.getters.entities}`
        ];
      },
      getParentsPage(state, getters) {
        return getters[
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.getters.page}`
        ];
      },
    }),

    parentsSelectorStyle() {
      return this.areParentsVisible
        ? "parent-selector-clicked"
        : "parent-selector";
    },

    parentsSelectorHeaderStyle() {
      return this.areParentsVisible
        ? "parent-selector-header-clicked"
        : "parent-selector-header";
    },

    parentsSelectorHeaderTextStyle() {
      return this.areParentsVisible
        ? "parent-selector-header-text-clicked"
        : "parent-selector-header-text";
    },

    parentsSelectorHeaderIconStyle() {
      return this.areParentsVisible ? "arrow-icon-clicked" : "arrow-icon";
    },

    parentsSelectorErrorStyle() {
      return this.areParentsVisible ? "empty-field-error-clicked" : "";
    },

    areNewValuesEmpty() {
      return this.newValues.map((newValue) => newValue.value.length === 0);
    },

    isNewParentEmpty() {
      return this.newParentId.length === 0;
    },
  },

  methods: {
    ...mapActions({
      loadParents(dispatch, payload) {
        return dispatch(
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.actions.loader}`,
          payload
        );
      },
    }),

    displayParents() {
      this.areParentsVisible = !this.areParentsVisible;
    },

    submit() {
      let newData = {};

      let isAnyFieldEmpty = false;

      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (proxy.value !== undefined && proxy.value !== null) {
          if (proxy.value !== "") newData[proxy.label] = proxy.value;
          else {
            isAnyFieldEmpty = true;
            break;
          }
        }
      }

      if (isAnyFieldEmpty) this.areEmptyErrorsVisible = true;
      else if (
        !(
          newData &&
          Object.keys(newData).length === 0 &&
          Object.getPrototypeOf(newData) === Object.prototype
        )
      ) {
        console.log({
          entity: newData,
          parentId: this.newParentId,
        });
        this.$emit("newEntity", {
          entity: newData,
          parentId: this.newParentId,
        });
      }
    },

    validateData() {},

    //TODO make getEntities as one implementation in store
    //TODO saving parent

    reset() {
      for (const key in Object.keys(this.newValues))
        this.newValues[key].value =
          this.entityProperties[Number(key)].initialValue;
    },

    fetchParents(page) {
      this.areParentsLoaded = false;
      this.areParentsError = false;

      this.loadParents({
        queryParams: { page: page },
        ifSuccessHandler: () => {
          this.areParentsLoaded = true;
          this.areParentsError = false;
        },
        ifErrorHandler: () => {
          this.areParentsLoaded = true;
          this.areParentsError = true;
        },
      });
    },

    changeParentsPage(page) {
      this.fetchParents(page - 1);
    },

    setNewParentId(newParentId) {
      this.newParentId = newParentId;
      this.areParentsVisible = false;
    },

    emitCloseComponent() {
      this.$emit("closeComponent");
    },
  },

  created() {
    this.$watch(
      () => this.id,
      () => {
        this.fetchParents(0);
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.creator {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;

  background: var(--toast-color);
  border-radius: 5px;

  padding: 0.2rem 1.2rem;

  box-shadow: 0 2rem 3rem 0 rgba(18, 20, 40, 0.1);
  border: 2px solid var(--main-color);
}

.default-margin {
  margin: 0 1.2rem;
}

.creator-header {
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

.parents-selector-container {
  display: flex;
  gap: 0.4rem;
  align-items: center;
}

.parent-selector {
  margin: 0 1.2rem;
  border-radius: 5px;
  border: 1px solid transparent;
}

.parent-selector:hover {
  border: 1px solid var(--main-color);
}

.parent-selector-clicked {
  margin: 0 1.2rem;
  border-radius: 5px;
  border: 1px solid var(--main-color);
}

.parent-selector-header {
  padding: 0 0.2rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}

.parent-selector-header:hover {
  /*background-color: var(--card-color);*/
}

.parent-selector-header-clicked {
  padding: 0 0.2rem;
  background-color: var(--main-color);
  /*padding: 0 1.2rem;*/
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
}

.parent-selector-header-text-clicked {
  color: var(--background-color);
  /*font-size: 2rem;*/
}

.arrow-container {
  height: 3rem;
  width: 3rem;
  margin-left: auto;

  display: flex;
  align-items: center;
  justify-content: center;
}

.arrow-icon {
  height: 2.4rem;
  width: 2.4rem;
  stroke: var(--main-color);
}

.arrow-icon-clicked {
  height: 2.4rem;
  width: 2.4rem;
  stroke: var(--background-color);
}

.empty-field-error-clicked {
  align-self: flex-start;
}

.parents-list {
  background: var(--card-color);
}

.control-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  margin-bottom: 0.4rem;
  size: 1.6rem;
}

.create-button {
  font-size: 1.4rem;
}
</style>
