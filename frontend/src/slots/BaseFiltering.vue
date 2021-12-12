<template>
  <base-search-bar></base-search-bar>
  <base-filter-list
    v-on:deactivateFilter="popDeactivateFilter"
    v-bind:filters="filters"
  ></base-filter-list>
</template>

<script>
import BaseSearchBar from "@/slots/BaseSearchBar";
import BaseFilterList from "@/slots/BaseFilterList";
export default {
  name: "BaseFiltering",
  components: { BaseFilterList, BaseSearchBar },
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
    popDeactivateFilter(event) {
      this.$emit(event);
    },
  },
};
</script>

<style scoped></style>
