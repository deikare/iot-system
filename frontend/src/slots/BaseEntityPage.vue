<template>
  <base-entity-grid
    v-bind:link-generator-function="entityLinkGeneratorFunction"
    v-bind:entities="entitiesPage.entities"
  ></base-entity-grid>

  <base-paginator
    v-bind:n="entitiesPage.n"
    v-bind:short-list-length="entitiesPage.shortListLength"
    v-bind:current-page="entitiesPage.currentPage"
    v-bind:link-generator-function="entitiesPageLinkGeneratorFunction"
  ></base-paginator>
</template>

<script>
import BaseEntityGrid from "@/slots/BaseEntityGrid";
import BasePaginator from "@/slots/BasePaginator";
export default {
  name: "BaseEntityPage",

  components: { BasePaginator, BaseEntityGrid },

  props: {
    entitiesPage: {
      type: Object,
      required: true,
      default() {
        return {
          entities: [],
          n: 0,
          shortListLength: 0,
          currentPage: 0,
        };
      },
    },
    entitiesPageLinkGeneratorFunction: {
      type: Function,
      required: true,
      default() {
        return function () {
          return null;
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
  },
};
</script>

<style scoped></style>
