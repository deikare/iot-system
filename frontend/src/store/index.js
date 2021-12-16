import { createStore } from "vuex";
import axios from "axios";

const hubsPageModule = {
  namespaced: true,
  state() {
    return {
      hubsPage: {
        _embedded: {},
        _links: {},
        page: {},
      },
    };
  },
  mutations: {
    saveNewHubsPage(state, data) {
      state.hubsPage = {
        ...state.hubsPage,
        _embedded: data["_embedded"],
        _links: data["_links"],
        page: data["page"],
      };
    },
  },
  actions: {
    loadNewHubsPage({ commit }, payload) {
      const queryParams = payload.queryParams;
      //TODO add function handler on success and on error
      return axios
        .get("http://localhost:8080/hubs", {
          params: { ...queryParams, size: 16 },
        })
        .then((response) => commit("saveNewHubsPage", response.data));
    },
  },
  getters: {
    getHubsPage(state) {
      return {
        entities: state.hubsPage["_embedded"].hubs.map((hub) => {
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
        }),

        pagesNumber: state.hubsPage["page"].totalPages,
        currentPage: state.hubsPage["page"].number + 1,
      };
    },
  },
};

export default createStore({
  modules: {
    hubs: hubsPageModule,
  },
});
