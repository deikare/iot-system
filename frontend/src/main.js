import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import clickOutside from "click-outside-vue3";

createApp(App).use(store).use(router).use(clickOutside).mount("#app");
