<template>
  <div class="container">
    <slot name="filter-form"></slot>

    <base-filtering
      v-bind:filters="activeFilters"
      v-on:deactivateFilter="emitDeactivateFilter"
    ></base-filtering>

    <base-entity-grid
      v-bind:link-generator-function="entityLinkGeneratorFunction"
      v-bind:entities="entitiesPage.entities"
    ></base-entity-grid>

    <base-paginator
      v-bind:pages-number="entitiesPage.pagesNumber"
      v-bind:short-list-length="shortListLength"
      v-bind:current-page="entitiesPage.currentPage"
      v-on:changePage="emitChangePage"
    ></base-paginator>
  </div>
</template>

<script>
import BaseEntityGrid from "@/slots/BaseEntityGrid";
import BasePaginator from "@/slots/BasePaginator";
import BaseFiltering from "@/slots/BaseFiltering";
export default {
  name: "BaseEntityPage",

  components: { BaseFiltering, BasePaginator, BaseEntityGrid },

  props: {
    entitiesPage: {
      type: Object,
      required: true,
      default() {
        return {
          entities: [],
          pagesNumber: 0,
          currentPage: 0,
        };
      },
    },
    entityLinkGeneratorFunction: {
      type: Function,
      required: true,
      default() {
        return function () {
          return null;
        };
      },
    },
    activeFilters: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  emits: ["changePage", "deactivateFilter"],

  data() {
    return {
      shortListLength: 4,
      nameQuery: "",
    };
  },

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
    },
    emitDeactivateFilter(key) {
      this.$emit("deactivateFilter", key);
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
}
</style>
