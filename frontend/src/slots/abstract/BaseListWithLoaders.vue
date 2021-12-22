<template>
  <entities-error v-if="isError"></entities-error>
  <main v-else>
    <loading-spinner v-if="displayLoading"> </loading-spinner>
    <base-list v-else v-bind:page="page" v-on:changePage="emitChangePage">
      <template v-slot:default>
        <slot></slot>
      </template>
    </base-list>
  </main>
</template>

<script>
import EntitiesError from "@/slots/abstract/EntitiesError";
import LoadingSpinner from "@/components/LoadingSpinner";
import BaseList from "@/slots/abstract/BaseList";
export default {
  name: "BaseListWithLoaders",
  components: { BaseList, LoadingSpinner, EntitiesError },

  props: {
    isError: {
      type: Boolean,
      required: true,
      default: false,
    },
    isLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },
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
  },

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },
  },

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
    },
  },
};
</script>

<style scoped>
main {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
</style>
