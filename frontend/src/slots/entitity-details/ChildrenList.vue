<template>
  <base-list>
    <template v-slot:default>
      <children-list-item
        v-for="childProperty in childrenProperties"
        v-bind:key="childProperty"
        v-bind:child-properties="childProperty"
        v-on:childClicked="emitChildClicked"
        v-on:editChild="emitEditChild"
        v-on:deleteChild="emitDeleteChild"
      ></children-list-item>
    </template>
  </base-list>
</template>

<script>
import ChildrenListItem from "@/slots/entitity-details/ChildrenListItem";
import BaseList from "@/slots/abstract/BaseList";
export default {
  name: "ChildrenList",
  components: { BaseList, ChildrenListItem },
  props: {
    childrenProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  emits: ["childClicked", "editChild", "deleteChild"],

  methods: {
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

<style scoped></style>
