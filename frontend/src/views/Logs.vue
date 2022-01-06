<template>
  <logs-search-bar
    v-bind:initial-query="initialQuery"
    v-on:newQuery="goToNewQuery"
    v-on:wrongInitialQuery="alert"
  ></logs-search-bar>
</template>

<script>
import LogsSearchBar from "@/components/logs-list/LogsSearchBar";

export default {
  name: "Logs",
  components: { LogsSearchBar },
  data() {
    return {
      isLoaded: false,
      isError: false,
    };
  },

  props: {
    queriedStart: {
      type: String,
      required: false,
      default: "1970-01-01T00:00:00Z",
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

    // activeQuery: {
    //   type: Object,
    //   required: true,
    //   default() {
    //     return {};
    //   },
    // },
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
  },

  methods: {
    goToNewQuery(newFilters) {
      console.log("CHUJ CHUJ", newFilters);
      this.$router.push({ name: "logs", query: newFilters });
    },

    alert() {
      console.log("CHUJ CHUJ CHUJ", "wrong initial query");
    },
  },

  created() {
    console.log(this.$props);
  },
};
</script>

<style scoped></style>
