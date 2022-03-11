import { ColumnChart } from '/static/backOffice/js/components/column-chart.component.js?no-cache';
import { FindAllPaymentsTotalByMonth } from '/static/portal/js/services/payment.service.js?no-cache';
import { DateAdapterForInput, getMonthByNumber } from '/static/utilities/date.utilities.js?no-cache';
export function PaymentsChart() {
    var paymentsChart = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-12">
            <div class="spinner-border text-secondary" role="status" v-if="isLoading"></div>
            <div v-else class="border">
                <column-chart :opt="columnChart.options" :arr="columnChart.arrayToDataTable" :type="columnChart.type" v-if="!serverError"></column-chart>
                <div v-else class="alert alert-danger" role="alert">
                    Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
                </div>
            </div>
        </div>
    </div>
</div>
        `,
        data() {
            return {
                columnChart: null,
                totalPaymentsByMonth: [],
                serverError: false,
                isLoading: true
            };
        },
        components: {
            'column-chart': ColumnChart()
        },
        methods: {
            getMonthName: function(month){
                return getMonthByNumber(month);
            },
            manageDatas: function(datas){
                const months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
                const newDatas = datas;
                months.forEach(month => {
                    var found = false;
                    datas.forEach(data => {
                        if(data[0] === month){
                            found =  true;
                        }
                    });
                    if(!found){
                        newDatas.push([month, 0]);
                    }
                });
                newDatas.sort((a, b) => (a[0] > b[0]) ? 1 : ((b[0] > a[0]) ? -1 : 0));
                return newDatas;
            }
        },
        created() {
            var start = new Date();
            var end = new Date();
            start.setMonth(0);
            start.setDate(1);
            end.setMonth(11);
            end.setDate(31);
            const thisYear = start.getFullYear();

            const response = FindAllPaymentsTotalByMonth(DateAdapterForInput(start), DateAdapterForInput(end));
            var datas = [];
            if(response.status === 'success'){
                datas = this.manageDatas(response.data);
                this.isLoading = false;
            } else {
                this.serverError = true;
                this.isLoading = false;
            }
            const dataTable = [];
            dataTable.push(['Mese', 'Totale']);
            datas.forEach(item => {
                dataTable.push([this.getMonthName(item[0]), item[1]]);
            });

            this.columnChart = {
                options: {title: 'Pagamenti dell\'anno ' + thisYear, height: 400},
                arrayToDataTable: dataTable,
                type: 'bar'
            };
        }
    };
    return paymentsChart;
}
