/**
 * Created by 16030117 on 2016/3/28.
 */
define(function (require, exports, module) {
    var $ = require("jquery");
    var publicUpdate = require("publicUpdate");
    var publicjs = require("publicJs");
    require("datepicker");
    require("jqgridLocalCn");
    require("jqgrid");
    require("sweetalert");
    $.jqm.params.closeoverlay = false;//默认为true 指定当点击overlay的外部区域时,是否自动关闭这个overlay,默认是关闭。
    var grid_selector = "#table_list_1";
    var pager_selector = "#pager_list_1";

    $(function ($) {
        $.jgrid.defaults.styleUI = 'Bootstrap';

        $(window).on('resize.jqGrid', function () {
            publicjs.throttle(resizeJqGrid, $(".tms_body").width());
        });

        function resizeJqGrid(width){
            $(grid_selector).jqGrid('setGridWidth', width);
        }

        $(grid_selector).jqGrid({
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            url: "../../route/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.routeList", // 实际模型的人口
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
                editoper: "routeUpdate", // 当在edit模式中提交数据时，操作的名称
                addoper: "routeAdd", // 当在add模式中提交数据时，操作的名称
                deloper: "routeDel", // 当在delete模式中提交数据时，操作的名称
                subgridid: "id", // 当点击以载入数据到子表时，传递的数据名称
                npage: null,
                totalrows: "totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
            },
            height: '350px',
            autowidth: true,
            cellEdit: false,
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
                    width: "100px",
                    label: "id",
                    sortable: false,
                    hidden: true,
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'name',
                    index: 'name',
                    label: "线路名 <text style='color:red'>*</text>",
                    width: "150px",
                    sortable: false,
                    editrules: {required: true},
                    editable: true,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'termination',
                    index: 'termination',
                    label: "集散点 <text style='color:red'>*</text>",
                    width: "200px",
                    sortable: false,
                    editrules: {required: true},
                    editable: true,
                    align: 'center'
                },
                {
                    name: 'city',
                    index: 'city',
                    label: "城市",
                    width: "80px",
                    sortable: false,
                    editable: false,
                    editrules: {required: true},
                    align: 'center'
                },
                {
                    name: 'area',
                    index: 'area',
                    label: "城区 <text style='color:red'>*</text>",
                    width: "80px",
                    sortable: false,
                    editable: true,
                    editrules: {required: true},
                    search: false,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: '东区:东区;西区:西区'
                    }
                },
                {
                    name: 'remark',
                    index: 'remark',
                    label: "备注",
                    width: "350px",
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'createdBy',
                    label: '新增人',
                    index: 'createdBy',
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'createdAt',
                    label: '新增时间',
                    index: 'createdAt',
                    width: "200px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: "date",
                    formatoptions: {srcformat: 'u', newformat: 'Y-m-d H:i:s'}
                },
                {
                    name: 'updatedBy',
                    label: '更新人',
                    index: 'updatedBy',
                    width: "100px",
                    sortable: false,
                    editable: false, search: false, align: 'center'
                },
                {
                    name: 'updatedAt',
                    label: '更新时间',
                    index: 'updatedAt',
                    width: "200px",
                    sortable: true,
                    editable: false,
                    search: false,
                    align: 'center',
                    formatter: "date",
                    formatoptions: {srcformat: 'u', newformat: 'Y-m-d H:i:s'}
                }
            ],
            beforeSelectRow: function (rowid, e) {
                var $myGrid = $(this),
                    i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
                    cm = $myGrid.jqGrid('getGridParam', 'colModel');
                return (cm[i].name === 'cb');
            },
            onSelectAll: function (aRowids, status) {
                publicUpdate.onSelectAll(aRowids, status, grid_selector);
            },
            onSelectRow: function (id) {
                if ($('#' + id).attr('aria-selected') == "true") {
                    $(grid_selector).jqGrid('editRow', id, true);
                } else {
                    $(grid_selector).jqGrid('restoreRow', id);
                }
            },
            pager: pager_selector,
            viewrecords: true,
            hidegrid: false,
            caption: "线路列表",
            editurl: "../../route/routeOper"
        });


        $(grid_selector).jqGrid('navGrid', pager_selector, {
                edit: false,
                add: true,
                del: false,
                search: false
            }, {}, // edit options
            {
                closeAfterAdd: true,
                afterSubmit: function (response, postdata) {
                    return publicUpdate.afterSubmitAdd(response, postdata);
                }
            }, // add options
            {
                afterSubmit: function (response, postdata) {
                    return publicUpdate.afterSubmitDel(response, postdata);
                }
            }, // del options
            {} // search options
        ).navButtonAdd(pager_selector, {
            caption: " ",
            buttonicon: "glyphicon-edit",
            onClickButton: function () {
                update();
            },
            position: "first"
        }).navButtonAdd(pager_selector, {
	        caption: " ",
	        buttonicon: "glyphicon-trash",
	        title:'删除所选记录',
	        onClickButton: function () {
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../route/routeOper","routeDel","删除成功");
	        }
	    });

    });

    function statusFormatter(cellvalue, options, rowdata) {
        var temp = "";
        if (cellvalue == null) {
            temp = "";
        } else if (cellvalue.indexOf('1') >= 0) {
            temp = "1-有效";
        } else if (cellvalue.indexOf('0') >= 0) {
            temp = "0-无效";
        }
        return temp;
    }

    $("#query").click(function () {
        $(grid_selector).jqGrid("setGridParam",
            {
                contentType: "application/json;charset=utf-8",
                datatype: "json",
                url: "../../route/query",
                mtype: 'POST',// 请求方式
                jsonReader: {
                    root: "data.routeList", // 实际模型的人口
                    total: "data.totalpages",
                    page: "data.currpage",
                    records: "data.totalrecords",
                    repeatitems: false
                },
                postData: {
                    "name": $("#name").val(),
                    "termination": $("#termination").val()
                }, //发送数据
            }).trigger("reloadGrid");
    });

    function update() {
        publicUpdate.updateData(grid_selector);
    }

});