<template>
  <div v-if="isComponentRenderable" class="paginator">
    <router-link v-if="isLeftArrowVisible" v-bind:to="leftArrowUrl">
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
    </router-link>

    <ul class="links-list" v-if="isListShort">
      <router-link
        v-for="buttonLabel in buttonLabelsArray"
        v-bind:key="'short-' + buttonLabel"
        v-bind:to="linkGeneratorFunction(buttonLabel)"
        v-bind:class="[
          buttonLabel === currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
      >
        {{ buttonLabel }}
      </router-link>
    </ul>

    <ul class="links-list" v-else>
      <router-link
        v-if="buttonLabelsShortArray[0] > 1"
        v-bind:to="linkGeneratorFunction(1)"
        v-bind:class="[
          1 === currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
      >
        1
      </router-link>

      <li v-if="buttonLabelsShortArray[0] > 2" class="triple-dots">&hellip;</li>

      <router-link
        v-for="buttonLabel in buttonLabelsShortArray"
        v-bind:key="'long-' + buttonLabel"
        v-bind:to="linkGeneratorFunction(buttonLabel)"
        v-bind:class="[
          buttonLabel === currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
      >
        {{ buttonLabel }}
      </router-link>

      <li
        v-if="buttonLabelsShortArray[buttonLabelsShortArray.length - 1] < n - 1"
        class="triple-dots"
      >
        &hellip;
      </li>

      <router-link
        v-if="buttonLabelsShortArray[buttonLabelsShortArray.length - 1] < n"
        v-bind:to="linkGeneratorFunction(lastButtonLabel)"
        v-bind:class="[
          n === currentPage ? 'number-link-active' : '',
          'number-link',
        ]"
      >
        {{ lastButtonLabel }}
      </router-link>
    </ul>

    <router-link v-if="isRightArrowVisible" v-bind:to="rightArrowUrl">
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
    </router-link>
  </div>
</template>

<script>
export default {
  name: "BasePaginator",
  props: {
    n: {
      type: Number,
      required: true,
      default: 0,
    },
    shortListLength: {
      type: Number,
      required: true,
      default: 0,
    },
    currentPage: {
      type: Number,
      required: true,
      default: 0,
    },
    linkGeneratorFunction: {
      type: Function,
      required: true,
      default() {
        return function () {
          return null;
        };
      },
    },
  },

  computed: {
    isComponentRenderable() {
      return this.n > 1;
    },

    isLeftArrowVisible() {
      return this.currentPage > 1;
    },

    isRightArrowVisible() {
      return this.currentPage < this.n;
    },

    leftArrowUrl() {
      return this.linkGeneratorFunction(this.currentPage - 1);
    },

    rightArrowUrl() {
      return this.linkGeneratorFunction(this.currentPage + 1);
    },

    isListShort() {
      return this.n <= this.shortListLength;
    },

    buttonLabelsArray() {
      return Array.from({ length: this.n }, (element, idx) => idx + 1);
    },

    //only even numbers as shortListLength
    buttonLabelsShortArray() {
      let delta = this.shortListLength / 2;

      return Array.from({ length: this.n }, (element, idx) => idx + 1).slice(
        Math.max(this.currentPage - delta - 1, 0),
        Math.min(this.currentPage + delta, this.n)
      );
    },

    lastButtonLabel() {
      return this.n;
    },
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

a:link {
  color: var(--main-color);
  background-color: var(--background-color);
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

a:hover {
  background-color: var(--main-color);
  border-radius: 50%;
}

a:hover > svg {
  stroke: var(--background-color);
}

a:hover.number-link {
  color: var(--background-color);
}

a:hover {
  background-color: var(--main-color);
  color: var(--background-color);
  cursor: pointer;
  border-radius: 50%;
}

.number-link-active {
  background-color: var(--main-color);
  color: var(--background-color);
  cursor: pointer;
  border-radius: 50%;
}
</style>
