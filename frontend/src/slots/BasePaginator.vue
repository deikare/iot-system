<template>
  <div v-if="isComponentRenderable" class="paginator">
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

    <ul class="links-list" v-if="isListShort">
      <button
        v-for="buttonLabel in buttonLabelsArray"
        v-bind:key="'short-' + buttonLabel"
        v-bind:class="[
          buttonLabel === page.currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
        v-on:click="validateAndEmitChangePage(buttonLabel)"
      >
        {{ buttonLabel }}
      </button>
    </ul>

    <ul class="links-list" v-else>
      <button
        v-if="buttonLabelsShortArray[0] > 1"
        v-bind:class="[
          1 === page.currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
        v-on:click="validateAndEmitChangePage(1)"
      >
        1
      </button>

      <li v-if="buttonLabelsShortArray[0] > 2" class="triple-dots">&hellip;</li>

      <button
        v-for="buttonLabel in buttonLabelsShortArray"
        v-bind:key="'long-' + buttonLabel"
        v-bind:class="[
          buttonLabel === page.currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
        v-on:click="validateAndEmitChangePage(buttonLabel)"
      >
        {{ buttonLabel }}
      </button>

      <li
        v-if="
          buttonLabelsShortArray[buttonLabelsShortArray.length - 1] <
          page.pagesNumber - 1
        "
        class="triple-dots"
      >
        &hellip;
      </li>

      <button
        v-if="
          buttonLabelsShortArray[buttonLabelsShortArray.length - 1] <
          page.pagesNumber
        "
        v-bind:class="[
          page.pagesNumber === page.currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
        v-on:click="validateAndEmitChangePage(lastButtonLabel)"
      >
        {{ lastButtonLabel }}
      </button>
    </ul>

    <button
      v-if="isRightArrowVisible"
      v-on:click="validateAndEmitChangePage(this.page.currentPage + 1)"
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
  name: "BasePaginator",
  props: {
    page: {
      type: Object,
      required: true,
      default() {
        return {
          pageNumber: 0,
          currentPage: 0,
        };
      },
    },
    shortListLength: {
      type: Number,
      required: false,
      default: 4,
    },
  },

  emits: ["changePage"],

  computed: {
    isComponentRenderable() {
      return this.page.pageNumber > 1;
    },

    isLeftArrowVisible() {
      return this.page.currentPage > 1;
    },

    isRightArrowVisible() {
      return this.page.currentPage < this.page.pageNumber;
    },

    isListShort() {
      return this.page.pageNumber <= this.shortListLength;
    },

    buttonLabelsArray() {
      return Array.from(
        { length: this.page.pageNumber },
        (element, idx) => idx + 1
      );
    },

    //only even numbers as shortListLength
    buttonLabelsShortArray() {
      let delta = this.shortListLength / 2;

      return Array.from(
        { length: this.page.pageNumber },
        (element, idx) => idx + 1
      ).slice(
        Math.max(this.page.currentPage - delta - 1, 0),
        Math.min(this.page.currentPage + delta, this.page.pageNumber)
      );
    },

    lastButtonLabel() {
      return this.page.pageNumber;
    },
  },

  methods: {
    validateAndEmitChangePage(page) {
      if (this.page.currentPage !== page) this.emitChangePage(page);
    },

    emitChangePage(page) {
      this.$emit("changePage", page);
    },
  },

  watch: {
    page() {
      console.log(this.page);
    },
  },

  created() {
    console.log(this.page);
  },
};
</script>

<style scoped>
.paginator {
  display: flex;

  margin: 2rem;
  gap: 1.2rem;
  align-items: center;
  justify-content: center;

  background-color: var(--background-color);
}

.links-list {
  display: flex;
  gap: 1.2rem;
  align-items: center;
  justify-content: center;
}

li {
  list-style: none;
}

button {
  color: var(--main-color);
  background-color: var(--background-color);
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 3.6rem;
  width: 3.6rem;
}

.number-link {
  font-size: 1.8rem;
  width: 4.8rem;
  height: 4.8rem;
  border-radius: 50%;
  background: var(--background-color);
}

.arrow {
  width: 2.4rem;
  height: 2.4rem;
  stroke: var(--main-color);
}

button:hover {
  background-color: var(--main-color);
  border-radius: 50%;
  cursor: pointer;
}

button:hover > svg {
  stroke: var(--background-color);
}

button:hover.number-link {
  color: var(--background-color);
}

.number-link-active {
  background-color: var(--main-color);
  color: var(--background-color);
  cursor: text !important;
  border-radius: 50%;
}
</style>
