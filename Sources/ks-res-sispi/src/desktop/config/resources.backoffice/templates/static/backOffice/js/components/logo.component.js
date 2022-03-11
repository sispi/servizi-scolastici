import { FindByKey } from "/static/portal/js/services/media.service.js?no-cache";
export function Logo() {
    var logo = {
        template: `
<div v-if="!serverError">
    <template v-if="!logoNotFound">
        <h4>Logo Ente:</h4>
        <hr />
        <img :src="b64" alt="Logo" style="max-height: 200px;" v-if="logo" />
        <br />
        <br />
        <a v-bind:href="'/backOffice/media/media-create?id=' + logo.id + '&logo=true'" class="btn btn-primary btn-sm"><i class="fas fa-image"></i> Modifica Logo</a>
    </template>
    <template v-else>
        <a>Logo Ente mancante</a><br />
        <br />
        <a href="media/media-create?logo=true" class="btn btn-primary btn-sm"><i class="fas fa-image"></i> Carica Logo</a>
    </template>
    <br />
    <br />
</div>
<div v-else class="alert alert-danger" role="alert">
    Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
</div>

        `,
        data() {
            return {
                logo: null,
                b64: null,
                serverError: false,
                logoNotFound: false
            };
        },
        created() {
            const response = FindByKey("logo");
            if (response.status === "success") {
                this.logo = response.data;
                this.b64 = "data:" + this.logo.fileType + ";base64," + this.logo.file;
            } else if(response.status.responseJSON.status === 404){
                this.logoNotFound = true;
            } else {
                this.serverError = true;
            }
        },
    };
    return logo;
}
