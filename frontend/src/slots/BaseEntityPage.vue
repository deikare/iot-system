<template>
  <base-entity-grid></base-entity-grid>
  <base-paginator></base-paginator>
</template>

<script>
import BaseEntityGrid from "@/slots/BaseEntityGrid";
import BasePaginator from "@/slots/BasePaginator";
export default {
  name: "BaseEntityPage",
  components: { BasePaginator, BaseEntityGrid },
  props: ["entitiesPage", "baseAddress"],
};
</script>

<style scoped></style>
