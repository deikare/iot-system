<template>
  <div class="hub-container">
    <base-entity-details
      v-bind:id="id"
      v-bind:is-base-loaded="isHubLoaded"
      v-bind:is-base-error="isErrorInHub"
      v-bind:transaction-mappings="transactionMappings"
      v-bind:if-base-deleted-route="ifHubDeletedRoute"
      v-bind:new-child-properties="newDeviceProperties"
      v-bind:buttons-properties="buttonsProperties"
      v-bind:query-entity="queryEntity"
      v-on:childClicked="goToDevice"
    >
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
      <template v-slot:entityId> Hub {{ id }}</template>
      <template v-slot:children-header>Devices</template>
      <template v-slot:childType>device</template>
      <template v-slot:parentOfChildType>Hub</template>
    </base-entity-details>
  </div>
</template>

<script>
import BaseEntityDetails from "@/slots/entitity-details/BaseEntityDetails";

export default {
  name: "Hub",

  components: { BaseEntityDetails },

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
          actions: {
            loader: "loadHub",
            deleter: "deleteHub",
            patcher: "patchHub",
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
            sender: "createDevice",
          },
        },

        parents: {
          namespace: "hubs",
          getters: {
            entities: "getEntitiesAsParents",
            page: "getPage",
          },
          actions: {
            loader: "loadEntities",
          },
        },

        logs: {
          namespace: "logs",
          getter: "getLogsInHub",
          loader: "loadLogs",
        },

        dataSeries: {
          namespace: "data",
          getter: "getDataInHub",
          loader: "loadDataSeries",
        },
      },

      ifHubDeletedRoute: { name: "hubs" },

      newDeviceProperties: [
        {
          type: "text",
          initialValue: "",
          label: "name",
        },
        {
          type: "select",
          initialValue: "",
          label: "deviceType",
          options: [
            { label: "Controlled Device", value: "CONTROLLED_DEVICE" }, //because label can differ from value
            { label: "Generator", value: "GENERATOR" },
            { label: "Both", value: "BOTH" },
          ],
        },
      ],
      buttonsProperties: {
        isEditVisible: false,
        isDeleteVisible: true,
      },
      queryEntity: {
        id: this.id,
        idName: "hubIds",
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
    goToDevice(deviceId) {
      this.$router.push({ name: "device", params: { id: deviceId } });
    },
  },
};
</script>

<style scoped>
.hub-container {
  display: flex;
  flex-direction: column;
  gap: 2.4rem;
}
</style>
