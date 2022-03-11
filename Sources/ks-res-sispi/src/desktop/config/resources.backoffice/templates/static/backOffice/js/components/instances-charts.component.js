import { PieChart } from '/static/backOffice/js/components/pie-chart.component.js?no-cache';
import { LineChart } from '/static/backOffice/js/components/line-chart.component.js?no-cache';
import { CountInstanceByStatus, CountInstanceGroupByMonth } from '/static/portal/js/services/instance.service.js?no-cache';
import { DateAdapterForInput, getMonthByNumber } from '/static/utilities/date.utilities.js?no-cache';
export function InstancesCharts() {
    var instancesCharts = {
        template: `
<div>
    <div class="mt-5"></div>
    <div class="row">
        <div class="col-md-8">
            <div class="spinner-border text-secondary" role="status" v-if="lineChart.isLoading"></div>
            <div v-else class="border">
                <line-chart :opt="lineChart.options" :arr="lineChart.arrayToDataTable" :type="lineChart.type" v-if="!lineChart.serverError"></line-chart>
                <div v-else class="alert alert-danger" role="alert">
                    Abbiamo riscontrato un problema durante il caricamento dei dati, riprovare più tardi.
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="spinner-border text-secondary" role="status" v-if="pieChart.isLoading"></div>
            <div v-else class="border">
                <pie-chart :opt="pieChart.options" :arr="pieChart.arrayToDataTable" :type="pieChart.type" v-if="!pieChart.serverError"></pie-chart>
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
                pieChart: {
                    serverError: false,
                    isLoading: true
                },
                lineChart: {
                    serverError: false,
                    isLoading: true
                },
            };
        },
        components: {
            'pie-chart': PieChart(),
            'line-chart': LineChart(),
        },
        methods: {
            loadPieChart: function(){
                var dataTable = [];
                dataTable.push(['Istanza', 'Numero di istanze']);
                const pieChartResponse = CountInstanceByStatus();
                if(pieChartResponse.status === 'success'){
                    dataTable.push(['Attive', pieChartResponse.data.Status1]);
                    dataTable.push(['Completate', pieChartResponse.data.Status2]);
                    dataTable.push(['Abortite', pieChartResponse.data.Status3]);
                    this.pieChart = {
                        options: {title: 'Istanze per stato', height: 400},
                        arrayToDataTable: dataTable,
                        type: 'pie',
                        serverError: false,
                        isLoading: false
                    };
                } else {
                    this.pieChart = {
                        serverError: true,
                        isLoading: false
                    };
                }
            },
            loadLineChart: function(){
                var start = new Date();
                var end = new Date();
                start.setMonth(0);
                start.setDate(1);
                end.setMonth(11);
                end.setDate(31);
                const thisYear = start.getFullYear();

                const lineChartResponse = CountInstanceGroupByMonth(DateAdapterForInput(start), DateAdapterForInput(end));
                var datas = [];
                if(lineChartResponse.status === 'success'){
                    datas = this.manageDatas(lineChartResponse.data);
                    const dataTable = [];
                    dataTable.push(['Mese', 'Create']);
                    datas.forEach(item => {
                        dataTable.push([this.getMonthName(item[0]), item[1]]);
                    });
                    this.lineChart = {
                        options: {
                            title: 'Istanze create nell\'anno ' + thisYear,
                            // curveType: 'function',
                            legend: {
                                position: 'bottom'
                            },
                            height: 400
                        },
                        arrayToDataTable: dataTable,
                        type: 'line',
                        serverError: false,
                        isLoading: false
                    };
                } else {
                    this.lineChart = {
                        serverError: true,
                        isLoading: false
                    };
                }
            },
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
            this.loadPieChart();
            this.loadLineChart();
        }
    };
    return instancesCharts;
}
