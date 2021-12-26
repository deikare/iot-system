<template>
  <base-card>
    <template v-slot:header>
      <slot name="children-header"></slot>
      <button>asd</button>
      <!--      TODO implement add/delete-->
      <button>asd</button>
    </template>
    <template v-slot:default>
      <loading-spinner v-if="displayLoading"> </loading-spinner>

      <entities-error v-else-if="isError"></entities-error>
      <div class="children-list" v-else>
        <base-list>
          <template v-slot:default>
            <slot name="entity-item"></slot>
          </template>
        </base-list>
        <base-paginator
          v-bind:page="page"
          v-bind:short-list-length="shortListLength"
          v-on:changePage="emitChangeChildrenPage"
        ></base-paginator>
      </div>
    </template>
  </base-card>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import BaseList from "@/slots/abstract/BaseList";
import LoadingSpinner from "@/components/LoadingSpinner";
import BasePaginator from "@/slots/abstract/BasePaginator";
import EntitiesError from "@/slots/abstract/EntitiesError";
export default {
  name: "ChildrenList",
  components: {
    EntitiesError,
    BasePaginator,
    LoadingSpinner,
    BaseList,
    BaseCard,
  },

  data() {
    return {
      shortListLength: 2,
    };
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

  computed: {
    displayLoading() {
      return !this.isLoaded;
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
