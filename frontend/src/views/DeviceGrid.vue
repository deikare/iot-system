<template>
  <div>
    <main>
      <teleport to=".main-with-margin">
        <entity-creator
          class="device-creator"
          v-if="isAddDeviceVisible"
          v-bind:entity-properties="newDeviceProperties"
          v-bind:transaction-mappings="addDeviceTransactionMappings"
          v-click-outside="closeAddDevice"
          v-on:closeComponent="closeAddDevice"
          v-on:newEntityCreated="refresh"
        >
          <template v-slot:entityType> device </template>
          <template v-slot:parentType> Hub </template>
        </entity-creator>
      </teleport>
      <base-entity-page
        v-bind:transaction-mappings="transactionMappings"
        v-bind:entity-link-generator-function="getDeviceLink"
        v-bind:active-query="activeQuery"
        v-bind:page="page"
        v-on:changePage="changePage"
        v-on:deactivateFilter="deactivateFilter"
      >
        <template v-slot:filter-form>
          <device-search-bar
            v-on:newQuery="newQuery"
            v-on:openAddDevice="openAddDevice"
          ></device-search-bar>
        </template>
      </base-entity-page>
    </main>
  </div>
</template>

<script>
import BaseEntityPage from "@/slots/entities-grid/BaseEntityPage";
import DeviceSearchBar from "@/components/entities-grid/devices/DeviceSearchBar";
import EntityCreator from "@/slots/entities-creator/EntityCreator";

export default {
  name: "DeviceGrid",
  components: { EntityCreator, DeviceSearchBar, BaseEntityPage },

  props: {
    page: {
      type: String,
      required: true,
      default: "1",
    },

    queriedName: {
      type: String,
      required: false,
      default: "",
    },

    queriedHubId: {
      type: String,
      required: false,
      default: "",
    },

    queriedDeviceType: {
      type: String,
      required: false,
      default: "",
    },

    activeQuery: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },

  data() {
    return {
      isAddDeviceVisible: false,
      transactionMappings: {
        getters: {
          namespace: "devices",
          entities: "getEntities",
          page: "getPage",
        },
        actions: {
          namespace: "devices",
          loader: "loadEntities",
        },
      },

      addDeviceTransactionMappings: {
        entity: {
          namespace: "devices",
          actions: {
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
      },

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
    };
  },

  methods: {
    closeAddDevice() {
      this.isAddDeviceVisible = false;
    },

    openAddDevice() {
      this.isAddDeviceVisible = true;
    },

    getDeviceLink(id) {
      return { name: "device", params: { id: id } };
    },

    changePage(newPage) {
      const route = {
        name: "devices",
        query: {
          page: newPage,
          ...this.activeQuery,
        },
      };

      this.$router.push(route);
    },

    deactivateFilter(key) {
      const newQuery = Object.assign(this.activeQuery);
      delete newQuery[key];

      let route = {
        name: "devices",
        query: {
          page: 1,
          ...newQuery,
        },
      };
      this.$router.push(route);
    },
    newQuery(event) {
      console.log(event);
      let route = {
        name: "devices",
        query: {
          page: 1,
          ...event,
        },
      };

      console.log(route);

      this.$router.push(route);
    },

    refresh() {
      this.$router.go(0);
    },
  },
};
</script>

<style scoped>
.device-creator {
  position: absolute;
  left: 50%;
  transform: translate(-50%, 0);
}
</style>
