<template>
  <base-grid-card>
    <template v-slot:header>Properties</template>

    <template v-slot:default>
      <loading-spinner
        v-if="displayLoading"
        v-bind:active="displayLoading"
      ></loading-spinner>
      <ul v-else-if="displayProperties">
        <li v-for="property in propertiesAsArray" v-bind:key="property.key">
          <div class="to-uppercase">{{ property.key }}</div>
          : {{ property.value }}
        </li>
      </ul>
    </template>
  </base-grid-card>
</template>

<script>
import BaseGridCard from "@/slots/BaseGridCard";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "BaseEntityProperties",
  components: { LoadingSpinner, BaseGridCard },
  props: {
    isLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },
    properties: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },

    displayProperties() {
      return this.isLoaded;
    },

    propertiesAsArray() {
      const result = [];
      for (const [key, value] of Object.entries(this.properties)) {
        result.push({ key: key, value: value });
      }
      return result;
    },
  },
};
</script>

<style scoped>
.to-uppercase {
  display: inline-block;
  text-transform: capitalize;
}
</style>
