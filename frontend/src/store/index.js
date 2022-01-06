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
  const config = { params: { ...queryParams }, timeout: 5000 };
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
          { key: "time", value: log.time },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },

    getLogsInDevice(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: log.time },
          { key: "hubId", value: log.hubId },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },

    getLogs(state) {
      return state.logsPage.logs.map((log) => {
        return [
          { key: "time", value: log.time },
          { key: "hubId", value: log.hubId },
          { key: "deviceId", value: log.deviceId },
          { key: "value", value: log.value },
        ];
      });
    },
  },
};

const deviceTagsModule = {
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
    deviceTags: deviceTagsModule,
    messages: messagesModule,
  },
});
