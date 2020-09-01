$(document).ready(function () {
    let table = $('#table-date').DataTable({
        "paging": false,
        "order": [[0, 'asc']],
        "columns": [
            null,
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
            {"orderSequence": ["desc", "asc"]},
        ],
    });

    $('.dataTables_filter input').keyup(() => {
        let regexSearch = '\\b' + $(".dataTables_filter input").val();
        table.columns(0).search(regexSearch, true, false).draw();
    });

    $('#date-button').click(function () {
        location.href = '/date/' + $('#date-input').val();
    });
});

