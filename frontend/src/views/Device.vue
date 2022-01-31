<template>
  <div class="device-container">
    <base-entity-details
      v-bind:id="id"
      v-bind:is-base-loaded="isDeviceLoaded"
      v-bind:is-base-error="isErrorInDevice"
      v-bind:transaction-mappings="transactionMappings"
      v-bind:if-base-deleted-route="isDeviceDeletedRoute"
      v-bind:new-child-properties="newControlSignalProperties"
      v-bind:buttons-properties="buttonsProperties"
      v-bind:query-entity="queryEntity"
      v-on:childClicked="onControlSignalClicked"
    >
      <template v-slot:icon>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          class="device-icon"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z"
          />
        </svg>
      </template>
      <template v-slot:entityId> Device {{ id }}</template>
      <template v-slot:children-header>Control signals</template>
      <template v-slot:childType>control signal</template>
      <template v-slot:parentOfChildType>Device</template>
    </base-entity-details>
  </div>
</template>

<script>
import BaseEntityDetails from "@/slots/entitity-details/BaseEntityDetails";
import { mapActions } from "vuex";

export default {
  name: "Device",
  components: { BaseEntityDetails },

  data() {
    return {
      isDeviceLoaded: false,
      isErrorInDevice: false,
      transactionMappings: {
        base: {
          namespace: "device",
          getters: {
            properties: "getProperties",
          },
          actions: {
            loader: "loadDevice",
            deleter: "deleteDevice",
            patcher: "patchDevice",
          },
        },

        children: {
          namespace: "controlSignals",
          getters: {
            entities: "getEntitiesAsChildren",
            page: "getPage",
          },
          actions: {
            loader: "loadEntities",
            deleter: "deleteControlSignal",
            sender: "createControlSignal",
          },
        },

        parents: {
          namespace: "devices",
          getters: {
            entities: "getEntitiesAsParents",
            page: "getPage",
          },
          actions: {
            loader: "loadEntities",
          },
        },

        child: {
          namespace: "controlSignal",
          getters: {
            entity: "getProperties",
          },
          actions: {
            loader: "loadControlSignal",
            patcher: "patchControlSignal",
          },
        },

        logs: {
          namespace: "logs",
          getter: "getLogsInDevice",
          loader: "loadLogs",
        },

        dataSeries: {
          namespace: "data",
          getter: "getDataInDevice",
          loader: "loadDataSeriesInEntity",
        },
      },

      isDeviceDeletedRoute: { name: "devices" },

      newControlSignalProperties: [
        {
          type: "text",
          initialValue: "",
          label: "name",
        },
        {
          type: "text",
          initialValue: "",
          label: "messageContent",
        },
      ],
      buttonsProperties: {
        isEditVisible: true,
        isDeleteVisible: true,
      },
      queryEntity: {
        id: this.id,
        idName: "deviceIds",
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
    ...mapActions("controlSignal", ["sendControlSignal"]),

    onControlSignalClicked(controlSignalId) {
      this.sendControlSignal({
        id: controlSignalId,
        ifSuccessHandler: () => {},
        ifErrorHandler: () => {},
      });
    },
  },
};
</script>

<style scoped>
.device-container {
  display: flex;
  flex-direction: column;
  gap: 2.4rem;
}
</style>
