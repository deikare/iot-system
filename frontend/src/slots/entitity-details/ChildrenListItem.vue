<template>
  <button class="child-item" v-on:click="emitChildClicked">
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

    <slot name="additional" class="additional"></slot>
  </button>
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

  emits: ["childClicked"],

  methods: {
    emitChildClicked() {
      let childId = this.childProperties.find(
        (property) => property.key === "id"
      ).value;
      this.$emit("childClicked", childId);
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
  justify-content: center;

  gap: 0.8rem;

  background: transparent;

  border: none;
  border-bottom: 1px solid var(--main-color);

  font-size: 1.6rem;
}

.child-item:hover,
.child-item:active {
  border: 2px solid var(--main-color);
  cursor: pointer;
}

.child-property {
  display: flex;
}

.additional {
  display: flex;
}
</style>
