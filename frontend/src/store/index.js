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

const getEntities = function (entities, mapperFunction) {
  return entities.map((entity) => mapperFunction(entity));
};

const getPage = function (page) {
  const result = {
    totalPages: page.totalPages,
    currentPage: page.number + 1,
  };
  console.log("Returning page", result);
  return result;
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

const axiosDelete = function (url) {
  const config = { timeout: 5000 };
  return axios.delete(url, config);
};

const axiosDeleteWithHandlers = function (
  url,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosDelete(url)
    .then(() => {
      console.log("Successfully deleted");
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log("Error occurred during get", error);
      ifErrorHandler();
    });
};

const axiosPatch = function (url, data) {
  const config = { timeout: 5000 };

  return axios.patch(url, data, config);
};

const axiosPatchWithHandlers = function (
  url,
  data,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosPatch(url, data)
    .then((response) => {
      console.log("Succesfully patched", response.data);
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log("Error occurred during get", error);
      ifErrorHandler();
    });
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
    loadEntities({ commit }, payload) {
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
    getEntities(state) {
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

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getPage(state) {
      return getPage(state.entitiesPage.page);
    },
  },
};

const devicesPageModule = {
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
      saveEntitiesPage(state, data, "devices");
    },
  },
  actions: {
    loadEntities({ commit }, payload) {
      const commitHandler = (data) => commit("saveEntitiesPage", data);

      loadEntities(
        "http://localhost:8080/devices",
        payload.queryParams,
        commitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteAllEntities({ commit }, payload) {
      console.log(commit, payload);
      axiosDeleteWithHandlers(
        "http://localhost:8080/devices",
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteDevice({ commit }, payload) {
      console.log(commit, payload);

      const url = `http://localhost:8080/devices/${payload.id}`;

      axiosDeleteWithHandlers(
        url,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getEntities(state) {
      const mapperFunction = (device) => {
        return {
          type: "Device",
          name: device.name,
          id: device["_links"].self.href.split("/").at(-1),
          properties: [
            {
              key: "Id",
              value: device["_links"].self.href.split("/").at(-1),
            },
            {
              key: "DeviceType",
              value: device.deviceType,
            },
          ],
        };
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getEntitiesAsChildren(state) {
      const mapperFunction = (device) => {
        return [
          { key: "name", value: device.name },
          { key: "id", value: device["_links"].self.href.split("/").at(-1) },
          // { key: "type", value: device.deviceType },
        ];
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getPage(state) {
      return getPage(state.entitiesPage.page);
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

    // eslint-disable-next-line no-unused-vars
    deleteHub({ commit }, payload) {
      const url = `http://localhost:8080/hubs/${payload.id}`;

      axiosDeleteWithHandlers(
        url,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    // eslint-disable-next-line no-unused-vars
    patchHub({ commit }, payload) {
      const url = `http://localhost:8080/hubs/${payload.id}`;

      axiosPatchWithHandlers(
        url,
        payload.data,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getProperties(state) {
      const result = [
        {
          type: "text",
          initialValue: state.hub.name,
          label: "name",
        },
        {
          type: "select",
          initialValue: state.hub.status,
          label: "status",
          options: [
            { label: "Started", value: "Started" }, //because label can differ from value
            { label: "Stopped", value: "Stopped" },
          ],
        },
      ];
      console.log("returning properties of hub", result);
      return result;
    },
  },
};

export default createStore({
  modules: {
    hubs: hubsPageModule,
    hub: hubModule,
    devices: devicesPageModule,
  },
});
