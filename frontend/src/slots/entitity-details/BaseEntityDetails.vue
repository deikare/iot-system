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
          v-bind:transaction-mappings="addChildTransactionMappings"
          v-bind:initial-parent-id="id"
          v-click-outside="closeAddChild"
          v-on:closeComponent="closeAddChild"
          v-on:newEntityCreated="fetchChildrenEntities(0)"
        >
          <template v-slot:entityType>
            <slot name="childType"></slot>
          </template>
          <template v-slot:parentType>
            <slot name="parentOfChildType"></slot>
          </template>
        </entity-creator>
      </teleport>

      <teleport to=".main-with-margin">
        <entity-modifier
          class="entity-creator"
          v-if="isEditChildVisible && !isAddChildVisible"
          v-bind:transaction-mappings="editChildTransactionMappings"
          v-bind:id="editedChildId"
          v-click-outside="closeEditChild"
          v-on:closeComponent="closeEditChild"
          v-on:entityUpdated="fetchChildrenEntities(0)"
        >
          <template v-slot:entityType>
            <slot name="childType"></slot>
          </template>
        </entity-modifier>
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
          v-on:editChild="editChild"
          v-on:childClicked="emitChildClicked"
          v-on:deleteChild="deleteChildAndReload"
        >
          <template v-slot:children-header>
            <slot name="children-header"></slot>
          </template>
        </children-list-with-paginator>
      </div>

      <div class="second-column">
        <logs-list
          v-bind:transaction-mappings="logsTransactionMappings"
          v-bind:query-entity="queryEntity"
        ></logs-list>
      </div>

      <div class="third-column">
        <entity-data
          v-bind:transaction-mappings="dataSeriesTransactionMappings"
          v-bind:query-entity="queryEntity"
        >
        </entity-data>
      </div>
    </main>
  </div>
</template>

<script>
import BaseEntityProperties from "@/slots/entitity-details/BaseEntityProperties";
import { mapState, mapActions } from "vuex";
import ChildrenListWithPaginator from "@/slots/entitity-details/ChildrenListWithPaginator";
import BaseEntityHeader from "@/slots/entitity-details/BaseEntityHeader";
import EntityCreator from "@/slots/entities-creator/EntityCreator";
import EntityModifier from "@/slots/entities-modifier/EntityModifier";
import LogsList from "@/slots/entity-logs-list/LogsList";
import EntityData from "@/slots/entity-data/EntityData";

export default {
  name: "BaseEntityDetails",
  components: {
    EntityData,
    LogsList,
    EntityModifier,
    EntityCreator,
    BaseEntityHeader,
    ChildrenListWithPaginator,
    BaseEntityProperties,
  },

  data() {
    return {
      isBaseLoaded: false,
      isBaseError: false,
      areChildrenLoaded: false,
      areChildrenError: false,
      isAddChildVisible: false,
      isEditChildVisible: false,

      editedChildId: "",
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
              sender: "",
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
          child: {
            namespace: "",
            getters: {
              entity: "",
            },
            actions: {
              loader: "",
              patcher: "",
            },
          },
          logs: {
            namespace: "",
            loader: "",
            getter: "",
          },

          dataSeries: {
            namespace: "",
            loader: "",
            getter: "",
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

    queryEntity: {
      type: Object,
      required: true,
      default() {
        return {
          id: "",
          idName: "",
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

    addChildTransactionMappings() {
      return {
        parents: { ...this.transactionMappings.parents },
        entity: {
          namespace: this.transactionMappings.children.namespace,
          actions: {
            sender: this.transactionMappings.children.actions.sender,
          },
        },
      };
    },

    logsTransactionMappings() {
      return this.transactionMappings.logs;
    },

    dataSeriesTransactionMappings() {
      return this.transactionMappings.dataSeries;
    },

    editChildTransactionMappings() {
      return this.transactionMappings.child;
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
        ifErrorHandler: () => {},
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
        ifErrorHandler: () => {},
      };

      this.patchBaseEntity(payload);
    },

    fetchChildrenEntities(page) {
      this.areChildrenLoaded = false;
      this.areChildrenError = false;

      this.loadChildren({
        queryParams: {
          page: page,
          parentId: this.id,
        },
        size: 10,
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
        },
        ifErrorHandler: () => {},
      };

      this.deleteChild(payload);
    },

    showAddChild() {
      this.isAddChildVisible = true;
    },

    editChild(childId) {
      this.editedChildId = childId;
      this.isEditChildVisible = true;
    },

    closeAddChild() {
      this.isAddChildVisible = false;
    },

    closeEditChild() {
      this.editedChildId = "";
      this.isEditChildVisible = false;
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
