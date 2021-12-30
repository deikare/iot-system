<template>
  <ul class="toast-list">
    <toast
      v-for="message in getMessages"
      v-bind:key="message.id"
      v-bind:message="message"
      v-bind:timeout="123000000"
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
      default: 5000,
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
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}
</style>
