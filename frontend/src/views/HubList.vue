<template>
  <div class="container">
    <main>
      <base-entity-page
        v-bind:transaction-mappings="transactionMappings"
        v-bind:entity-link-generator-function="getHubLink"
        v-bind:active-query="activeQuery"
        v-bind:page="page"
        v-on:changePage="changePage"
        v-on:deactivateFilter="deactivateFilter"
      >
        <template v-slot:filter-form>
          <hub-filtering v-on:newQuery="newQuery"></hub-filtering>
        </template>
      </base-entity-page>
    </main>
  </div>
</template>

<script>
import BaseEntityPage from "@/slots/BaseEntityPage";
import HubFiltering from "@/slots/HubFiltering";
export default {
  name: "HubList",

  components: { HubFiltering, BaseEntityPage },

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

    activeQuery: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },

  computed: {
    transactionMappings() {
      return {
        getters: {
          namespace: "hubs",
          entities: "getEntities",
          page: "getPage",
        },
        actions: {
          namespace: "hubs",
          loader: "loadEntities",
        },
      };
    },
  },

  methods: {
    getHubLink(id) {
      return { name: "hub", params: { id: id } };
    },

    changePage(newPage) {
      const route = {
        name: "hubs",
        query: {
          page: newPage,
          ...(this.queriedName !== "" && { name: this.queriedName }),
        },
      };

      this.$router.push(route);
    },

    deactivateFilter(key) {
      let route = {
        name: "hubs",
        query: {
          page: 1,
        },
      };
      if (key === "name") this.$router.push(route);
    },
    newQuery(newQueryName) {
      let route = {
        name: "hubs",
        query: {
          page: 1,
          name: newQueryName,
        },
      };

      this.$router.push(route);
    },
  },
};
</script>

<style scoped>
/*
SPACING SYSTEM (px)
2 / 4 / 8 / 12 / 16 / 24 / 32 / 48 / 64 / 80 / 96 / 128

FONT SIZE SYSTEM (px)
10 / 12 / 14 / 16 / 18 / 20 / 24 / 30 / 36 / 44 / 52 / 62 / 74 / 86 / 98
*/
</style>
