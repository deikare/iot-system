<template>
  <div class="container">
    <ul class="list">
      <base-list-item>
        <template v-slot:default>
          <slot></slot>
        </template>
      </base-list-item>
    </ul>

    <base-paginator
      v-bind:page="page"
      v-bind:short-list-length="shortListLength"
      v-on:changePage="emitChangePage"
      class="card-background"
    >
    </base-paginator>
  </div>
</template>

<script>
import BasePaginator from "@/slots/abstract/BasePaginator";
import BaseListItem from "@/slots/abstract/BaseListItem";
export default {
  name: "BaseList",
  components: { BaseListItem, BasePaginator },
  props: {
    page: {
      type: Object,
      required: true,
      default() {
        return {
          totalPages: 0,
          currentPage: 0,
        };
      },
    },
    shortListLength: {
      type: Number,
      required: false,
      default: 2,
    },
  },

  emits: ["changePage"],

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
    },
  },

  //TODO add item.v-for.(keys, values)
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
}

.card-background {
  background-color: var(--card-color);
}
</style>
