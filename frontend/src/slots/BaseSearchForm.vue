<template>
  <div class="container" v-on:mouseleave="resetError">
    <p class="error-message">
      <slot name="error-message" v-if="isErrorRenderable"></slot>
    </p>
    <form v-on:submit.prevent="validateAndEmitNewQuery">
      <slot name="form"></slot>
      <button class="submit-form-button">Submit</button>
    </form>
  </div>
</template>

<script>
export default {
  name: "BaseSearchForm",

  data() {
    return {
      isQueryValid: true,
    };
  },

  props: {
    newQueryEmitter: {
      type: Function,
      required: true,
      default() {
        return () => {};
      },
    },

    queryValidator: {
      type: Function,
      required: true,
      default() {
        return () => {};
      },
    },
  },

  computed: {
    isErrorRenderable() {
      return !this.isQueryValid;
    },
  },

  methods: {
    resetError() {
      this.isQueryValid = true;
    },

    validateAndEmitNewQuery() {
      if (this.queryValidator()) {
        this.isQueryValid = true;
        this.newQueryEmitter();
      } else this.isQueryValid = false;
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  gap: 0.4rem;
}

.error-message {
  margin: 0;
  display: flex;
  justify-content: flex-start;
  color: var(--error-color);
  font-weight: bold;
}

form {
  display: flex;
  gap: 0.4rem;
}

::v-slotted(.input-text) {
  font-size: 1.8rem;
  color: var(--main-color);
  background-color: var(--background-color);
  border: 1px solid var(--main-color);
}

.submit-form-button {
  font-size: 1.8rem;
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
}
</style>
