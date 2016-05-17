/**
 * Created by 16030117 on 2016/3/28.
 */
define(function (require, exports, module) {
    var $ = require("jquery");
    var publicUpdate = require("publicUpdate");
    var publicValidate = require("publicValidate");
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

        var errorMessage=GetQueryString("errorMessage")
        if(undefined !=errorMessage && null!=errorMessage && errorMessage!=""){
        	alert("导入出错，具体信息："+decodeURI(errorMessage));
        }

        $(window).on('resize.jqGrid', function () {
            publicjs.throttle(resizeJqGrid, $(".tms_body").width());
        });

        function resizeJqGrid(width){
            $(grid_selector).jqGrid('setGridWidth', width);
        }

        $(grid_selector).jqGrid({
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            url: "../../receiver/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.receiverList", // 实际模型的人口
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
                editoper: "receiverUpdate", // 当在edit模式中提交数据时，操作的名称
                addoper: "receiverAdd", // 当在add模式中提交数据时，操作的名称
                deloper: "receiverDel", // 当在delete模式中提交数据时，操作的名称
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
                    width: "100px",
                    sortable: false,
                    hidden: true,
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'name',
                    index: 'name',
                    label: "收货方",
                    width: "80px",
                    sortable: false,
                    editable: true,
                    editrules: {required: true},
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'area',
                    index: 'area',
                    label: "收货方城区",
                    width: "100px",
                    sortable: false,
                    editrules: {required: true},
                    editable: true,
                    align: 'center'
                },
                {
                    name: 'contact',
                    index: 'contact',
                    label: "收货人",
                    width: "120px",
                    sortable: false,
                    editrules: {required: true},
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'tel',
                    index: 'tel',
                    width: "150px",
                    label: "手机号",
                    editrules: {required: true,custom:true,custom_func:publicValidate.validatePhoneNum},
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center',
                },
                {
                    name: 'address',
                    index: 'address',
                    label: "收货地址",
                    width: "300px",
                    editrules: {required: true},
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center'
                },
            ],

            /* loadComplete: function() {
             },*/
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
            caption: "收货方地址列表",
            //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
            editurl: "../../receiver/receiverOper"
        });


        $(grid_selector).jqGrid('navGrid', pager_selector, {
                edit: false,
                add: true,
                del: false,
                search: false
            }, {}, // edit options
            {
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
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../receiver/receiverOper","receiverDel","删除成功");
	        }
	    });

        /*  function pickdates(id) {
         //alert($("#" + id + "_cerExpireDate").val())
         $("#" + id + "_cerExpireDate", grid_selector).datepicker({
         dateFormat : "yy-mm-dd"
         });
         }*/
    });

    function update() {
    	var ids = $(grid_selector).jqGrid('getGridParam','selarrrow');
    	var rowDates = new Array();
    	for(var i=0;i<ids.length;i++){
    		var object = new Object();
    		object.id = $("#"+ids[i]+"_id").val();
    		object.name = $("#"+ids[i]+"_name").val();
    		object.area = $("#"+ids[i]+"_area").val();
    		object.contact = $("#"+ids[i]+"_contact").val();
    		object.tel = $("#"+ids[i]+"_tel").val();
    		if(!publicValidate.validatePhoneNum($("#"+ids[i]+"_tel").val())){
    			alert("第"+(i+1)+"行，不是完整的11位手机号或者手机号不正确");
    			return;
    		};
    		object.address = $("#"+ids[i]+"_address").val();
    		rowDates.push(object);
    	}
    	publicUpdate.updateDataBatch(grid_selector,"确定修改？","../../receiver/receiverUpdate",rowDates);
        //publicUpdate.updateData(grid_selector);
    }

    $("#downTemplate").click(function () {
        window.open("../../template/receiver.xlsx");
    });
    function endwith(obj, endStr) {
        var d = obj.length - endStr.length;
        return (d >= 0 && obj.lastIndexOf(endStr) == d)
    }

    $("#checkForm").click(function () {

        var fileBuildInfo = $("#excel_file").val();
        if (fileBuildInfo == null || fileBuildInfo == "") {
            alert("请选择文件");
            return false;
        }
        if (!endwith(fileBuildInfo, ".xlsx")) {
            alert("请上传excel文件");
            return false;
        }
        $("#upload").submit();
    });
    
    function GetQueryString(name) { 
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
        var r = window.location.search.substr(1).match(reg);  //获取url中"?"符后的字符串并正则匹配
        var context = ""; 
        if (r != null) 
             context = r[2]; 
        reg = null; 
        r = null; 
        return context == null || context == "" || context == "undefined" ? "" : context; 
    }
});