define(function(require, exports, module) {
	 var $=require("jquery");
	    require("sweetalert");
	    require("bootstrap");
	    var publicUpdate=require("publicUpdate");
	    var publicJs = require("publicJs");
	    var usrole = require("authority/js/userrole.tree.manage");
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


	function updateRole(id,name){
		$('#myModal').modal({
		    remote: "userRoleOper?id="+id+"&name="+name
		});
	}
	
    var dataFommater = {
	        showModal : function(cellvalue, options, rowdata){
	            return '<a style="cursor:pointer" onclick=updateUserRole("'+rowdata.id+'","'+rowdata.login_name+'")>修改角色</a>';
	        },
   };
    
	window.updateUserRole = function(id, name){
		$('#modalId').val(id);
		$('#modalname').val(name);
		$('#myModal').modal({remote: "userrole.html"});
	}
		
$(function(){
	 
	$("#addUser").click(function(){
		$('#myModal').modal({
		    remote: "userOper?action=add"
		});
	});
	
	$("#updateUser").click(function(){
		var user = $('input[name="userc"]:checked');
		var userid = user.attr("userid");
		if( userid == null || userid == ""){
			alert("请选择数据");
			return;
		}
		$('#myModal').modal({
		    remote: "userOper?id="+userid+"&action=update"
		});
	});
	$("#myModal").on("hidden.bs.modal", function() {
	    $(this).removeData("bs.modal");
	   
	});
	
	 $('#myModal').on('shown.bs.modal', function () {
		 usrole.loadModalData();
     });
	
	$("#delUser").click(function(){
		
		var user = $('input[name="userc"]:checked');
		var userid = user.attr("userid");
		if( userid == null || userid == ""){
			alert("请选择数据");
			return;
		}
		var data = "id="+userid;
		 $.ajax({
	    	 url:"userDel?callback=?",
	    	 data:data,
	    	 type:'post',
	    	 dataType:'json',
	    	 cache:false,
	    	 success:function(result){
	    		 if(result.status){
	    			 alert("删除成功");
	    			 user.parent().parent().remove();
	    		 }else{
	    			 alert("删除失败");
	    		 }
	    	 },
	    	 error:function(XMLHttpRequest, textStatus, errorThrown){
	         }
	 
	  });
		
		
	});
	
	$.jgrid.defaults.styleUI = 'Bootstrap';
	
	$(window).on('resize.jqGrid', function () {
		$(grid_selector).jqGrid( 'setGridWidth', $(".tms_body").width());
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
	    url: "../../user/query",
        mtype: 'POST',//请求方式
        jsonReader: {
        	root : "data.userList", // 实际模型的人口
        	total: "data.totalpages",
        	page: "data.currpage",
        	records: "data.totalrecords",
        	repeatitems: false
        },
        prmNames : {
        	page:"page", // 表示请求页码的参数名称
        	rows:"rows", // 表示请求行数的参数名称
        	sort: "sidx", // 表示用于排序的列名的参数名称
        	order: "sord", // 表示采用的排序方式的参数名称
        	search:"_search", // 表示是否是搜索请求的参数名称
        	nd:"nd", // 表示已经发送请求的次数的参数名称
        	id:"id", // 表示当在编辑数据模块中发送数据时，使用的id的名称
        	oper:"oper", // operation参数名称
        	editoper:"userUpdate", // 当在edit模式中提交数据时，操作的名称
        	addoper:"userAdd", // 当在add模式中提交数据时，操作的名称
        	deloper:"userDel", // 当在delete模式中提交数据时，操作的名称
        	subgridid:"id", // 当点击以载入数据到子表时，传递的数据名称
        	npage: null,
        	totalrows:"totalrows" // 表示需从Server得到总共多少行数据的参数名称，参见jqGrid选项中的rowTotal
        	},
	    height: '350px',
	    autowidth: true,
	    shrinkToFit: false,
	    autoScroll: true,
	    multiselect:true,
	    multiboxonly:false,
	    rowNum:10,
	    rowList: [10, 20, 30],
	    colModel: [
	               {name:'id',index:'id', label:"修改用户角色",width:"100px", sortable:false,hidden:false,editable:false,search:false,align:'center',formatter: dataFommater.showModal},
	               {name:'login_name',index:'login_name',label:"登录名", width:"150px",sortable:false, editable:true,hidden:false,editrules:{required:true},search:false,align:'center'},
	               {name:'password',index:'password',label:"密码", width:"150px",sortable:false, editable:true,hidden:false,search:false,align:'center',formatter:passFormatter},
	               {name:'vsername',index:'vsername',label:"用户名", width:"150px",sortable:false, editable:true,align:'center'},
	               {name:'mobile',index:'mobile',label:"电话号码", width:"150px",sortable:false, editrules:{custom: true,custom_func: teleCheck},editable:true,search:false,align:'center'},
	               {name:'email',index:'email', width:"250px",label:"邮箱",sortable:false, editable:true,editrules:{custom: true,custom_func: emailCheck},search:false,align:'center',},
	    ],
	    
	    /* loadComplete: function() {  
	     },*/
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
        caption: "用户列表",
        //formEdit:true,//弹出一个新的编辑窗口进行编辑和新增
        editurl: "../../user/userOper"
	});
	

	$(grid_selector).jqGrid('navGrid', pager_selector, {
		edit : false,
		add : true,
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
		caption : " ",
		buttonicon:"glyphicon-edit",
		onClickButton : function() {
			update();
		},
		position : "first"
	}).navButtonAdd(pager_selector, {
        caption: " ",
        buttonicon: "glyphicon-trash",
        title:'删除所选记录',
        onClickButton: function () {
        	publicUpdate.cancelOrConfirmData(grid_selector, "确认删除所选记录？","../../user/userOper","userDel","删除成功");
        }
    });
	
	function update(){
		publicUpdate.updateData(grid_selector);
	}
	function add(){
		var ids=$(grid_selector).jqGrid('getGridParam','selarrrow');
		ids.forEach(function(id){
			if(!valiform($('#'+id+'_login_name').val(),$('#'+id+'_password').val(),$('#'+id+'_password').val(),$('#'+id+'_vsername').val(),$('#'+id+'_mobile').val(),$('#'+id+'_email').val(),"add")){
				return;
			}else{
				publicUpdate.updateData(grid_selector);
			}
		});
	}
	function passFormatter(){
		return "******";
	}
	
	function valiform(loginname,Password,Passwordrepeat,Name,Mobile,Email,action){
		 swal({
             title: "编辑提示 <small>详情:</small>",
             text: "登录名已经存在",
             html: true
         });
		var data = "action="+action;
		
		if(isempty(loginname)){
			if(action == "add"){
				var da = "loginName="+loginname;
				var re = true;
				 $.ajax({
			    	 url:"../../user/userIsExists?callback=?",
			    	 data:da,
			    	 type:'post',
			    	 dataType:'json',
			    	 cache:false,
			    	 async:false,
			    	 success:function(result){
			    		 if(result.status){
//			    			 alert("登录名已经存在");	
			    			 swal({
			                        title: "编辑提示 <small>详情:</small>",
			                        text: "登录名已经存在",
			                        html: true
			                    });
			    			 re =  false;
			    		 }
			    	 },
			    	 error:function(XMLHttpRequest, textStatus, errorThrown){
			         }
			 
			  });
				 if(!re){
					 return false;
				 }
			}
			data += "&loginname=" + loginname;
		}else{
			alert("登录名不能为空");
			return false;
		}
		
		if(isempty(Password)){
			if(isempty(Passwordrepeat)){
				if(Password != Passwordrepeat){
					alert("两次输入密码不一致");
					return false;
				}
			}else{
				alert("重复密码不能为空");
				return false;
			}
			data += "&Password=" + Password;
		}else{
			alert("密码不能为空");
			return false;
		}
		
		if(!isempty(Name)){
			alert("用户名不能为空");
			return false;
		}else{
			data += "&Name=" + Name;
		}
		
		
		if(isempty(Mobile)){
			if(!valiTele(Mobile)){
				alert("手机号码错误");
				return false;
			}
			data += "&Mobile=" + Mobile;
		}
		
		 var pattern = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
		if(isempty(Email)){
			if(!pattern.test(Email)){
				alert("邮箱格式错误");
				return false;
			}
			data += "&Email=" + Email;
		}
		return data;
	}
	function isempty(obj){
		if(obj == null || obj == ""){
			return false;
		}
		return true;
	}
	//验证手机号
	function valiTele(tele) {
		if (tele.length == 11 && /^0?1[3|4|5|7|8][0-9]\d{8}$/.test(tele)) {
			return true;
		}
		return false;
	}
	
	function emailCheck(value, colname){
		var pattern = /^([\.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
		if(isempty(value)){
			if(!pattern.test(value)){
				return [false, "邮箱格式错误"];
			}
		}
		return [true, ""];
	}
	
	function teleCheck(value, colname){
		if(isempty(value)){
			if(valiTele(value)){
				return [true, ""];
			}
		}else{
			return [true, ""];
		}
		return [false, "手机号格式错误"];
	}
	
	
});

});