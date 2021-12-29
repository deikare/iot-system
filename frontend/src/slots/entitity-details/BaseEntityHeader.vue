<template>
  <div class="container">
    <div class="name-with-icon">
      <slot name="icon"></slot>

      <h2>
        <slot class="entity-id" name="entityId"></slot>
      </h2>
    </div>

    <div class="button-list" v-if="isBaseLoaded">
      <button v-on:click="emitDeleteEntity" class="action-button">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="action-icon"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
          />
        </svg>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "BaseEntityHeader",

  props: {
    isBaseLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },
  },

  emits: ["deleteEntity"],

  methods: {
    emitDeleteEntity() {
      this.$emit("deleteEntity");
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 0 1.6rem;

  background-color: var(--main-color);

  gap: 2.4rem;
}

.name-with-icon {
  display: flex;
  gap: 2.4rem;

  align-items: center;
  justify-content: center;
}

h2 {
  font-size: 3.6rem;
  color: var(--background-color);
  margin: 0;
}

::v-slotted(svg) {
  height: 3.6rem;
  width: 3.6rem;
  stroke: var(--background-color);
}

.button-list {
  display: flex;
  align-items: center;
  justify-content: right;
  gap: 0.8rem;
}

button {
  background-color: transparent;
  border: none;
  border-radius: 100rem;

  width: 3.6rem;
  height: 3.6rem;

  padding: 0;

  display: flex;
  align-items: center;
  justify-content: center;
}

.action-icon {
  width: 3.2rem;
  height: 3.2rem;
  background-color: transparent;
  stroke: var(--background-color);
}

button:hover,
button:active {
  cursor: pointer;
  background-color: var(--background-color);
}

button:hover > svg,
button:active > svg {
  stroke: var(--main-color);
}
</style>
