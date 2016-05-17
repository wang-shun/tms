/**
 * Created by 16030117 on 2016/3/28.
 */
define(function (require, exports, module) {
    var $ = require("jquery");
    require("sweetalert");
    require("bootstrap");
    require("jqgridLocalCn");
    require("jqgrid");
    require("bootstrapSuggest");
    var publicUpdate = require("publicUpdate");
    var waybillModal = require("tmsCustomer/waybillModal.js");
    var publicjs = require("publicJs");
    require("datepicker");

    var grid_selector = "#table_list_1";
    var pager_selector = "#pager_list_1";

    var Action = {
        //查询
        queryWaybill: function () {
            var statusArray = new Array();
            $("input[name='statusGroup']:checked").each(function () {
                statusArray.push($(this).attr('value'));
            });
            $(grid_selector).jqGrid("setGridParam", {
                contentType: "application/json;charset=utf-8",
                datatype: "json",
                url: "../../waybill/query",
                mtype: 'POST',// 请求方式
                jsonReader: {
                    root: "data.waybillList", // 实际模型的人口
                    total: "data.totalpages",
                    page: "data.currpage",
                    records: "data.totalrecords",
                    repeatitems: false
                },
                postData: {
                    "no": $("#no").val(),
                    "orderNo": $("#orderNo").val(),
                    "dName": $("#dName").val(),
                    "rName": $("#rName").val(),
                    "status": statusArray.toString(),
                    "deliveryStartDate": $("#deliveryStartDate").val(),
                    "deliveryEndDate": $("#deliveryEndDate").val()
                }, //发送数据
            }).trigger("reloadGrid");
        },

        openWaybillModal: function (id) {
            $("#modalId").val(id);
            $('#myModal').modal({remote: "waybillModal.html"});
        },

        waybillTrack: function (id, status) {
            if ("00" == status) {
                alert("订单还未确认!");
                return;
            }
            window.location.href = "../tmsTransport/waybillTransportDetail.html?id=" + id + "&no=" + no;
        },

        print: function () {
            var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
            if (ids == '') {
                alert('请先选择数据!');
                return;
            }
            sweetConfirm("确认打印？", function () {
                //暂时在点击时直接关闭confirm，之后可移动到success之后
                sweetAlert.close();
                var idstr = "";
                //先将所有的数据保存
                for (var i = 0; i < ids.length; i++) {
                    idstr += ids[i] + ",";
                }
                $.ajax({
                    data: "idstr=" + idstr,
                    async: false,
                    url: "../../waybill/printbill",
                    type: 'POST',// 请求方式
                    dataType: 'json',
                    success: function (data) {
                    }
                });
            });
        }
    }

    var DataFormat = {
        statusFormatter: function (cellvalue, options, rowdata) {
            var temp = "";
            if (cellvalue == null) {
                temp = "";
            } else if (cellvalue.indexOf('00') >= 0) {
                temp = "00-运单创建";
            } else if (cellvalue.indexOf('10') >= 0) {
                temp = "10-运单确认";
            } else if (cellvalue.indexOf('20') >= 0) {
                temp = "20-大车运输";
            } else if (cellvalue.indexOf('30') >= 0) {
                temp = "30-小车配送";
            } else if (cellvalue.indexOf('40') >= 0) {
                temp = "40-签收";
            } else if (cellvalue.indexOf('90') >= 0) {
                temp = "90-作废";
            }
            return temp;
        },

        scanStatusFormatter: function (cellvalue, options, rowdata) {
            var temp = "";
            if (cellvalue == null) {
                temp = "";
            } else if (cellvalue.indexOf('00') >= 0) {
                temp = "00-未扫描";
            } else if (cellvalue.indexOf('10') >= 0) {
                temp = "10-大车扫描未确认";
            } else if (cellvalue.indexOf('20') >= 0) {
                temp = "20-生成配送单";
            } else {
                return cellvalue;
            }
            return temp;
        },

        pickdates: function (id) {
            $("#" + id + "_receiveAtStr").datepicker({
                autoclose: true
            });
        },

        //运单跟踪详情
        gotoTransDetl: function (cellvalue, options, rowdata) {
            var col_div = document.createElement("div");
            col_div.innerText = "跟踪";
            col_div.classList.add("clear_button");
            col_div.dataset.id = rowdata.id;
            col_div.dataset.status = rowdata.status;
            col_div.dataset.colName = "transDetl-col";
            if ("00" == rowdata.status) {
                col_div.classList.add("clear_button_disable");
                col_div.disabled = true;
            } else {
                col_div.classList.add("btn-info");
            }
            return col_div.outerHTML;
        },

        //添加详细按钮
        showModal: function (cellvalue, options, rowdata) {
            var col_a = document.createElement("a");
            col_a.innerText = cellvalue;
            col_a.dataset.id = rowdata.id;
            col_a.dataset.colName = "modal-col";
            return col_a.outerHTML;
        }
    }

    $(document).ready(function () {
        var senderList = {value: []};
        $.ajax({
            async: false,
            url: "../../sender/senderOptions",
            type: 'POST',// 请求方式
            dataType: 'json',
            success: function (data) {
                (data.data.senderList).forEach(function (sender) {
                    senderList.value.push({
                        "发货方": sender["发货方"],
                        "发货人": sender["发货人"],
                        "发货方ID": sender.ID
                    });
                });
            }
        });
        var receiverList = {value: []};
        $.ajax({
            async: false,
            url: "../../receiver/receiverOptions",
            type: 'POST',// 请求方式
            dataType: 'json',
            success: function (data) {
                (data.data.receiverList).forEach(function (receiver) {
                    receiverList.value.push({
                        "收货方": receiver["收货方"],
                        "收货人": receiver["收货人"],
                        "收货方ID": receiver.ID
                    });
                });
            }
        });

        $("#dName").bsSuggest({
            showHeader: true,
            delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
            indexId: 2,  //data.value 的第几个数据，作为input输入框的内容
            indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
            data: senderList
        });

        $("#rName").bsSuggest({
            showHeader: true,
            delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
            indexId: 2,  //data.value 的第几个数据，作为input输入框的内容
            indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
            data: receiverList
        });

        $("#myModal").on("hidden.bs.modal", function () {
            $(this).removeData("bs.modal");
        });
        $('#myModal').on('shown.bs.modal', function () {
            waybillModal.loadModalData();
        });

        $.jgrid.defaults.styleUI = 'Bootstrap';

        $(window).on('resize.jqGrid', function () {
            publicjs.throttle(resizeJqGrid, $(".tms_body").width());
        });

        function resizeJqGrid(width) {
            $(grid_selector).jqGrid('setGridWidth', width);
        }

        $(grid_selector).jqGrid({
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            url: "../../waybill/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.waybillList", // 实际模型的人口
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
                oper: "action", // operation参数名称
                editoper: "waybillConfirm", // 当在edit模式中提交数据时，操作的名称
                addoper: "add", // 当在add模式中提交数据时，操作的名称
                deloper: "waybillCancel", // 当在delete模式中提交数据时，操作的名称
                subgridid: "id", // 当点击以载入数据到子表时，传递的数据名称
                npage: null,
                totalrows: "totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
            },
            height: '350px',
            autowidth: true,
            shrinkToFit: false,
            autoScroll: true,
            multiselect: true,
            multiboxonly: false,
            rowNum: 10,
            rowList: [10, 20, 30],
            colModel: [
                {
                    name: 'id',
                    index: 'id',
                    label: "运单跟踪",
                    width: "70px",
                    sortable: false,
                    hidden: false,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: DataFormat.gotoTransDetl
                },
                {
                    name: 'no',
                    index: 'no',
                    label: "运单编号",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center',
                    formatter: DataFormat.showModal
                },
                {
                    name: 'orderNo',
                    index: 'orderNo',
                    label: "客户订单号",
                    width: "80px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                },
                {
                    name: 'status',
                    index: 'status',
                    label: "状态",
                    width: "125px",
                    sortable: false,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center', edittype: 'select', editoptions: {
                    value: '00:00-运单创建;10:10-运单确认;20:20-在途（卡车配送）;30:30-在途（电动车配送）;40:40-客户签收;90:90-作废'
                },
                    formatter: DataFormat.statusFormatter
                },
                {
                    name: 'scanStatus',
                    index: 'scanStatus',
                    label: "扫描状态",
                    width: "125px",
                    sortable: false,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center', edittype: 'select', editoptions: {
                    value: '00:00-运单创建;10:10-大车扫描;20:20-扫描确认;'
                },
                    formatter: DataFormat.scanStatusFormatter
                },
                {
                    name: 'dName',
                    index: 'dName',
                    label: "发货方",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'rName',
                    index: 'rName',
                    label: "收货方",
                    width: "120px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'rArea',
                    index: 'rArea',
                    label: "收货城区",
                    width: "80px",
                    sortable: true,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'rAddressDetl',
                    index: 'rAddressDetl',
                    label: "收货地址",
                    width: "300px",
                    sortable: true,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'deliveryDuration',
                    label: '配送时间段',
                    index: 'deliveryDuration',
                    width: "150px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'transportFee',
                    index: 'transportFee',
                    label: "运费(元)",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'receiveAtStr',
                    index: 'receiveAtStr',
                    label: "接单时间",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'recipient',
                    index: 'recipient',
                    label: "揽件人",
                    width: "80px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'remark',
                    index: 'remark',
                    label: "备注",
                    width: "350px",
                    sortable: true,
                    editable: false,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'createdBy',
                    index: 'createdBy',
                    label: "创建人",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'createdAt',
                    index: 'createAt',
                    label: "创建时间",
                    width: "150px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: "date",
                    formatoptions: {srcformat: 'u', newformat: 'Y-m-d H:i:s'}
                },
                {
                    name: 'updatedBy',
                    index: 'updateBy',
                    label: "修改人",
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'updatedAt',
                    index: 'updateAt',
                    label: "修改时间",
                    width: "150px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: "date",
                    formatoptions: {srcformat: 'u', newformat: 'Y-m-d H:i:s'}
                },
            ],

            beforeSelectRow: function (rowid, e) {
                var $myGrid = $(this),
                    i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                    cm = $myGrid.jqGrid('getGridParam', 'colModel');
                return (cm[i].name === 'cb');
            },
            /* loadComplete: function() {
             },*/
            onSelectAll: function (aRowids, status) {
                publicUpdate.onSelectAllWithDate(aRowids, status, DataFormat.pickdates, grid_selector);
            },
            onSelectRow: function (id) {
                if ($('#' + id).attr('aria-selected') == "true") {
                    $(grid_selector).jqGrid('editRow', id, true, DataFormat.pickdates);
                } else {
                    $(grid_selector).jqGrid('restoreRow', id);
                }
            },
            pager: pager_selector,
            viewrecords: true,
            hidegrid: false,
            //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
            editurl: "../../waybill/waybillConfirmOrCancel"
        });

        $(grid_selector).jqGrid('navGrid', pager_selector,
            {edit: false, add: false, del: false, search: false},
            {}, // edit options
            {}, // add options
            {
                afterSubmit: function (response, postdata) {
                    return publicUpdate.afterSubmitDel(response, postdata);
                }
            }).navButtonAdd(pager_selector, {
                caption: " ",
                buttonicon: "glyphicon-ok",
                title: '确认运单',
                onClickButton: function () {
                    publicUpdate.cancelOrConfirmData(grid_selector, "确认运单？", "../../waybill/waybillConfirmOrCancel", "waybillConfirm", "运单确认成功");
                },
                position: "first"
            })
            .navButtonAdd(pager_selector, {
                caption: " ",
                buttonicon: "glyphicon-trash",
                title: '作废运单',
                onClickButton: function () {
                    publicUpdate.cancelOrConfirmData(grid_selector, "确认作废？", "../../waybill/waybillConfirmOrCancel", "waybillCancel", "运单作废成功");
                }
            });

        $(document).on("click", function (event) {
            var target = event.target;
            switch (target.name) {
                case "statusGroup" :
                    Action.queryWaybill();
                    return;
            }
            switch (target.id) {
                case "print":
                    Action.print();
                    return;
                case "query":
                    Action.queryWaybill();
                    return;
                case "clearStatus":
                    $("input[name='statusGroup']:checked").each(function () {
                        $(this).prop("checked", false);
                    });
                    Action.queryWaybill();
                    return;
                case "waybillConfirm":
                    publicUpdate.cancelOrConfirmData(
                        grid_selector, "确认运单？", "../../waybill/waybillConfirmOrCancel",
                        "waybillConfirm", "运单确认成功");
                    return;
                case "waybillCancel":
                    publicUpdate.cancelOrConfirmData(
                        grid_selector, "确认作废？", "../../waybill/waybillConfirmOrCancel",
                        "waybillCancel", "运单作废成功");
                    return;
            }
            ;
            switch (target.dataset.colName) {
                case "modal-col":
                    Action.openWaybillModal(target.dataset.id);
                    return;
                case "transDetl-col":
                    Action.waybillTrack(target.dataset.id, target.dataset.status);
                    return;
            }
        });

        $("#deliveryStartDate").datepicker({
            autoclose: true
        });
        $("#deliveryEndDate").datepicker({
            autoclose: true
        });
    });
});

