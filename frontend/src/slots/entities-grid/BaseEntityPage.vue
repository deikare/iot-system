<template>
  <entities-error v-if="displayError"></entities-error>

  <div v-else class="container">
    <slot name="filter-form"></slot>

    <base-filter-list
      v-bind:filters="activeQuery"
      v-on:deactivateFilter="emitDeactivateFilter"
    >
    </base-filter-list>

    <loading-spinner v-if="displayLoading"></loading-spinner>

    <base-entities-grid-with-paginator
      v-else
      v-bind:entities="getEntities"
      v-bind:short-list-length="shortListLength"
      v-bind:page="getPage"
      v-bind:link-generator-function="entityLinkGeneratorFunction"
      v-on:changePage="emitChangePage"
    ></base-entities-grid-with-paginator>
  </div>
</template>

<script>
import EntitiesError from "@/slots/abstract/EntitiesError";
import BaseFilterList from "@/slots/abstract/BaseFilterList";
import { mapState, mapActions } from "vuex";
import LoadingSpinner from "@/components/LoadingSpinner";
import BaseEntitiesGridWithPaginator from "@/slots/entities-grid/BaseEntitiesGridWithPaginator";

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
      isBaseLoaded: false,
      isBaseError: false,
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
      return !this.isBaseLoaded;
    },

    displayError() {
      return this.isBaseError;
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

      this.isBaseLoaded = false;

      this.loadEntitiesPage({
        queryParams: queryParams,

        ifSuccessHandler: () => {
          this.isBaseLoaded = true;
          this.isBaseError = false;
        },

        ifErrorHandler: (error) => {
          this.isBaseLoaded = true;
          this.isBaseError = true;
          console.log(error); //TODO add custom toast
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
