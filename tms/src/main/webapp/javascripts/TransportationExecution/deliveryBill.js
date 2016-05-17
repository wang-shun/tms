define(function (require, exports, module) {
    var $ = require("jquery");
    var publicUpdate = require("publicUpdate");
    var distributionbillModal = require("TransportationExecution/distributionbill");
    var publicjs = require("publicJs");
    require("bootstrap");
    require("datepicker");
    require("jqgridLocalCn");
    require("jqgrid");
    require("plugins/bootstrapSuggest/bootstrap-suggest.min.sea.js");
    require("sweetalert");

    var grid_selector = "#table_list_1";
    var pager_selector = "#pager_list_1";

    var Action = {
        query: function () {
            var statusArray = new Array();
            $("input[name='statusGroup']:checked").each(function () {
                statusArray.push($(this).attr('value'));
            });
            $(grid_selector).jqGrid("setGridParam", {
                contentType: "application/json;charset=utf-8",
                datatype: "json",
                url: "../../deliveryBill/query",
                mtype: 'POST',// 请求方式
                jsonReader: {
                    root: "data.deliveryBillList", // 实际模型的人口
                    total: "data.totalpages",
                    page: "data.currpage",
                    records: "data.totalrecords",
                    repeatitems: false
                },
                postData: {
                    "vNo": $("#vNo").val(),
                    "dName": $("#dName").val(),
                    "status": statusArray.toString()
                },
            }).trigger("reloadGrid");
        },

        showModalAndPrint : function (id) {
            $("#modalId").val(id);
            $('#myModal').modal({remote: "../TransportationExecution/distributionbill.html"});
        }
    }

    var DataFormat = {
        statusFormatter: function (cellvalue, options, rowdata) {
            var temp = "";
            if (cellvalue == null) {
                temp = "";
            } else if (cellvalue.indexOf('00') >= 0) {
                temp = "创建";
            } else if (cellvalue.indexOf('10') >= 0) {
                temp = "确认";
            } else if (cellvalue.indexOf('20') >= 0) {
                temp = "在途";
            } else if (cellvalue.indexOf('30') >= 0) {
                temp = "到达";
            }
            return temp;
        },

        printFormat: function (cell_value, options, row_data) {
            var col_div = document.createElement("div");
            col_div.className = "clear_button btn-info";
            col_div.innerText = "打印";
            col_div.dataset.id = row_data.id;
            col_div.dataset.colName = "print-col";
            return col_div.outerHTML;
        }
    }

    $(function ($) {
        //初始化下拉框
        var driverOptions = {value: []};
        var vehicleOptions = {value: []};
        $.ajax({
            async: false,
            url: "../../driver/driverOptions",
            type: 'POST',// 请求方式
            dataType: 'json',
            data: "status : ''",
            success: function (data) {
                var driverList = data.data.driverList;
                driverList.forEach(function (driver) {
                    if (driver.status == '1') {
                        driverOptions.value.push({
                            "司机姓名": driver.name
                        });
                    }
                });
            }
        });

        $.ajax({
            async: false,
            url: "../../vehicle/vehicleOptions",
            type: 'POST',// 请求方式
            dataType: 'json',
            data: {status: ''},
            success: function (data) {
                var vehicleList = data.data.vehicleList;
                vehicleList.forEach(function (vehicle) {
                    if (vehicle.status == '1') {
                        vehicleOptions.value.push({
                            "车牌号": vehicle.vNo
                        });
                    }
                });
            }
        });

        $("#dName").bsSuggest({
            indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
            indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
            data: driverOptions
        });

        $("#vNo").bsSuggest({
            indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
            indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
            data: vehicleOptions
        });

        $("#myModal").on("hidden.bs.modal", function () {
            $(this).removeData("bs.modal");
        });
        $('#myModal').on('shown.bs.modal', function () {
            distributionbillModal.loadModalData();
            document.getElementById("billqr").onload = function () {
                printModal();
                $("#myModal").modal("hide");
            }
        });
        var printModal = function () {
            var newWindow = window.open("打印窗口", "_blank");//打印窗口要换成页面的url
            var docStr = document.getElementById("myModal").innerHTML;
            newWindow.document.write(docStr);
            newWindow.document.getElementById("billqr").onload = function () {
                newWindow.document.close();
                newWindow.print();
                newWindow.close();
            }
        }

        $(window).on('resize.jqGrid', function () {
            publicjs.throttle(resizeJqGrid, $(".tms_body").width());
        });
        function resizeJqGrid(width) {
            $(grid_selector).jqGrid('setGridWidth', width);
        }

        var lastsel;
        $.jgrid.defaults.styleUI = 'Bootstrap';
        $(grid_selector).jqGrid({
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            url: "../../deliveryBill/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.deliveryBillList", // 实际模型的人口
                total: "data.totalpages",
                page: "data.currpage",
                records: "data.totalrecords",
                repeatitems: false
            },
            prmNames: {
                page: "page", // 表示请求页码的参数名称
                rows: "rows", // 表示请求行数的参数名称
                sort: "sidx", // 表示用于排序的列名的参数名称
                order: "sord", // 表示采用的排序方式的参数名称
                search: "_search", // 表示是否是搜索请求的参数名称
                nd: "nd", // 表示已经发送请求的次数的参数名称
                id: "id", // 表示当在编辑数据模块中发送数据时，使用的id的名称
                oper: "oper", // operation参数名称
                editoper: "update", // 当在edit模式中提交数据时，操作的名称
                addoper: "add", // 当在add模式中提交数据时，操作的名称
                deloper: "delete", // 当在delete模式中提交数据时，操作的名称
                subgridid: "id", // 当点击以载入数据到子表时，传递的数据名称
                npage: null,
                totalrows: "totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
            },
            height: '350px',
            autowidth: true,
            shrinkToFit: true,
            autoScroll: true,
            multiselect: true,
            multiboxonly: false,
            rowNum: 10,
            rowList: [10, 20, 30],
            colModel: [
                {
                    name: 'id',
                    index: 'id',
                    label: "配送单打印",
                    width: "70px",
                    sortable: false,
                    hidden: false,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: DataFormat.printFormat
                },
                {
                    name: 'status',
                    index: 'status',
                    label: "状态",
                    width: "80px",
                    sortable: false,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center', edittype: 'select', editoptions: {
                    value: '00-创建;10-确认;20-上车;30-下车'
                },
                    formatter: DataFormat.statusFormatter
                },
                {
                    name: 'no',
                    index: 'no',
                    label: "配送单号",
                    width: "150px",
                    sortable: false,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'vNo',
                    index: 'vNo',
                    label: "车牌号",
                    width: "150px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                },
                {
                    name: 'dName',
                    index: 'dName',
                    label: "司机名称",
                    width: "130px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'deliveryOrigin',
                    index: 'deliveryOrigin',
                    label: "配送起点",
                    width: "200px",
                    sortable: true,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'deliveryTerm',
                    index: 'deliveryTerm',
                    label: "配送集散点",
                    width: "200px",
                    sortable: true,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
            ],
            beforeSelectRow: function (rowid, e) {
                var $myGrid = $(this),
                    i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                    cm = $myGrid.jqGrid('getGridParam', 'colModel');
                return (cm[i].name === 'cb');
            },
            onSelectRow: function (id) {
                if ($('#' + id).attr('aria-selected') == "true") {
                    $(grid_selector).jqGrid('editRow', id, true, pickdates);
                } else {
                    $(grid_selector).jqGrid('restoreRow', lastsel);
                    $(grid_selector).jqGrid('saveRow', id, true);
                }
                lastsel = id;
            },
            pager: pager_selector,
            viewrecords: true,
            hidegrid: false,
            caption: "配送单列表",
            editurl: "../../deliveryBill/deliveryBillOper"
        });

        $(grid_selector).jqGrid('navGrid', pager_selector, {
            edit: false,
            add: false,
            del: false,
            search: false
        });

        $(document).click(function(event){
            var target = event.target;
            switch (target.name) {
                case "statusGroup" :
                    Action.query();
                    return;
            }
            switch (target.id) {
                case "query":
                    Action.query();
                    return;
                case "clearStatus":
                    $("input[name='statusGroup']:checked").each(function () {
                        $(this).prop("checked", false);
                    });
                    Action.query();
                    return;
            }
            switch (target.dataset.colName) {
                case "print-col":
                    Action.showModalAndPrint(target.dataset.id);
                    return;
            }
        });
    });
});