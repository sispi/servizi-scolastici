export function ColumnChart() {
    var columnChart = {
        template: `
<div>
    <div id="columnChart"></div>
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
            var chart = new google.visualization.ColumnChart(document.getElementById('columnChart'));
            chart.draw(data, options);
            }
        }
    };
    return columnChart;
}
