/**
 * Created by 16030117 on 2016/3/28.
 */
var grid_selector = "#table_list_1";
var pager_selector = "#pager_list_1";

$(function ($) {
	//初始化下拉框
    var senderList = Array();
    var vehicleList = Array();
    $.ajax({
        async: false,
        url: "../../vehicleConfirmation/Options",
        type : 'POST',// 请求方式
        dataType:'json',
        success : function(data){
        	driverList = data.data.driverList;
        	driverList.forEach(function(driver){
                $("#driverId").append("<option value="+driver.id+">"+driver.name+"</option>");
            });
        	
        	vehicleList = data.data.vehicleList;
        	vehicleList.forEach(function(vehicle){
                $("#vehicleId").append("<option value="+vehicle.id+">"+vehicle.vNo+"</option>");
            });
        }
    });
    
    
	
    var lastsel;
    $.jgrid.defaults.styleUI = 'Bootstrap';

    $(window).on('resize.jqGrid', function () {
        $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
    })
    var parent_column = $(grid_selector).closest('[class*="col-"]');
    $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
        if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
            setTimeout(function () {
                $(grid_selector).jqGrid('setGridWidth', parent_column.width());
            }, 0);
        }
    })
    $(grid_selector).jqGrid({
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        url: "../../vehicleConfirmation/query",
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
                label:"id",
                width: "70px",
                sortable: false,
                hidden: true,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'status',
                index: 'status',
                label:"状态",
                width: "150px",
                sortable: false,
                editable: false,
                hidden: false,
                search: false,
                align: 'center',edittype:'select',editoptions:{
         		   value:'00:00-运单创建;10:10-运单确认;20:20-在途（卡车配送）;21:21-在途（电动车配送）;40:40-客户签收;90:90-作废'},
        		   formatter:statusFormatter
            },
            {
                name: 'no',
                index: 'no',
                label:"运单编号",
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
                label:"客户订单号",
                width: "100px",
                sortable: true,
                editable: false,
                search: false,
                align: 'center',
            },
            {
                name: 'dName',
                index: 'dName',
                label:"发货方",
                width: "100px",
                sortable: false,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'rName',
                index: 'rName',
                label:"收货方",
                width: "100px",
                sortable: false,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'rArea',
                index: 'rArea',
                label:"收货城区",
                width: "130px",
                sortable: true,
                editable: false,
                hidden: false,
                search: false,
                align: 'center'
            },
            {
                name: 'rAddress',
                index: 'rAddress',
                label:"收货地址",
                width: "300px",
                sortable: true,
                editable: false,
                hidden: false,
                search: false,
                align: 'center'
            },
            {
                name: 'transportFee',
                index: 'transportFee',
                label:"费用",
                width: "100px",
                sortable: false,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'receiveAtStr',
                index: 'receiveAtStr',
                label:"接单时间",
                width: "100px",
                sortable: false,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'recipient',
                index: 'recipient',
                label:"揽件人",
                width: "80px",
                sortable: true,
                editable: false,
                search: false,
                align: 'center'
            },
            {
                name: 'remark',
                index: 'remark',
                label:"备注",
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
        onSelectRow: function (id) {
            if ($('#' + id).attr('aria-selected') == "true") {
                jQuery(grid_selector).jqGrid('editRow', id, true,pickdates);
            } else {
                jQuery(grid_selector).jqGrid('restoreRow', lastsel);
                jQuery(grid_selector).jqGrid('saveRow', id, true);
            }
            lastsel = id;
        },
        pager: pager_selector,
        viewrecords: true,
        hidegrid: false,
        caption: "运单列表",
        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
        editurl: "../../vehicleConfirmation/vehicleConfirmationOper"
    });


    jQuery(grid_selector).jqGrid('navGrid', pager_selector, {
        edit: false,
        add: false,
        del: true,
        search: false
    })

    $("#query").click(function(){
    	 var driverId = $("#driverId").val();
//    	 if(drivernum == null || drivernum == ""){
//    		 alert("请输入手机号码进行查询");
//    		 return false;
//    	 }
//    	 var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
//    	 if(drivernum != null && drivernum != ""){
//	    	 if(!myreg.test(drivernum)) 
//	    	 { 
//	    	     alert('请输入有效的手机号码！'); 
//	    	     return false; 
//	    	 } 
//    	}
    	 queryByStatus(driverId);
    });
    
    
    $("#Confirm").click(function(){
    	 var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
    	    if (ids == '') {
    	        alert('请选择修改数据!');
    	        return;
    	    }
    	   
	    var driverId = $("#driverId").val(); 
    	if (driverId == '') {
	        alert('请选择司机!');
	        return;
	    }    
    	   
    	var vehicleId = $("#vehicleId").val(); 
    	if (vehicleId == '') {
	        alert('请选择车辆!');
	        return;
	    }
    	
    	var idstr="";
	    //先将所有的数据保存
	    for (var i = 0; i < ids.length; i++) {
	        idstr+=ids[i]+",";
	    }
    	
    	if(window.confirm("你确定要派车吗？")){
		    $(grid_selector).jqGrid(
			        "setGridParam",
			        {
			            contentType: "application/json;charset=utf-8",
			            datatype: "json",
			            url: "../../vehicleConfirmation/vehicleConfirmationOper",
			            mtype: 'POST',// 请求方式
			            jsonReader: {
			                root: "data.waybillList", // 实际模型的人口
			                total: "data.totalpages",
			                page: "data.currpage",
			                records: "data.totalrecords",
			                repeatitems: false
			            },
			            postData: {
			                'id': idstr.substring(0,idstr.length-1),
			            	'vehicleId':vehicleId,
			            	'driverId':driverId,
			            	'oper':'confrimVehicle'
			            }, //发送数据
			        }).trigger("reloadGrid"); 
    	}
   });
    
    
    
    
    $("#vehicleConfirmationConfirm").click(function(){
    	vehicleConfirmationOper("vehicleConfirmationConfirm","确认订单？");
    });
    $("#vehicleConfirmationCancel").click(function(){
    	vehicleConfirmationOper("vehicleConfirmationCancel","确定作废？");
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
    $("#deliveryDate").datepicker();
    $("#deliveryDate2").datepicker();
});

function statusFormatter(cellvalue, options, rowdata)
{	
	var temp = "";
	if(cellvalue==null){
		temp = "";
	}else if(cellvalue.indexOf('00')>=0){
		temp = "00-运单创建";
	} else if(cellvalue.indexOf('10')>=0){
		temp = "10-运单确认";
	} else if(cellvalue.indexOf('20')>=0){
		temp = "20-在途（卡车配送）";
	} else if(cellvalue.indexOf('21')>=0){
		temp = "21-在途（电动车配送）";
	} else if(cellvalue.indexOf('40')>=0){
		temp = "40-客户签收";
	} else if(cellvalue.indexOf('90')>=0){
		temp = "90-作废";
	}
	return temp;
}

function queryByStatus(driverId){
    $(grid_selector).jqGrid("setGridParam",{
            contentType : "application/json;charset=utf-8",
            datatype : "json",
            url : "../../vehicleConfirmation/query",
            mtype : 'POST',// 请求方式
            jsonReader : {
                root : "data.waybillList", // 实际模型的人口
                total : "data.totalpages",
                page : "data.currpage",
                records : "data.totalrecords",
                repeatitems : false
            },
            postData : {
                "driverId": driverId
            }, //发送数据
        }).trigger("reloadGrid");
}

vehicleId

function update() {
    var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
    if (ids == '') {
        alert('请先选择修改数据!');
        return;
    }
    if(window.confirm("确定修改？")){
	    for (var i = 0; i < ids.length; i++) {
	        jQuery(grid_selector).jqGrid('saveRow', ids[i]);
	    }
	    $(grid_selector).jqGrid("setGridParam").trigger("reloadGrid");//刷新数据
    }
}

function vehicleConfirmationOper(action,message) {
    var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
    if (ids == '') {
        alert('请先选择修改数据!');
        return;
    }
    if(window.confirm(message)){
    	var idstr="";
	    //先将所有的数据保存
	    for (var i = 0; i < ids.length; i++) {
	        idstr+=ids[i]+",";
	    }
	    $(grid_selector).jqGrid(
	        "setGridParam",
	        {
	            contentType: "application/json;charset=utf-8",
	            datatype: "json",
	            url: "../../vehicleConfirmation/vehicleConfirmationOper",
	            mtype: 'POST',// 请求方式
	            jsonReader: {
	                root: "data.waybillList", // 实际模型的人口
	                total: "data.totalpages",
	                page: "data.currpage",
	                records: "data.totalrecords",
	                repeatitems: false
	            },
	            postData: {
	                'ids': idstr.substring(0,idstr.length-1),
	            	'oper':'confrimVehicle'
	            }, //发送数据
	        }).trigger("reloadGrid");

    }
}