<template>
  <ul class="toast-list">
    <toast
      v-for="message in getMessages"
      v-bind:key="message.id"
      v-bind:message="message"
      v-bind:timeout="timeout"
      v-on:closeToast="removeToast"
    >
    </toast>
  </ul>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import Toast from "@/components/toasts/Toast";
export default {
  name: "ToastList",
  // eslint-disable-next-line vue/no-unused-components
  components: { Toast },

  props: {
    timeout: {
      type: Number,
      required: false,
      // default: 5000,
      default: 123123123,
    },
  },

  computed: {
    ...mapGetters("messages", ["getMessages"]),
  },

  methods: {
    ...mapMutations("messages", ["remove"]),

    removeToast(id) {
      this.remove(id);
    },
  },
};
</script>

<style scoped>
.toast-list {
  max-width: 20%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 0.8rem;
  flex: 0 1 auto;
}
</style>
