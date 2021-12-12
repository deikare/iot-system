<template>
  <!--  TODO this component should return event to higher component,
  which should return whole query to the top-->
  <ul v-if="isListRenderable">
    <li class="filter" v-for="filter in activeFilters" v-bind:key="filter.id">
      <div class="filter-text">{{ filter.key }}: {{ filter.value }}</div>
      <button v-on:click="deactivateFilter(filter.id)">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="close-button"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
          />
        </svg>
      </button>
    </li>
  </ul>
</template>

<script>
export default {
  name: "BaseFilterList",

  props: {
    filters: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  emits: ["deactivateFilter"],

  computed: {
    isListRenderable() {
      return this.filters.length > 0;
    },

    activeFilters() {
      return this.filters.filter((element) => element.isActive);
    },
  },

  methods: {
    deactivateFilter(id) {
      this.$emit("deactivateFilter", id);
    },
  },
};
</script>

<style scoped></style>
