import { createRouter, createWebHistory } from "vue-router";
import NotFoundView from "../views/NotFoundView";
import Hub from "@/views/Hub";
import HubGrid from "@/views/HubGrid";
import Device from "@/views/Device";
import DeviceGrid from "@/views/DeviceGrid";
import Data from "@/views/Data";
import Logs from "@/views/Logs";

const routes = [
  {
    path: "/",
    redirect: "/hubs",
    name: "home",
  },
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
    path: "/data",
    name: "data",
    props: (route) => ({
      queriedBucket: route.query.bucket,
      queriedStart: route.query.start,
      queriedEnd: route.query.end,
      queriedDescending: route.query.desc,
      queriedLimit: route.query.limit,
      queriedHubIds: route.query.hubIds,
      queriedDeviceIds: route.query.deviceIds,
      queriedMeasurementTypes: route.query.measurementTypes,
      activeQuery: {
        ...(typeof route.query.bucket !== "undefined" &&
          route.query.bucket !== "" && { bucket: route.query.bucket }),
        ...(typeof route.query.start !== "undefined" &&
          route.query.start !== "" && { start: route.query.start }),
        ...(typeof route.query.end !== "undefined" &&
          route.query.end !== "" && { end: route.query.end }),
        ...(typeof route.query.desc !== "undefined" &&
          route.query.desc !== "" && { desc: route.query.desc }),
        ...(typeof route.query.limit !== "undefined" &&
          route.query.limit !== "" && { limit: route.query.limit }),
        ...(typeof route.query.hubIds !== "undefined" &&
          route.query.hubIds !== "" && { hubIds: route.query.hubIds }),
        ...(typeof route.query.deviceIds !== "undefined" &&
          route.query.deviceIds !== "" && { deviceIds: route.query.deviceIds }),
        ...(typeof route.query.measurementTypes !== "undefined" &&
          route.query.measurementTypes !== "" && {
            measurementTypes: route.query.measurementTypes,
          }),
      },
    }),
    component: Data,
  },
  {
    path: "/logs",
    name: "logs",
    props: (route) => ({
      queriedStart: route.query.start,
      queriedEnd: route.query.end,
      queriedDescending: route.query.desc,
      queriedLimit: route.query.limit,
      queriedHubIds: route.query.hubIds,
      queriedDeviceIds: route.query.deviceIds,
      // activeQuery: {
      //   ...(typeof route.query.start !== "undefined" &&
      //     route.query.start !== "" && { start: route.query.start }),
      //   ...(typeof route.query.end !== "undefined" &&
      //     route.query.end !== "" && { end: route.query.end }),
      //   ...(typeof route.query.desc !== "undefined" &&
      //     route.query.desc !== "" && { desc: route.query.desc }),
      //   ...(typeof route.query.limit !== "undefined" &&
      //     route.query.limit !== "" && { limit: route.query.limit }),
      //   ...(typeof route.query.hubIds !== "undefined" &&
      //     route.query.hubIds !== "" && { hubIds: route.query.hubIds }),
      //   ...(typeof route.query.deviceIds !== "undefined" &&
      //     route.query.deviceIds !== "" && { deviceIds: route.query.deviceIds }),
      // },
    }),
    component: Logs,
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

  if (to.name === "data")
    allowedQuery.push(
      "bucket",
      "start",
      "stop",
      "desc",
      "limit",
      "hubIds",
      "deviceIds",
      "measurementTypes"
    );

  if (to.name === "logs")
    allowedQuery.push("start", "stop", "desc", "limit", "hubIds", "deviceIds");

  if (!isQueryValid(to.query, allowedQuery)) {
    console.log("Invalid query params, routing to not found");
    next({
      name: "not-found",
      params: { notFound: "/notFound" },
    });
  } else next();
});

export default router;
