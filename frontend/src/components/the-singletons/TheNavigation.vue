<template>
  <div id="the-header">
    <header>
      <h1>Iot System</h1>
    </header>
    <nav>
      <router-link v-bind:to="hubsLink">
        <div class="link">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M19 11H5m14 0a2 2 0 012 2v6a2 2 0 01-2 2H5a2 2 0 01-2-2v-6a2 2 0 012-2m14 0V9a2 2 0 00-2-2M5 11V9a2 2 0 012-2m0 0V5a2 2 0 012-2h6a2 2 0 012 2v2M7 7h10"
            />
          </svg>
          <h2>Hubs</h2>
        </div>
      </router-link>

      <router-link v-bind:to="devicesLink">
        <div class="link">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 18h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z"
            />
          </svg>
          <h2>Devices</h2>
        </div>
      </router-link>

      <router-link v-bind:to="dataLink">
        <div class="link">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
            />
          </svg>
          <h2>Data</h2>
        </div>
      </router-link>
      <router-link v-bind:to="logsLink">
        <div class="link">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-6 w-6"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"
            />
          </svg>
          <h2>Logs</h2>
        </div>
      </router-link>
    </nav>
  </div>
</template>

<script>
export default {
  name: "TheNavigation",
  computed: {
    hubsLink() {
      return { name: "hubs" };
    },
    devicesLink() {
      return { name: "devices" };
    },
    dataLink() {
      return { name: "data" };
    },
    logsLink() {
      return { name: "logs" };
    },
  },
};
</script>

<style scoped>
/*
SPACING SYSTEM (px)
2 / 4 / 8 / 12 / 16 / 24 / 32 / 48 / 64 / 80 / 96 / 128

FONT SIZE SYSTEM (px)
10 / 12 / 14 / 16 / 18 / 20 / 24 / 30 / 36 / 44 / 52 / 62 / 74 / 86 / 98
*/
nav {
  background: transparent;
  padding-left: 2.4rem;
  padding-right: 2.4rem;

  color: var(--background-color);

  display: flex;
  flex-direction: column;
  flex-grow: 1;
  justify-content: center;
  gap: 0.8rem;
}

h1 {
  font-size: 3.2rem;
  color: var(--background-color);
}

h2 {
  color: inherit;
  font-size: 2.4rem;
  margin: 0;
}

svg {
  width: 2.4rem;
  height: 2.4rem;
  color: var(--background-color);
}

.link {
  display: flex;
  color: var(--background-color);
  justify-content: left;
  align-items: center;
  gap: 1rem;
  padding: 0 1rem;
}

a:link {
  color: var(--background-color);
  text-decoration: none;
}

a.router-link-active > div,
a:hover > div {
  background-color: var(--background-color);
}

a.router-link-active > div > h2,
a:hover > div > h2 {
  color: var(--main-color);
}

a.router-link-active > div > svg,
a:hover > div > svg {
  stroke: var(--main-color);
}
</style>
