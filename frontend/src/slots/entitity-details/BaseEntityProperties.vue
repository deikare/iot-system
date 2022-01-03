<template>
  <base-card>
    <template v-slot:header> Properties </template>

    <template v-slot:default>
      <entities-error v-if="isError"></entities-error>
      <loading-spinner v-else-if="displayLoading"></loading-spinner>

      <div class="properties-modifier" v-else-if="displayProperties">
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
        </div>

        <div class="control-buttons" v-if="displayButtons">
          <button class="submit-button" v-on:click="reset">Reset</button>
          <button class="submit-button" v-on:click="submit">Update</button>
        </div>
      </div>
    </template>
  </base-card>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import LoadingSpinner from "@/components/LoadingSpinner";
import EntitiesError from "@/slots/abstract/EntitiesError";

export default {
  name: "BaseEntityProperties",
  components: { EntitiesError, LoadingSpinner, BaseCard },

  data() {
    return {
      newValues: this.entityProperties.map((property) => {
        return { label: property.label, value: property.initialValue };
      }),
    };
  },

  props: {
    isLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },
    isError: {
      type: Boolean,
      required: true,
      default: false,
    },
    entityProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  emits: ["changeProperties"],

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },

    displayProperties() {
      return this.isLoaded;
    },

    displayButtons() {
      let areNewValues = false;
      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (
          proxy.value !== undefined &&
          proxy.value !== null &&
          proxy.value !== this.entityProperties[Number(key)].initialValue
        ) {
          areNewValues = true;
          break;
        }
      }
      return !this.isError && this.isLoaded && areNewValues;
    },
  },

  methods: {
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
        this.$emit("changeProperties", newData);
    },

    reset() {
      for (const key in Object.keys(this.newValues))
        this.newValues[key].value =
          this.entityProperties[Number(key)].initialValue;
    },
  },

  watch: {
    //workaround for bugged reactivity
    isLoaded(newValue, oldValue) {
      if (newValue === true && oldValue === false) this.reset();
    },
  },
};
</script>

<style scoped>
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
}

.submit-button {
  align-self: center;
}
</style>
