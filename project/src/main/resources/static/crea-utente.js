export default Vue.component("crea-utente", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea ombrelloni"></tool-bar>
    <form @submit.prevent="registra">
            <ion-list>
                <ion-item>
                    <ion-label>username</ion-label>
                    <ion-input @ionChange="username = $event.target.value" placeholder="username" type="text"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>password</ion-label>
                    <ion-input @ionChange="password = $event.target.value" placeholder="password" type="password"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>ripeti password</ion-label>
                    <ion-input @ionChange="ripetipassword = $event.target.value" placeholder="ripeti password" type="password"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>tipo</ion-label>
                    <ion-select @ionChange="tipo = $event.target.value" placeholder="posizione">
                    <ion-select-option value="addetto">addetto</ion-select-option>
                    </ion-select>
                </ion-item>
                <ion-item :disabled="tipo != 'addetto'">
                    <ion-label>posizioni</ion-label>
                    <ion-select multiple="true" @ionChange="selectedPosizioniId = $event.target.value" placeholder="posizione">
                    <ion-select-option v-for="posizione in posizioni" :value="posizione.id">{{posizione.nome}}</ion-select-option>
                    </ion-select>
                </ion-item>
            </ion-list>
            <ion-button type="submit">registra</ion-button>
        </form>
    </ion-content>
    `,
  data() {
    return {
      username: "",
      password: "",
      tipo: "",
      ripetipassword: "",
      posizioni: [],
      selectedPosizioniId: [],
    };
  },
  async mounted() {
    this.posizioni = await (
      await fetch("api/" + this.utente.tipo + "/posizioni")
    ).json();
  },
  methods: {
    async registra() {
      if (
        this.username === "" ||
        this.password === "" ||
        this.tipo === "" ||
        (this.tipo === "addetto" && this.selectedPosizioniId.length == 0)
      ) {
        this.$emit("notifica", "campo mancante");
      } else if (this.password === this.ripetipassword) {
        this.$emit("caricamento", true);
        let idUtente = await (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/registraUtente?username=" +
              this.username +
              "&password=" +
              this.password +
              "&tipo=" +
              this.tipo,
            { method: "POST" }
          )
        ).json();
        if (this.tipo == "addetto") {
          this.selectedPosizioniId.forEach((p) =>
            fetch(
              "api/" +
                this.utente.tipo +
                "/addPosizioneToAddetto?idAddetto=" +
                idUtente +
                "&idPosizione=" +
                p,
              { method: "PUT" }
            )
          );
        }
        if (idUtente >= 0) this.$emit("notifica", "successo");
        else this.$emit("notifica", "errore");
        this.$emit("caricamento", false);
        this.$router.go(-1);
      } else {
        this.$emit("notifica", "password differenti");
      }
    },
  },
});
