<template>
  <div class="entity-card">
    <base-grid-card>
      <template v-slot:header>{{ entity.name }}</template>

      <template v-slot:default>
        <section>
          <ul class="property-list">
            <li v-for="property in entity.properties" v-bind:key="property.key">
              <span class="bold">{{ property.key }}: </span>
              {{ property.value }}
            </li>
          </ul>
        </section>
      </template>
    </base-grid-card>
  </div>
</template>

<script>
import BaseGridCard from "@/slots/BaseGridCard";
export default {
  name: "BaseEntityCard",
  components: {
    BaseGridCard,
  },
  props: ["entity"],
};
</script>

<style scoped>
.property-list {
  list-style: none;

  padding: 0;

  display: flex;
  flex-direction: column;
  align-items: start;
  justify-content: space-between;
  gap: 0.4rem;
}

li {
  display: flex;
  text-align: left;
  justify-content: space-between;
  padding-left: 0.8rem;
}

.bold {
  font-weight: bold;
}
</style>
