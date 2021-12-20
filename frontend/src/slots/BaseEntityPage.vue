<template>
  <entities-error v-if="isError"></entities-error>

  <div v-else class="container">
    <slot name="filter-form"></slot>

    <base-filter-list
      v-bind:filters="activeFilters"
      v-on:deactivateFilter="emitDeactivateFilter"
    >
    </base-filter-list>

    <base-entity-grid
      v-bind:link-generator-function="entityLinkGeneratorFunction"
      v-bind:entities="entitiesPage.entities"
      v-bind:is-loaded="isLoaded"
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
import EntitiesError from "@/slots/EntitiesError";
import BaseFilterList from "@/slots/BaseFilterList";
export default {
  name: "BaseEntityPage",

  components: {
    BaseFilterList,
    EntitiesError,
    BasePaginator,
    BaseEntityGrid,
  },

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
    isLoaded: {
      type: Boolean,
      required: true,
      default() {
        return false;
      },
    },
    isError: {
      type: Boolean,
      required: true,
      default() {
        return false;
      },
    },
  },

  emits: ["changePage", "deactivateFilter"],

  data() {
    return {
      shortListLength: 4,
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
