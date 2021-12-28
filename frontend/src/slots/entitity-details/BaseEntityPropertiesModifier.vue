<template>
  <form class="properties-modifier" v-on:submit.prevent="submit">
    <div
      class="property-container"
      v-for="(property, index) in entityProperties"
      v-bind:key="property.label"
    >
      <div class="property-label">{{ property.label }}:</div>

      <input
        class="property-modifier"
        type="text"
        v-if="property.type === `text`"
        v-model="newValues[index].value"
      />
      <select
        class="property-modifier"
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

    <button class="submit-button">Submit</button>
  </form>
</template>

<script>
export default {
  name: "BaseEntityPropertiesModifier",

  data() {
    return {
      newValues: this.entityProperties.map((property) => {
        return { label: property.label, value: property.initialValue };
      }),
    };
  },

  props: {
    entityProperties: {
      type: Array,
      required: true,
      default() {
        return [
          {
            type: "", //text or select
            initialValue: "", //value from entity
            label: "", //e.g. name, status
            //if select, then options are also required in pairs {label, value}
          },
        ];
      },
    },
  },

  methods: {
    submit() {
      let newData = {};

      console.log(this.newValues);

      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (
          proxy.value !== undefined &&
          proxy.value !== null &&
          proxy.value !== this.entityProperties[Number(key)].initialValue
        ) {
          newData[proxy.label] = proxy.value;
        }
      }

      console.log("test", newData);
    },
  },

  created() {
    console.log("test", this.newValues);
  },

  watch: {
    newValues() {
      console.log("change", this.newValues);
    },
  },
};
</script>

<style scoped>
.properties-modifier {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.property-container {
  display: flex;
  gap: 0.8rem;
  align-items: center;
  justify-content: flex-start;
}

.property-label {
  text-transform: capitalize;
}

.property-modifier {
  font-size: 1.6rem;
  color: var(--main-color);
}

.submit-button {
  align-self: center;
}
</style>
