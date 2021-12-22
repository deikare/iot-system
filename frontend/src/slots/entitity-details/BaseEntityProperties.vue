<template>
  <base-card>
    <template v-slot:header>
      <div class="flexbox">
        <div class="inline">Properties</div>

        <!--        TODO add routing to editing entity-->
        <router-link to="">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="edit-button"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"
            />
          </svg>
        </router-link>
      </div>
    </template>

    <template v-slot:default>
      <entities-error v-if="isError"></entities-error>
      <loading-spinner v-else-if="displayLoading"></loading-spinner>
      <ul v-else-if="displayProperties">
        <li v-for="property in propertiesAsArray" v-bind:key="property.key">
          <div class="to-uppercase">{{ property.key }}</div>
          : {{ property.value }}
        </li>
      </ul>
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

  methods: {},
};
</script>

<style scoped>
.flexbox {
  display: flex;
  gap: 0.8rem;
  align-items: center;
  justify-content: center;
}

.inline {
  display: inline-block;
  color: var(--background-color);
}

.to-uppercase {
  display: inline-block;
  text-transform: capitalize;
}

.edit-button {
  stroke: var(--background-color);
  height: 2.4rem;

  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
