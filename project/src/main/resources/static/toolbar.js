export default Vue.component('tool-bar', {
    props: {
        titolo: {
            type: String
        }
    },
    template:
        /*html*/`
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-button @click="indietro()">
                    <ion-icon name="chevron-back-outline"></ion-icon>
                    <ion-label>{{titolo}}</ion-label>
                </ion-button>
            </ion-buttons>
        </ion-toolbar>
        `,
    methods: {
        indietro() {
            this.$router.go(-1);
        }
    }
});