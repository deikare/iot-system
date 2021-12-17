<template>
  <div class="container">
    <main>
      <base-entity-page
        v-bind:entities-page="getEntitiesPage"
        v-bind:entity-link-generator-function="getHubLink"
        v-on:changePage="changePage"
        v-on:deactivateFilter="deactivateFilter"
        v-bind:active-filters="getActiveFilters"
        v-bind:is-loaded="getLoaded"
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
import { mapGetters, mapActions } from "vuex";
export default {
  name: "HubList",

  components: { HubFiltering, BaseEntityPage },

  data() {
    return {
      entitiesPage: {},
      isLoaded: false,
      isError: false,
    };
  },

  watch: {
    page() {
      console.log("hub-list", this.page);
    },

    queriedName() {
      console.log("name-hub-list", this.queriedName);
    },
  },

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
  },

  computed: {
    ...mapGetters("hubs", ["getHubsPage"]),

    getActiveFilters() {
      let result = [];

      if (this.queriedName && this.queriedName !== "")
        result.push({
          id: 1,
          key: "name",
          value: this.queriedName,
        });

      return result;
    },

    getLoaded() {
      return this.isLoaded;
    },

    getError() {
      return this.isError;
    },

    getEntitiesPage() {
      return this.entitiesPage;
    },
  },

  methods: {
    ...mapActions("hubs", ["loadNewHubsPage"]),

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

    fetchData() {
      const queryParams = {
        page: Number(this.page) - 1,
        ...(this.queriedName !== "" && { name: this.queriedName }),
      };

      this.isLoaded = false;

      this.loadNewHubsPage({
        queryParams: queryParams,
        ifSuccessHandler: () => {
          this.isLoaded = true;
          this.isError = false;

          this.entitiesPage = this.getHubsPage;
        },
        ifErrorHandler: () => {
          this.isLoaded = true;
          this.isError = true;
          this.entitiesPage = {};
        },
      });
    },
  },

  created() {
    this.$watch(
      () => [this.page, this.queriedName],
      () => this.fetchData(),
      { immediate: true, deep: true }
    );
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
