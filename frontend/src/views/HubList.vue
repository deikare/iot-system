<template>
  <div class="container">
    <form>
      <input
        name="name"
        type="text"
        placeholder="Find a hub named..."
        v-model="nameQuery"
      />
      <button>Submit</button>
    </form>

    <main>
      <base-entity-grid
        v-bind:entities="getEntities"
        v-bind:link-generator-function="getHubLink"
      ></base-entity-grid>

      <base-paginator
        v-bind:n="15"
        v-bind:short-list-length="4"
        v-bind:link-generator-function="getHubPaginatorLink"
        v-bind:current-page="page"
      ></base-paginator>
    </main>
  </div>
</template>

<script>
import BaseEntityGrid from "@/slots/BaseEntityGrid";
import BasePaginator from "@/slots/BasePaginator";
export default {
  name: "HubList",

  components: { BasePaginator, BaseEntityGrid },

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
  },

  props: ["page", "name"],

  computed: {
    getEntities() {
      return this.hubs.map((hub) => {
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
      });
    },
  },

  methods: {
    getHubLink(id) {
      return { name: "hub", params: { id: id } };
    },

    getHubPaginatorLink(index) {
      return { name: "hubs", query: { page: index } };
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
.container {
  display: flex;
  flex-direction: column;
  gap: 1.6rem;
}

form {
  display: flex;
  gap: 0.4rem;
}

input {
  font-size: 1.8rem;
  color: var(--main-color);
  background-color: var(--background-color);
  border: 1px solid var(--main-color);
}

button {
  font-size: 1.8rem;
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
}
</style>
