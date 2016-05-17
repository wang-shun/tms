/**
 * Created by 16030117 on 2016/3/28.
 */
define(function (require, exports, module) {
    var $ = require("jquery");
    var publicUpdate = require("publicUpdate");
    var publicValidate = require("publicValidate");
    var publicjs = require("publicJs");
    require("datepicker");
    require("sweetalert");
    require("jqgridLocalCn");
    require("jqgrid");
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
            url: "../../driver/query",
            mtype: 'POST',//请求方式
            jsonReader: {
                root: "data.driverList", // 实际模型的人口
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
                editoper: "driverUpdate", // 当在edit模式中提交数据时，操作的名称
                addoper: "driverAdd", // 当在add模式中提交数据时，操作的名称
                deloper: "driverDel", // 当在delete模式中提交数据时，操作的名称
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
                    width: "100px",
                    sortable: false,
                    hidden: true,
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'name',
                    label: '姓名  <text style="color:red">*</text>',
                    index: 'name',
                    width: "80px",
                    sortable: false,
                    editable: true,
                    editrules: {required: true},
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'cerCarType',
                    label: '驾驶车辆类型 <text style="color:red">*</text>',
                    index: 'cerCarType',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    editrules: {required: true},
                    search: false,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: '1:1-卡车;2:2-电动车'
                    },
                    formatter: cerCarTypeFormatter
                },
                {
                    name: 'cerType',
                    label: '驾驶证类别',
                    index: 'cerType',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: ':;A1:A1;A2:A2'
                    }
                },
                {
                    name: 'cerNo',
                    label: '驾驶证号码',
                    index: 'cerNo',
                    width: "180px",
                    sortable: false,
                    editable: true,
                    align: 'center',
                    editrules: {number: true, edithidden: true}
                },
                {
                    name: 'cerExpireDateStr',
                    label: '驾驶证到期日',
                    index: 'cerExpireDate',
                    width: "120px",
                    sortable: false,
                    editable: true,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'dId',
                    label: '身份证号 <text style="color:red">*</text>',
                    index: 'dId',
                    width: "200px",
                    sortable: false,
                    editrules: {required: true, number: true, edithidden: true,custom:true,custom_func:publicValidate.validateCard},
                    editable: true,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'contact',
                    label: '联系方式 <text style="color:red">*</text>',
                    index: 'contact',
                    width: "150px",
                    sortable: false,
                    editrules: {required: true, number: true,custom:true,custom_func:publicValidate.validatePhoneNum},
                    editable: true,
                    hidden: false,
                    search: false,
                    align: 'center'
                },
                {
                    name: 'sex',
                    label: '性别',
                    index: 'sex',
                    width: "100px",
                    sortable: false,
                    editable: true,
                    editrules: {required: true},
                    search: false,
                    align: 'center',
                    edittype: 'select',
                    editoptions: {
                        value: 'M:M-男;F:F-女'
                    },
                    formatter: sexFormatter
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
            	if(status==true){
        			for(var i=0;i<aRowids.length;i++){
        				$(grid_selector).jqGrid('editRow', aRowids[i], true,pickdates);
        				var id = aRowids[i];
        				cerCarTypeChange(id);
//        				$("#" + id + "_cerCarType").change(function () {
//        					alert(id);
//                        	cerCarTypeChange(id)
//                        });

        				//解决闭包问题，new一个新的函数类 
        				$("#" + id + "_cerCarType").bind("change",{id:id},changeHandler)
        			}
        		}else{
        			for(var i=0;i<aRowids.length;i++){
        				$(grid_selector).jqGrid('restoreRow',aRowids[i]);
        			}
        		}
//                publicUpdate.onSelectAllWithDate(aRowids, status, pickdates, grid_selector);
            },
            onSelectRow: function (id) {
                if ($('#' + id).attr('aria-selected') == "true") {
                    $(grid_selector).jqGrid('editRow', id, true, pickdates);
                    cerCarTypeChange(id)
                    $("#" + id + "_cerCarType").change(function () {
                    	cerCarTypeChange(id)
                    });
                } else {
                    $(grid_selector).jqGrid('restoreRow', id);
                }
            },
            pager: pager_selector,
            viewrecords: true,
            hidegrid: false,
            caption: "司机列表",
            //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
            editurl: "../../driver/driverOper"
        });

        $(grid_selector).jqGrid('navGrid', pager_selector, {
                edit: false,
                add: true,
                del: false,
                search: false
            }, {}, // edit options
            {
                closeAfterAdd: true,
                beforeShowForm: function (formid) {
                    $("#cerExpireDateStr").datepicker({
                        startView: 0,
                        maxViewMode: 0,
                        minViewMode: 0,
                        autoclose: true,
                        format: 'yyyy-mm-dd'
                    });
                },
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
        })
        .navButtonAdd(pager_selector, {
	        caption: " ",
	        buttonicon: "glyphicon-trash",
	        title:'删除所选记录',
	        onClickButton: function () {
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../driver/driverOper","driverDel","删除成功");
	        }
	    });

        function pickdates(id) {
            $("#" + id + "_cerExpireDateStr").datepicker({
                startView: 0,
                maxViewMode: 0,
                minViewMode: 0,
                autoclose: true,
                format: 'yyyy-mm-dd'
            });
        }

        $("#query").click(function () {
            $(grid_selector).jqGrid(
                "setGridParam",
                {
                    contentType: "application/json;charset=utf-8",
                    datatype: "json",
                    url: "../../driver/query",
                    mtype: 'POST',// 请求方式
                    jsonReader: {
                        root: "data.driverList", // 实际模型的人口
                        total: "data.totalpages",
                        page: "data.currpage",
                        records: "data.totalrecords",
                        repeatitems: false
                    },
                    postData: {
                        "name": $("#name").val()
                    }, //发送数据
                }).trigger("reloadGrid");
        });

        function update() {
            var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
            var dataArray = new Array();
            //先将所有的数据保存
            for (var i = 0; i < ids.length; i++) {
            	var name = $('#' + ids[i] + '_name').val();
                var cerCarType = $('#' + ids[i] + '_cerCarType').val();
                var cerType = $('#' + ids[i] + '_cerType').val();
                var cerNo = $('#' + ids[i] + '_cerNo').val();
                var contact = $('#' + ids[i] + '_contact').val();
                var dId = $('#' + ids[i] + '_dId').val();
                var sex = $('#' + ids[i] + '_sex').val();
                var cerExpireDateStr = $('#' + ids[i] + '_cerExpireDateStr').val();
                if(!isNullValidate(name)){
                	swal({
                        title: "编辑提示 <small>详情:</small>",
                        text: "姓名 <span style='color:#F8BB86'>【姓名】</span> 必填.",
                        html: true
                    });
                	return;
                };
                if (cerCarType == '1') {
                    if (cerType == null || cerType == "") {
                        swal({
                            title: "编辑提示 <small>详情:</small>",
                            text: "卡车 <span style='color:#F8BB86'>【驾驶证类型】</span> 必填.",
                            html: true
                        });
                        return;
                    }
                    if (cerNo == null || cerNo == "") {
                        swal({
                            title: "编辑提示 <small>详情:</small>",
                            text: "卡车 <span style='color:#F8BB86'>【驾驶证号码】</span> 必填.",
                            html: true
                        });
                        return;
                    }
                    if (cerExpireDateStr == null || cerExpireDateStr == "") {
                        swal({
                            title: "编辑提示 <small>详情:</small>",
                            text: "卡车 <span style='color:#F8BB86'>【驾驶证到期日】</span> 必填.",
                            html: true
                        });
                        return;
                    }
                } else {
                    $('#' + ids[i] + '_cerType').val("");
                }
                
                if(!publicValidate.validatePhoneNum(contact)){
                	alert("第"+(i+1)+"行，不是完整的11位手机号或者手机号不正确");
                	return;
                }
                if(!publicValidate.validateCardValue(dId)){
                	alert("第"+(i+1)+"行，身份证号码不完整");
                	return;
                }
                var object = new Object();
                object.id=ids[i];
                object.name = name;
                object.cerCarType = cerCarType;
                object.cerType = cerType;
                object.cerNo = cerNo;
                object.cerExpireDate = new Date(cerExpireDateStr+" 00:00:00");
                object.dId = dId;
                object.contact = contact;
                object.sex = sex;
                dataArray.push(object)
            }
            //publicUpdate.updateData(grid_selector);
            publicUpdate.updateDataBatch(grid_selector,"确定修改？","../../driver/driverUpdate",dataArray);
        }
    });

    function cerTypeFormatter(cellvalue, options, rowdata) {
        var temp = "";
        if (cellvalue == null) {
            temp = "";
        } else if (cellvalue == 'A1') {
            temp = "A1";
        } else if (cellvalue == 'A2') {
            temp = "A2";
        }
        return temp;
    }

    function cerCarTypeFormatter(cellvalue, options, rowdata) {
        var temp = "";
        if (cellvalue == null) {
            temp = "";
        } else if (cellvalue.indexOf('1') >= 0) {
            temp = "1-卡车";
        } else if (cellvalue.indexOf('2') >= 0) {
            temp = "2-电动车";
        }
        return temp;
    }

    function sexFormatter(cellvalue, options, rowdata) {
        var temp = "";
        if (cellvalue == null) {
            temp = "";
        } else if (cellvalue.indexOf('F') >= 0) {
            temp = "F-女";
        } else if (cellvalue.indexOf('M') >= 0) {
            temp = "M-男";
        }
        return temp;
    }
    //单条记录绑定事件
    function cerCarTypeChange(id) {
    	 if ($("#" + id + "_cerCarType").val().trim() == "2") {
             $('#' + id + '_cerNo').attr('disabled', true);
             $('#' + id + '_cerType').attr('readOnly', true);
             $('#' + id + '_cerExpireDateStr').attr('disabled', true);
         } else {
             $('#' + id + '_cerNo').attr('disabled', false);
             $('#' + id + '_cerType').attr('readOnly', false);
             $('#' + id + '_cerExpireDateStr').attr('disabled', false);
         }
    }
    //循环绑定事件使用
    function changeHandler(event) {  
        cerCarTypeChange(event.data.id);  
    }  
    //是否为空
    function isNullValidate(value) {  
    	if(value==null || value==""){
    		return false;
    	}else{
    		return true;
    	};
    }  
});