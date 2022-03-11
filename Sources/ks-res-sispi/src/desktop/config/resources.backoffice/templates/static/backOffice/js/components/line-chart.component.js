export function LineChart() {
    var lineChart = {
        template: `
<div>
    <div id="linechart"></div>
</div>
        `,
        props: {
            opt: Object,
            arr: Object,
            type: Object
        },
        data() {
            return {};
        },
        created() {
            const options = this.opt;
            const array = this.arr;
            
            // Load google charts
            google.charts.load('current', {'packages':['corechart']});
            google.charts.setOnLoadCallback(drawChart);

            // Draw the chart and set the chart values
            function drawChart() {
            var data = google.visualization.arrayToDataTable(array);

            // Display the chart inside the <div> element with id="linechart"
            var chart = new google.visualization.LineChart(document.getElementById('linechart'));
            chart.draw(data, options);
            }
        }
    };
    return lineChart;
}
