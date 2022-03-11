export function CustomTemplate() {
    var nullaOstaPortoDArmi = {
        template: `
            <div>
                <p>Questo è il mio template per il procedimento porto d'armi</p>
                <i>{{message}}</i>
                <table class="table">
                    <thead>
                        <tr>
                        <th scope="col">Nome dell'istanza</th>
                        <th scope="col">Versione</th>
                        <th scope="col">Processo</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>{{bpmInstance.name}}</th>
                            <td>{{bpmInstance.version}}</td>
                            <td>{{bpmInstance.processId}}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            `,
        data() {
            return {
                message: "E questa tabella viene dal template custom",
                bpmInstance: data("data-bpm-instance")
            };
        },
    };
    return nullaOstaPortoDArmi;
}
