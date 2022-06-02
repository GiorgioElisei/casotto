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
            <ion-card v-if="notifiche.length > 0">
                <ion-card-header>
                  <ion-card-title>Notifiche</ion-card-title>
                </ion-card-header>
              <ion-card-content>
                <ion-list>
                  <ion-item v-for="notifica in notifiche">
                    <ion-label>{{notifica.messaggio}}</ion-label>
                    <ion-button color="danger" shape="round" @click="eliminaNotifica(notifica.id)">
                        <ion-icon name="trash"></ion-icon>
                    </ion-button>
                  </ion-item>
                </ion-list>
              </ion-card-content>
            </ion-card>
            <ion-button @click="esci" color="danger">esci</ion-button>
            </form>
        </ion-grid>
      </ion-content>
        `,
  data() {
    return {
      notifiche: []
    }
  },
  async mounted() {
    this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.notifiche = await (
        await fetch("api/notifiche")
      ).json();
      this.notifiche.sort((x, y) => y.id - x.id);
    },
    async eliminaNotifica(id) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/eliminaNotifica?idNotifica=" + id,
          {
            method: "DELETE",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
    esci() {
      this.$emit("esci");
    },
  },
});