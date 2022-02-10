export default Vue.component("crea-stagione", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea stagioni"></tool-bar>
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
            <ion-label>sorting</ion-label>
            <ion-input @ionChange="sorting = $event.target.value" placeholder="sorting" type="number" :value="sorting"></ion-input>
        </ion-item>
        <ion-button expand="full" type="submit">crea</ion-button>
    </form>
    </ion-content>
    `,
  data() {
    return {
      nome: "",
      sorting: 0,
      prezzo: 0,
    };
  },
  methods: {
    async crea() {
      if (this.nome == "" || this.sorting <= 0 || this.prezzo <= 0) {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        const status = (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/creaStagione?nome=" +
              this.nome +
              "&sorting=" +
              this.sorting +
              "&prezzo=" +
              this.prezzo,
            { method: "POST" }
          )
        ).status;
        if (status >= 200 && status < 300) {
          this.$emit("notifica", "successo");
          this.$router.go(-1);
        } else this.$emit("notifica", "errore durante la creazione");
        this.$emit("caricamento", false);
      }
    },
  },
});
