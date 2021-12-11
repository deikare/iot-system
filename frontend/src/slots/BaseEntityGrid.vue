<template>
  <ul class="entities-list">
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
export default {
  name: "BaseEntityGrid",
  components: {
    BaseEntityCard,
  },
  props: ["entities", "linkGeneratorFunction"],
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
</style>
