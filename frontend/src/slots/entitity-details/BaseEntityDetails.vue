<template>
  <main>
    <div class="first-column">
      <base-entity-properties
        class="properties"
        v-bind:properties="baseProperties"
        v-bind:is-loaded="isBaseLoaded"
        v-bind:is-error="isBaseError"
        v-on:changeProperties="emitChangeProperties"
      >
      </base-entity-properties>

      <base-entity-properties-modifier
        v-bind:entity-properties="propertiesModifier"
      ></base-entity-properties-modifier>

      <children-list-with-paginator
        class="children-list-with-paginator"
        v-bind:children-properties="getChildrenProperties"
        v-bind:page="getChildrenPage"
        v-bind:is-error="areChildrenError"
        v-bind:is-loaded="areChildrenLoaded"
        v-on:changeChildrenPage="changeChildrenPage"
        v-on:childClicked="emitChildClicked"
        v-on:deleteChild="deleteChildAndReload"
      >
        <template v-slot:children-header>
          <slot name="children-header"></slot>
        </template>
      </children-list-with-paginator>
    </div>

    <div class="second-column">
      <base-card class="logs">
        <template v-slot:header>Logs</template>
        <template v-slot:default>asd</template>
      </base-card>
    </div>

    <div class="third-column">
      <base-card class="data">
        <template v-slot:header>Data</template>
        <template v-slot:default>asd</template>
      </base-card>
    </div>
  </main>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import BaseEntityProperties from "@/slots/entitity-details/BaseEntityProperties";
import { mapState, mapActions } from "vuex";
import ChildrenListWithPaginator from "@/slots/entitity-details/ChildrenListWithPaginator";
import BaseEntityPropertiesModifier from "@/slots/entitity-details/BaseEntityPropertiesModifier";

export default {
  name: "BaseEntityDetails",
  components: {
    BaseEntityPropertiesModifier,
    ChildrenListWithPaginator,
    BaseEntityProperties,
    BaseCard,
  },

  data() {
    return {
      areChildrenLoaded: false,
      areChildrenError: false,
    };
  },

  props: {
    id: {
      type: String,
      required: true,
      default: "",
    },

    isBaseLoaded: {
      type: Boolean,
      required: true,
      default: false,
    },

    isBaseError: {
      type: Boolean,
      required: true,
      default: false,
    },

    baseProperties: {
      type: Object,
      required: true,
      default() {
        return {};
      },
    },

    transactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          children: {
            namespace: "",
            getters: {
              entities: "",
              page: "",
            },
            actions: {
              loader: "",
              deleter: "",
            },
          },
        };
      },
    },

    propertiesModifier: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },
  },

  emits: [
    "reloadBaseProperties",
    "changeChildrenPage",
    "childClicked",
    "changeProperties",
  ],

  computed: {
    ...mapState({
      getChildrenProperties(state, getters) {
        return getters[
          `${this.transactionMappings.children.namespace}/${this.transactionMappings.children.getters.entities}`
        ];
      },
      getChildrenPage(state, getters) {
        return getters[
          `${this.transactionMappings.children.namespace}/${this.transactionMappings.children.getters.page}`
        ];
      },
    }),
  },

  methods: {
    ...mapActions({
      loadChildren(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.children.namespace}/${this.transactionMappings.children.actions.loader}`,
          payload
        );
      },

      deleteChild(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.children.namespace}/${this.transactionMappings.children.actions.deleter}`,
          payload
        );
      },
    }),

    fetchChildrenEntities(page) {
      this.areChildrenLoaded = false;
      this.areChildrenError = false;

      const queryParams = {
        page: page,
        hubId: this.id,
      };

      console.log(queryParams);

      this.loadChildren({
        queryParams: {
          page: page,
          hubId: this.id,
        },
        ifSuccessHandler: () => {
          this.areChildrenLoaded = true;
          this.areChildrenError = false;
        },
        ifErrorHandler: () => {
          this.areChildrenLoaded = true;
          this.areChildrenError = true;
        },
      });
    },

    changeChildrenPage(page) {
      this.fetchChildrenEntities(page - 1);
    },

    emitChildClicked(childId) {
      this.$emit("childClicked", childId);
    },

    emitChangeProperties() {
      this.$emit("changeProperties");
    },

    deleteChildAndReload(childId) {
      const payload = {
        id: childId,
        ifSuccessHandler: () => {
          this.fetchChildrenEntities(this.getChildrenPage.currentPage - 1);
          //TODO add custom toasts
        },
        ifErrorHandler: () => {
          //TODO add custom toasts
          console.log("error");
        },
      };

      this.deleteChild(payload);
    },
  },

  watch: {
    isBaseLoaded(newValue, oldValue) {
      if (newValue === true && oldValue === false) {
        this.fetchChildrenEntities(0);
      }
    },
  },
};
</script>

<style scoped>
main {
  display: flex;
  flex: 0 0 25rem;
  /*grid-template-columns: 20% 1fr 1fr;*/
  gap: 1.6rem;
}

header {
  grid-row: 1;
  grid-column: 1/-1;
  display: flex;
  align-items: center;
  justify-content: left;

  padding-left: 1.6rem;

  background-color: var(--main-color);

  gap: 2.4rem;
}

.first-column {
  display: flex;
  flex-direction: column;
  gap: 1.6rem;

  flex-grow: 0;
  flex-shrink: 0;
  flex-basis: 20%;
}

.second-column,
.third-column {
  flex: 1;
}

.children-list-with-paginator {
}
</style>
