<template>
  <entities-error v-if="displayError"></entities-error>

  <div v-else class="container">
    <slot name="filter-form"></slot>

    <base-filter-list
      v-bind:filters="activeQuery"
      v-on:deactivateFilter="emitDeactivateFilter"
    >
    </base-filter-list>

    <loading-spinner
      v-if="displayLoading"
      v-bind:active="displayLoading"
    ></loading-spinner>

    <base-entities-grid-with-paginator
      v-else
      v-bind:entities="getEntities"
      v-bind:short-list-length="shortListLength"
      v-bind:page="getPage"
      v-bind:link-generator-function="entityLinkGeneratorFunction"
      v-on:changePage="emitChangePage"
    ></base-entities-grid-with-paginator>

    <!--    <base-entity-grid
      v-bind:link-generator-function="entityLinkGeneratorFunction"
      v-bind:entities="entitiesPage.entities"
      v-bind:is-loaded="isLoaded"
    ></base-entity-grid>

    <base-paginator
      v-bind:pages-number="entitiesPage.pagesNumber"
      v-bind:short-list-length="shortListLength"
      v-bind:current-page="entitiesPage.currentPage"
      v-on:changePage="emitChangePage"
    ></base-paginator>-->
  </div>
</template>

<script>
import EntitiesError from "@/slots/EntitiesError";
import BaseFilterList from "@/slots/BaseFilterList";
import { mapState, mapActions } from "vuex";
import LoadingSpinner from "@/components/LoadingSpinner";
import BaseEntitiesGridWithPaginator from "@/slots/BaseEntitiesGridWithPaginator";

export default {
  name: "BaseEntityPage",

  components: {
    BaseEntitiesGridWithPaginator,
    LoadingSpinner,
    BaseFilterList,
    EntitiesError,
  },

  data() {
    return {
      isLoaded: false,
      isError: false,
    };
  },

  props: {
    transactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          getters: {
            namespace: "",
            entities: "",
            page: "",
          },
          actions: {
            namespace: "",
            loader: "",
          },
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
      getEntities(state, getters) {
        return getters[
          `${this.transactionMappings.getters.namespace}/${this.transactionMappings.getters.entities}`
        ];
      },
      getPage(state, getters) {
        return getters[
          `${this.transactionMappings.getters.namespace}/${this.transactionMappings.getters.page}`
        ];
      },
    }),

    displayLoading() {
      return !this.isLoaded;
    },

    displayError() {
      return this.isError;
    },

    shortListLength() {
      return 4;
    },
  },

  methods: {
    ...mapActions({
      loadEntitiesPage(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.actions.namespace}/${this.transactionMappings.actions.loader}`,
          payload
        );
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
