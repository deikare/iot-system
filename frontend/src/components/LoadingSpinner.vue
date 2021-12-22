<template>
  <loading v-bind:active="displayLoading" color="var(--main-color)"></loading>
</template>

<script>
import Loading from "vue3-loading-overlay";

export default {
  name: "LoadingSpinner",

  components: {
    Loading,
  },

  computed: {
    displayLoading() {
      return true;
    },
  },
};
</script>

<style scoped></style>
