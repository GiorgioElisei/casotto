export default Vue.component("utente", {
  props: {
    utente: {
      type: Object,
    },
  },
  template:
    /*html*/
    `<ion-content>
      <ion-grid class="container-centered-image2">
        <form v-show="utente" class="item-centered">
            <ion-list>
                <ion-item>
                    <ion-label>username</ion-label>
                    <ion-title>{{utente.username}}</ion-title>
                </ion-item>
                <ion-item>
                    <ion-label>tipo</ion-label>
                    <ion-title>{{utente.tipo}}</ion-title>
                </ion-item>
            </ion-list>
            <ion-button @click="esci" color="danger">esci</ion-button>
            </form>
        </ion-grid>
      </ion-content>
        `,
  methods: {
    esci() {
      this.$emit("esci");
    },
  },
});