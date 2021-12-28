<template>
  <div class="properties-modifier">
    <form>
      <input type="text" name="name" v-model="newName" />
      <select v-model="newHubStatus">
        <option v-for=""
      </select>
    </form>
  </div>
</template>

<script>
export default {
  name: "HubPropertiesModifier",

  data() {
    return {
      newName: this.properties.name,
      newHubStatus: this.properties.status,

      hubStatuses: ["Started", "Stopped"],
    };
  },

  props: {
    properties: {
      type: Object,
      required: true,
      default() {
        return {
          name: "",
          status: "",
        };
      },
    },
  },
};
</script>

<style scoped></style>
