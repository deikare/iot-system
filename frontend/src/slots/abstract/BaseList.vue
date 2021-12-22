<template>
  <ul class="list">
    <li class="item" v-for="(item, index) in items" v-bind:key="index">
      <base-list-item>
        <template v-slot:default>
          {{ item.content }}
        </template>
      </base-list-item>
    </li>
  </ul>
</template>

<script>
import BaseListItem from "@/slots/abstract/BaseListItem";
export default {
  name: "BaseList",
  components: { BaseListItem },
  props: {
    items: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  //TODO add item.v-for.(keys, values)
};
</script>

<style scoped>
.list {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.item {
  border-bottom: 1px solid var(--main-color);
}
</style>
