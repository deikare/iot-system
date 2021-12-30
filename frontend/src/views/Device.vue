<template>
  <toast-list></toast-list>
</template>

<script>
import ToastList from "@/components/toasts/ToastList";
export default {
  name: "Device",
  components: { ToastList },
  data() {
    return {
      message: {
        id: "123",
        content:
          "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Delectus eum\n" +
          "      labore magnam maiores nostrum. A aspernatur consequatur doloribus earum\n" +
          "      neque, possimus reprehenderit vitae! Deleniti laborum molestiae nam optio\n" +
          "      quidem vel.",
      },
    };
  },
};
</script>

<style scoped></style>
