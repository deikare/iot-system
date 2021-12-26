<template>
  <button class="child-item">
    <div
      class="child-property"
      v-for="(property, index) in childProperties"
      v-bind:key="index"
    >
      {{
        index === childProperties.length - 1
          ? `${property.key}: ${property.value}`
          : `${property.key}: ${property.value},`
      }}
    </div>
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
};
</script>

<style scoped>
.child-item {
  list-style-type: none;
  display: flex;
  align-items: center;
  justify-content: center;

  border-bottom: 1px solid var(--main-color);
}

.child-property {
}
</style>
