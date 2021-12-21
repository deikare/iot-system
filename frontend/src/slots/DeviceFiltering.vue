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

      <div class="device-types">
        <p class="device-types-label">Device types:</p>
        <ul class="device-types-list">
          <li v-for="deviceType in deviceTypes" v-bind:key="deviceType.value">
            <label>
              <input
                class="input-checkbox"
                name="deviceType"
                type="radio"
                v-bind:value="deviceType.value"
                v-bind:checked="deviceTypeQuery === deviceType.value"
                v-model="deviceTypeQuery"
                v-on:click.prevent="uncheckDeviceType(deviceType.value)"
              />
              {{ deviceType.label }}
            </label>
          </li>
        </ul>
      </div>
    </template>

    <template v-slot:additional-buttons>
      <button class="additional-button">Add device</button>
    </template>
  </base-search-form>
</template>

<script>
import BaseSearchForm from "@/slots/BaseSearchForm";
export default {
  name: "DeviceFiltering",
  components: { BaseSearchForm },
  emits: ["newQuery"],

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
        console.log(this.deviceTypeQuery);
      };
    },
    queryValidator() {
      return () => {
        return true;
      };
    },
  },

  methods: {
    uncheckDeviceType(clickedValue) {
      console.log(clickedValue);
      if (this.deviceTypeQuery === clickedValue) this.deviceTypeQuery = null;
      else this.deviceTypeQuery = clickedValue;
    },
  },

  watch: {
    deviceTypeQuery() {
      console.log(this.deviceTypeQuery);
    },
  },
};
</script>

<style scoped>
.device-types {
  display: flex;
  flex-direction: column;
  gap: 1.2rem;
  align-items: flex-start;
  justify-content: center;
}

.device-types-label {
  margin: 0;
}

ul {
  list-style-type: none;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
</style>
