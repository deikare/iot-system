<template>
  <div class="shadowed-card">
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "BaseShadowedCard",
};
</script>

<style scoped>
.shadowed-card {
  background: var(--toast-color);
  border-radius: 5px;

  padding: 0.2rem 1.2rem;

  box-shadow: 0 2rem 3rem 0 rgba(18, 20, 40, 0.1);
  border: 2px solid var(--main-color);
}
</style>
