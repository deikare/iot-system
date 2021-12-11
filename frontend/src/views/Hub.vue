<template>
  <div>
    <p>bla {{ id }}</p>
    <button v-on:click="showId">Show ids</button>
  </div>
</template>

<script>
export default {
  name: "Hub",
  props: ["id"],
  methods: {
    showId() {
      console.log(this.id);
    },
  },
};
</script>

<style scoped></style>
