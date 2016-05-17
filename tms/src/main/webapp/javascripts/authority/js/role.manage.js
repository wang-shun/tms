define(function (require, exports, module) {
	var $=require("jquery");
    require("sweetalert");
    require("bootstrap");
    var publicUpdate=require("publicUpdate");
    var publicJs = require("publicJs");
    
    var modal = require("authority/js/rolemenu.tree.manage");
    require("datepicker");
    require("jqgridLocalCn");
    require("jqgrid");
    $.jqm.params.closeoverlay = false;//默认为true 指定当点击overlay的外部区域时,是否自动关闭这个overlay,默认是关闭。
    var grid_selector = "#table_list_1";
    var pager_selector = "#pager_list_1";
    
    function search(){
	
			var name = $("#name").val();
			var age =  $("#age").val();
		    if(name == null  || name == ""){
		    	alert("姓名或年龄不能为空");
		        return false;
		    }else{
		    	  document.demo.action="search";
		    	  document.demo.submit();
		    }
		    
		}
		
    var dataFommater = {
	        showModal : function(cellvalue, options, rowdata){
	            return '<a style="cursor:pointer" onclick=updateRole("'+rowdata.id+'","'+rowdata.name+'")>修改权限</a>';
	        },
   };


$(function(){
	 
	$("#addRole").click(function(){
		addRole();
	});
	
	function addRole(){
		$('#modalAction').val("add");
		$('#myModal').modal({remote: "rolemenu.html"});
	}
	
	
	window.updateRole = function(id, name){
		$('#modalAction').val("update");
		$('#modalId').val(id);
		$('#modalname').val(name);
		$('#myModal').modal({remote: "rolemenu.html"});
	}
	
	  $("#myModal").on("hidden.bs.modal", function () {
          $(this).removeData("bs.modal");
          
      });
	  
	  
	  $('#myModal').on('shown.bs.modal', function () {
		  modal.loadModalData();
      });
	
	function deleteRole(){
		 var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
		 
		 if (ids == '') {
			 alert("请选择数据");
		        return;
		    }
		 
		 var idstr="";
		    //先将所有的数据保存
	    for (var i = 0; i < ids.length; i++) {
	        idstr+=ids[i]+",";
	    }
		 
		var data = "ids="+idstr;
		
			sweetConfirm("你确定要删除？"||"确定修改？", function(){
				 $.ajax({
			    	 url:"../../role/roleDel",
			    	 data:data,
			    	 type:'post',
			    	 dataType:'json',
			    	 cache:false,
			    	 success:function(result){
			    		 if(result.status){
			    			 alert("删除成功");
			    			 $(grid_selector).jqGrid().trigger("reloadGrid");
			    		 }else{
			    			 alert("删除失败");
			    		 }
			    	 },
			    	 error:function(XMLHttpRequest, textStatus, errorThrown){
			         }
			    	 
			    	 
			 
			  });
		});	
		
	}
	
	var lastsel;
	$.jgrid.defaults.styleUI = 'Bootstrap';

	$(window).on('resize.jqGrid', function () {
		$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
    })
	var parent_column = $(grid_selector).closest('[class*="col-"]');
	$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
		if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
			setTimeout(function() {
				$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
			}, 0);
		}
    })
	
    $(grid_selector).jqGrid({
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        url: "../../role/query",
        mtype: 'POST',//请求方式
        jsonReader: {
            root: "data.roleList", // 实际模型的人口
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
                   {name:'id',index:'id',label: "修改权限",width:"150px", sortable:false,hidden:false,editable:false,search:false,align:'center',formatter: dataFommater.showModal},
	               {name:'name',index:'name',label:"角色名称", width:"200px",sortable:false, editable:false,editrules:{required:true},hidden:false,search:false,align:'center'},
        ],

        beforeSelectRow: function (rowid, e) { 
	        var $myGrid = $(this),  
	            i = $.jgrid.getCellIndex($(e.target).closest('td')[0]),  
	            cm = $myGrid.jqGrid('getGridParam', 'colModel');  
	        return (cm[i].name === 'cb');  
	    },
	    onSelectAll:function(aRowids,status){
	    	publicUpdate.onSelectAll(aRowids,status,grid_selector);
	    },
        onSelectRow: function(id) {
        		if ($('#' + id).attr('aria-selected')=="true") {  
            		$(grid_selector).jqGrid('editRow', id, true);
            		$('#'+id+'_login_name').attr('readOnly',true);
            	}else{
            		$(grid_selector).jqGrid('restoreRow', id);
            	}
        },
        pager: pager_selector,
        viewrecords: true,
        hidegrid: false,
        caption: "角色列表",
        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
        editurl: "../../role/roleOper"
    });
	
	$(grid_selector).jqGrid('navGrid', pager_selector, {
		edit : false,
		add : false,
		del : false,
		search: false
	},{
		
		
	}, // edit options
	{
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
        buttonicon: "glyphicon-trash",
        onClickButton: function () {
        		deleteRole();
        }
    }).navButtonAdd(pager_selector, {
        caption: " ",
        buttonicon: "glyphicon-plus",
        onClickButton: function () {
        	addRole();
        }
    });
	
	 
	   
	
});

});