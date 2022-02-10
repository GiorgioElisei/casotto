export default Vue.component("crea-ombrellone", {
  props: {
    utente: {
      type: Object,
    },
  },
  /*html*/
  template: `<ion-content>
    <tool-bar titolo="crea ombrelloni"></tool-bar>
    <form @submit.prevent="crea">
        <ion-item>
            <ion-label>posizioni</ion-label>
            <ion-select @ionChange="selectedPosizioneId = $event.target.value" placeholder="posizione">
            <ion-select-option v-for="posizione in posizioni" :value="posizione.id">{{posizione.nome}} - {{posizione.prezzo}}€</ion-select-option>
            </ion-select>
        </ion-item>
        <ion-item>
            <ion-label>stagioni</ion-label>
            <ion-select multiple="true" @ionChange="selectedStagioni = $event.target.value" placeholder="stagione">
            <ion-select-option v-for="stagione in stagioni" :value="stagione.id">{{stagione.nome}} - {{stagione.prezzo}}€</ion-select-option>
            </ion-select>
        </ion-item>
        <ion-item>
            <ion-label>ombrelloni totali</ion-label>
            <ion-input @ionChange="ombrelloniTotali = $event.target.value" placeholder="ombrelloni totali" type="number" :value="ombrelloniTotali"></ion-input>
        </ion-item>
        <ion-button expand="full" type="submit">crea</ion-button>
    </form>
    </ion-content>
    `,
  data() {
    return {
      selectedPosizioneId: -1,
      selectedStagioni: [],
      posizioni: [],
      stagioni: [],
      ombrelloniTotali: 1,
    };
  },
  async mounted() {
    this.posizioni = await (
      await fetch("api/" + this.utente.tipo + "/posizioni")
    ).json();
    this.stagioni = await (
      await fetch("api/" + this.utente.tipo + "/stagioni")
    ).json();
  },
  methods: {
    async crea() {
      if (
        this.selectedPosizioneId < 0 ||
        this.selectedStagioni.length == 0 ||
        this.ombrelloniTotali <= 0
      ) {
        this.$emit("notifica", "campo mancante");
      } else {
        this.$emit("caricamento", true);
        const id = await (
          await fetch(
            "api/admin/creaOmbrellone?posizioneId=" +
              this.selectedPosizioneId +
              "&ombrelloniTotali=" +
              this.ombrelloniTotali,
            { method: "POST" }
          )
        ).text();
        for (const stagioneId of this.selectedStagioni) {
          await fetch(
            "api/admin/addStagione?ombrelloneId=" +
              id +
              "&stagioneId=" +
              stagioneId,
            { method: "PUT" }
          );
        }
        if (id >= 0) {
          this.$emit("notifica", "successo");
          this.$router.go(-1);
        } else this.$emit("notifica", "errore durante la creazione");
        this.$emit("caricamento", false);
      }
    },
  },
});
