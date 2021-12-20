<template>
  <base-filter-list
    v-on:deactivateFilter="emitDeactivateFilter"
    v-bind:filters="filters"
  ></base-filter-list>
</template>

<script>
import BaseFilterList from "@/slots/BaseFilterList";
export default {
  name: "BaseFiltering",
  components: { BaseFilterList },
  props: {
    filters: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },
  emits: ["deactivateFilter"],
  methods: {
    emitDeactivateFilter(event) {
      this.$emit("deactivateFilter", event);
    },
  },
};
</script>

<style scoped></style>
