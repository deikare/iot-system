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
import BaseSearchForm from "@/slots/abstract/BaseSearchForm";
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

<style scoped></style>
