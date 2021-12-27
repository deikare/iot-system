<template>
  <div class="child-item" v-on:click="emitChildEvent(`childClicked`)">
    <div class="child-properties">
      <div
        class="child-property"
        v-for="(property, index) in childProperties"
        v-bind:key="index"
      >
        {{
          index === childProperties.length - 1
            ? `${property.key}: ${property.value}`
            : `${property.key}: ${property.value}, `
        }}
      </div>
    </div>

    <!--      click stop to prevent div from emitting clicked child event-->
    <div class="additional-buttons">
      <button
        class="additional-button"
        v-on:click.stop="emitChildEvent(`editChild`)"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="button-icon"
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
      </button>

      <button
        class="additional-button"
        v-on:click.stop="emitChildEvent(`deleteChild`)"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="button-icon"
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
  name: "ChildrenListItem",

  props: {
    childProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  computed: {
    childId() {
      return this.childProperties.find((property) => property.key === "id")
        .value;
    },
  },

  emits: ["childClicked", "editChild", "deleteChild"],

  methods: {
    emitChildEvent(eventName) {
      this.$emit(eventName, this.childId);
    },
  },
};
</script>

<style scoped>
.child-item {
  list-style-type: none;
  display: inline-flex;
  width: 100%;
  align-items: center;
  justify-content: space-between;

  gap: 0.8rem;

  background: transparent;

  border: 2px solid transparent;
  border-bottom: 1px solid var(--main-color);

  font-size: 1.6rem;
}

.child-item:hover,
.child-item:active {
  border-bottom: 2px solid var(--main-color);
  border-top: 1px solid var(--main-color);
  cursor: pointer;
}

.child-item:first-child:hover,
.child-item:first-child:active {
  border-top: 2px solid var(--main-color);
}

.child-properties {
  display: flex;
}

.child-property {
  display: flex;
}

.additional-buttons {
  display: flex;
}

.additional-button {
  background: none;
  border: 2px solid transparent;

  display: flex;
  align-items: center;
  justify-content: center;

  width: 3.6rem;
  height: 3.6rem;
}

.button-icon {
  stroke: var(--main-color);
  width: 2.4rem;
  height: 2.4rem;
}

.additional-button:hover,
.additional-button:active {
  border: 2px solid var(--main-color);
  border-radius: 50%;
  cursor: pointer;
}
</style>
