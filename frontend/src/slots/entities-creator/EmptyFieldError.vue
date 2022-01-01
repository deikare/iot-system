<template>
  <div class="field-error">Cannot be empty</div>
</template>

<script>
export default {
  name: "EmptyFieldError",
};
</script>

<style scoped>
.field-error {
  color: var(--error-color);
  font-size: 1.2rem;

  display: flex;
  align-items: center;
}
</style>
