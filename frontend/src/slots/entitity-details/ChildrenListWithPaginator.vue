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
        </div>
      </div>
    </template>

    <entity-list-with-paginator
      v-bind:is-loaded="isLoaded"
      v-bind:is-error="isError"
      v-bind:entities-properties="childrenProperties"
      v-bind:buttons-properties="buttonsProperties"
      v-bind:page="page"
      v-on:entityClicked="emitChildClicked"
      v-on:editEntity="emitEditChild"
      v-on:deleteEntity="emitDeleteChild"
      v-on:changePage="emitChangeChildrenPage"
    ></entity-list-with-paginator>

    <!--    <loading-spinner v-if="displayLoading"> </loading-spinner>-->

    <!--    <entities-error v-else-if="isError"></entities-error>-->
    <!--    <div class="children-list-with-paginator" v-else>-->
    <!--      <entity-list-->
    <!--        v-bind:entities-properties="childrenProperties"-->
    <!--        v-bind:buttons-properties="buttonsProperties"-->
    <!--        v-on:entityClicked="emitChildClicked"-->
    <!--        v-on:editEntity="emitEditChild"-->
    <!--        v-on:deleteEntity="emitDeleteChild"-->
    <!--      ></entity-list>-->
    <!--      <two-way-paginator-->
    <!--        v-bind:page="page"-->
    <!--        v-on:changePage="emitChangeChildrenPage"-->
    <!--      ></two-way-paginator>-->
    <!--    </div>-->
  </base-card>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import EntityListWithPaginator from "@/components/entity-list/EntityListWithPaginator";
export default {
  name: "ChildrenListWithPagination",
  components: {
    EntityListWithPaginator,
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

    buttonsProperties: {
      type: Object,
      required: false,
      default() {
        return {
          isEditVisible: true,
          isDeleteVisible: true,
        };
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
  border: 2px solid transparent;
  height: 3.6rem;
  width: 3.6rem;
  border-radius: 50%;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-icon {
  stroke: var(--card-color);
  height: 3rem;
  width: 3rem;

  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.control-button:hover,
.control-button:active {
  cursor: pointer;
  border: 2px solid var(--card-color);
}
</style>
