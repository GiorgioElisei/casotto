export default Vue.component("ombrelloni", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `
  <ion-content>
      <ion-button v-if="utente.tipo=='admin'" @click="$router.push('/crea-ombrelloni')" expand="full">
        crea ombrelloni
      </ion-button>
      <ion-card v-if="ombrelloni.length == 0">
        <ion-title>non ci sono ancora ombrelloni disponibili</ion-title>
      </ion-card>
      <ion-card v-else v-for="gruppo in ombrelloni">
      <ion-card-header>
        <ion-card-title>[ID:{{gruppo.id}}]</ion-card-title>
        <ion-card-subtitle>{{gruppo.ombrelloniTotali}} ombrelloni totali in posizione {{gruppo.posizione.nome}}</ion-card-subtitle>
      </ion-card-header>
      <ion-card-content>
        <ion-card-subtitle>stagioni</ion-card-subtitle>
        <ion-list>
          <ion-item v-for="stagione in gruppo.stagioni" 
          :button="(utente.tipo=='admin' || utente.tipo=='addetto') && gruppo.prenotazioni.filter((g) => g.stagione.id == stagione.id).length > 0" 
          @click="clickStagione(gruppo, stagione)">
            <ion-label>
                {{stagione.nome}} - {{stagione.prezzo + gruppo.posizione.prezzo}}€
            </ion-label>
            <ion-label>
                {{prenotati(gruppo, stagione)}} / {{gruppo.ombrelloniTotali}}
            </ion-label>
            <div v-if="utente.tipo=='cliente'">
              <div  v-if="prenotazione(gruppo.id, stagione.id)">
                <ion-button @click="watch(prenotazione(gruppo.id, stagione.id).id)">
                    <ion-label v-if="prenotazioneCodice==prenotazione(gruppo.id, stagione.id).id">
                      {{prenotazione(gruppo.id, stagione.id).id}}
                    </ion-label>
                    <ion-icon  v-else
                    name="eye-outline"></ion-icon>
                </ion-button>
                <ion-button color="danger" @click="sprenota(gruppo.id, stagione.id)">
                  rimuovi prenotazione
                </ion-button>
              </div>
              <ion-button v-else-if="(gruppo.ombrelloniTotali - prenotati(gruppo, stagione)) > 0"
              @click="prenota(gruppo.id, stagione.id)">
                prenota
              </ion-button>
              <ion-button v-else disabled>
                ombrelloni non disponibili
              </ion-button>
            </div>
          </ion-list>
        </ion-item>
      </ion-card-content>
      <ion-button v-if="utente.tipo=='admin'" color="danger" expand="full" @click="deleteOmbrellone(gruppo.id)">
          <ion-icon name="trash"></ion-icon>
      </ion-button>
    </ion-card>
    <ion-modal v-if="utente.tipo=='admin' || utente.tipo=='addetto'" :is-open="prenotazioni.length>0">
      <ion-content><ion-list>
      <ion-button color="danger" expand="full" @click="close()">close</ion-button>
          <ion-item v-for="prenotazione in prenotazioni">
            <ion-label>{{prenotazione.id}}</ion-label>
          </ion-item>
      </ion-content>
    </ion-modal>
  </ion-content>
        `,
  data() {
    return {
      prenotazioni: [],
      ombrelloni: [],
      prenotazioneCodice: false,
    };
  },
  async mounted() {
    await this.aggiorna();
  },
  methods: {
    watch(id) {
      this.prenotazioneCodice = this.prenotazioneCodice == id ? false : id;
    },
    prenotazione(ombrelloniId, stagioneId) {
      return this.utente.prenotazioni.find(
        (o) =>
          o.gruppoOmbrelloni.id == ombrelloniId && o.stagione.id == stagioneId
      );
    },
    prenotati(gruppo, stagione) {
      return gruppo.prenotazioni.filter((x) => x.stagione.id == stagione.id)
        .length;
    },
    async aggiorna() {
      this.$emit("caricamento", true);
      this.ombrelloni = await (
        await fetch("/api/" + this.utente.tipo + "/ombrelloni")
      ).json();
      this.ombrelloni.forEach((o) =>
        o.stagioni.sort((x, y) => x.sorting - y.sorting)
      );
      this.$emit("caricamento", false);
    },
    async deleteOmbrellone(id) {
      this.$emit("caricamento", true);
      if(confirm("Sei sicuro di voler eliminare il gruppo di ombrelloni "+id)) {
        const res = await (
          await fetch(
            "/api/" + this.utente.tipo + "/eliminaOmbrellone?idOmbrellone=" + id,
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
    async prenota(idOmbrellone, idStagione) {
      this.$emit("caricamento", true);
      const res = await (
        await fetch(
          "api/" +
            this.utente.tipo +
            "/prenotaOmbrellone?idOmbrellone=" +
            idOmbrellone +
            "&idStagione=" +
            idStagione,
          { method: "PUT" }
        )
      ).text();
      this.$emit("notifica", res);
      this.$emit("caricamento", false);
      await this.aggiorna();
      await this.$emit("aggiorna");
    },
    async sprenota(ombrelloniId, stagioneId) {
      this.$emit("caricamento", true);
      if(confirm("Sei sicuro di voler eliminare la prenotazione?")) {
        const res = await (
          await fetch(
            "api/" +
              this.utente.tipo +
              "/eliminaPrenotazione?idPrenotazione=" +
              this.prenotazione(ombrelloniId, stagioneId).id,
            { method: "DELETE" }
          )
        ).text();
        this.$emit("notifica", res);
      }
      this.$emit("caricamento", false);
      await this.aggiorna();
      await this.$emit("aggiorna");
    },
    clickStagione(gruppo, stagione) {
      this.close();
      setTimeout(
        () =>
          (this.prenotazioni = gruppo.prenotazioni.filter(
            (g) => g.stagione.id == stagione.id
          )),
        100
      );
    },
    close() {
      this.prenotazioni = [];
    },
  },
});
