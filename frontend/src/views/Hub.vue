<template>
  <div class="hub-container">
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
        <button v-on:click="removeHub">
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
      v-bind:id="id"
      v-bind:is-base-loaded="isHubLoaded"
      v-bind:is-base-error="isErrorInHub"
      v-bind:transaction-mappings="transactionMappings"
      v-on:changeChildrenPage="changeDevicePage"
      v-on:childClicked="goToDevice"
      v-on:changeProperties="changePropertiesAndReload"
    >
      <template v-slot:children-header>Devices</template>
    </base-entity-details>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import BaseEntityDetails from "@/slots/entitity-details/BaseEntityDetails";
import BaseEntityHeader from "@/slots/entitity-details/BaseEntityHeader";

export default {
  name: "Hub",

  components: { BaseEntityHeader, BaseEntityDetails },

  data() {
    return {
      isHubLoaded: false,
      isErrorInHub: false,
      transactionMappings: {
        base: {
          namespace: "hub",
          getters: {
            properties: "getProperties",
          },
        },

        children: {
          namespace: "devices",
          getters: {
            entities: "getEntitiesAsChildren",
            page: "getPage",
          },
          actions: {
            loader: "loadEntities",
            deleter: "deleteDevice",
          },
        },
      },
    };
  },

  props: {
    id: {
      type: String,
      required: true,
      default: "",
    },
  },

  methods: {
    ...mapActions("hub", ["loadHub", "deleteHub", "patchHub"]),

    showId() {
      console.log(this.id);
    },

    fetchHubData() {
      this.isHubLoaded = false;
      this.isErrorInHub = false;

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

    removeHub() {
      const payload = {
        id: this.id,
        ifSuccessHandler: () => {
          this.$router.push({ name: "hubs" });
        },
        ifErrorHandler: () => {
          //TODO add custom toasts
        },
      };

      console.log("hub", payload);

      this.deleteHub(payload);
    },

    changeDevicePage(page) {
      console.log(page);
    },

    goToDevice(deviceId) {
      this.$router.push({ name: "device", params: { id: deviceId } });
    },

    deleteDevice(deviceId) {
      console.log(deviceId);
    },

    changePropertiesAndReload(data) {
      const payload = {
        id: this.id,
        data: data,
        //TODO - custom overlayed window for editing an entity
        ifSuccessHandler: () => {
          this.fetchHubData();
        },
        ifErrorHandler: () => {
          //TODO - case if there is no entity which mod was requested
        },
      };

      this.patchHub(payload);
      console.log("change", this.id);
    },
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

<style scoped>
.hub-container {
  display: flex;
  flex-direction: column;
  gap: 2.4rem;
}

.delete-button {
  background: none;
  border: none;
  width: fit-content;
  height: fit-content;

  display: flex;
  align-items: center;
  justify-content: center;
}

.delete-icon {
  stroke: var(--main-color);
  width: 2.4rem;
  height: 2.4rem;
}

.delete-button:hover > .delete-icon {
  border: 2px solid var(--main-color);
  border-radius: 50%;
}
</style>
