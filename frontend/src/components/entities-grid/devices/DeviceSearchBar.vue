<template>
  <base-search-form
    v-bind:new-query-emitter="newQueryEmitter"
    v-bind:query-validator="queryValidator"
  >
    <template v-slot:error-message> Device name cannot be empty </template>
    <template v-slot:form>
      <input
        class="input-text"
        name="name"
        type="text"
        placeholder="Find a device named..."
        v-model="nameQuery"
      />

      <input
        class="input-text"
        name="hubId"
        type="text"
        placeholder="Find a device with hubId..."
        v-model="hubIdQuery"
      />

      <select v-model="deviceTypeQuery" class="input-select">
        <option class="placeholder-option" value="" selected>
          -- select device type --
        </option>
        <option
          v-for="deviceType in deviceTypes"
          v-bind:key="deviceType.label"
          v-bind:value="deviceType.value"
        >
          {{ deviceType.label }}
        </option>
      </select>
    </template>

    <template v-slot:additional-buttons>
      <button class="additional-button" v-on:click="emitOpenAddDevice">
        Add device
      </button>
    </template>
  </base-search-form>
</template>

<script>
import BaseSearchForm from "@/slots/abstract/BaseSearchForm";
export default {
  name: "DeviceFiltering",
  components: { BaseSearchForm },
  emits: ["newQuery", "openAddDevice"],

  data() {
    return {
      nameQuery: "",
      hubIdQuery: "",
      deviceTypes: [
        {
          value: "GENERATOR",
          label: "Generator",
        },
        {
          value: "CONTROLLED_DEVICE",
          label: "Controlled Device",
        },
        {
          value: "BOTH",
          label: "Both",
        },
      ],
      deviceTypeQuery: "",
    };
  },

  computed: {
    newQueryEmitter() {
      return () => {
        const event = {
          ...(this.nameQuery !== "" && {
            name: this.nameQuery,
          }),
          ...(this.hubIdQuery !== "" && {
            hubId: this.hubIdQuery,
          }),
          ...(this.deviceTypeQuery !== "" && {
            deviceType: this.deviceTypeQuery,
          }),
        };

        this.$emit("newQuery", event);

        this.nameQuery = "";
        this.hubIdQuery = "";
        this.deviceTypeQuery = "";
      };
    },
    queryValidator() {
      return () => {
        return (
          this.deviceTypeQuery !== "" ||
          this.nameQuery !== "" ||
          this.hubIdQuery !== ""
        );
      };
    },
  },

  methods: {
    uncheckDeviceType(clickedValue) {
      if (this.deviceTypeQuery === clickedValue) this.deviceTypeQuery = null;
      else this.deviceTypeQuery = clickedValue;
    },
    emitOpenAddDevice() {
      this.$emit("openAddDevice");
    },
  },

  watch: {
    deviceTypeQuery() {
      console.log(this.deviceTypeQuery, typeof this.deviceTypeQuery);
    },
  },
};
</script>

<style scoped>
ul {
  list-style-type: none;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
