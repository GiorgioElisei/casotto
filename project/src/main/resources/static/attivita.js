export default Vue.component("attivita", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button v-if="utente.tipo=='addetto'" @click="$router.push('/crea-attivita')" expand="full">
        crea attivita
      </ion-button>
      <ion-card v-if="attivita.length == 0">
        <ion-title>non ci sono ancora attivita disponibili</ion-title>
      </ion-card>
      <ion-card v-else v-for="(att, key) in attivita" :key="key">
      <ion-card-header>
        <ion-card-title>[ID:{{att.id}}]</ion-card-title>
        <ion-card-title>{{att.nome}} - {{att.stagione.nome}}</ion-card-title>
        <ion-card-title v-if="!att.stato" color="danger">Attività eliminata</ion-card-title>
        <ion-card-subtitle>partecipanti: {{att.partecipanti.length}} / {{att.numeroMassimoPartecipanti}}</ion-card-subtitle>
      </ion-card-header>
      <ion-card-content>
        <ion-label>{{att.descrizione}}</ion-label>
      </ion-card-content>
      <ion-button v-if="utente.tipo=='addetto' && att.stato" color="danger" expand="full" @click="eliminaAttivita(att.id)">
          <ion-icon name="trash"></ion-icon>
      </ion-button>
      <div v-if="utente.tipo=='cliente' && att.stato">
        <ion-button v-if="!att.partecipanti.map(p => p.id).includes(utente.id)" expand="full" @click="partecipaAttivita(att.id)">
            partecipa
        </ion-button>
        <ion-button v-else color="danger" expand="full" @click="eliminaPartecipaAttivita(att.id)">
            non partecipare più
        </ion-button>
      </div>
    </ion-card>
  </ion-content>
        `,
  data() {
    return {
      attivita: [],
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.$emit("caricamento", true);
      this.attivita = await (await fetch("/api/" + this.utente.tipo + "/attivita")).json();
      this.attivita.sort((x, y) => y.id - x.id);
      this.$emit("caricamento", false);
    },
    async eliminaAttivita(id) {
      this.$emit("caricamento", true);
      if(confirm("Sei sicuro di voler eliminare l'attivita?")) {
        const res = await (
          await fetch(
            "/api/" + this.utente.tipo + "/eliminaAttivita?idAttivita=" + id,
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
    async partecipaAttivita(id) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/partecipaAttivita?idAttivita=" + id,
          {
            method: "PUT",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
    async eliminaPartecipaAttivita(id) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/eliminaPartecipaAttivita?idAttivita=" + id,
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
