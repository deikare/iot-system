<template>
  <slot class="base-list"></slot>
</template>

<script>
export default {
  name: "BaseList",

  //TODO add item.v-for.(keys, values)
};
</script>

<style scoped>
.base-list {
  display: flex;
  gap: 0.8rem;

  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.card-background {
  background-color: var(--card-color);
}
</style>
