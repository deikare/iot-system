<template>
  <div class="container">
    <base-entity-grid
      v-bind:entities="entities"
      v-bind:link-generator-function="linkGeneratorFunction"
    ></base-entity-grid>

    <base-paginator
      v-bind:page="page"
      v-bind:short-list-length="shortListLength"
      v-on:changePage="emitChangePage"
    ></base-paginator>
  </div>
</template>

<script>
import BaseEntityGrid from "@/slots/entities-grid/BaseEntityGrid";
import BasePaginator from "@/slots/abstract/BasePaginator";
export default {
  name: "BaseEntitiesGridWithPaginator",
  components: { BasePaginator, BaseEntityGrid },

  props: {
    entities: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
    page: {
      type: Object,
      required: true,
      default() {
        return {
          pagesNumber: 0,
          currentPage: 0,
        };
      },
    },
    shortListLength: {
      type: Number,
      required: false,
      default: 4,
    },
    linkGeneratorFunction: {
      type: Function,
      required: true,
      default() {
        return () => {};
      },
    },
  },

  emits: ["changePage"],

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
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
