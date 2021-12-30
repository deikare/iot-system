<template>
  <div v-if="type === 'info'" class="status success">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      class="icon success-icon"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        stroke-width="2"
        d="M5 13l4 4L19 7"
      />
    </svg>
  </div>

  <div v-else-if="type === 'error'" class="status warning">
    <svg
      xmlns="http://www.w3.org/2000/svg"
      class="icon warning-icon"
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        stroke-linecap="round"
        stroke-linejoin="round"
        stroke-width="2"
        d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
      />
    </svg>
  </div>
</template>

<script>
export default {
  name: "StatusIcon",

  props: {
    type: {
      type: String,
      required: true,
      default: "info",
    },
  },
};
</script>

<style scoped>
.status {
  width: 2.4rem;
  height: 2.4rem;

  display: flex;
  align-items: center;
  justify-content: center;

  background: transparent;
  border: none;
  border-radius: 50%;

  align-self: center;
}

.success {
  background-color: var(--success-color);
}

.warning {
  background-color: var(--toast-color);
  min-width: 5rem;
  min-height: 5rem;
}

.success-icon {
  width: 1.6rem;
  height: 1.6rem;

  stroke: var(--toast-color);
}

.warning-icon {
  width: 3.7rem;
  height: 3.6rem;
  stroke: var(--toast-color);
  border: none;

  fill: var(--warning-color);
}
</style>
