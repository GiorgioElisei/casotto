export default Vue.component("crea-attivita", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea attività"></tool-bar>
    <form @submit.prevent="crea">
        <ion-item>
            <ion-label>nome</ion-label>
            <ion-input @ionChange="nome = $event.target.value" placeholder="nome"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>numero massimo partecipanti</ion-label>
            <ion-input @ionChange="numeroMassimoPartecipanti = $event.target.value" min="1" placeholder="numeroMassimoPartecipanti" type="number" :value="numeroMassimoPartecipanti"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>descrizione</ion-label>
            <ion-textarea @ionChange="descrizione = $event.target.value" placeholder="descrizione"></ion-textarea>
        </ion-item>
        <ion-item>
            <ion-label>stagioni</ion-label>
            <ion-select @ionChange="selectedStagione = $event.target.value" placeholder="stagione">
            <ion-select-option v-for="stagione in stagioni" :value="stagione.id">{{stagione.nome}}</ion-select-option>
            </ion-select>
        </ion-item>
        <ion-button expand="full" type="submit">crea</ion-button>
    </form>
    </ion-content>
    `,
  data() {
    return {
      nome: "",
      descrizione: "",
      numeroMassimoPartecipanti: 1,
      stagioni: [],
      selectedStagione: undefined
    };
  },
    async mounted() {
      this.stagioni = await (
        await fetch("api/" + this.utente.tipo + "/stagioni")
      ).json();
    },
  methods: {
    async crea() {
      if (this.nome == "" || this.descrizione == "" || this.numeroMassimoPartecipanti <= 0) {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        const res = await (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/creaAttivita?nome=" +
              this.nome +
              "&descrizione=" +
              this.descrizione +
              "&numeroMassimoPartecipanti=" +
              this.numeroMassimoPartecipanti +
              "&idStagione=" +
              this.selectedStagione,
            { method: "POST" }
          )
        ).text();
        if (res == "OK") {
          this.$emit("notifica", "successo");
          this.$router.go(-1);
        } else this.$emit("notifica", "[ERRORE]: "+res);
        this.$emit("caricamento", false);
      }
    },
  },
});
