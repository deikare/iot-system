<template>
  <div class="container">
    <main>
      <base-entity-page
        v-bind:entities-page="getEntitiesPage"
        v-bind:entity-link-generator-function="getHubLink"
        v-on:changePage="changePage"
        v-on:deactivateFilter="deactivateFilter"
        v-bind:active-filters="getActiveFilters"
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

  data() {
    return {
      hubs: [
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130002",
          name: "h1",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130003",
          name: "h2",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130004",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130005",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130006",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130007",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130008",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130009",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130014",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130024",
          name: "h3",
          state: "Started",
        },
        {
          id: "c5aa96be-57b3-11ec-bf63-0242ac130034",
          name: "h3",
          state: "Started",
        },
      ],
      nameQuery: "",
    };
  },

  watch: {
    page() {
      console.log("hub-list", this.page);
    },

    queriedName() {
      console.log("name-hub-list", this.queriedName);
    },

    nameQuery() {
      console.log(this.nameQuery);
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

    test: {
      type: String,
      required: false,
      default: "",
    },
  },

  computed: {
    getEntitiesPage() {
      return {
        entities: this.hubs.map((hub) => {
          return {
            type: "Hub",
            name: hub.name,
            id: hub.id,
            properties: [
              {
                key: "Id",
                value: hub.id,
              },
              {
                key: "State",
                value: hub.state,
              },
            ],
          };
        }),
        pagesNumber: 15,
        currentPage: Number(this.page),
      };
    },

    getActiveFilters() {
      let result = [];
      if (this.queriedName && this.queriedName !== "")
        result.push({
          id: 1,
          key: "name",
          value: this.queriedName,
        });

      if (this.test && this.test !== "")
        result.push({
          id: 2,
          key: "test",
          value: this.test,
        });

      return result;
    },
  },

  methods: {
    getHubLink(id) {
      return { name: "hub", params: { id: id } };
    },

    changePage(newPage) {
      let route = {
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
