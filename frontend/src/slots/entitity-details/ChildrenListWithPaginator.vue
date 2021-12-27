<template>
  <base-card>
    <template v-slot:header class="header">
      <div class="flexbox">
        <slot name="children-header"></slot>

        <div class="buttons">
          <button v-on:click="emitAddChild" class="control-button">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="control-icon"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 4v16m8-8H4"
              />
            </svg>
          </button>
          <button v-on:click="emitDeleteAllChildren" class="control-button">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="control-icon"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"
              />
            </svg>
          </button>
        </div>
      </div>
    </template>

    <loading-spinner v-if="displayLoading"> </loading-spinner>

    <entities-error v-else-if="isError"></entities-error>
    <div class="children-list-with-paginator" v-else>
      <children-list
        v-bind:children-properties="childrenProperties"
        v-on:childClicked="emitChildClicked"
        v-on:editChild="emitEditChild"
        v-on:deleteChild="emitDeleteChild"
      ></children-list>
      <two-way-paginator
        v-bind:page="page"
        v-on:changePage="emitChangeChildrenPage"
      ></two-way-paginator>
    </div>
  </base-card>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import LoadingSpinner from "@/components/LoadingSpinner";
import EntitiesError from "@/slots/abstract/EntitiesError";
import TwoWayPaginator from "@/components/entitity-details/TwoWayPaginator";
import ChildrenList from "@/slots/entitity-details/ChildrenList";
export default {
  name: "ChildrenListWithPagination",
  components: {
    ChildrenList,
    TwoWayPaginator,
    EntitiesError,
    LoadingSpinner,
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

    childrenProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },
  },

  emits: [
    "changeChildrenPage",
    "addChild",
    "deleteAllChildren",
    "childClicked",
    "editChild",
    "deleteChild",
  ],

  methods: {
    emitChangeChildrenPage(page) {
      this.$emit("changeChildrenPage", page);
    },

    emitAddChild() {
      this.$emit("addChild");
    },

    emitDeleteAllChildren() {
      this.$emit("deleteAllChildren");
    },

    emitChildClicked(childId) {
      this.$emit("childClicked", childId);
    },

    emitEditChild(childId) {
      this.$emit("editChild", childId);
    },
    emitDeleteChild(childId) {
      this.$emit("deleteChild", childId);
    },
  },
};
</script>

<style scoped>
.flexbox {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
}

.buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.2rem;
}

.control-button {
  background: var(--main-color);
  border: none;
  height: 3.6rem;
  width: 3.6rem;
  border-radius: 50%;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-icon {
  stroke: var(--background-color);
  height: 3.6rem;
  width: 3.6rem;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-button:hover,
.control-button:active {
  cursor: pointer;
  background: var(--background-color);
}

.control-button:hover > .control-icon {
  stroke: var(--main-color);
}
</style>
