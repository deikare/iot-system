<template>
  <div class="creator">
    <div class="properties-modifier">
      <div
        class="property-container"
        v-for="(property, index) in entityProperties"
        v-bind:key="property.label"
      >
        <div class="property-label">{{ property.label }}:</div>

        <input
          class="property-modifier text-modifier"
          type="text"
          v-if="property.type === `text`"
          v-model="newValues[index].value"
          size="15"
        />
        <select
          class="property-modifier select-modifier"
          v-else-if="property.type === `select`"
          v-model="newValues[index].value"
        >
          <option disabled value="">Please select one</option>
          <option
            v-for="option in property.options"
            v-bind:key="option.value"
            v-bind:value="option.value"
          >
            {{ option.label }}
          </option>
        </select>
      </div>
    </div>
    <entity-list-with-paginator
      v-bind:buttons-properties="buttonsProperties"
      v-bind:entities-properties="getParentsProperties"
      v-bind:page="getParentsPage"
      v-bind:is-error="areParentsError"
      v-bind:is-loaded="areParentsLoaded"
    ></entity-list-with-paginator>
    <div class="buttons"></div>
  </div>
</template>

<script>
import EntityListWithPaginator from "@/components/entity-list/EntityListWithPaginator";
import { mapState, mapActions } from "vuex";

export default {
  name: "EntityCreator",
  components: { EntityListWithPaginator },
  data() {
    return {
      newValues: this.entityProperties.map((property) => {
        return { label: property.label, value: property.initialValue };
      }),
      buttonsProperties: {
        isEditVisible: false,
        isDeleteVisible: false,
      },
      areParentsLoaded: false,
      areParentsError: false,
      newParentProperties: this.initialParentProperties,
      isParentChosen: this.initialParentProperties.length > 0,
    };
  },

  props: {
    entityProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
    initialParentProperties: {
      type: Array,
      required: false,
      default() {
        return [];
      },
    },
    parentsTransactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          namespace: "",
          getters: {
            entities: "",
            page: "",
          },
          actions: {
            loader: "",
          },
        };
      },
    },
  },

  emits: ["newEntity"],

  computed: {
    ...mapState({
      getParentsProperties(state, getters) {
        return getters[
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.getters.entities}`
        ];
      },
      getParentsPage(state, getters) {
        return getters[
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.getters.page}`
        ];
      },
    }),
  },

  methods: {
    ...mapActions({
      loadParents(dispatch, payload) {
        return dispatch(
          `${this.parentsTransactionMappings.namespace}/${this.parentsTransactionMappings.actions.loader}`,
          payload
        );
      },
    }),

    submit() {
      let newData = {};

      for (const key in Object.keys(this.newValues)) {
        const proxy = this.newValues[key];
        if (proxy.value !== undefined && proxy.value !== null) {
          newData[proxy.label] = proxy.value;
        }
      }

      if (
        !(
          newData &&
          Object.keys(newData).length === 0 &&
          Object.getPrototypeOf(newData) === Object.prototype
        )
      )
        this.$emit("newEntity", {
          entity: newData,
          parentId: this.newParentId,
        });
    },

    //TODO make getEntities as one implementation in store
    //TODO add changePage, saving parent, style a bit more

    reset() {
      for (const key in Object.keys(this.newValues))
        this.newValues[key].value =
          this.entityProperties[Number(key)].initialValue;
    },

    fetchParents(page) {
      this.areParentsLoaded = false;
      this.areParentsError = false;

      this.loadParents({
        queryParams: { page: page },
        ifSuccessHandler: () => {
          this.areParentsLoaded = true;
          this.areParentsError = false;
        },
        ifErrorHandler: () => {
          this.areParentsLoaded = true;
          this.areParentsError = true;
        },
      });
    },
  },

  created() {
    this.$watch(
      () => this.id,
      () => {
        this.fetchParents(0);
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.creator {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.properties-modifier {
  padding-left: 0.2rem;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  gap: 0.4rem;
  padding-bottom: 0.2rem;
}

.property-container {
  display: flex;
  gap: 0.8rem;
  align-items: center;
  justify-content: center;
}

.property-label {
  text-transform: capitalize;
}

.property-modifier {
  font-size: 1.6rem;
  color: var(--main-color);
}

.text-modifier {
  background: transparent;
  border: none;

  color: var(--main-color);
}

.text-modifier:focus {
  outline-width: 0;
  border-bottom: 2px solid var(--main-color);
}

.select-modifier {
  background: transparent;
  border: 2px solid var(--main-color);
}

.buttons {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
}

.submit-button {
  align-self: center;
}
</style>
