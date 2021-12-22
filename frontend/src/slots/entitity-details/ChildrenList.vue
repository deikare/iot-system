<template>
  <base-card>
    <template v-slot:header>
      <slot name="children-header"></slot>
      <button>asd</button>
      <!--      TODO implement add/delete-->
      <button>asd</button>
    </template>
    <template v-slot:default>
      <base-list-with-loaders
        v-bind:page="page"
        v-bind:is-error="isError"
        v-bind:is-loaded="isLoaded"
        v-on:changePage="emitChangeChildrenPage"
      >
        <template v-slot:default>
          <slot></slot>
        </template>
      </base-list-with-loaders>
    </template>
  </base-card>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import BaseListWithLoaders from "@/slots/abstract/BaseListWithLoaders";
export default {
  name: "ChildrenList",
  components: {
    BaseListWithLoaders,
    BaseCard,
  },

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

  emits: ["changeChildrenPage"],

  methods: {
    emitChangeChildrenPage(page) {
      this.$emit("changeChildrenPage", page);
    },
  },
};
</script>

<style scoped></style>
