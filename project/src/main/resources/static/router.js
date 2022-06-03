import("./toolbar.js");

function autenticato() {
  const res = document.cookie.includes("id=");
  if (!res) {
    localStorage.removeItem("username");
    localStorage.removeItem("password");
  }
  return res;
}

const routes = [
  { path: "/autenticazione", component: () => import("./autenticazione.js") },
  { path: "/registrazione", component: () => import("./registrazione.js") },
  { path: "/utente", component: () => import("./utente.js") },

  { path: "/posizioni", component: () => import("./posizioni.js") },
  { path: "/crea-posizione", component: () => import("./crea-posizione.js") },
  { path: "/ombrelloni", component: () => import("./ombrelloni.js") },
  { path: "/crea-ombrelloni", component: () => import("./crea-ombrelloni.js") },
  { path: "/stagioni", component: () => import("./stagioni.js") },
  { path: "/crea-stagione", component: () => import("./crea-stagione.js") },
  { path: "/utenti", component: () => import("./utenti.js") },
  { path: "/crea-utente", component: () => import("./crea-utente.js") },
  { path: "/attivita", component: () => import("./attivita.js") },
  { path: "/crea-attivita", component: () => import("./crea-attivita.js") },
  { path: "/prodotti", component: () => import("./prodotti.js") },
  { path: "/crea-prodotto", component: () => import("./crea-prodotto.js") },

  {
    path: "/",
    redirect: () => {
      if (autenticato()) return "/utente";
      else return "/autenticazione";
    },
  },
];

new Vue({
  router: new VueRouter({ routes }),
  data() {
    return {
      utente: undefined,
      loading: undefined,
    };
  },
  async created() {
    await this.aggiorna();
  },
  methods: {
    setCookie(key, value, time = 60) {
      var limit = new Date();
      var now = new Date();
      limit.setTime(now.getTime() + parseInt(time) * 60000);
      document.cookie = key + "=" + value + "; expires=" + limit.toGMTString();
    },
    delAllCookies() {
      document.cookie
        .split(";")
        .forEach((c) => this.setCookie(c.split("=")[0].trim(), "", -1));
    },
    async aggiorna() {
      try {
        if (autenticato()) {
          this.utente = await this.getUser(
            localStorage.getItem("username"),
            localStorage.getItem("password")
          );
        } else {
          this.esci();
        }
      } catch (e) {
        this.esci();
      }
    },
    async getUser(username, password) {
      return await (
        await fetch(
          "api/autentica?username=" + username + "&password=" + password
        )
      ).json();
    },
    async autentica(username, password) {
      try {
        this.utente = await this.getUser(username, password);
        localStorage.setItem("username", username);
        localStorage.setItem("password", password);
        this.setCookie('id', this.utente.id);
        this.$router.replace({ path: "/utente" });
      } catch (e) {
        localStorage.clear();
        this.delAllCookies();
        this.notifica('errore di autenticazione');
      }
    },
    esci() {
      this.utente = undefined;
      this.delAllCookies();
      localStorage.clear();
      this.$router.replace({ path: "/autenticazione" });
    },
    caricamento(flag) {
      try {
        if (flag && !this.loading) {
          this.loading = document.createElement("ion-loading");
          this.loading.cssClass = "caricamento";
          this.loading.message = "caricamento...";
          document.body.appendChild(this.loading);
          this.loading.present();
        } else {
          this.loading.dismiss();
        }
      } catch (e) { }
    },
    notifica(testo) {
      const toast = document.createElement("ion-toast");
      toast.message = testo;
      toast.duration = 2000;
      document.body.appendChild(toast);
      return toast.present();
    },
  },
}).$mount("#app");
