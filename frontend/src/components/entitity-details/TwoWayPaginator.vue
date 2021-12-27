<template>
  <p v-if="isError" class="error-message">Enter valid value</p>
  <div class="paginator">
    <button
      v-if="isLeftArrowVisible"
      v-on:click="emitChangePage(this.page.currentPage - 1)"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="arrow"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M15 19l-7-7 7-7"
        />
      </svg>
    </button>

    <div class="page-info">
      <input
        class="new-page-number"
        type="text"
        size="5"
        v-model="newCurrentPage"
        v-bind:style="{
          width: `${newCurrentPage.length > 0 ? newCurrentPage.length : 1}ch`,
        }"
        v-on:keydown.enter="validateAndEmitChangePage"
        v-on:focusout="resetNewPageValue"
      />
      {{ `/${page.totalPages}` }}
    </div>

    <button
      v-if="isRightArrowVisible"
      v-on:click="emitChangePage(this.page.currentPage + 1)"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        class="arrow"
        fill="none"
        viewBox="0 0 24 24"
        stroke="currentColor"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M9 5l7 7-7 7"
        />
      </svg>
    </button>
  </div>
</template>

<script>
export default {
  name: "TwoWayPaginator",

  data() {
    return {
      newCurrentPage: this.page.currentPage.toString(),
      isError: false,
    };
  },

  props: {
    page: {
      type: Object,
      required: true,
      default() {
        return {
          totalPages: 0,
          currentPage: 0,
        };
      },
    },
  },

  emits: ["changePage"],

  computed: {
    isComponentRenderable() {
      return (
        this.page.totalPages > 1 &&
        this.page.currentPage <= this.page.totalPages
      );
    },

    isLeftArrowVisible() {
      return this.page.currentPage > 1;
    },

    isRightArrowVisible() {
      return this.page.currentPage < this.page.totalPages;
    },
  },

  methods: {
    emitChangePage(page) {
      this.$emit("changePage", page);
    },

    validateAndEmitChangePage() {
      try {
        let newCurrentPageAsNumber = Number(this.newCurrentPage);
        if (
          newCurrentPageAsNumber >= 1 &&
          newCurrentPageAsNumber <= this.page.totalPages
        ) {
          this.isError = false;
          this.emitChangePage(newCurrentPageAsNumber);
        } else this.isError = true;
      } catch (error) {
        this.isError = true;
      }
    },

    resetNewPageValue() {
      this.isError = false;
      this.newCurrentPage = this.page.currentPage;
    },
  },
};
</script>

<style scoped>
.paginator {
  display: flex;

  margin: 1rem 0.8rem;
  gap: 1.2rem;
  align-items: center;
  justify-content: center;

  background-color: transparent;
}

.page-info {
  font-size: 1.6rem;
  color: var(--main-color);

  display: flex;
  align-items: flex-start;
  justify-content: center;
}

.new-page-number {
  background: transparent;
  display: inline;
  border: none;

  border-bottom: 2px solid var(--main-color);
  font-size: 1.6rem;
}

.new-page-number:focus {
  outline-width: 0;
}

.error-message {
  color: var(--error-color);
  font-size: 1.6rem;
}

.arrow {
  width: 2.4rem;
  height: 2.4rem;
  stroke: var(--main-color);
}

button {
  color: var(--main-color);
  background-color: transparent;
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 3.6rem;
  width: 3.6rem;
}

button:hover {
  background-color: var(--main-color);
  border-radius: 50%;
  cursor: pointer;
}

button:hover > svg {
  stroke: var(--background-color);
}
</style>
