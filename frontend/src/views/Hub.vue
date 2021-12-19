<template>
  <div>
    <p>bla {{ id }}</p>
    <button v-on:click="showId">Show ids</button>
  </div>
</template>

<script>
import { mapActions } from "vuex";

export default {
  name: "Hub",
  props: ["id"],
  methods: {
    ...mapActions("hub", ["loadHub"]),

    showId() {
      console.log(this.id);
    },

    fetchData() {
      this.loadHub({ hubId: this.id });
    },
  },

  created() {
    this.$watch(
      () => this.id,
      () => this.fetchData(),
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped></style>
