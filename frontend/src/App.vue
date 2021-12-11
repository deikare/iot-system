<template>
  <the-navigation class="navigation-bar"></the-navigation>
  <main class="main-with-margin">
    <router-view />
  </main>
  <the-footer class="footer"></the-footer>
</template>

<script>
import TheNavigation from "@/views/TheNavigation";
import TheFooter from "@/views/TheFooter";

export default {
  components: {
    TheNavigation,
    TheFooter,
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Inter:wght@400;500;700&family=Roboto+Condensed:wght@300;400;700&family=Roboto+Mono:wght@400;500;700&family=Rubik:wght@500;700&display=swap");

/*
SPACING SYSTEM (px)
2 / 4 / 8 / 12 / 16 / 24 / 32 / 48 / 64 / 80 / 96 / 128

FONT SIZE SYSTEM (px)
10 / 12 / 14 / 16 / 18 / 20 / 24 / 30 / 36 / 44 / 52 / 62 / 74 / 86 / 98
*/

/*
MAIN COLOR: #121428
GREY COLOR: #343a40
*/
:root {
  --main-color: #121428;
  /*--main-color: #343a40;*/
  --background-color: #f8f9fa;
  --card-color: #e9ecef;
}

html {
  font-size: 62.5%;
  overflow: auto !important;
  /*  1 rem = 10px*/
}

body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

#app {
  font-family: "Roboto Mono", monospace;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;

  box-sizing: border-box;

  display: grid;
  grid-template-columns: fit-content(20rem) 1fr;
  grid-template-rows: fit-content(400rem) fit-content(20rem);

  align-content: space-between;

  max-width: 192rem;

  min-height: 100vh;

  font-size: 1.6rem;

  background: var(--background-color);
}

ul {
  margin: 0;
  padding: 0;
}

a:link,
a:visited {
  text-decoration: none;
  color: var(--main-color);
}

.navigation-bar {
  grid-column: 1;
  grid-row: 1 /-1;
  background-color: var(--main-color);
  height: 100%;
}

main {
  grid-column: 2;
  grid-row: 1;
  overflow: auto;
}

.main-with-margin {
  margin-left: 1.6rem;
  margin-right: 1.6rem;

  margin-top: 3.2rem;
}

.footer {
  grid-column: 2;
  grid-row: 2;
  background-color: var(--main-color);
}
</style>
