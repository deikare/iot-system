<template>
  <div class="entity-card">
    <base-card>
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
    </base-card>
  </div>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
export default {
  name: "BaseEntityCard",
  components: {
    BaseCard,
  },
  props: {
    entity: {
      type: Object,
      required: true,
      default() {
        return { name: "not found", properties: [] };
      },
    },
  },
};
</script>

<style scoped>
.property-list {
  list-style: none;

  padding: 0;

  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: space-between;
  gap: 0.4rem;
}

li {
  display: flex;
  text-align: left;
  justify-content: space-between;
  padding: 0.2rem 0.8rem;
}

.bold {
  font-weight: bold;
}
</style>
