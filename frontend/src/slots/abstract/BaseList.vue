<template>
  <slot class="base-list"></slot>
</template>

<script>
export default {
  name: "BaseList",
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
</style>
