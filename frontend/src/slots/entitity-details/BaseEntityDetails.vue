<template>
  <main>
    <base-entity-properties
      class="properties"
      v-bind:properties="getProperties"
      v-bind:is-loaded="isBaseLoaded"
      v-bind:is-error="isBaseError"
    >
    </base-entity-properties>

    <base-card class="children">
      <template v-slot:header><slot name="children-header"></slot></template>
      <template v-slot:default> </template>
    </base-card>

    <base-card class="logs">
      <template v-slot:header>Logs</template>
      <template v-slot:default></template>
    </base-card>

    <base-card class="data">
      <template v-slot:header>Data</template>
      <template v-slot:default></template>
    </base-card>
  </main>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import BaseEntityProperties from "@/slots/entitity-details/BaseEntityProperties";
import { mapState } from "vuex";

export default {
  name: "BaseEntityDetails",
  components: {
    BaseEntityProperties,
    BaseCard,
  },
  props: {
    isBaseLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },

    isBaseError: {
      type: Boolean,
      required: true,
      default: false,
    },

    areChildrenLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },

    areChildrenError: {
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
  grid-template-columns: 20% 1fr 1fr;
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

.children {
  grid-row: 3;
  grid-column: 1;
}

.logs {
  grid-row: 2/-1;
  grid-column: 2;
}

.data {
  grid-row: 2/-1;
  grid-column: 3;
}
</style>
