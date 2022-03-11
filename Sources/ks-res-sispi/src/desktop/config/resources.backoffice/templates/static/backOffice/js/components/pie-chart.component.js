export function PieChart() {
    var pieChart = {
        template: `
<div>
    <div id="piechart"></div>
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

            // Display the chart inside the <div> element with id="piechart"
            var chart = new google.visualization.PieChart(document.getElementById('piechart'));
            chart.draw(data, options);
            }
        }
    };
    return pieChart;
}
