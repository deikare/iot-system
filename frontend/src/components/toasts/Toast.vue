<template>
  <div class="toast">
    <div class="type-block">
      <div class="color-block" v-bind:style="blockColor">&nbsp;</div>
      <status-icon v-bind:type="message.type"></status-icon>
    </div>

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
import StatusIcon from "@/components/toasts/StatusIcon";
export default {
  name: "Toast",
  components: { StatusIcon },
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
          type: "",
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

  computed: {
    blockColor() {
      return {
        backgroundColor:
          this.message.type === "info"
            ? `var(--success-color)`
            : this.message.type === "error"
            ? `var(--warning-color)`
            : `transparent`,
      };
    },
  },

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
  background: var(--toast-color);

  flex-basis: 0;

  align-items: center;
  justify-content: space-between;
  gap: 1.2rem;

  padding: 1rem 1.2rem;

  /*min-width: fit-content;*/
  /*max-width: 100%;*/
  border-radius: 5px;

  box-shadow: 0 1rem 1.5rem 0 rgba(0, 0, 0, 0.1);
}

.type-block {
  display: flex;
  align-self: stretch;
  gap: 1.2rem;
}

.color-block {
  width: 0.3rem;
  min-height: 100%;
  display: inline-block;
  border-radius: 100rem;
  content: "";
  align-self: stretch;
}

.toast-message {
  color: var(--main-color);
  display: flex;
  font-size: 1.2rem;

  text-align: start;

  justify-self: flex-start;
}

.close-button {
  width: 3.6rem;
  height: 3.6rem;

  justify-self: flex-end;

  display: flex;
  align-items: center;
  justify-content: center;

  background: transparent;
  border: 2px solid transparent;
  border-radius: 50%;
}

.close-button:hover,
.close-button:active {
  border: 2px solid var(--main-color);
  cursor: pointer;
}

.close-icon {
  background: transparent;
  stroke: var(--main-color);

  width: 2.4rem;
  height: 2.4rem;
}
</style>
