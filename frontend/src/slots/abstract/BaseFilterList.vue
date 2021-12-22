<template>
  <ul v-if="isListRenderable">
    <li class="filter" v-for="(value, filter) in filters" v-bind:key="value">
      <div class="filter-text">{{ filter }}: {{ value }}</div>
      <button v-on:click="deactivateFilter(filter)" class="close-button">
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
    </li>
  </ul>
</template>

<script>
export default {
  name: "BaseFilterList",

  props: {
    filters: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },

  emits: ["deactivateFilter"],

  computed: {
    isListRenderable() {
      return Object.keys(this.filters).length > 0;
    },
  },

  methods: {
    deactivateFilter(key) {
      this.$emit("deactivateFilter", key);
    },
  },
};
</script>

<style scoped>
ul {
  display: flex;
  gap: 1.6rem;
  align-items: center;
  justify-content: flex-start;
}

li {
  list-style: none;
  display: flex;
  gap: 0.8rem;
  align-items: center;
  justify-content: space-between;

  font-size: 1.8rem;

  background-color: var(--main-color);
  color: var(--background-color);
  font-weight: bold;
  padding: 0.2rem 0.8rem 0.2rem 0.8rem;
  border-radius: 100rem;
}

.filter-text {
  display: inline-block;
}

.close-button {
  padding: 0;
  width: 1.8rem;
  height: 1.8rem;
  border: 2px solid var(--background-color);
  border-radius: 50%;
  background: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.close-icon {
  stroke: var(--background-color);

  width: 1.4rem;
  height: 1.4rem;
}
</style>
