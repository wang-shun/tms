	/**
	 * Created by 16030117 on 2016/3/28.
	 */
define(function (require, exports, module) {
	var $ = require("jquery");
	require("sweetalert");
	var publicUpdate = require("publicUpdate");
	var waybillModal = require("tmsCustomer/waybillModal.js");
	var publicjs = require("publicJs");
	require("bootstrap");
	require("datepicker");
	require("jqgridLocalCn");
	require("jqgrid");
	require("bootstrapSuggest");

	var grid_selector = "#table_list_1";
	var pager_selector = "#pager_list_1";

	$(document).ready(function() {
		var senderList = {value : []};
		$.ajax({
			async: false,
			url: "../../sender/senderOptions",
			type : 'POST',// 请求方式
			dataType:'json',
			success : function(data){
				(data.data.senderList).forEach(function(sender){
					senderList.value.push({
						"发货方": sender["发货方"],
						"发货人": sender["发货人"],
						"发货方ID": sender.ID
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
	        url: "../../rearTransportFee/query",
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
	                name: 'no',
	                index: 'no',
	                label:"运单编号",
	                width: "120px",
	                sortable: false,
	                editable: false,
	                hidden: false,
	                search: false,
	                align: 'center'
	            },
	            {
	                name: 'dName',
	                index: 'dName',
	                label:"发货方",
	                width: "150px",
	                sortable: true,
	                editable: false,
	                search: false,
	                align: 'center',
	            },
	            {
	            	name: 'rName',
	            	index: 'rName',
	            	label:"收货方",
	            	width: "80px",
	            	sortable: true,
	            	editable: false,
	            	search: false,
	            	align: 'center',
	            },
	            {
	                name: 'rArea',
	                index: 'rArea',
	                label:"收货城区",
	                width: "125px",
	                sortable: false,
	                editable: false,
	                hidden: false,
	                search: false
	            },
	            {
	            	name: 'rAddress',
	            	index: 'rAddress',
	            	label:"收货地址",
	            	width: "250px",
	            	sortable: false,
	            	editable: false,
	            	hidden: false,
	            	search: false
	            },
	            {
	            	name: 'transportFee',
	            	index: 'transportFee',
	            	label:"运费",
	            	width: "80px",
	            	sortable: false,
	            	editable: false,
	            	hidden: false,
	            	search: false,
	            	formatter:"number"
	            },
	            {
	                name: 'tFeeStatus',
	                index: 'tFeeStatus',
	                label:"状态",
	                width: "80px",
	                sortable: false,
	                editable: false,
	                search: false,
	                align: 'center',
	                formatter:tFeestatusFormatter
	            },
	            {
	            	name: 'remark',
	            	index: 'remark',
	            	label:"备注",
	            	width: "200px",
	            	sortable: false,
	            	editable: false,
	            	search: false,
	            	align: 'center'
	            },
	            {
	                name: 'createdBy',
	                index: 'createdBy',
	                label:"创建人",
	                width: "100px",
	                sortable: false,
	                editable: false,
	                search: false,
	                align: 'center'
	            },
	            {
	                name: 'createdAt',
	                index: 'createAt',
	                label:"创建时间",
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
	                label:"修改人",
	                width: "100px",
	                sortable: false,
	                editable: false,
	                search: false,
	                align: 'center'
	            },
	            {
	                name: 'updatedAt',
	                index: 'updateAt',
	                label:"修改时间",
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
	        onSelectAll:function(aRowids,status){
	        	publicUpdate.onSelectAllWithDate(aRowids,status,pickdates,grid_selector);
		    },
	        onSelectRow: function (id) {
	            if ($('#' + id).attr('aria-selected') == "true") {
	                $(grid_selector).jqGrid('editRow', id, true,pickdates);
	            } else {
	                $(grid_selector).jqGrid('restoreRow', id);
	            }
	        },
	        pager: pager_selector,
	        viewrecords: true,
	        hidegrid: false,
	        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
	        editurl: "../../rearTransportFee/waybillConfirmOrCancel"
	    });
	
	
	    $(grid_selector).jqGrid('navGrid', pager_selector, 
	    		{edit: false,add: false,del: false,search: false},
	    		{}, // edit options
	    		{}, // add options
	    		{
					afterSubmit: function (response,postdata){
						return publicUpdate.afterSubmitDel(response,postdata);
					}
	    		} // del options
		);
	
	    $("#query").click(function(){
	        queryWaybill();
	    });
	
	    $("#saveTransportFee").click(function(){
			var transportFee = $('#transportFee').val();
			if(transportFee==""){
				alert("请填写运费单价。");
				return;
			}
			if(isNaN(transportFee)){
				$('#transportFee').focus();
				alert("运费单价请填数字。");
				return;
			}
	    	publicUpdate.cancelOrConfirmData(grid_selector, "确认设置运费？","../../rearTransportFee/saveTransportFee",transportFee,"成功确认收款");
	    });
	    
		$("#detailedExport").click( function(){
			alert("导出成功");
		});
	
	    function pickdates(id) {
	        $("#" + id + "_receiveAtStr").datepicker({
	            startView: 0,
	            maxViewMode: 0,
	            minViewMode:0,
	            autoclose : true,
	            format : 'yyyy-mm-dd'
	        });
	    }
	    $("#createdAtMonth").datepicker({
			startView: 1,
			maxViewMode: 1,
			minViewMode:1,
			autoclose : true,
			format : 'yyyy-mm'
		});
	});
	
	function tFeestatusFormatter(cellvalue, options, rowdata)
	{	
		var temp = "";
		if(cellvalue==null){
			temp = "";
		} else if (cellvalue.indexOf('1') >= 0) {
			temp = "1-未结算";
		} else if (cellvalue.indexOf('2') >= 0) {
			temp = "2-已结算";
		}
		return temp;
	}

	window.queryWaybill = function(){
	    var statusArray= new Array();
	    $("input[name='statusGroup']:checked").each(function() {
	        statusArray.push($(this).attr('value'));
	    });
	    $(grid_selector).jqGrid("setGridParam",{
	            contentType : "application/json;charset=utf-8",
	            datatype : "json",
	            url : "../../rearTransportFee/query",
	            mtype : 'POST',// 请求方式
	            jsonReader : {
	                root : "data.waybillList", // 实际模型的人口
	                total : "data.totalpages",
	                page : "data.currpage",
	                records : "data.totalrecords",
	                repeatitems : false
	            },
	            postData : {
	                "dName": $("#dName").val(),
	                "createdAtMonth": $("#createdAtMonth").val()
	            }, //发送数据
	        }).trigger("reloadGrid");
	}
	
});

