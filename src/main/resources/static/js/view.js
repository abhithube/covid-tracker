let charts, table, chartView, tableView;

$(document).ready(function () {
    charts = $("#charts");
    table = $("#table");
    chartView = $('#chart-view');
    tableView = $('#table-view');

    chartView.click(function () {
        showCharts();
        localStorage.setItem("view", "chart");
    })

    tableView.click(function () {
        showTable();
        localStorage.setItem("view", "table");
    })
})

function showCharts() {
    charts.css({display: "block"});
    chartView.prop({disabled: true});

    table.css({display: 'none'});
    tableView.prop({disabled: false});
}

function showTable() {
    table.css({display: 'block'});
    tableView.prop({disabled: true});

    charts.css({display: "none"});
    chartView.prop({disabled: false});
}