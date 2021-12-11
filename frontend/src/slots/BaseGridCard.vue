<template>
  <div>
    <header>
      <h2><slot name="header"></slot></h2>
    </header>
    <slot></slot>
  </div>
</template>

<script>
export default {
  name: "BaseGridCard",
};
</script>

<style scoped>
div {
  background-color: var(--card-color);
  border: 2px solid var(--main-color);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

h2 {
  margin: 0;
  background-color: var(--main-color);
  color: var(--card-color);
  padding: 0.2rem 0;
}
</style>
