export default Vue.component("posizioni", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button expand="full" @click="$router.push('/crea-posizione')">
        crea posizione
      </ion-button>
    <ion-card v-for="posizione in posizioni">
      <ion-card-header>
        <ion-card-title>{{posizione.nome}} - {{posizione.prezzo}}€</ion-card-title>
      </ion-card-header>
      <ion-card-content>
        {{posizione.descrizione}}
      </ion-card-content>
      <ion-button color="danger" expand="full" @click="deletePosizione(posizione.id)">
          <ion-icon name="trash"></ion-icon>
      </ion-button>
    </ion-card>
  </ion-content>
        `,
  data() {
    return {
      posizioni: [],
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.$emit("caricamento", true);
      this.posizioni = await (
        await fetch("/api/" + this.utente.tipo + "/posizioni")
      ).json();
      this.$emit("caricamento", false);
    },
    async deletePosizione(id) {
      this.$emit("caricamento", true);
      if(confirm("Sei sicuro di voler eliminare la posizione?")) {
        const res = await (
          await fetch(
            "/api/" + this.utente.tipo + "/eliminaPosizione?idPosizione=" + id,
            {
              method: "DELETE",
            }
          )
        ).text();
        this.$emit("notifica", res);
      }
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
  },
});
