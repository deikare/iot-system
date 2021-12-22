import { createRouter, createWebHistory } from "vue-router";
import NotFoundView from "../views/NotFoundView";
import Hub from "@/views/Hub";
import HubGrid from "@/views/HubGrid";
import Device from "@/views/Device";
import Plots from "@/views/Plots";
import DeviceGrid from "@/views/DeviceGrid";

const routes = [
  {
    path: "/",
    redirect: "/hubs",
    name: "home",
  },
  //TODO add regexes to filter page query param in hubs and devices
  //TODO add redirect in routing when from /hubs to /hubs?page=1
  {
    path: "/hubs",
    name: "hubs",
    props: (route) => ({
      queriedName: route.query.name,
      page: route.query.page,
      activeQuery: {
        ...(typeof route.query.name !== "undefined" &&
          route.query.name !== "" && { name: route.query.name }),
      },
    }),
    component: HubGrid,
  },
  {
    path: "/hubs/:id(\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b)",
    name: "hub",
    component: Hub,
    props: true,
  },
  {
    path: "/devices",
    name: "devices",
    props: (route) => ({
      queriedName: route.query.name,
      queriedHubId: route.query.hubId,
      queriedDeviceType: route.query.deviceType,
      page: route.query.page,
      activeQuery: {
        ...(typeof route.query.name !== "undefined" &&
          route.query.name !== "" && { name: route.query.name }),
        ...(typeof route.query.hubId !== "undefined" &&
          route.query.hubId !== "" && { hubId: route.query.hubId }),
        ...(typeof route.query.deviceType !== "undefined" &&
          route.query.deviceType !== "" && {
            deviceType: route.query.deviceType,
          }),
      },
    }),
    component: DeviceGrid,
  },
  {
    path: "/devices/:id(\\d+)",
    name: "device",
    component: Device,
    props: true,
  },
  {
    path: "/plot",
    name: "plot",
    component: Plots,
  },
  {
    path: "/:notFound(.*)",
    name: "not-found",
    component: NotFoundView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

const isQueryValid = (query, allowed) => {
  for (const key in query) {
    if (!allowed.includes(key)) return false;
  }

  return true;
};

router.beforeEach((to, from, next) => {
  console.log("Routing from", from, "to", to);

  const allowedQuery = [];

  if (to.name === "hubs") allowedQuery.push("page", "name");

  if (to.name === "devices")
    allowedQuery.push("page", "name", "hubId", "deviceType");

  if (!isQueryValid(to.query, allowedQuery)) {
    console.log("Invalid query params, routing to not found");
    next({
      name: "not-found",
      params: { notFound: "/notFound" },
    });
  } else next();
});

export default router;
