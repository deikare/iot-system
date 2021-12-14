import { createRouter, createWebHistory } from "vue-router";
import NotFoundView from "../views/NotFoundView";
import Hub from "@/views/Hub";
import HubList from "@/views/HubList";
import DeviceList from "@/views/DeviceList";
import Device from "@/views/Device";
import Plots from "@/views/Plots";

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
      test: route.query.test,
      page: route.query.page,
    }),
    component: HubList,
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
    component: DeviceList,
  },
  {
    path: "/devices/:deviceId(\\d+)",
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
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from) => {
  console.log("Routing: ", from, to);
});

export default router;
