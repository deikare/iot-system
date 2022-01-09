import { createStore } from "vuex";
import axios from "axios";

const sendErrorMessage = (messageCommitHandler, error) => {
  const message = { type: "error", content: "" };
  if (error.response)
    message.content = `Server responded with: ${error.response.data.error}`;
  else if (error.request) message.content = `Server not responding`;
  else message.content = "Unexpected error, please refresh";

  messageCommitHandler(message);
};

const getFullUrl = function (path) {
  const serverFQDN = "/backend";

  return `${serverFQDN}/${path}`;
};

const axiosLoad = function (path, queryParams) {
  const config = {
    params: { ...queryParams },
    timeout: 5000,
  };
  return axios.get(getFullUrl(path), config);
};

const axiosLoadWithHandlers = function (
  path,
  queryParams,
  commitHandler,
  messageCommitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosLoad(path, queryParams)
    .then((response) => {
      commitHandler(response.data);
      console.log("Successfully fetched", response.data);
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log(
        "Error occurred during get",
        error,
        error.response,
        error.request
      );
      sendErrorMessage(messageCommitHandler, error);
      ifErrorHandler(error);
    });
};

const loadEntities = function (
  path,
  queryParams,
  size,
  commitHandler,
  messageCommitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  const queryParamsWithSize = {
    ...queryParams,
    size: size,
  };

  axiosLoadWithHandlers(
    path,
    queryParamsWithSize,
    commitHandler,
    messageCommitHandler,
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

const axiosDelete = function (path) {
  const config = { timeout: 5000 };
  return axios.delete(getFullUrl(path), config);
};

const axiosDeleteWithHandlers = function (
  path,
  ifSuccessMessage,
  messageCommitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosDelete(path)
    .then(() => {
      console.log("Successfully deleted");
      messageCommitHandler({ type: "info", content: ifSuccessMessage });
      ifSuccessHandler();
    })
    .catch((error) => {
      console.log(
        "Error occurred during delete",
        error,
        error.response,
        error.request
      );
      sendErrorMessage(messageCommitHandler, error);
      ifErrorHandler(error);
    });
};

const axiosPost = function (path, data, queryParams) {
  const config = { params: { ...queryParams }, timeout: 5000 };

  return axios.post(getFullUrl(path), data, config);
};

const axiosPostWithHandlers = function (
  path,
  data,
  queryParams,
  ifSuccessMessage,
  messageCommitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosPost(path, data, queryParams)
    .then((response) => {
      console.log("Successfully posted", response.data);
      messageCommitHandler({ type: "info", content: ifSuccessMessage });

      ifSuccessHandler();
    })
    .catch((error) => {
      console.log(
        "Error occurred during post",
        error,
        error.response,
        error.request
      );
      sendErrorMessage(messageCommitHandler, error);
      ifErrorHandler(error);
    });
};

const axiosPatch = function (path, data) {
  const config = { timeout: 5000 };

  return axios.patch(getFullUrl(path), data, config);
};

const axiosPatchWithHandlers = function (
  path,
  data,
  ifSuccessMessage,
  messageCommitHandler,
  ifSuccessHandler,
  ifErrorHandler
) {
  axiosPatch(path, data)
    .then((response) => {
      console.log("Successfully patched", response.data);
      messageCommitHandler({ type: "info", content: ifSuccessMessage });

      ifSuccessHandler();
    })
    .catch((error) => {
      console.log(
        "Error occurred during patch",
        error,
        error.response,
        error.request
      );
      sendErrorMessage(messageCommitHandler, error);
      ifErrorHandler(error);
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

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      loadEntities(
        "hubs",
        payload.queryParams,
        payload.size,
        commitHandler,
        messageCommitHandler,
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
              value: hub.status,
            },
          ],
        };
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getEntitiesAsParents(state) {
      const mapperFunction = (hub) => {
        return [
          { key: "name", value: hub.name },
          { key: "id", value: hub["_links"].self.href.split("/").at(-1) },
        ];
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

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      if (payload.queryParams["parentId"] !== undefined) {
        const parentId = payload.queryParams["parentId"];
        delete payload.queryParams["parentId"];
        payload.queryParams["hubId"] = parentId;
      }

      loadEntities(
        "devices",
        payload.queryParams,
        payload.size,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteDevice({ commit }, payload) {
      const path = `devices/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosDeleteWithHandlers(
        path,
        `Successfully deleted device ${payload.id}`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    createDevice({ commit }, payload) {
      const path = `devices`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      const queryParams = {
        hubId: payload.parentId,
      };

      axiosPostWithHandlers(
        path,
        payload.entity,
        queryParams,
        `Successfully created device`,
        messageCommitHandler,
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
        ];
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getEntitiesAsParents(state) {
      const mapperFunction = (device) => {
        return [
          { key: "name", value: device.name },
          { key: "id", value: device["_links"].self.href.split("/").at(-1) },
        ];
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getPage(state) {
      return getPage(state.entitiesPage.page);
    },
  },
};

const controlSignalsPageModule = {
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
      saveEntitiesPage(state, data, "controlSignals");
    },
  },
  actions: {
    loadEntities({ commit }, payload) {
      const commitHandler = (data) => commit("saveEntitiesPage", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      if (payload.queryParams["parentId"] !== undefined) {
        const parentId = payload.queryParams["parentId"];
        delete payload.queryParams["parentId"];
        payload.queryParams["deviceId"] = parentId;
      }

      loadEntities(
        "control_signals",
        payload.queryParams,
        payload.size,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteControlSignal({ commit }, payload) {
      const path = `control_signals/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosDeleteWithHandlers(
        path,
        `Successfully deleted control signal ${payload.id}`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    createControlSignal({ commit }, payload) {
      const path = `control_signals`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      const queryParams = {
        deviceId: payload.parentId,
      };

      axiosPostWithHandlers(
        path,
        payload.entity,
        queryParams,
        `Successfully created control signal`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getEntities(state) {
      const mapperFunction = (controlSignal) => {
        return {
          type: "ControlSignal",
          name: controlSignal.name,
          id: controlSignal["_links"].self.href.split("/").at(-1),
          properties: [
            {
              key: "Id",
              value: controlSignal["_links"].self.href.split("/").at(-1),
            },
            {
              key: "MessageContent",
              value: controlSignal.messageContent,
            },
          ],
        };
      };

      return getEntities(state.entitiesPage.entities, mapperFunction);
    },

    getEntitiesAsChildren(state) {
      const mapperFunction = (controlSignal) => {
        return [
          { key: "name", value: controlSignal.name },
          {
            key: "id",
            value: controlSignal["_links"].self.href.split("/").at(-1),
          },
          {
            key: "messageContent",
            value: controlSignal.messageContent,
          },
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
      },
    };
  },
  mutations: {
    saveHub(state, data) {
      state.hub = {
        ...state.hub,
        name: data.name,
        id: data["_links"].self.href.split("/").at(-1),
        status: data.status,
      };

      console.log("Successfully saved state", state);
    },
  },
  actions: {
    loadHub({ commit }, payload) {
      const path = `hubs/${payload.id}`;
      const commitHandler = (data) => commit("saveHub", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      loadEntities(
        path,
        {},
        null,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteHub({ commit }, payload) {
      const path = `hubs/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosDeleteWithHandlers(
        path,
        `Successfully deleted hub ${payload.id}`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    patchHub({ commit }, payload) {
      const path = `hubs/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosPatchWithHandlers(
        path,
        payload.data,
        `Successfully updated hub ${payload.id}`,
        messageCommitHandler,
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
            { label: "Started", value: "STARTED" }, //because label can differ from value
            { label: "Stopped", value: "STOPPED" },
          ],
        },
      ];
      console.log("returning properties of hub", result);
      return result;
    },
  },
};

const deviceModule = {
  namespaced: true,
  state() {
    return {
      device: {
        name: "",
        id: "",
        deviceType: "",
      },
    };
  },
  mutations: {
    saveDevice(state, data) {
      state.device = {
        ...state.device,
        name: data.name,
        id: data["_links"].self.href.split("/").at(-1),
        deviceType: data.deviceType,
      };

      console.log("Successfully saved state", state);
    },
  },
  actions: {
    loadDevice({ commit }, payload) {
      const path = `devices/${payload.id}`;
      const commitHandler = (data) => commit("saveDevice", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      loadEntities(
        path,
        {},
        null,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    deleteDevice({ commit }, payload) {
      const path = `devices/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosDeleteWithHandlers(
        path,
        `Successfully deleted device ${payload.id}`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    patchDevice({ commit }, payload) {
      const path = `devices/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosPatchWithHandlers(
        path,
        payload.data,
        `Successfully updated device ${payload.id}`,
        messageCommitHandler,
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
          initialValue: state.device.name,
          label: "name",
        },
        {
          type: "select",
          initialValue: state.device.deviceType,
          label: "deviceType",
          options: [
            { label: "Controlled device", value: "CONTROLLED_DEVICE" }, //because label can differ from value
            { label: "Generator", value: "GENERATOR" },
            { label: "Both", value: "BOTH" },
          ],
        },
      ];
      console.log("returning properties of device", result);
      return result;
    },
  },
};

const controlSignalModule = {
  namespaced: true,
  state() {
    return {
      controlSignal: {
        name: "",
        id: "",
        messageContent: "",
      },
    };
  },
  mutations: {
    saveControlSignal(state, data) {
      state.controlSignal = {
        ...state.controlSignal,
        name: data.name,
        id: data["_links"].self.href.split("/").at(-1),
        messageContent: data.messageContent,
      };

      console.log("Successfully saved state", state);
    },
  },
  actions: {
    loadControlSignal({ commit }, payload) {
      const path = `control_signals/${payload.id}`;
      const commitHandler = (data) => commit("saveControlSignal", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      loadEntities(
        path,
        {},
        null,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    patchControlSignal({ commit }, payload) {
      const path = `control_signals/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosPatchWithHandlers(
        path,
        payload.data,
        `Successfully updated control signal ${payload.id}`,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },

    sendControlSignal({ commit }, payload) {
      const path = `send_device_control/${payload.id}`;

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosPostWithHandlers(
        path,
        {},
        {},
        `Successfully sent control signal ${payload.id}`,
        messageCommitHandler,
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
          initialValue: state.controlSignal.name,
          label: "name",
        },
        {
          type: "text",
          initialValue: state.controlSignal.messageContent,
          label: "messageContent",
        },
      ];
      console.log("returning properties of control signal", result);
      return result;
    },
  },
};

const logseriesModule = {
  namespaced: true,
  state() {
    return {
      logsPage: {
        logs: [],
        links: {},
      },
    };
  },
  mutations: {
    saveLogsPage(state, data) {
      state.logsPage = {
        ...state.logsPage,
        logs:
          Object.prototype.hasOwnProperty.call(data, "logseries") &&
          Object.prototype.hasOwnProperty.call(data["logseries"], "logs")
            ? data["logseries"]["logs"]
            : [],
        links: data["_links"],
      };

      console.log("Successfully saved", state.logsPage);
    },
  },
  actions: {
    loadLogs({ commit }, payload) {
      const commitHandler = (data) => commit("saveLogsPage", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      console.log(payload.queryParams);

      axiosLoadWithHandlers(
        "logs",
        payload.queryParams,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },
  getters: {
    getLogsInHub(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: new Date(log.time).toLocaleString() },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },

    getLogsInDevice(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: new Date(log.time).toLocaleString() },
          { key: "hubId", value: log.hubId },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },

    getLogs(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: new Date(log.time).toLocaleString() },
          { key: "hubId", value: log.hubId },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },
  },
};

const getChartTime = (isoTime) => {
  return new Date(isoTime).getTime();
};

const getColor = (shuffledColorsArray, index) => {
  return shuffledColorsArray[index % shuffledColorsArray.length];
};

const shuffleColors = () => {
  return [
    "#6e40aa",
    "#7140ab",
    "#743fac",
    "#773fad",
    "#7a3fae",
    "#7d3faf",
    "#803eb0",
    "#833eb0",
    "#873eb1",
    "#8a3eb2",
    "#8d3eb2",
    "#903db2",
    "#943db3",
    "#973db3",
    "#9a3db3",
    "#9d3db3",
    "#a13db3",
    "#a43db3",
    "#a73cb3",
    "#aa3cb2",
    "#ae3cb2",
    "#b13cb2",
    "#b43cb1",
    "#b73cb0",
    "#ba3cb0",
    "#be3caf",
    "#c13dae",
    "#c43dad",
    "#c73dac",
    "#ca3dab",
    "#cd3daa",
    "#d03ea9",
    "#d33ea7",
    "#d53ea6",
    "#d83fa4",
    "#db3fa3",
    "#de3fa1",
    "#e040a0",
    "#e3409e",
    "#e5419c",
    "#e8429a",
    "#ea4298",
    "#ed4396",
    "#ef4494",
    "#f14592",
    "#f34590",
    "#f5468e",
    "#f7478c",
    "#f9488a",
    "#fb4987",
    "#fd4a85",
    "#fe4b83",
    "#ff4d80",
    "#ff4e7e",
    "#ff4f7b",
    "#ff5079",
    "#ff5276",
    "#ff5374",
    "#ff5572",
    "#ff566f",
    "#ff586d",
    "#ff596a",
    "#ff5b68",
    "#ff5d65",
    "#ff5e63",
    "#ff6060",
    "#ff625e",
    "#ff645b",
    "#ff6659",
    "#ff6857",
    "#ff6a54",
    "#ff6c52",
    "#ff6e50",
    "#ff704e",
    "#ff724c",
    "#ff744a",
    "#ff7648",
    "#ff7946",
    "#ff7b44",
    "#ff7d42",
    "#ff8040",
    "#ff823e",
    "#ff843d",
    "#ff873b",
    "#ff893a",
    "#ff8c38",
    "#ff8e37",
    "#fe9136",
    "#fd9334",
    "#fb9633",
    "#f99832",
    "#f89b32",
    "#f69d31",
    "#f4a030",
    "#f2a32f",
    "#f0a52f",
    "#eea82f",
    "#ecaa2e",
    "#eaad2e",
    "#e8b02e",
    "#e6b22e",
    "#e4b52e",
    "#e2b72f",
    "#e0ba2f",
    "#debc30",
    "#dbbf30",
    "#d9c131",
    "#d7c432",
    "#d5c633",
    "#d3c934",
    "#d1cb35",
    "#cece36",
    "#ccd038",
    "#cad239",
    "#c8d53b",
    "#c6d73c",
    "#c4d93e",
    "#c2db40",
    "#c0dd42",
    "#bee044",
    "#bce247",
    "#bae449",
    "#b8e64b",
    "#b6e84e",
    "#b5ea51",
    "#b3eb53",
    "#b1ed56",
    "#b0ef59",
    "#adf05a",
    "#aaf159",
    "#a6f159",
    "#a2f258",
    "#9ef258",
    "#9af357",
    "#96f357",
    "#93f457",
    "#8ff457",
    "#8bf457",
    "#87f557",
    "#83f557",
    "#80f558",
    "#7cf658",
    "#78f659",
    "#74f65a",
    "#71f65b",
    "#6df65c",
    "#6af75d",
    "#66f75e",
    "#63f75f",
    "#5ff761",
    "#5cf662",
    "#59f664",
    "#55f665",
    "#52f667",
    "#4ff669",
    "#4cf56a",
    "#49f56c",
    "#46f46e",
    "#43f470",
    "#41f373",
    "#3ef375",
    "#3bf277",
    "#39f279",
    "#37f17c",
    "#34f07e",
    "#32ef80",
    "#30ee83",
    "#2eed85",
    "#2cec88",
    "#2aeb8a",
    "#28ea8d",
    "#27e98f",
    "#25e892",
    "#24e795",
    "#22e597",
    "#21e49a",
    "#20e29d",
    "#1fe19f",
    "#1edfa2",
    "#1ddea4",
    "#1cdca7",
    "#1bdbaa",
    "#1bd9ac",
    "#1ad7af",
    "#1ad5b1",
    "#1ad4b4",
    "#19d2b6",
    "#19d0b8",
    "#19cebb",
    "#19ccbd",
    "#19cabf",
    "#1ac8c1",
    "#1ac6c4",
    "#1ac4c6",
    "#1bc2c8",
    "#1bbfca",
    "#1cbdcc",
    "#1dbbcd",
    "#1db9cf",
    "#1eb6d1",
    "#1fb4d2",
    "#20b2d4",
    "#21afd5",
    "#22add7",
    "#23abd8",
    "#25a8d9",
    "#26a6db",
    "#27a4dc",
    "#29a1dd",
    "#2a9fdd",
    "#2b9cde",
    "#2d9adf",
    "#2e98e0",
    "#3095e0",
    "#3293e1",
    "#3390e1",
    "#358ee1",
    "#378ce1",
    "#3889e1",
    "#3a87e1",
    "#3c84e1",
    "#3d82e1",
    "#3f80e1",
    "#417de0",
    "#437be0",
    "#4479df",
    "#4676df",
    "#4874de",
    "#4a72dd",
    "#4b70dc",
    "#4d6ddb",
    "#4f6bda",
    "#5169d9",
    "#5267d7",
    "#5465d6",
    "#5663d5",
    "#5761d3",
    "#595fd1",
    "#5a5dd0",
    "#5c5bce",
    "#5d59cc",
    "#5f57ca",
    "#6055c8",
    "#6153c6",
    "#6351c4",
    "#6450c2",
    "#654ec0",
    "#664cbe",
    "#674abb",
    "#6849b9",
    "#6a47b7",
    "#6a46b4",
    "#6b44b2",
    "#6c43af",
    "#6d41ad",
    "#6e40aa",
  ]
    .map((color) => ({ color, sort: Math.random() }))
    .sort((colorA, colorB) => colorA.sort - colorB.sort)
    .map(({ color }) => color);
};

const dataSeriesModule = {
  namespaced: true,
  state() {
    return {
      dataSeries: {
        allSeries: [],
        start: "",
        end: "",
        links: {},
        // series: [{ hubId: "", deviceId: "", measurementType: "", data: [] }],
      },
    };
  },
  mutations: {
    saveDataSeries(state, data) {
      state.dataSeries = {
        ...state.dataSeries,
        allSeries:
          Object.prototype.hasOwnProperty.call(data, "timeseriesList") &&
          Object.prototype.hasOwnProperty.call(
            data["timeseriesList"],
            "timeseriesList"
          )
            ? Object.entries(data["timeseriesList"]["timeseriesList"])
            : [],

        start:
          Object.prototype.hasOwnProperty.call(data, "timeseriesList") &&
          Object.prototype.hasOwnProperty.call(data["timeseriesList"], "start")
            ? data["timeseriesList"].start
            : "",

        end:
          Object.prototype.hasOwnProperty.call(data, "timeseriesList") &&
          Object.prototype.hasOwnProperty.call(data["timeseriesList"], "end")
            ? data["timeseriesList"].end
            : "",

        links: data["_links"],
      };

      console.log("Successfully saved", state.dataSeries);
    },
  },
  actions: {
    loadDataSeries({ commit }, payload) {
      const commitHandler = (data) => commit("saveDataSeries", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      console.log(payload.queryParams);

      axiosLoadWithHandlers(
        "data",
        payload.queryParams,
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getDataInHub(state) {
      const shuffledColors = shuffleColors();
      return {
        start: getChartTime(state.dataSeries.start),
        end: getChartTime(state.dataSeries.end),
        labels: [
          getChartTime(state.dataSeries.start),
          getChartTime(state.dataSeries.end),
        ],
        datasets: state.dataSeries.allSeries.map((series, index) => {
          const tagsSplit = series[0].split(/[,{}=]/);

          return {
            label: `hubId=${tagsSplit[4]},deviceId=${tagsSplit[8]},measurement=${tagsSplit[6]}`,
            data: series[1].points
              .map((point) => {
                return {
                  x: getChartTime(point.time),
                  y: point.value.toFixed(3),
                };
              })
              .reverse(),
            borderColor: getColor(shuffledColors, index),
            tension: 0.1,
          };
        }),
      };
    },

    getDataInDevice(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: new Date(log.time).toLocaleString() },
          { key: "value", value: log.value },
        ];
      });
    },

    getData(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: new Date(log.time).toLocaleString() },
          { key: "hubId", value: log.hubId },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },
  },
};

const dataTagsModule = {
  namespaced: true,
  state() {
    return {
      tags: {
        hubIds: [],
        deviceIds: [],
        measurementTypes: [],
      },
    };
  },
  mutations: {
    saveTags(state, data) {
      state.tags = {
        ...state.tags,
        hubIds:
          Object.prototype.hasOwnProperty.call(data, "bucket") &&
          Object.prototype.hasOwnProperty.call(data["bucket"], "tagMap") &&
          Object.prototype.hasOwnProperty.call(
            data["bucket"]["tagMap"],
            "InfluxTagKey{name='hubId'}"
          )
            ? data["bucket"]["tagMap"]["InfluxTagKey{name='hubId'}"]
            : [],
        deviceIds:
          Object.prototype.hasOwnProperty.call(data, "bucket") &&
          Object.prototype.hasOwnProperty.call(data["bucket"], "tagMap") &&
          Object.prototype.hasOwnProperty.call(
            data["bucket"]["tagMap"],
            "InfluxTagKey{name='deviceId'}"
          )
            ? data["bucket"]["tagMap"]["InfluxTagKey{name='deviceId'}"]
            : [],
        measurementTypes:
          Object.prototype.hasOwnProperty.call(data, "bucket") &&
          Object.prototype.hasOwnProperty.call(data["bucket"], "tagMap") &&
          Object.prototype.hasOwnProperty.call(
            data["bucket"]["tagMap"],
            "InfluxTagKey{name='type'}"
          )
            ? data["bucket"]["tagMap"]["InfluxTagKey{name='type'}"]
            : [],
      };

      console.log("Successfully saved", state.tags);
    },
  },

  actions: {
    loadTags({ commit }, payload) {
      const path = `buckets/${payload.dataBucket}`;
      const commitHandler = (data) => commit("saveTags", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosLoadWithHandlers(
        path,
        {},
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getTags(state) {
      return state.tags;
    },
  },
};

const logTagsModule = {
  namespaced: true,
  state() {
    return {
      tags: {
        hubIds: [],
        deviceIds: [],
      },
    };
  },
  mutations: {
    saveTags(state, data) {
      state.tags = {
        ...state.tags,
        hubIds:
          Object.prototype.hasOwnProperty.call(data, "bucket") &&
          Object.prototype.hasOwnProperty.call(data["bucket"], "tagMap") &&
          Object.prototype.hasOwnProperty.call(
            data["bucket"]["tagMap"],
            "InfluxTagKey{name='hubId'}"
          )
            ? data["bucket"]["tagMap"]["InfluxTagKey{name='hubId'}"].values
            : [],
        deviceIds:
          Object.prototype.hasOwnProperty.call(data, "bucket") &&
          Object.prototype.hasOwnProperty.call(data["bucket"], "tagMap") &&
          Object.prototype.hasOwnProperty.call(
            data["bucket"]["tagMap"],
            "InfluxTagKey{name='deviceId'}"
          )
            ? data["bucket"]["tagMap"]["InfluxTagKey{name='deviceId'}"].values
            : [],
      };

      console.log("Successfully saved", state.tags);
    },
  },

  actions: {
    loadTags({ commit }, payload) {
      const path = "buckets/logs";
      const commitHandler = (data) => commit("saveTags", data);

      const messageCommitHandler = (message) => {
        commit("messages/add", message, { root: true });
      };

      axiosLoadWithHandlers(
        path,
        {},
        commitHandler,
        messageCommitHandler,
        payload.ifSuccessHandler,
        payload.ifErrorHandler
      );
    },
  },

  getters: {
    getTags(state) {
      return state.tags;
    },
  },
};

const messagesModule = {
  namespaced: true,
  state() {
    return {
      messages: new Map(),
      messagesChangeTracker: 0, //so vue sees map for reactivity
    };
  },

  mutations: {
    add(state, payload) {
      state.messages.set(
        Date.now().toString(36) + Math.random().toString(36).substr(2),
        { type: payload.type, content: payload.content }
      );
      state.messagesChangeTracker += 1;
      console.log("Added message", payload.content);
    },
    remove(state, id) {
      if (state.messages.delete(id)) {
        state.messagesChangeTracker -= 1;
        console.log("Deleted message of", id);
      }
    },
    clear(state) {
      state.messages.clear();
      state.messagesChangeTracker = 0;
      console.log("Clear toasts", state.messagesChangeTracker);
    },
  },

  getters: {
    getMessages(state) {
      return (
        state.messagesChangeTracker &&
        Array.from(state.messages)
          .reverse()
          .map((entry) => {
            return {
              id: entry[0],
              type: entry[1].type,
              content: entry[1].content,
            };
          })
      );
    },
  },
};

export default createStore({
  modules: {
    hubs: hubsPageModule,
    hub: hubModule,
    devices: devicesPageModule,
    device: deviceModule,
    controlSignals: controlSignalsPageModule,
    controlSignal: controlSignalModule,
    logs: logseriesModule,
    logTags: logTagsModule,
    data: dataSeriesModule,
    dataTags: dataTagsModule,
    messages: messagesModule,
  },
});
