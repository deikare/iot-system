<template>
  <base-search-form
    v-bind:new-query-emitter="newQueryEmitter"
    v-bind:query-validator="queryValidator"
  >
    <template v-slot:error-message> Hub name cannot be empty </template>
    <template v-slot:form>
      <input
        class="input-text"
        name="name"
        type="text"
        placeholder="Find a hub named..."
        v-model="nameQuery"
      />
    </template>
  </base-search-form>
</template>

<script>
import BaseSearchForm from "@/slots/BaseSearchForm";
export default {
  name: "HubFiltering",
  components: { BaseSearchForm },
  emits: ["newQuery"],

  data() {
    return {
      nameQuery: "",
    };
  },

  computed: {
    newQueryEmitter() {
      return () => {
        this.$emit("newQuery", this.nameQuery);
        this.nameQuery = "";
      };
    },
    queryValidator() {
      return () => this.nameQuery !== "";
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
