<template>Data</template>

<script>
export default {
  name: "Data",

  props: {
    queriedBucket: {
      type: String,
      required: false,
      default: "data",
    },

    queriedStart: {
      type: String,
      required: false,
      default: "1970-01-01T00:00:00Z",
    },

    queriedEnd: {
      type: String,
      required: false,
      default: "",
    },

    queriedDescending: {
      type: String,
      required: false,
      default: "true",
    },

    queriedLimit: {
      type: String,
      required: false,
      default: "",
    },

    queriedHubIds: {
      type: String,
      required: false,
      default: "",
    },

    queriedDeviceIds: {
      type: String,
      required: false,
      default: "",
    },

    queriedMeasurementTypes: {
      type: String,
      required: false,
      default: "",
    },

    activeQuery: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },
  },
};
</script>

<style scoped></style>
