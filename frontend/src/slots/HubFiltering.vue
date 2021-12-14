<template>
  <div class="container" v-on:mouseleave="resetError">
    <p v-if="isErrorRenderable">Hub name cannot be empty</p>
    <form v-on:submit.prevent="validateAndEmitNewQuery">
      <input
        name="name"
        type="text"
        placeholder="Find a hub named..."
        v-model="nameQuery"
      />
      <button>Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "HubFiltering",

  emits: ["newQuery"],

  data() {
    return {
      nameQuery: "",
      isQueryValid: true,
    };
  },

  computed: {
    isErrorRenderable() {
      return !this.isQueryValid;
    },
  },

  methods: {
    validateAndEmitNewQuery() {
      if (this.nameQuery !== "") {
        this.isQueryValid = true;
        this.emitNewQuery();
        this.nameQuery = "";
      } else this.isQueryValid = false;
    },

    emitNewQuery() {
      this.$emit("newQuery", this.nameQuery);
    },

    resetError() {
      this.isQueryValid = true;
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

p {
  margin: 0;
  display: flex;
  justify-content: flex-start;
  color: var(--error-color);
  font-weight: bold;
}

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
