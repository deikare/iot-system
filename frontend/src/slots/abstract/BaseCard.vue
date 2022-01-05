<template>
  <div>
    <header>
      <h2><slot name="header"></slot></h2>
    </header>
    <div class="text-left">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "BaseCard",
};
</script>

<style scoped>
div {
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
  display: flex;
  flex-direction: column;
  /*text-align: left;*/
}

h2 {
  margin: 0;
  background-color: var(--main-color);
  color: var(--card-color);
  padding: 0.2rem 0;
}

.text-left {
  text-align: left;
}
</style>
