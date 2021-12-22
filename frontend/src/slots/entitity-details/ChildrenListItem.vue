<template>
  <ul class="child-item">
    <div
      class="child-property"
      v-for="(property, index) in childProperties"
      v-bind:key="index"
    >
      {{ property.key }}: {{ property.value }}
      {{ index === childProperties.length - 1 ? "" : "," }}
    </div>
  </ul>
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
  flex-direction: column;
}
</style>
