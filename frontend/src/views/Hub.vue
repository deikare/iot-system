<template>
  <div>
    <base-entity-header>
      <template v-slot:icon>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="hub-icon"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M8 14v3m4-3v3m4-3v3M3 21h18M3 10h18M3 7l9-4 9 4M4 10h16v11H4V10z"
          />
        </svg>
      </template>

      <template v-slot:entityId> hub {{ id }} </template>

      <!--      class action-icon is required to style icons-->
      <template v-if="isHubLoaded" v-slot:buttons>
        <button v-if="displayUnlock">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="action-icon"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M8 11V7a4 4 0 118 0m-4 8v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2z"
            />
          </svg>
        </button>

        <button v-else-if="displayLock">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="action-icon"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"
            />
          </svg>
        </button>

        <button>
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="action-icon"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
            />
          </svg>
        </button>
      </template>
    </base-entity-header>

    <base-entity-details
      v-bind:is-base-loaded="isHubLoaded"
      namespace="hub"
      v-bind:is-base-error="isErrorInHub"
      v-bind:are-children-loaded="areDevicesLoaded"
      v-bind:are-children-error="isErrorInDevices"
    >
      <template v-slot:children-header>Devices</template>
    </base-entity-details>
  </div>
</template>

<script>
import { mapActions, mapGetters } from "vuex";
import BaseEntityDetails from "@/slots/BaseEntityDetails";
import BaseEntityHeader from "@/slots/BaseEntityHeader";

export default {
  name: "Hub",

  components: { BaseEntityHeader, BaseEntityDetails },

  data() {
    return {
      isHubLoaded: false,
      isErrorInHub: false,
      areDevicesLoaded: false,
      isErrorInDevices: false,
    };
  },

  props: {
    id: {
      type: String,
      required: true,
      default() {
        return "";
      },
    },
  },

  methods: {
    ...mapActions("hub", ["loadHub"]),

    showId() {
      console.log(this.id);
    },

    fetchHubData() {
      this.loadHub({
        id: this.id,
        ifSuccessHandler: () => {
          this.isHubLoaded = true;
          this.isErrorInHub = false;
        },
        ifErrorHandler: () => {
          this.isHubLoaded = true;
          this.isErrorInHub = true;
        },
      });
    },
  },

  computed: {
    ...mapGetters("hub", ["displayLock", "displayUnlock"]),
  },

  created() {
    this.$watch(
      () => this.id,
      () => this.fetchHubData(),
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped></style>
