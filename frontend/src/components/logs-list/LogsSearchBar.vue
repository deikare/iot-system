<template>
  <loading-spinner v-if="isLoadingVisible"></loading-spinner>
  <form
    class="filters-container"
    v-else-if="areTagsLoaded"
    v-on:submit.prevent="emitNewQuery"
  >
    <div class="filter-container">
      <div class="filter-label">Start</div>
      <input
        type="datetime-local"
        v-model="filters.start"
        placeholder="Start"
      />
    </div>

    <div class="filter-container">
      <div class="filter-label">End</div>
      <input type="datetime-local" v-model="filters.end" />
    </div>

    <div class="filter-container">
      <div class="filter-label">Direction</div>

      <select v-model="filters.descending">
        <option value="true">Descending</option>
        <option value="false">Ascending</option>
      </select>
    </div>

    <div class="filter-container">
      <div class="filter-label">Limit</div>
      <input type="number" v-model="filters.limit" min="1" />
    </div>

    <div class="filter-container hub-ids-container">
      <div class="filter-label">Hub ids</div>
      <div class="hub-ids-interior">
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
    </div>

    <div class="filter-container device-ids-container">
      <div class="filter-label">Device ids</div>
      <div class="device-ids-interior">
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
    </div>

    <button class="submit-button">Submit</button>
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
        ...(this.filters.start !== "" && {
          start: this.filters.start,
        }),
        ...(this.filters.end !== "" && {
          end: this.filters.end,
        }),
        ...(this.filters.descending !== "" && {
          desc: this.filters.descending,
        }),
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
        this.initialQuery.start !== "" &&
        !this.initialQuery.start.match(
          "\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d"
        )
      ) {
        return false;
      }

      if (
        this.initialQuery.end !== "" &&
        !this.initialQuery.end.match(
          "\\d{4}-[01]\\d-[0-3]\\dT[0-2]\\d:[0-5]\\d"
        )
      ) {
        return false;
      }

      if (
        this.initialQuery.hubIds.length > 0 &&
        !this.initialQuery.hubIds.every((hubId) =>
          this.getTags.hubIds.includes(hubId)
        )
      ) {
        return false;
      }

      if (
        this.initialQuery.deviceIds.length > 0 &&
        !this.initialQuery.deviceIds.every((deviceId) =>
          this.getTags.deviceIds.includes(deviceId)
        )
      ) {
        return false;
      }

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

  watch: {
    filters() {
      console.log(this.filters);
    },
  },

  created() {
    this.$watch(
      () => this.initialQuery,
      () => {
        this.fetchTags();
      },
      { immediate: true, deep: true }
    );

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

<style scoped>
.filters-container {
  display: flex;
  gap: 1.2rem;
  align-items: flex-start;
}

.filter-container {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

input {
  font-size: 1.4rem;
  min-height: 1.86rem;
}

input::-webkit-calendar-picker-indicator {
}

select {
  padding: 1px;
  font-size: 1.4rem;
  min-height: 2.46rem;
}

option {
  font-size: 1.4rem;
}

.filter-label {
  background-color: var(--main-color);
  color: var(--background-color);
  font-size: 1.8rem;

  padding: 0.2rem 0.4rem;
}

.hub-ids-container {
  background-color: var(--card-color);
}

.hub-ids-interior {
  height: 8rem;
  padding-right: 0.4rem;
  overflow-y: scroll;
  background-color: var(--card-color);
}

.device-ids-container {
  background-color: var(--card-color);
}

.device-ids-interior {
  height: 8rem;
  padding-right: 0.4rem;
  overflow-y: scroll;
}

.submit-button {
  font-size: 1.6rem;
}
</style>
