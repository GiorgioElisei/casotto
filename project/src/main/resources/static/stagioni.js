export default Vue.component("stagioni", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button expand="full" @click="$router.push('/crea-stagione')">
        crea stagione
      </ion-button>
      <ion-card v-if="stagioni.length == 0">
        <ion-title>non ci sono ancora stagioni disponibili</ion-title>
      </ion-card>
      <ion-list v-else>
        <ion-item v-for="stagione in stagioni">
          <ion-label>{{stagione.nome}} - {{stagione.prezzo}}€</ion-label>
          <ion-button color="danger" shape="round" @click="deleteStagione(stagione.id)">
              <ion-icon name="trash"></ion-icon>
          </ion-button>
        </ion-item>
      </ion-list>
  </ion-content>
        `,
  data() {
    return {
      stagioni: [],
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.$emit("caricamento", true);
      this.stagioni = (
        await (await fetch("/api/" + this.utente.tipo + "/stagioni")).json()
      ).sort((x, y) => x.sorting < y.sorting);
      this.$emit("caricamento", false);
    },
    async deleteStagione(id) {
      this.$emit("caricamento", true);
      if(confirm("Sei sicuro di voler eliminare la stagione?")) {
        const res = await (
          await fetch(
            "/api/" + this.utente.tipo + "/eliminaStagione?idStagione=" + id,
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
