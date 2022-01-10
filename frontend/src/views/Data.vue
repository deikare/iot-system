<template>
  <div class="data-container">
    <data-search-bar
      v-bind:initial-query="initialQuery"
      v-on:newQuery="goToNewQuery"
      v-on:wrongInitialQuery="alert"
    ></data-search-bar>

    <loading-spinner v-if="isLoadingVisible"></loading-spinner>
    <entities-error v-else-if="isError"></entities-error>

    <div class="chart-container" v-else>
      <data-chart
        v-bind:plot-data="getData"
        v-bind:labels-position="labelsPosition"
        v-bind:x-labels-number="xLabelsNumber"
      >
      </data-chart>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import EntitiesError from "@/slots/abstract/EntitiesError";
import LoadingSpinner from "@/components/LoadingSpinner";
import DataSearchBar from "@/components/data/DataSearchBar";
import DataChart from "@/components/data/DataChart";

export default {
  name: "Logs",
  components: { DataChart, DataSearchBar, LoadingSpinner, EntitiesError },

  data() {
    return {
      isLoaded: false,
      isError: false,
      labelsPosition: "bottom",
      xLabelsNumber: 6,
    };
  },

  props: {
    queriedBucket: {
      type: String,
      required: false,
      default: "data",
    },

    queriedStart: {
      type: String,
      required: false,
      default: "1970-01-01T00:00",
    },

    queriedEnd: {
      type: String,
      required: false,
      default: "",
    },

    queriedLimit: {
      type: String,
      required: false,
      default: "",
    },

    queriedHubIds: {
      required: false,
      default() {
        return [];
      },
    },

    queriedDeviceIds: {
      required: false,
      default() {
        return [];
      },
    },

    queriedMeasurementTypes: {
      required: false,
      default() {
        return [];
      },
    },
  },

  computed: {
    initialQuery() {
      return {
        bucket: this.queriedBucket,
        start: this.queriedStart,
        end: this.queriedEnd,
        limit: this.queriedLimit,
        hubIds:
          typeof this.queriedHubIds === "string"
            ? [this.queriedHubIds]
            : this.queriedHubIds,
        deviceIds:
          typeof this.queriedDeviceIds === "string"
            ? [this.queriedDeviceIds]
            : this.queriedDeviceIds,
        measurementTypes:
          typeof this.queriedMeasurementTypes === "string"
            ? [this.queriedMeasurementTypes]
            : this.queriedMeasurementTypes,
      };
    },

    queryToBackend() {
      return {
        bucket: this.queriedBucket,

        start: new Date(this.queriedStart).toISOString(),

        ...(typeof this.queriedEnd !== "undefined" &&
          this.queriedEnd !== "" && {
            stop: new Date(this.queriedEnd).toISOString(),
          }),

        ...(typeof this.queriedLimit !== "undefined" &&
          this.queriedLimit !== "" && { limit: this.queriedLimit }),

        ...(typeof this.queriedHubIds !== "undefined" &&
          this.queriedHubIds !== [] && {
            hubIds: this.getHubIdsToBackend,
          }),

        ...(typeof this.queriedDeviceIds !== "undefined" &&
          this.queriedDeviceIds !== [] && {
            deviceIds: this.getDeviceIdsToBackend,
          }),

        ...(typeof this.queriedMeasurementTypes !== "undefined" &&
          this.queriedMeasurementTypes !== [] && {
            measurementTypes: this.getMeasurementTypesToBackend,
          }),
      };
    },

    getHubIdsToBackend() {
      let result = "";

      if (typeof this.queriedHubIds === "string")
        result = result.concat(this.queriedHubIds);
      else {
        this.queriedHubIds.forEach((hubId, index) => {
          if (index !== 0) result = result.concat(",");
          result = result.concat(hubId);
        });
      }

      return result;
    },

    getDeviceIdsToBackend() {
      let result = "";

      if (typeof this.queriedDeviceIds === "string")
        result = result.concat(this.queriedDeviceIds);
      else {
        this.queriedDeviceIds.forEach((deviceId, index) => {
          if (index !== 0) result = result.concat(",");
          result = result.concat(deviceId);
        });
      }

      return result;
    },

    getMeasurementTypesToBackend() {
      let result = "";

      if (typeof this.queriedMeasurementTypes === "string")
        result = result.concat(this.queriedMeasurementTypes);
      else {
        this.queriedMeasurementTypes.forEach((measurementType, index) => {
          if (index !== 0) result = result.concat(",");
          result = result.concat(measurementType);
        });
      }

      return result;
    },

    ...mapGetters("data", ["getData"]),

    isLoadingVisible() {
      return !this.isLoaded && !this.isError;
    },
  },

  methods: {
    goToNewQuery(newFilters) {
      this.$router.push({ name: "data", query: newFilters });
    },

    alert() {
      this.$router.push({
        name: "not-found",
        params: { notFound: "/notFound" },
      });
    },

    ...mapActions("data", ["loadDataSeries"]),

    fetchData() {
      this.isLoaded = false;
      this.isError = false;

      const payload = {
        queryParams: this.queryToBackend,
        ifSuccessHandler: () => {
          this.isLoaded = true;
          this.isError = false;
        },
        ifErrorHandler: () => {
          this.isLoaded = true;
          this.isError = true;
        },
      };

      this.loadDataSeries(payload);
    },
  },

  created() {
    this.$watch(
      () => this.initialQuery,
      () => {
        this.fetchData();
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.data-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  margin-bottom: 2rem;
}

.chart-container {
  height: 100rem;
  width: 98%;
}
</style>
