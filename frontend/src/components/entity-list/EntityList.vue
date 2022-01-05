<template>
  <base-list>
    <template v-slot:default>
      <entity-list-item
        v-for="entityProperty in entitiesProperties"
        v-bind:key="entityProperty"
        v-bind:entity-properties="entityProperty"
        v-bind:buttons-properties="buttonsProperties"
        v-bind:highlight-items="highlightItems"
        v-on:entityClicked="emitEntityClicked"
        v-on:editEntity="emitEditEntity"
        v-on:deleteEntity="emitDeleteEntity"
      ></entity-list-item>
    </template>
  </base-list>
</template>

<script>
import EntityListItem from "@/components/entity-list/EntityListItem";
import BaseList from "@/slots/abstract/BaseList";
export default {
  name: "EntityList",
  components: { BaseList, EntityListItem },
  props: {
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

  emits: ["entityClicked", "editEntity", "deleteEntity"],

  methods: {
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

<style scoped></style>
