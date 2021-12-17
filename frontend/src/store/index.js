import { createStore } from "vuex";
import axios from "axios";

const loadEntities = function (
  url,
  queryParams,
  commit,
  ifSuccessHandler,
  ifErrorHandler
) {
  axios
    .get(url, {
      params: { ...queryParams, size: 16 },
      timeout: 5000,
    })
    .then((response) => {
      commit("saveEntitiesPage", response.data);
      console.log("Successfully fetched", response.data);
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log("Error occurred during get", error);
      ifErrorHandler();
    });
};

const getEntitiesPage = function (entities, mapperFunction, page) {
  return {
    entities: entities.map((entity) => mapperFunction(entity)),
    pagesNumber: page.totalPages,
    currentPage: page.number + 1,
  };
};

const saveEntitiesPage = function (state, data, entitiesContainerName) {
  state.entitiesPage = {
    ...state.entitiesPage,
    entities:
      Object.prototype.hasOwnProperty.call(data, "_embedded") &&
      Object.prototype.hasOwnProperty.call(
        data["_embedded"],
        entitiesContainerName
      )
        ? data["_embedded"][entitiesContainerName]
        : [],
    links: data["_links"],
    page: data["page"],
  };

  console.log("Successfully saved", state.entitiesPage);
};

const hubsPageModule = {
  namespaced: true,
  state() {
    return {
      entitiesPage: {
        entities: [],
        links: {},
        page: {},
      },
    };
  },
  mutations: {
    saveEntitiesPage(state, data) {
      saveEntitiesPage(state, data, "hubs");
    },
  },
  actions: {
    loadNewHubsPage({ commit }, payload) {
      loadEntities(
        "http://localhost:8080/hubs",
        payload.queryParams,
        commit,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },
  getters: {
    getHubsPage(state) {
      const mapperFunction = (hub) => {
        return {
          type: "Hub",
          name: hub.name,
          id: hub["_links"].self.href.split("/").at(-1),
          properties: [
            {
              key: "Id",
              value: hub["_links"].self.href.split("/").at(-1),
            },
            {
              key: "State",
              value: "Started",
              //  TODO add proper value in final backend
            },
          ],
        };
      };

      return getEntitiesPage(
        state.entitiesPage.entities,
        mapperFunction,
        state.entitiesPage.page
      );
    },
  },
};

export default createStore({
  modules: {
    hubs: hubsPageModule,
  },
});
