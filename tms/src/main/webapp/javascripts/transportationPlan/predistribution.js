/**
 * Created by 16020025 on 2016/05/16.
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
            $(grid_selector).jqGrid("setGridParam", {
                contentType: "application/json;charset=utf-8",
                datatype: "json",
                url: "../../predistribution/query",
                mtype: 'POST',// 请求方式
                jsonReader: {
                    root: "data.waybillList", // 实际模型的人口
                    total: "data.totalpages",
                    page: "data.currpage",
                    records: "data.totalrecords",
                    repeatitems: false
                },
                postData: {
                    "rArea": $("#rArea").val(),
                    "deliveryStatus": $("#deliveryStatus").val()
                }, //发送数据
            }).trigger("reloadGrid");
        },
        //预分配
        predistributionDeliveryBill: function () {
        	publicUpdate.cancelOrConfirmData(grid_selector, "确定预分配这些运单？", "../../predistribution/predistributionDeliveryBill", null, "预分配成功");
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
            } else if (cellvalue.indexOf('15') >= 0) {
                temp = "15-已分配";
            } else if (cellvalue.indexOf('20') >= 0) {
                temp = "20-大车运输";
            } else if (cellvalue.indexOf('30') >= 0) {
                temp = "30-小车配送";
            } else if (cellvalue.indexOf('40') >= 0) {
                temp = "40-签收";
            } else if (cellvalue.indexOf('90') >= 0) {
                temp = "90-作废";
            }else{
            	temp=cellvalue;
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
        }
    }

    $(document).ready(function () {

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
            url: "../../predistribution/query",
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
            rowList: [10, 20, 30,50,100,200],
            colModel: [
                {
                    name: 'id',
                    index: 'id',
                    label: "id",
                    width: "70px",
                    sortable: false,
                    hidden: true,
                    editable: false,
                    search: false,
                    align: 'center'
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
                    align: 'center'
                },
                {
                    name: 'orderNo',
                    index: 'orderNo',
                    label: "客户订单号",
                    width: "150px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                },
                {
                	name: 'deliveryNo',
                	index: 'deliveryNo',
                	label: "配送单号",
                	width: "150px",
                	sortable: true,
                	editable: false,
                	hidden: false,
                	search: false,
                	align: 'center'
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
                    align: 'center',
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
            editurl: "../../predistribution/predistributionDeliveryBill"
        });

        $(grid_selector).jqGrid('navGrid', pager_selector,
            {edit: false, add: false, del: false, search: false},
            {}, // edit options
            {}, // add options
            {
                afterSubmit: function (response, postdata) {
                    return publicUpdate.afterSubmitDel(response, postdata);
                }
            });

        $(document).on("click", function (event) {
            var target = event.target;
            switch (target.id) {
                case "predistribution":
                    Action.predistributionDeliveryBill();
                    return;
                case "query":
                    Action.queryWaybill();
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

