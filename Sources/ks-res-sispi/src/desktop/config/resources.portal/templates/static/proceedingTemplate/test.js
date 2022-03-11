export function CustomTemplate() {
    var test = {
        template: `
            <div>
                <p>Questo è il mio template per il procedimento test</p>
                <i>{{message}}</i>
            </div>
            `,
        data() {
            return {
                message: "E questo è il mio messaggio",
            };
        },
    };
    return test;
}
