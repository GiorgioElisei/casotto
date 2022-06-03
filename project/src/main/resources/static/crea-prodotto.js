export default Vue.component("crea-prodotto", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea prodotto"></tool-bar>
    <form @submit.prevent="crea">
        <ion-item>
            <ion-label>nome</ion-label>
            <ion-input @ionChange="nome = $event.target.value" placeholder="nome"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>disponibilita</ion-label>
            <ion-input @ionChange="numeroDisponibilita = $event.target.value" min="1" placeholder="numeroDisponibilita" type="number" :value="numeroDisponibilita"></ion-input>
        </ion-item>
        <ion-item>
            <ion-label>prezzo</ion-label>
            <ion-textarea @ionChange="prezzo = $event.target.value" placeholder="prezzo"></ion-textarea>
        </ion-item>
        <ion-button expand="full" type="submit">crea</ion-button>
    </form>
    </ion-content>
    `,
  data() {
    return {
      nome: "",
      prezzo: "",
      numeroDisponibilita: 1,
    };
  },
  methods: {
    async crea() {
      if (this.nome == "" || this.prezzo == "" || this.numeroDisponibilita <= 0) {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        const res = await (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/creaProdotto?nome=" +
              this.nome +
              "&prezzo=" +
              this.prezzo +
              "&numeroDisponibilita=" +
              this.numeroDisponibilita,
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
