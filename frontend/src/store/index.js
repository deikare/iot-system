import { createStore } from "vuex";
import axios from "axios";

const axiosLoad = function (url, queryParams) {
  const config = { params: { ...queryParams }, timeout: 5000 };
  return axios.get(url, config);
};

const axiosLoadWithHandlers = function (
  url,
  queryParams,
  commitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosLoad(url, queryParams)
    .then((response) => {
      commitHandler(response.data);
      console.log("Successfully fetched", response.data);
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log("Error occurred during get", error);
      ifErrorHandler();
    });
};

const loadEntities = function (
  url,
  queryParams,
  commitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  const queryParamsWithSize = {
    ...queryParams,
    size: 16,
  };

  axiosLoadWithHandlers(
    url,
    queryParamsWithSize,
    commitHandler,
    ifSuccessHandler,
    ifErrorHandler
  );
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
    loadEntitiesPage({ commit }, payload) {
      const commitHandler = (data) => commit("saveEntitiesPage", data);

      loadEntities(
        "http://localhost:8080/hubs",
        payload.queryParams,
        commitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },
  getters: {
    getEntitiesPage(state) {
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
              key: "Status",
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

const hubModule = {
  namespaced: true,
  state() {
    return {
      hub: {
        name: "",
        id: "",
        status: "",
        devices: [],
      },
    };
  },
  mutations: {
    saveHub(state, data) {
      state.hub = {
        ...state.hub,
        name: data.name,
        id: data["_links"].self.href.split("/").at(-1),
        status: "Started",
        devices: [],
      };

      console.log("Successfully saved state", state);
    },
  },
  actions: {
    loadHub({ commit }, payload) {
      const url = `http://localhost:8080/hubs/${payload.id}`;
      const commitHandler = (data) => commit("saveHub", data);

      loadEntities(
        url,
        {},
        commitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );

      // axiosLoad(url, {})
      //   .then((response) => {
      //     console.log(response.data);
      //     commit("saveHub", response.data);
      //     return axiosLoad(
      //       response.data["_links"].devices.href.split("{").at(0),
      //       {}
      //     );
      //   })
      //   .then((response) => {
      //     console.log(response.data);
      //   });
    },
  },

  getters: {
    getProperties(state) {
      const result = {
        name: state.hub.name,
        status: state.hub.status,
      };
      console.log("returning properties of hub", result);
      return result;
    },

    displayLock(state) {
      return state.hub.status === "Started";
    },

    displayUnlock(state) {
      return state.hub.status === "Stopped";
    },
  },
};

export default createStore({
  modules: {
    hubs: hubsPageModule,
    hub: hubModule,
  },
});
