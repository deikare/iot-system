<template>
  <div class="toast">
    <div class="toast-message">{{ message.content }}</div>
    <button class="close-button" v-on:click="emitCloseToast">
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="close-icon"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M6 18L18 6M6 6l12 12"
        />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: "Toast",

  data() {
    return {
      timeoutHandler: () =>
        setTimeout(() => this.emitCloseToast(), this.timeout),
      timeoutObject: null,
    };
  },

  props: {
    message: {
      type: Object,
      required: true,
      default() {
        return {
          id: "",
          content: "",
        };
      },
    },

    timeout: {
      type: Number,
      required: false,
      default: 5000,
    },
  },

  emits: ["closeToast"],

  methods: {
    emitCloseToast() {
      clearTimeout(this.timeoutObject);
      this.$emit("closeToast", this.message.id);
    },
  },

  mounted() {
    this.timeoutObject = this.timeoutHandler();
  },
};
</script>

<style scoped>
.toast {
  display: flex;
  background: var(--main-color);

  align-items: center;
  justify-content: space-between;
  gap: 2rem;

  padding: 1rem 1.2rem;

  max-width: 50%;
}

.toast-message {
  color: var(--background-color);
  display: flex;
  font-size: 1.4rem;

  text-align: start;
}

.close-button {
  width: 3.6rem;
  height: 3.6rem;

  display: flex;
  align-items: center;
  justify-content: center;

  background: var(--main-color);
  border: 2px solid transparent;
  border-radius: 50%;
}

.close-button:hover,
.close-button:active {
  border: 2px solid var(--background-color);
  cursor: pointer;
}

.close-icon {
  background: transparent;
  stroke: var(--background-color);

  width: 2.4rem;
  height: 2.4rem;
}
</style>
