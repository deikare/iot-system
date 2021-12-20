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

  props: {
    active: {
      type: Boolean,
      required: true,
      default() {
        return false;
      },
    },
  },

  computed: {
    displayLoading() {
      return this.active;
    },
  },
};
</script>

<style scoped></style>
