<template>
  <div>
    <main>
      <base-entity-page
        v-bind:transaction-mappings="transactionMappings"
        v-bind:entity-link-generator-function="getDeviceLink"
        v-bind:active-query="activeQuery"
        v-bind:page="page"
        v-on:changePage="changePage"
        v-on:deactivateFilter="deactivateFilter"
      >
        <template v-slot:filter-form>
          <device-search-bar v-on:newQuery="newQuery"></device-search-bar>
        </template>
      </base-entity-page>
    </main>
  </div>
</template>

<script>
import BaseEntityPage from "@/slots/entities-grid/BaseEntityPage";
import DeviceSearchBar from "@/components/entities-grid/devices/DeviceSearchBar";

export default {
  name: "DeviceGrid",
  components: { DeviceSearchBar, BaseEntityPage },

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
    };
  },

  methods: {
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
  },
};
</script>

<style scoped></style>
