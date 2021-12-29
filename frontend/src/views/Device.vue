<template>
  <div>adasdasd</div>
  <base-toast v-bind:message="message"> </base-toast>
</template>

<script>
import BaseToast from "@/slots/abstract/BaseToast";
export default {
  name: "Device",
  components: { BaseToast },
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
