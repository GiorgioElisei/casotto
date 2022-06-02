export default Vue.component("crea-posizione", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea posizioni"></tool-bar>
    <form @submit.prevent="crea">
        <ion-item>
            <ion-label>nome</ion-label>
            <ion-input @ionChange="nome = $event.target.value" placeholder="nome"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>prezzo</ion-label>
            <ion-input @ionChange="prezzo = $event.target.value" placeholder="prezzo" type="number" :value="prezzo"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>descrizione</ion-label>
            <ion-textarea @ionChange="descrizione = $event.target.value" placeholder="descrizione"></ion-textarea>
        </ion-item>
        <ion-button expand="full" type="submit">crea</ion-button>
    </form>
    </ion-content>
    `,
  data() {
    return {
      nome: "",
      descrizione: "",
      prezzo: 0,
    };
  },
  methods: {
    async crea() {
      if (this.nome == "" || this.descrizione == "" || this.prezzo <= 0) {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        const res = await (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/creaPosizione?nome=" +
              this.nome +
              "&descrizione=" +
              this.descrizione +
              "&prezzo=" +
              this.prezzo,
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
