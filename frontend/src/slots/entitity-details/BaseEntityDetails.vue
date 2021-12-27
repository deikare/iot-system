<template>
  <main>
    <div class="first-column">
      <base-entity-properties
        class="properties"
        v-bind:properties="baseProperties"
        v-bind:is-loaded="isBaseLoaded"
        v-bind:is-error="isBaseError"
      >
      </base-entity-properties>

      <children-list
        class="children-list"
        v-bind:page="getChildrenPage"
        v-bind:is-error="areChildrenError"
        v-bind:is-loaded="areChildrenLoaded"
        v-on:changeChildrenPage="changeChildrenPage"
      >
        <template v-slot:children-header>
          <slot name="children-header"></slot>
        </template>

        <template v-slot:entity-item>
          <children-list-item
            v-for="child in getChildrenEntities"
            v-bind:key="child"
            v-bind:child-properties="child"
            v-on:childClicked="emitChildClicked"
          >
            <template v-slot:additional>
              <slot name="additional-content"></slot>
            </template>
          </children-list-item>
        </template>
      </children-list>
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
import ChildrenList from "@/slots/entitity-details/ChildrenList";
import ChildrenListItem from "@/slots/entitity-details/ChildrenListItem";

export default {
  name: "BaseEntityDetails",
  components: {
    ChildrenListItem,
    ChildrenList,
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
            },
          },
        };
      },
    },
  },

  emits: ["reloadBaseProperties", "changeChildrenPage", "childClicked"],

  computed: {
    ...mapState({
      getChildrenEntities(state, getters) {
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

  /*flex: 0 0 max-content;*/
}

.second-column,
.third-column {
  flex: 1;
}

.children-list {
}
</style>
