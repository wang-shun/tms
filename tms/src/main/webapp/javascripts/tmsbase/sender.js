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

		$(window).on('resize.jqGrid', function () {
			publicjs.throttle(resizeJqGrid, $(".tms_body").width());
		});

		function resizeJqGrid(width){
			$(grid_selector).jqGrid('setGridWidth', width);
		}

	    $(grid_selector).jqGrid({
	        contentType: "application/json;charset=utf-8",
	        datatype: "json",
	        url: "../../sender/query",
	        mtype: 'POST',//请求方式
	        jsonReader: {
	            root: "data.senderList", // 实际模型的人口
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
	                index: 'id',
	                hidden: true
	            },
	            {
	                label:'发货方名称 <text style="color:red">*</text>',
	                name: 'name',
	                index: 'name',
	                width: "180px",
	                editable: true,
					editrules:{required: true},
	                align: 'center'
	            },
	            {
	                label:'发货人 <text style="color:red">*</text>',
	                name: 'contact',
	                index: 'contact',
	                width: "80px",
	                editable: true,
					editrules:{required: true},
	                align: 'center'
	            },
	            {
					label:'联系电话 <text style="color:red">*</text>',
	                name: 'tel',
	                index: 'tel',
	                width: "110px",
	                sortable: true,
	                editable: true,
					editrules:{required: true, number: true,custom:true,custom_func:publicValidate.validatePhoneNum},
	                align: 'center',
	            },
	            {
	                label:'发货点 <text style="color:red">*</text>',
	                name: 'point',
	                index: 'point',
	                width: "150px",
	                editable: true,
					editrules:{required: true},
	                align: 'center'
	            },
	            {
	                label:'发货城区 <text style="color:red">*</text>',
	                name: 'area',
	                index: 'area',
	                width: "80px",
	                editable: true,
					editrules:{required: true},
	                align: 'center',
					edittype: 'select',editoptions: {value: '东区:东区;西区:西区'}
	            },
	            {
	                label:'发货地址 <text style="color:red">*</text>',
	                name: 'address',
	                index: 'address',
	                width: "300px",
	                sortable: true,
	                editable: true,
					editrules:{required: true},
	                align: 'center'
	            },
	            {
	                label:'省',
	                name: 'province',
	                index: 'province',
	                width: "75px",
	                editable: true,
	                align: 'center',
	                edittype: 'select',editoptions: {value: '上海:上海'}
	            },
	            {
	                label:'市',
	                name: 'city',
	                index: 'city',
	                width: "90px",
	                editable: true,
	                align: 'center',
	                edittype: 'select',editoptions: {value: '上海:上海'}
	            },
	            {
	                label:'区',
	                name: 'county',
	                index: 'county',
	                width: "90px",
	                sortable: true,
	                editable: true,
	                align: 'center',
	                edittype: 'select',
					editoptions: {
	                    value: '黄浦:黄浦;徐汇:徐汇;长宁:长宁;静安:静安;普陀:普陀;虹口:虹口;杨浦:杨浦;闵行:闵行;宝山:宝山;嘉定:嘉定;浦东:浦东;金山:金山;松江:松江;青浦:青浦;奉贤:奉贤;崇明县:崇明县'
	                },
	            },
	            {
	                label:'新增人',
	                name: 'createdBy',
	                index: 'createdBy',
	                width: "100px",
	                align: 'center'
	            },
	            {
	                label:'新增时间',
	                name: 'createdAt',
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
	                label:'修改人',
	                name: 'updatedBy',
	                index: 'updateBy',
	                width: "100px",
	                align: 'center'
	            },
	            {
	                label:'修改时间',
	                name: 'updatedAt',
	                index: 'updateAt',
	                width: "150px",
	                sortable: true,
	                align: 'center',
	                formatter: "date",
	                formatoptions: {srcformat: 'u', newformat: 'Y-m-d H:i:s'}
	            }
	        ],
	
	        //屏蔽点击行中任意字段时进入编辑模式功能
	        beforeSelectRow: function (rowid, e) {
	            var $myGrid = $(this),
	                i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
	                cm = $myGrid.jqGrid('getGridParam', 'colModel');
	            return (cm[i].name === 'cb');
	        },
	        onSelectAll:function(aRowids,status){
	        	publicUpdate.onSelectAll(aRowids,status,grid_selector);
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
	        caption: "发货方列表",
	        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
	        editurl: "../../sender/oper"
	    });
	
	
	    $(grid_selector).jqGrid('navGrid', pager_selector, {
	        edit: false,
	        add: true,
	        del: false,
	        search: false
	    },{}, // edit options
	    {
			closeAfterAdd:true,
			afterSubmit:function(response, postdata){
				return publicUpdate.afterSubmitAdd(response,postdata);
			  }
		}, // add options
		{
			afterSubmit: function (response,postdata){ 
				return publicUpdate.afterSubmitDel(response,postdata);
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
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../sender/oper","delete","删除成功");
	        }
	    });
	
	    $("#query").click(function(){
	        $(grid_selector).jqGrid("setGridParam",{
	                contentType : "application/json;charset=utf-8",
	                datatype : "json",
	                url : "../../sender/query",
	                mtype : 'POST',// 请求方式
	                contentType:"application/json",
	                jsonReader : {
	                    root : "data.senderList", // 实际模型的人口
	                    total : "data.totalpages",
	                    page : "data.currpage",
	                    records : "data.totalrecords",
	                    repeatitems : false
	                },
	                postData : {
	                    "name": $("#name").val()
	                }, //发送数据
	            }).trigger("reloadGrid");
	    });
	});
	
	function update() {
		publicUpdate.updateData(grid_selector);
	}
	
});