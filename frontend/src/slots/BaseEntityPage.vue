<template>
  <entities-error v-if="isError"></entities-error>

  <div v-else class="container">
    <slot name="filter-form"></slot>

    <base-filter-list
      v-bind:filters="activeQuery"
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
import { mapState, mapActions } from "vuex";

export default {
  name: "BaseEntityPage",

  components: {
    BaseFilterList,
    EntitiesError,
    BasePaginator,
    BaseEntityGrid,
  },

  data() {
    return {
      entitiesPage: {
        entities: [],
        pagesNumber: 0,
        currentPage: 0,
      },
      isLoaded: false,
      isError: false,
      shortListLength: 4,
    };
  },

  props: {
    namespace: {
      type: String,
      required: true,
      default: "",
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
    page: {
      type: String,
      required: true,
      default: "",
    },
    activeQuery: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },

  emits: ["changePage", "deactivateFilter"],

  computed: {
    ...mapState({
      getEntitiesPage(state, getters) {
        return getters[`${this.namespace}/getEntitiesPage`];
      },
    }),
  },

  methods: {
    ...mapActions({
      loadEntitiesPage(dispatch, payload) {
        return dispatch(`${this.namespace}/loadEntitiesPage`, payload);
      },
    }),

    emitChangePage(page) {
      this.$emit("changePage", page);
    },
    emitDeactivateFilter(key) {
      this.$emit("deactivateFilter", key);
    },

    fetchData() {
      const queryParams = {
        page: Number(this.page) - 1,
        ...this.activeQuery,
      };

      this.isLoaded = false;

      this.loadEntitiesPage({
        queryParams: queryParams,

        ifSuccessHandler: () => {
          this.isLoaded = true;
          this.isError = false;

          this.entitiesPage = this.getEntitiesPage;
        },

        ifErrorHandler: () => {
          this.isLoaded = true;
          this.isError = true;
          this.entitiesPage = {};
        },
      });
    },
  },

  created() {
    this.$watch(
      () => [this.activeQuery, this.page],
      () => {
        console.log(
          this.activeQuery,
          this.namespace,
          this.page,
          this.entityLinkGeneratorFunction
        );
        this.fetchData();
      },
      { immediate: true, deep: true }
    );
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
