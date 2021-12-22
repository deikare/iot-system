<template>
  <ul class="property-list">
    <li v-for="property in properties" v-bind:key="property.key">
      <span class="bold">{{ property.key }}: </span>
      {{ property.value }}
    </li>
  </ul>
</template>

<script>
export default {
  name: "BaseItemProperties",

  props: {
    properties: {
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
.property-list {
  list-style: none;

  padding: 0;

  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.4rem;
}

li {
  display: flex;
  text-align: left;
  justify-content: space-between;
  padding-left: 0.8rem;
}

.bold {
  font-weight: bold;
}
</style>
