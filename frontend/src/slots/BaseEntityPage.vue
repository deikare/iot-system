<template>
  <div class="container">
    <base-filter-form>
      <template v-slot:default>
        <slot name="filter-form"> </slot>
      </template>
    </base-filter-form>

    <!--    <form>-->
    <!--      <slot name="filter-form"> </slot>-->
    <!--      <input-->
    <!--        name="name"-->
    <!--        type="text"-->
    <!--        placeholder="Find a hub named..."-->
    <!--        v-model="nameQuery"-->
    <!--      />-->
    <!--      <button>Submit</button>-->
    <!--    </form>-->

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
import BaseFilterForm from "@/slots/BaseFilterForm";
export default {
  name: "BaseEntityPage",

  components: { BaseFilterForm, BaseFiltering, BasePaginator, BaseEntityGrid },

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
