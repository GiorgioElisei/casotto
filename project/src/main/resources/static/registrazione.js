export default Vue.component("registrazione", {
  template: /*html*/ `
    <ion-content>
      <ion-grid class="container-centered-image">
        <form @submit.prevent="registra" class="item-centered">
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
            </ion-list>
            <ion-button type="submit">registra</ion-button>
            <ion-button color="light" @click="()=>$router.replace('/autenticazione')">autenticazione &rarr;</ion-button>
        </form>
      </ion-grid>
    </ion-content>
    `,
  data() {
    return {
      username: "",
      password: "",
      ripetipassword: "",
    };
  },
  methods: {
    async registra() {
      if (this.username === "" || this.password === "") {
        this.$emit("notifica", "campo mancante");
      } else if (this.password === this.ripetipassword) {
        this.$emit("caricamento", true);
         const res = await (
          await fetch(
            "api/registra?username=" +
              this.username +
              "&password=" +
              this.password,
            { method: "POST" }
          )
        ).text();
        if (res == "OK") {
          this.$emit("notifica", "successo");
          this.$router.go(-1);
        } else this.$emit("notifica", "[ERRORE]: "+res);
        this.$emit("caricamento", false);
      } else {
        this.$emit("notifica", "password differenti");
      }
    },
  },
});
