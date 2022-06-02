export default Vue.component("utenti", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button expand="full" @click="$router.push('/crea-utente')">
        crea utente
      </ion-button>
    <ion-card v-for="utente in utenti">
      <ion-card-header>
        <ion-card-title>{{utente.username}}</ion-card-title>
        <ion-card-description>{{utente.tipo}}</ion-card-description>
      </ion-card-header>
      <ion-card-content v-if="utente.tipo == 'addetto'">
      <ion-label>posizioni</ion-label>
      <ion-list>
          <ion-item v-for="posizione in utente.posizioni">
            <ion-label>
                {{posizione.nome}}
            </ion-label>
          </ion-item>
        </ion-list>
      </ion-card-content>
      <ion-button v-if="utente.tipo != 'cliente'" color="danger" expand="full" @click="deleteUtente(utente.id)">
          <ion-icon name="trash"></ion-icon>
      </ion-button>
    </ion-card>
  </ion-content>
        `,
  data() {
    return {
      utenti: [],
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.$emit("caricamento", true);
      this.utenti = await (
        await fetch("/api/" + this.utente.tipo + "/utenti")
      ).json();
      this.$emit("caricamento", false);
    },
    async deleteUtente(id) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/eliminaUtente?idUtente=" + id,
          {
            method: "DELETE",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
  },
});
