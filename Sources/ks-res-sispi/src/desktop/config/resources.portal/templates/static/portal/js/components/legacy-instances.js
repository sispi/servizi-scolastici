import { FormatDate } from "/static/utilities/date.utilities.js?no-cache";
export function LegacyInstances() {
    var legacyInstances = {
        template: `
        <div class="container step-container">
            <p>Di seguito trovi le pratiche archiviate fino al {{ formatDate(instances[0].startTs) }}</p>
            <div class="table-responsive">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Servizio</th>
                            <th scope="col">Pratica</th>
                            <th scope="col">Data di creazione</th>
                            <th scope="col">Stato</th>
                            <th scope="col"></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="instance in instances" :key="instance.id">
                            <td>{{ instance.service }}</td>
                            <td>{{ instance.proceedings }}</td>
                            <td>{{ formatDate(instance.startTs) }}</td>
                            <td>Completato</td>
                            <td>
                                <a v-bind:href="'/portal/features/legacyInstance?id=' + instance.id" class="btn btn-primary btn-xs"> <i class="fa fa-eye" aria-hidden="true"></i> Vedi </a>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        `,
        props: {
            instances: Array
        },
        data() {
            return {};
        },
        methods: {
            formatDate: function (date) {
                return FormatDate(date);
            }
        }
    };
    return legacyInstances;
}
