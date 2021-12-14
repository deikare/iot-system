<template>
  <slot></slot>
</template>

<script>
export default {
  name: "BaseFilterForm",

  data() {
    return {
      nameQuery: "",
    };
  },
};
</script>

<style scoped>
form {
  display: flex;
  gap: 0.4rem;
}

input {
  font-size: 1.8rem;
  color: var(--main-color);
  background-color: var(--background-color);
  border: 1px solid var(--main-color);
}

button {
  font-size: 1.8rem;
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
}
</style>
