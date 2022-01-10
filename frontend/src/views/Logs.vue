<template>
  <div class="data-container">
    <logs-search-bar
      v-bind:initial-query="initialQuery"
      v-on:newQuery="goToNewQuery"
      v-on:wrongInitialQuery="alert"
    ></logs-search-bar>

    <loading-spinner v-if="isLoadingVisible"></loading-spinner>
    <entities-error v-else-if="isError"></entities-error>

    <div class="data-view" v-else>
      <entity-list
        v-bind:highlight-items="highlightItems"
        v-bind:buttons-properties="buttonsProperties"
        v-bind:entities-properties="getLogs"
      >
      </entity-list>
    </div>
  </div>
</template>

<script>
import LogsSearchBar from "@/components/logs-list/LogsSearchBar";
import EntityList from "@/components/entity-list/EntityList";
import { mapGetters, mapActions } from "vuex";
import EntitiesError from "@/slots/abstract/EntitiesError";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "Logs",
  components: { LoadingSpinner, EntitiesError, EntityList, LogsSearchBar },

  data() {
    return {
      buttonsProperties: {
        isEditVisible: false,
        isDeleteVisible: false,
      },
      isLoaded: false,
      isError: false,
      highlightItems: false,
    };
  },

  props: {
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

    queriedDescending: {
      type: String,
      required: false,
      default: "true",
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
  },

  computed: {
    initialQuery() {
      return {
        start: this.queriedStart,
        end: this.queriedEnd,
        descending: this.queriedDescending,
        limit: this.queriedLimit,
        hubIds:
          typeof this.queriedHubIds === "string"
            ? [this.queriedHubIds]
            : this.queriedHubIds,
        deviceIds:
          typeof this.queriedDeviceIds === "string"
            ? [this.queriedDeviceIds]
            : this.queriedDeviceIds,
      };
    },

    queryToBackend() {
      return {
        start: new Date(this.queriedStart).toISOString(),

        ...(typeof this.queriedEnd !== "undefined" &&
          this.queriedEnd !== "" && {
            stop: new Date(this.queriedEnd).toISOString(),
          }),

        desc: this.queriedDescending,

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

    ...mapGetters("logs", ["getLogs"]),

    isLoadingVisible() {
      return !this.isLoaded && !this.isError;
    },
  },

  methods: {
    goToNewQuery(newFilters) {
      this.$router.push({ name: "logs", query: newFilters });
    },

    alert() {
      this.$router.push({
        name: "not-found",
        params: { notFound: "/notFound" },
      });
    },

    ...mapActions("logs", ["loadLogs"]),

    fetchLogs() {
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

      this.loadLogs(payload);
    },
  },

  created() {
    this.$watch(
      () => this.initialQuery,
      () => {
        this.fetchLogs();
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

.data-view {
  width: 50%;
  border-top: 1px solid var(--main-color);
}
</style>
