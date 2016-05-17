/**
 * Created by 16030117 on 2016/3/28.
 */
define(function (require, exports, module) {
    var $=require("jquery");
    require("sweetalert");
    var publicUpdate=require("publicUpdate");
    var publicJs = require("publicJs");
    require("datepicker");
    require("jqgridLocalCn");
    require("jqgrid");
    $.jqm.params.closeoverlay = false;//默认为true 指定当点击overlay的外部区域时,是否自动关闭这个overlay,默认是关闭。
    var grid_selector = "#table_list_1";
    var pager_selector = "#pager_list_1";

    $(document).ready(function() {
        $.jgrid.defaults.styleUI = 'Bootstrap';

        $(window).on('resize.jqGrid', function () {
            publicJs.throttle(resizeJqGrid, $(".tms_body").width());
        });

        function resizeJqGrid(width){
            $(grid_selector).jqGrid('setGridWidth', width);
        }

        $(grid_selector).jqGrid({
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            url: "../../vehicle/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.vehicleList", // 实际模型的人口
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
            shrinkToFit: false,
            autoScroll: true,
            multiselect: true,
            multiboxonly: false,
            rowNum: 10,
            rowList: [10, 20, 30],
            colModel: [
                {
                    name: 'id',
                    label: 'id',
                    index: 'id',
                    width: "70px",
                    sortable: false,
                    hidden: true,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'no',
                    label: '车辆编码 <text style="color:red">*</text>',
                    index: 'no',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    hidden: false,
                    search: false,
                    editrules:{required: true},
                    align: 'center'
                },
                {
                    name: 'vNo',
                    label: '车牌号 <text style="color:red">*</text>',
                    index: 'vNo',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    hidden: false,
                    search: false,
                    editrules:{required: true,custom: true,
                        custom_func: vNoCheck},
                    align: 'center'
                },
                {
                    name: 'regDateStr',
                    label: '登记日期',
                    index: 'regDateStr',
                    width: "105px",
                    sortable: true,
                    editable: true,
                    search: false,
                    editrules:{custom: true,
                        custom_func: earlierThanTodayCheck},
                    align: 'center'
                },
                {
                    name: 'vType',
                    label: '车辆类型',
                    index: 'vType',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: '1:1-卡车;2:2-电动车'
                    },
                    formatter:vtypeFormatter
                },
                {
                    name: 'driverType',
                    label: '驾驶证类型',
                    index: 'driverType',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: 'A1:A1;A2:A2'
                    },
                },
                {
                    name: 'vWeight',
                    label: '载货重量(公斤)',
                    index: 'vWeight',
                    width: "130px",
                    sortable: true,
                    editable: true,
                    hidden: false,
                    search: false,
                    editrules:{number: true},
                    align: 'center'
                },
                {
                    name: 'vSize',
                    label: '载货体积（立方米）',
                    index: 'vSize',
                    width: "150px",
                    sortable: true,
                    editable: true,
                    hidden: false,
                    search: false,
                    editrules:{number: true},
                    align: 'center'
                },
                {
                    name: 'deliveryType',
                    label: '配送类型',
                    index: 'deliveryType',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center',
                    edittype: 'select',editoptions: {value: '1:1-冷藏'},
                    formatter:deliveryTypeFormatter
                },
                {
                    name: 'remark',
                    label: '备注',
                    index: 'remark',
                    width: "350px",
                    sortable: false,
                    editable: true,
                    search: false,
                    editrules:{custom: true,
                        custom_func: maxLengthCheck},
                    align: 'center'
                },
                {
                    name: 'status',
                    label: '状态',
                    index: 'status',
                    width: "105px",
                    sortable: true,
                    editable: true,
                    search: false,
                    align: 'center',
                    edittype: 'select', editoptions: {
                        value: '1:1-可用;2:2-不可用;3:3-维修中'
                    },
                    formatter:statusFormatter
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
                    index: 'createAt',
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
                    label: '更新人',
                    index: 'updateBy',
                    width: "100px",
                    sortable: false,
                    editable: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'updatedAt',
                    label: '更新时间',
                    index: 'updateAt',
                    width: "150px",
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

            onSelectAll:function(aRowids,status){
                publicUpdate.onSelectAllWithDate(aRowids,status,pickdates,grid_selector);
                if(status==true){
        			for(var i=0;i<aRowids.length;i++){
                        $('#'+aRowids[i]+'_no').attr('readOnly',true);
                        $('#'+aRowids[i]+'_vNo').attr('readOnly',true);
        			}
        		}
            },

            onSelectRow: function (id) {
                if ($('#' + id).attr('aria-selected') == "true") {
                    $(grid_selector).jqGrid('editRow', id, true,pickdates);
                    $('#'+id+'_no').attr('readOnly',true);
                    $('#'+id+'_vNo').attr('readOnly',true);
                } else {
                    $(grid_selector).jqGrid('restoreRow', id);
                }
            },

            pager: pager_selector,
            viewrecords: true,
            hidegrid: false,
            caption: "车辆列表",
            editurl: "../../vehicle/vehicleOper"
        });

        $(grid_selector).jqGrid('navGrid', pager_selector, {
                edit: false,
                add: true,
                del: false,
                search: false
            },{}, // edit options
            {
                closeAfterAdd:true,
                beforeShowForm :function(formid){
                    $("#regDateStr").datepicker({
                        startView: 0,
                        maxViewMode: 0,
                        minViewMode:0,
                        autoclose : true,
                        format : 'yyyy-mm-dd'
                    });
                    $("#regDateStr").val(publicUpdate.getDate(null));
                },
                afterSubmit:function(response, postdata){
                    return publicUpdate.afterSubmitAdd(response,postdata,grid_selector);
                }
            }, // add options
            {
                afterSubmit: function (response,postdata){
                    return publicUpdate.afterSubmitDel(response,postdata,grid_selector);
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
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../vehicle/vehicleOper","delete","删除成功");
	        }
	    });

        $("#query").click(function(){
            $(grid_selector).jqGrid("setGridParam",{
                contentType : "application/json;charset=utf-8",
                datatype : "json",
                url : "../../vehicle/query",
                mtype : 'POST',// 请求方式
                jsonReader : {
                    root : "data.vehicleList", // 实际模型的人口
                    total : "data.totalpages",
                    page : "data.currpage",
                    records : "data.totalrecords",
                    repeatitems : false
                },
                postData : {
                    "vNo": $("#vNo").val(),
                    "regDate": $("#regDate").val(),
                    "vType": $("#vType").val(),
                    "regStartDate": $("#regStartDate").val(),
                    "regEndDate": $("#regEndDate").val(),
                    "status": $("#status").val(),
                    "driverType": $("#driverType").val()
                }, //发送数据
            }).trigger("reloadGrid");
        });
        /*  function pickdates(id) {
         //alert($("#" + id + "_cerExpireDate").val())
         jQuery("#" + id + "_cerExpireDate", grid_selector).datepicker({
         dateFormat : "yy-mm-dd"
         });
         }*/
        function pickdates(id) {
            $("#" + id + "_regDateStr").datepicker({
                startView: 0,
                maxViewMode: 0,
                minViewMode:0,
                autoclose : true,
                format : 'yyyy-mm-dd'
            });
        }

        $("#regStartDate").datepicker({
            startView: 0,
            maxViewMode: 0,
            minViewMode:0,
            autoclose : true,
            format : 'yyyy-mm-dd'
        });

        $("#regEndDate").datepicker({
            startView: 0,
            maxViewMode: 0,
            minViewMode:0,
            autoclose : true,
            format : 'yyyy-mm-dd'
        });
    });

    function update() {
        publicUpdate.updateData(grid_selector);
    }

    function vtypeFormatter(cellvalue, options, rowdata)
    {
        var temp = "";
        if(cellvalue==null){
            temp = "";
        }else if(cellvalue.indexOf('1')>=0){
            temp = "1-卡车";
        } else if(cellvalue.indexOf('2')>=0){
            temp = "2-电动车";
        }
        return temp;
    }

    function deliveryTypeFormatter(cellvalue, options, rowdata)
    {
        var temp = "";
        if(cellvalue==null){
            temp = "";
        }else if(cellvalue.indexOf('1')>=0){
            temp = "1-冷藏";
        }
        return temp;
    }

    function statusFormatter(cellvalue, options, rowdata)
    {
        var temp = "";
        if(cellvalue==null){
            temp = "";
        }else if(cellvalue.indexOf('1')>=0){
            temp = "1-可用";
        }else if(cellvalue.indexOf('2')>=0){
            temp = "2-不可用";
        }else if(cellvalue.indexOf('3')>=0){
            temp = "3-维修中";
        }else{
            return cellvalue;
        }
        return temp;
    }

    function earlierThanTodayCheck(value, colname) {
        var date = new Date(value);
        var today = new Date();
        if(date > today){
            return [false, "登记日期不允许大于当天日期。"];
        }else{
            return [true, ""];
        }
    }

    function maxLengthCheck(value, colname) {
        if(publicJs.getByteLen(value) > 10){
            return [false, "备注超长，不允许超过512字节或256个汉字"]
        }else{
            return [true, ""];
        }
    }
    function vNoCheck(value, colname) {
    	var vType = $("#vType").val();
    	if("1"==vType){
    		alert(vType);
    		//车牌号校验
    		var re = /^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/;
    		if(re.test(value)){
    			return [true, ""];
    		}
    		return [false,"车牌号格式不正确"];
    	}else{
    		return [true, ""];
    	}
   }
});