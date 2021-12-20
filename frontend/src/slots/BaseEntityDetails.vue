<template>
  <main>
    <base-entity-properties
      class="properties"
      v-bind:properties="getProperties"
      v-bind:is-loaded="isLoaded"
    >
    </base-entity-properties>

    <base-grid-card class="children">
      <template v-slot:header>Children</template>
      <template v-slot:default> </template>
    </base-grid-card>

    <base-grid-card class="data">
      <template v-slot:header>Data</template>
      <template v-slot:default></template>
    </base-grid-card>
  </main>
</template>

<script>
import BaseGridCard from "@/slots/BaseGridCard";
import BaseEntityProperties from "@/slots/BaseEntityProperties";
import { mapState } from "vuex";

export default {
  name: "BaseEntityDetails",
  components: { BaseEntityProperties, BaseGridCard },
  props: {
    isLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },

    namespace: {
      type: String,
      required: true,
      default: "",
    },
  },

  computed: {
    ...mapState({
      getProperties(state, getters) {
        return getters[`${this.namespace}/getProperties`];
      },
    }),
  },
};
</script>

<style scoped>
main {
  display: grid;
  grid-template-rows: fit-content(10rem) repeat(2, 1fr);
  grid-template-columns: 30% 1fr;
  grid-gap: 1.6rem;
}

header {
  grid-row: 1;
  grid-column: 1/-1;
  display: flex;
  align-items: center;
  justify-content: left;

  padding-left: 1.6rem;

  background-color: var(--main-color);

  gap: 2.4rem;
}

h2 {
  font-size: 3.6rem;
  color: var(--background-color);
  margin: 0;
}

div {
  border: 1px solid black;
}

.properties {
  grid-row: 2;
  grid-column: 1;
}

.data {
  grid-row: 2/-1;
  grid-column: 2;
}

.children {
  grid-row: 3;
  grid-column: 1;
}
</style>
