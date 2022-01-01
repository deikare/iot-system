<template>
  <div class="entity-container">
    <base-entity-header
      v-bind:is-base-loaded="isBaseLoaded"
      v-on:deleteEntity="removeBaseEntityThenRoute"
    >
      <template v-slot:icon>
        <slot name="icon"></slot>
      </template>

      <template v-slot:entityId>
        <slot name="entityId"></slot>
      </template>
    </base-entity-header>

    <main>
      <teleport to=".main-with-margin">
        <entity-creator
          class="entity-creator"
          v-if="isAddChildVisible"
          v-bind:entity-properties="newChildProperties"
          v-bind:parents-transaction-mappings="parentsTransactionMappings"
          v-bind:initial-parent-id="id"
          v-click-outside="closeAddChild"
          v-on:closeComponent="closeAddChild"
        ></entity-creator>
      </teleport>

      <div class="first-column">
        <base-entity-properties
          class="properties"
          v-bind:entity-properties="getBaseProperties"
          v-bind:is-loaded="isBaseLoaded"
          v-bind:is-error="isBaseError"
          v-on:changeProperties="patchBasePropertiesAndReload"
        >
        </base-entity-properties>

        <children-list-with-paginator
          class="children-list-with-paginator"
          v-bind:children-properties="getChildrenProperties"
          v-bind:page="getChildrenPage"
          v-bind:is-error="areChildrenError"
          v-bind:is-loaded="areChildrenLoaded"
          v-bind:buttons-properties="buttonsProperties"
          v-on:changeChildrenPage="changeChildrenPage"
          v-on:addChild="showAddChild"
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
  </div>
</template>

<script>
import BaseCard from "@/slots/abstract/BaseCard";
import BaseEntityProperties from "@/slots/entitity-details/BaseEntityProperties";
import { mapState, mapActions } from "vuex";
import ChildrenListWithPaginator from "@/slots/entitity-details/ChildrenListWithPaginator";
import BaseEntityHeader from "@/slots/entitity-details/BaseEntityHeader";
import EntityCreator from "@/slots/entities-creator/EntityCreator";

export default {
  name: "BaseEntityDetails",
  components: {
    EntityCreator,
    BaseEntityHeader,
    ChildrenListWithPaginator,
    BaseEntityProperties,
    BaseCard,
  },

  data() {
    return {
      isBaseLoaded: false,
      isBaseError: false,
      areChildrenLoaded: false,
      areChildrenError: false,
      isAddChildVisible: false,
    };
  },

  props: {
    id: {
      type: String,
      required: true,
      default: "",
    },

    transactionMappings: {
      type: Object,
      required: true,
      default() {
        return {
          base: {
            namespace: "",
            getters: {
              properties: "",
            },
            actions: {
              loader: "",
              deleter: "",
              patcher: "",
            },
          },
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
          parents: {
            namespace: "",
            getters: {
              entities: "",
              page: "",
            },
            actions: {
              loader: "",
            },
          },
        };
      },
    },

    ifBaseDeletedRoute: {
      type: Object,
      required: true,
      default() {
        return {
          name: "",
        };
      },
    },

    newChildProperties: {
      type: Array,
      required: true,
      default() {
        return [];
      },
    },

    buttonsProperties: {
      type: Object,
      required: false,
      default() {
        return {
          isEditVisible: true,
          isDeleteVisible: true,
        };
      },
    },
  },

  emits: ["childClicked"],

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
      getBaseProperties(state, getters) {
        return getters[
          `${this.transactionMappings.base.namespace}/${this.transactionMappings.base.getters.properties}`
        ];
      },
    }),

    parentsTransactionMappings() {
      return this.transactionMappings.parents;
    },
  },

  methods: {
    ...mapActions({
      loadBaseEntity(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.base.namespace}/${this.transactionMappings.base.actions.loader}`,
          payload
        );
      },
      deleteBaseEntity(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.base.namespace}/${this.transactionMappings.base.actions.deleter}`,
          payload
        );
      },
      patchBaseEntity(dispatch, payload) {
        return dispatch(
          `${this.transactionMappings.base.namespace}/${this.transactionMappings.base.actions.patcher}`,
          payload
        );
      },
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

    fetchBaseEntity() {
      this.isBaseLoaded = false;
      this.isBaseError = false;

      this.loadBaseEntity({
        id: this.id,
        ifSuccessHandler: () => {
          this.isBaseLoaded = true;
          this.isBaseError = false;
        },
        ifErrorHandler: () => {
          this.isBaseLoaded = true;
          this.isBaseError = true;
        },
      });
    },

    removeBaseEntityThenRoute() {
      const payload = {
        id: this.id,
        ifSuccessHandler: () => {
          this.$router.push(this.ifBaseDeletedRoute);
        },
        ifErrorHandler: () => {
          //TODO add custom toasts
        },
      };

      this.deleteBaseEntity(payload);
    },

    patchBasePropertiesAndReload(data) {
      const payload = {
        id: this.id,
        data: data,
        ifSuccessHandler: () => {
          this.fetchBaseEntity();
        },
        ifErrorHandler: (error) => {
          console.log(error);
          //TODO - case if there is no entity which mod was requested
        },
      };

      this.patchBaseEntity(payload);
    },

    fetchChildrenEntities(page) {
      this.areChildrenLoaded = false;
      this.areChildrenError = false;

      const queryParams = {
        page: page,
        hubId: this.id, //TODO
      };

      console.log(queryParams);

      this.loadChildren({
        queryParams: {
          page: page,
          hubId: this.id, //TODO
        },
        ifSuccessHandler: () => {
          this.areChildrenLoaded = true;
          this.areChildrenError = false;
        },
        ifErrorHandler: (error) => {
          console.log(error);
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

    deleteChildAndReload(childId) {
      const payload = {
        id: childId,
        ifSuccessHandler: () => {
          this.fetchChildrenEntities(this.getChildrenPage.currentPage - 1);
          //TODO add custom toasts
        },
        ifErrorHandler: (error) => {
          console.log(error);
          //TODO add custom toasts
          console.log("error");
        },
      };

      this.deleteChild(payload);
    },

    showAddChild() {
      this.isAddChildVisible = true;
    },

    closeAddChild() {
      this.isAddChildVisible = false;
    },
  },

  created() {
    this.$watch(
      () => this.id,
      () => {
        this.fetchBaseEntity();
        this.fetchChildrenEntities(0);
      },
      { immediate: true, deep: true }
    );
  },
};
</script>

<style scoped>
.entity-container {
  display: flex;
  flex-direction: column;
  gap: 2.4rem;

  /*position: relative;*/
}

main {
  display: flex;
  flex: 0 0 25rem;
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

.entity-creator {
  position: absolute;
  left: 50%;
  transform: translate(-50%, 0);
}
</style>
