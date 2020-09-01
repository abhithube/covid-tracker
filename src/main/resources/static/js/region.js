$(document).ready(function () {
    let table = $('#table-region').DataTable({
        "pageLength": 25,
        "lengthMenu": [[25, 50, 100, -1], [25, 50, 100, "All"]],
        "order": [[0, 'desc']],
        "columns": [
            null,
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
        ],
        "dom": 'lr<"table-filter-container">tip',
    });

    $('#table-filter select').on('change', function () {
        let val = String(this.value);
        if (val.length === 1) val = '0' + val;
        val = '2020-' + val;
        table.columns(0).search(val).draw();
        table.order = [[0, 'desc']];
    });

    $('#region-button').click(function () {
        location.href = '/region/' + $('#region-select select').val().split(' ').join('-').toLowerCase();
    });
});