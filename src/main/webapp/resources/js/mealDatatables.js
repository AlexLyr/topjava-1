var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize(),
        success: updateTableByData
    });
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (data, type, row) {
                    var date = data.substring(0, 10).split('-');
                    var time = data.substring(11, data.length - 3);
                    return date[2] + '-' + date[1] + '-' + date[0] + "  " + time;

                }
            },
            {
                "data": "description",
                "render": function (data, type, row) {
                    return data
                }
            },
            {
                "data": "calories",
                "render": function (data, type, row) {
                    return data
                }
            },
            {

                "defaultContent": "",
                "orderable": false,
                "render": renderEditBtn
            },
            {
                "defaultContent": "",
                "orderable": false,
                "render": renderDeleteBtn
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ],

        "createdRow": function (row, data, dataIndex) {
            $(row).addClass(data.exceed?"exceeded":"normal");
        },

        "initComplete": makeEditable
    });
});