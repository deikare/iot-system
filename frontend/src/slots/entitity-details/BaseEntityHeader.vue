<template>
  <div class="container">
    <div class="name-with-icon">
      <slot name="icon"></slot>

      <h2>
        <slot class="entity-id" name="entityId"></slot>
      </h2>
    </div>

    <div class="button-list">
      <slot name="buttons"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: "BaseEntityHeader",
};
</script>

<style scoped>
.container {
  display: flex;
  align-items: center;
  justify-content: space-between;

  padding: 0 1.6rem;

  background-color: var(--main-color);

  gap: 2.4rem;
}

.name-with-icon {
  display: flex;
  gap: 2.4rem;

  align-items: center;
  justify-content: center;
}

h2 {
  font-size: 3.6rem;
  color: var(--background-color);
  margin: 0;
}

::v-slotted(svg) {
  height: 3.6rem;
  width: 3.6rem;
  stroke: var(--background-color);
}

.button-list {
  display: flex;
  align-items: center;
  justify-content: right;
  gap: 0.8rem;
}

::v-slotted(button) {
  background-color: transparent;
  border: none;
  border-radius: 100rem;

  width: 3.6rem;
  height: 3.6rem;

  padding: 0;

  display: flex;
  align-items: center;
  justify-content: center;
}

::v-slotted(.action-icon) {
  width: 3.2rem;
  height: 3.2rem;
  background-color: transparent;
  stroke: var(--background-color);
}

::v-slotted(button):hover,
::v-slotted(button):active {
  cursor: pointer;
  background-color: var(--background-color);
}

::v-slotted(button):hover > svg,
::v-slotted(button):active > svg {
  stroke: var(--main-color);
}
</style>
