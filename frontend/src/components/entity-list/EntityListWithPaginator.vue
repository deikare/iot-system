<template>
  <div class="list-container">
    <loading-spinner v-if="displayLoading"> </loading-spinner>
    <entities-error v-else-if="isError"></entities-error>

    <div class="entity-list-with-paginator" v-else>
      <entity-list
        v-bind:entities-properties="entitiesProperties"
        v-bind:buttons-properties="buttonsProperties"
        v-bind:highlight-items="highlightItems"
        v-on:entityClicked="emitEntityClicked"
        v-on:editEntity="emitEditEntity"
        v-on:deleteEntity="emitDeleteEntity"
      ></entity-list>

      <two-way-paginator
        v-bind:page="page"
        v-on:changePage="emitChangePage"
      ></two-way-paginator>
    </div>
  </div>
</template>

<script>
import LoadingSpinner from "@/components/LoadingSpinner";
import EntitiesError from "@/slots/abstract/EntitiesError";
import EntityList from "@/components/entity-list/EntityList";
import TwoWayPaginator from "@/components/entitity-details/TwoWayPaginator";
export default {
  name: "EntityListWithPaginator",
  components: { TwoWayPaginator, EntityList, EntitiesError, LoadingSpinner },

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

    entitiesProperties: {
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

    highlightItems: {
      type: Boolean,
      required: false,
      default: true,
    },
  },

  emits: ["changePage", "entityClicked", "editEntity", "deleteEntity"],

  computed: {
    displayLoading() {
      return !this.isLoaded;
    },
  },

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
    },

    emitEntityClicked(entityId) {
      this.$emit("entityClicked", entityId);
    },

    emitEditEntity(entityId) {
      this.$emit("editEntity", entityId);
    },
    emitDeleteEntity(entityId) {
      this.$emit("deleteEntity", entityId);
    },
  },
};
</script>

<style scoped>
.list-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.entity-list-with-paginator {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
}
</style>
