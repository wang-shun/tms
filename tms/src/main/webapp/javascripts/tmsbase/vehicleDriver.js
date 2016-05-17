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
	require("plugins/bootstrapSuggest/bootstrap-suggest.min.sea.js");
	require("sweetalert");
	$.jqm.params.closeoverlay = false;//默认为true 指定当点击overlay的外部区域时,是否自动关闭这个overlay,默认是关闭。
	var grid_selector = "#table_list_1";
	var pager_selector = "#pager_list_1";
	
	$(function ($) {
	    var driverList = new Array();
	    var driverOptions = {value : []};
	    var vehicleList = new Array();
	    var vehicleOptions = {value : []};

	    $.ajax({
	        async: false,
	        url: "../../driver/driverOptions",
	        type : 'POST',// 请求方式
	        dataType:'json',
			data: "status : ''",
	        success : function(data){
	        	driverList = data.data.driverList;
	            driverList.forEach(function(driver){
	            	if(driver.status=='1'){
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
	        type : 'POST',// 请求方式
	        dataType:'json',
			data: {status : ''},
	        success : function(data){
	        	vehicleList = data.data.vehicleList
	        	vehicleList.forEach(function(vehicle){
	        		if(vehicle.status=='1'){
	        			vehicleOptions.value.push({
	        				"车牌号": vehicle.vNo
	        			});
	        		}
				});
	        }
	    });

		$("#vNo").bsSuggest({
			delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
			indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
			indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
			data: vehicleOptions
		});
		
		$("#dName").bsSuggest({
			delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
			indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
			indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
			data: driverOptions
		});
		
	    var lastsel;
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
	        url: "../../vehicleDriver/query",
	        mtype: 'POST',//请求方式
	        jsonReader: {
	            root: "data.vehicleDriverList", // 实际模型的人口
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
	                label:'id',
	                name: 'id',
	                index: 'id',
	                width: "70px",
	                sortable: false,
	                hidden: true,
	                editable: false,
	                search: false,
	                align: 'center'
	            },
	            {
	                label:'车牌号',
	                name: 'vId',
	                index: 'vId',
	                width: "130px",
	                sortable: false,
	                editable: true,
	                hidden: false,
	                search: false,
	                align: 'center',
	                edittype: 'select',
	                editoptions: {
	                    value: getVehicleOptions(vehicleList)
	                },
	                formatter:vehicleFormatter
	            },
	            {
	                label:'司机(手机号)',
	                name: 'dId',
	                index: 'dId',
	                width: "180px",
	                sortable: false,
	                editable: true,
	                hidden: false,
	                search: false,
	                align: 'center',
	                edittype: 'select',
	                editoptions: {
	                    value: getDriverOptions(driverList)
	                },
	                formatter:driverFormatter
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
				},
	        ],
	
	        afterSaveCell: function (rowid, name, val, iRow, iCol) {
	            if(name == "vNo"){
	                alert("After Save Cell");
	            }
	        },
	
	        beforeSelectRow: function (rowid, e) {
	            var $myGrid = $(this),
	                i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),
	                cm = $myGrid.jqGrid('getGridParam', 'colModel');
	            return (cm[i].name === 'cb');
	        },
	        /* loadComplete: function() {
	         },*/
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
	        caption: "车辆司机列表",
	        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
	        editurl: "../../vehicleDriver/oper"
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
	        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../vehicleDriver/oper","delete","删除成功");
	        }
	    });
	
	    $("#query").click(function(){
	        $(grid_selector).jqGrid("setGridParam",{
	                contentType : "application/json;charset=utf-8",
	                datatype : "json",
	                url : "../../vehicleDriver/query",
	                mtype : 'POST',// 请求方式
	                jsonReader : {
	                    root : "data.vehicleDriverList", // 实际模型的人口
	                    total : "data.totalpages",
	                    page : "data.currpage",
	                    records : "data.totalrecords",
	                    repeatitems : false
	                },
	                postData : {
	                    "vNo": $("#vNo").val(),
	                    "dName": $("#dName").val()
	                }, //发送数据
	            }).trigger("reloadGrid");
	    });
	
	    function getDriverOptions(drivers){
	        var formatDriversStr = "";
	        for(var i =0;i<drivers.length;i++){
				if(drivers[i].status == '1'){
					formatDriversStr+=drivers[i].id+":"+drivers[i].name+"("+drivers[i].contact+");";
				}
	        }
	        return formatDriversStr.substr(0, formatDriversStr.length-1);
	    }
	
	    function getVehicleOptions(vehicles){
	        var formatVehiclesStr = "";
	        for(var i =0;i<vehicles.length;i++){
				if(vehicles[i].status == '1'){
					formatVehiclesStr+=vehicles[i].id+":"+vehicles[i].vNo+";";
				}
	        }
	        return formatVehiclesStr.substr(0, formatVehiclesStr.length-1);
	    }
	
	    function driverFormatter(cellvalue, options, rowdata){
	        var temp = "";
	        for(var i =0;i<driverList.length;i++){
	            var id = driverList[i].id;
	            if(cellvalue == id){
	                temp = driverList[i].name+"("+driverList[i].contact+")";
	                break;
	            }
	        }
	        if(temp == ""){
	            temp = cellvalue;
	        }
	        return temp;
	    }
	
	    function vehicleFormatter(cellvalue, options, rowdata){
	        var temp = "";
	        for(var i =0;i<vehicleList.length;i++){
	            var id = vehicleList[i].id;
	            if(cellvalue == id){
	                temp = vehicleList[i].vNo;
	                break;
	            }
	        }
	        if(temp == ""){
	            temp = cellvalue;
	        }
	        return temp;
	    }
	});
	
	function update() {
		publicUpdate.updateData(grid_selector);
	}
	
	function statusFormatter(cellvalue, options, rowdata)
	{
	    var temp = "";
	    if(cellvalue==null){
	        temp = "";
	    }else if(cellvalue.indexOf('1')>=0){
	        temp = "1-有效";
	    } else if(cellvalue.indexOf('0')>=0){
	        temp = "0-无效";
	    }
	    return temp;
	}
	
});