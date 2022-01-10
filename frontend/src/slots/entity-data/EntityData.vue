<template>
  <base-card>
    <template v-slot:header class="header">
      <div class="flexbox">
        <header>Data</header>

        <div class="control-buttons">
          <button v-on:click="fetchData" class="control-button">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="control-icon"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15"
              />
            </svg>
          </button>
        </div>
      </div>
    </template>

    <loading-spinner class="flexbox" v-if="isLoadingVisible"></loading-spinner>
    <entities-error class="flexbox" v-else-if="isError"></entities-error>
    <div v-else>
      <data-chart
        class="chart-container"
        v-bind:plot-data="getData"
      ></data-chart>
    </div>
  </base-card>
</template>

<script>
import LoadingSpinner from "@/components/LoadingSpinner";
import BaseCard from "@/slots/abstract/BaseCard";
import EntitiesError from "@/slots/abstract/EntitiesError";
import { mapActions, mapState } from "vuex";
import DataChart from "@/components/data/DataChart";

export default {
  name: "EntityData",
  components: { DataChart, EntitiesError, BaseCard, LoadingSpinner },

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
          namespace: "",
          loader: "",
          getter: "",
        };
      },
    },

    queryEntity: {
      type: Object,
      required: true,
      default() {
        return {
          id: "",
          idName: "", //e.g hubId, deviceId
        };
      },
    },
  },

  computed: {
    ...mapState({
      getData(state, getters) {
        return getters[
          `${this.transactionMappings.namespace}/${this.transactionMappings.getter}`
        ];
      },
    }),

    isLoadingVisible() {
      return !this.isLoaded;
    },
  },

  methods: {
    ...mapActions({
      loadData(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.namespace}/${this.transactionMappings.loader}`,
          payload
        );
      },
    }),

    fetchData() {
      this.isLoaded = false;
      this.isError = false;

      const queryParams = { start: "1970-01-01T00:00:00Z" };

      queryParams[this.queryEntity.idName] = this.queryEntity.id;

      const payload = {
        queryParams: queryParams,
        ifSuccessHandler: () => {
          this.isLoaded = true;
          this.isError = false;
        },
        ifErrorHandler: () => {
          this.isLoaded = true;
          this.isError = true;
        },
      };

      this.loadData(payload);
    },
  },

  created() {
    this.$watch(
      () => this.queryEntity,
      () => {
        this.fetchData();
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.flexbox {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  text-align: center;
}

.control-buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.2rem;
}

.control-button {
  background: var(--main-color);
  border: 2px solid transparent;
  height: 3.6rem;
  width: 3.6rem;
  border-radius: 50%;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-icon {
  stroke: var(--card-color);
  height: 3rem;
  width: 3rem;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-button:hover,
.control-button:active {
  cursor: pointer;
  border: 2px solid var(--card-color);
}

.chart-container {
  height: 100rem;
  width: 100%;
}
</style>
