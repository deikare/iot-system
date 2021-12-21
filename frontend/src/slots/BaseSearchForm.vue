<template>
  <div class="container" v-on:mouseleave="resetError">
    <p class="error-message">
      <slot name="error-message" v-if="isErrorRenderable"></slot>
    </p>
    <div class="form-module">
      <form v-on:submit.prevent="validateAndEmitNewQuery">
        <slot name="form"></slot>
        <button class="submit-form-button">Submit</button>
      </form>

      <div class="additional-buttons">
        <slot name="additional-buttons"></slot>
      </div>
    </div>
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

.form-module {
  display: flex;
  gap: 0.4rem;
  align-items: center;
  justify-content: space-between;
}

form {
  display: flex;
  gap: 0.8rem;
  align-items: center;
}

::v-slotted(.input-text) {
  font-size: 1.8rem;
  color: var(--main-color);
  background-color: var(--background-color);
  border: 1px solid var(--main-color);
  height: fit-content;
}

.submit-form-button {
  font-size: 1.8rem;
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
  height: fit-content;
}

.additional-buttons {
  display: flex;
  gap: 0.4rem;
}

::v-slotted(.additional-button) {
  font-size: 1.8rem;
  background-color: var(--card-color);
  border: 1px solid var(--main-color);
}
</style>
