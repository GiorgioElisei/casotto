export default Vue.component("autenticazione", {
  template: /*html*/ `
    <ion-content>
      <ion-grid class="container-centered-image">
        <form @submit.prevent="autentica" class="item-centered">
            <ion-list>
                <ion-item>
                    <ion-label>username</ion-label>
                    <ion-input @ionChange="username = $event.target.value" placeholder="username" type="text"></ion-input>
                </ion-item>
                <ion-item>
                    <ion-label>password</ion-label>
                    <ion-input @ionChange="password = $event.target.value" placeholder="password" type="password"></ion-input>
                </ion-item>
            </ion-list>
            <ion-button type="submit">autentica</ion-button>
            <ion-button color="light" @click="()=>$router.replace('/registrazione')">registrazione &rarr;</ion-button>
        </form>
      </ion-grid>
    </ion-content>
    `,
  data() {
    return {
      username: "",
      password: "",
    };
  },
  methods: {
    async autentica() {
      if (this.username === "" || this.password === "") {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        try {
          await this.$emit("autentica", this.username, this.password);
        } catch (e) {
          this.$emit("notifica", "errore di autenticazione");
        }
        this.$emit("caricamento", false);
      }
    },
  },
});
