<template>
  <loading-spinner v-if="isLoadingVisible"></loading-spinner>
  <form v-else-if="areTagsLoaded" v-on:submit.prevent="emitNewQuery">
    <input type="datetime-local" v-model="filters.start" />
    <input type="datetime-local" v-model="filters.end" />
    <select v-model="filters.descending">
      <option value="true">yes</option>
      <option value="false">no</option>
    </select>
    <input type="number" v-model="filters.limit" />
    <div class="hub-ids">
      <div class="hub-id" v-for="hubId in getTags.hubIds" v-bind:key="hubId">
        <input
          type="checkbox"
          name="hub-id"
          v-bind:id="hubId"
          v-bind:value="hubId"
          v-model="filters.hubIds"
        />
        <label v-bind:for="hubId">{{ hubId }}</label>
      </div>
    </div>

    <div class="device-ids">
      <div
        class="device-id"
        v-for="deviceId in getTags.deviceIds"
        v-bind:key="deviceId"
      >
        <input
          type="checkbox"
          name="device-id"
          v-bind:id="deviceId"
          v-bind:value="deviceId"
          v-model="filters.deviceIds"
        />
        <label v-bind:for="deviceId">{{ deviceId }}</label>
      </div>
    </div>

    <button>Submit</button>
  </form>
</template>

<script>
import { mapGetters, mapActions } from "vuex";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "LogsSearchBar",
  components: { LoadingSpinner },
  data() {
    return {
      areTagsLoaded: false,
      isError: false,

      filters: {
        start: "",
        end: "",
        descending: "true",
        limit: "",
        hubIds: [],
        deviceIds: [],
      },
    };
  },

  props: {
    initialQuery: {
      type: Object,
      required: true,
      default() {
        return {
          start: "",
          end: "",
          descending: "true",
          hubIds: [],
          deviceIds: [],
        };
      },
    },
  },

  emits: ["wrongInitialQuery", "newQuery"],

  computed: {
    ...mapGetters("logTags", ["getTags"]),

    isLoadingVisible() {
      return !this.areTagsLoaded && !this.isError;
    },
  },

  methods: {
    emitNewQuery() {
      const newFilters = {
        ...(this.filters.start !== "" && { start: this.filters.start }),
        ...(this.filters.end !== "" && { end: this.filters.end }),
        ...(this.filters.descending !== "" && { desc: this.filters.desc }),
        ...(this.filters.limit !== "" && { limit: this.filters.limit }),
        ...(this.filters.hubIds !== [] && { hubIds: this.filters.hubIds }),
        ...(this.filters.deviceIds !== [] && {
          deviceIds: this.filters.deviceIds,
        }),
      };
      this.$emit("newQuery", newFilters);
    },

    ...mapActions("logTags", ["loadTags"]),

    fetchTags() {
      this.areTagsLoaded = false;
      this.isError = false;

      const payload = {
        ifSuccessHandler: () => {
          this.validateAndResetTags();
          this.areTagsLoaded = true;
          this.isError = false;
        },
        ifErrorHandler: () => {
          this.areTagsLoaded = true;
          this.isError = true;
        },
      };

      this.loadTags(payload);
    },

    validateAndResetTags() {
      if (!this.areInitialTagsValid()) this.$emit("wrongInitialQuery");
      else this.resetTags();
    },

    areInitialTagsValid() {
      if (
        this.initialQuery.start.match(
          "^\\d{4}-\\d{2}-\\d{2}T\\d{2}%3A\\d{2}%3A\\d{2}(?:%2E\\d+)?[A-Z]?(?:[+.-](?:08%3A\\d{2}|\\d{2}[A-Z]))?$"
        )
      )
        return false;

      if (
        this.initialQuery.end.match(
          "^\\d{4}-\\d{2}-\\d{2}T\\d{2}%3A\\d{2}%3A\\d{2}(?:%2E\\d+)?[A-Z]?(?:[+.-](?:08%3A\\d{2}|\\d{2}[A-Z]))?$"
        )
      )
        return false;

      if (
        !this.getTags.hubIds.some(
          (hubId) => this.initialQuery.hubIds.indexOf(hubId) >= 0
        )
      )
        return false;

      if (
        !this.getTags.deviceIds.some(
          (deviceId) => this.initialQuery.deviceIds.indexOf(deviceId) >= 0
        )
      )
        return false; //TODO there is error

      return true;
    },

    resetTags() {
      this.filters.start = this.initialQuery.start;
      this.filters.end = this.initialQuery.end;
      this.filters.descending = this.initialQuery.descending;
      this.filters.limit = this.initialQuery.limit;
      this.filters.hubIds = this.initialQuery.hubIds;
      this.filters.deviceIds = this.initialQuery.deviceIds;
    },
  },

  created() {
    console.log("CHUJ WEWQEQWE", this.initialQuery);
    this.fetchTags();
    this.$watch(
      () => this.filters,
      () => {
        console.log(this.filters);
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped></style>
