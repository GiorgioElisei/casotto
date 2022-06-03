export default Vue.component("prodotti", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button v-if="utente.tipo=='fornitore'" @click="$router.push('/crea-prodotto')" expand="full">
        crea prodotto
      </ion-button>
      <ion-card v-if="prodotti.length == 0">
        <ion-title>non ci sono ancora prodotti disponibili</ion-title>
      </ion-card>
      <ion-card v-else v-for="(prodotto, key) in prodotti" :key="key">
        <ion-card-header>
          <ion-card-title>[ID:{{prodotto.id}}]</ion-card-title>
          <ion-card-title>{{prodotto.nome}} - {{prodotto.prezzo}}€</ion-card-title>
          <ion-card-subtitle>disponibilità: {{prodotto.prenotazioni.length == 0 ? 0 : prodotto.prenotazioni.map(p => p.quantita).reduce((x, y) => x+y)}} / {{prodotto.numeroDisponibilita}}</ion-card-subtitle>
        </ion-card-header>
        <div v-if="utente.tipo=='fornitore'">
          <ion-card-content>
            <ion-list>
              <ion-item v-for="ordine in prodotto.prenotazioni">
                <ion-label>[{{ordine.cliente.id}}] - {{ordine.cliente.username}}</ion-label>
                <ion-button shape="round" @click="compraProdotto(prodotto.id, ordine.cliente.id)">
                    comprato
                </ion-button>
              </ion-item>
            </ion-list>
          </ion-card-content>
          <ion-button @click="incrementaProdotto(prodotto.id, prodotto.numeroDisponibilita+1)">
              +
          </ion-button>
          <ion-button @click="incrementaProdotto(prodotto.id, prodotto.numeroDisponibilita-1)">
              -
          </ion-button>
          <ion-button color="danger" expand="full" @click="eliminaProdotto(prodotto.id)">
              <ion-icon name="trash"></ion-icon>
          </ion-button>
        </div>
        <div v-if="utente.tipo=='cliente'">
          <ion-item>
              <ion-label>quantità</ion-label>
              <ion-input @ionChange="prodotto.quantitaSelezionata = $event.target.value" min="1" placeholder="quantita prodotto" type="number" :value="prodotto.quantitaSelezionata"></ion-input>
          </ion-item>
          <ion-button expand="full" @click="prenota(prodotto)">
              prenota prodotto
          </ion-button>
        </div>
      </ion-card>
    </ion-content>
        `,
  data() {
    return {
      prodotti: [],
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    async aggiorna() {
      this.$emit("caricamento", true);
      this.prodotti = await (await fetch("/api/" + this.utente.tipo + "/prodotti")).json();
      this.prodotti.sort((x, y) => y.id - x.id);
      this.prodotti.forEach(prod => {
        const ordine = prod.prenotazioni.find(p => p.cliente.id == this.utente.id);
        prod.quantitaSelezionata = ordine ? ordine.quantita : 0;
      });
      this.$emit("caricamento", false);
    },
    async eliminaProdotto(id) {
      this.$emit("caricamento", true);
      if (confirm("Sei sicuro di voler eliminare il prodotto?")) {
        const res = await (
          await fetch(
            "/api/" + this.utente.tipo + "/eliminaProdotto?idProdotto=" + id,
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
    async incrementaProdotto(id, count) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/rifornisciProdotto?idProdotto=" + id + "&numeroDisponibilita=" + count,
          {
            method: "PUT",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
    async prenota(prodotto) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/prenotaProdotto?idProdotto=" + prodotto.id + "&quantita=" + prodotto.quantitaSelezionata,
          {
            method: "PUT",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
    async compraProdotto(idProdotto, idCliente) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "/api/" + this.utente.tipo + "/compraProdotto?idProdotto=" + idProdotto + "&idCliente=" + idCliente,
          {
            method: "PUT",
          }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
    },
  },
});
