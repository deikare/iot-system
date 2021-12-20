<template>
  <loading-spinner v-bind:active="displayLoading"></loading-spinner>
  <div v-if="displayNoEntities" class="no-entities">
    Your search did not match&nbsp;<b>any</b> &nbsp;entities, try different
    query.
  </div>
  <ul v-else class="entities-list">
    <router-link
      v-bind:to="linkGeneratorFunction(entity.id)"
      class="entity-link"
      v-for="entity in entities"
      v-bind:key="entity.id"
    >
      <base-entity-card v-bind:entity="entity"> </base-entity-card>
    </router-link>
  </ul>
</template>

<script>
import BaseEntityCard from "@/slots/BaseEntityCard";
import LoadingSpinner from "@/components/LoadingSpinner";

export default {
  name: "BaseEntityGrid",
  components: {
    LoadingSpinner,
    BaseEntityCard,
  },
  props: {
    entities: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },

    linkGeneratorFunction: {
      type: Function,
      required: true,
      default() {
        return function () {
          return null;
        };
      },
    },

    isLoaded: {
      type: Boolean,
      required: true,
      default() {
        return false;
      },
    },
  },

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },

    displayNoEntities() {
      return this.entities.length === 0;
    },
  },
};
</script>

<style scoped>
.entities-list {
  color: var(--main-color);
  font-size: 2rem;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  grid-template-rows: repeat(4, 1fr);
  grid-gap: 2rem;
}

.no-entities {
  display: flex;
  align-items: center;
}
</style>
