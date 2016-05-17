define(function (require, exports, module) {
	var $=require("jquery");
    require("sweetalert");
    require("jquery.ztree.core-3.5");
    
    exports.loadModalData = function(){
	  
    	action = $('#modalAction').val();
		if(action == "add"){
			$("#myModalLabel").html("增加权限");
		}else{
			$("#myModalLabel").html("修改权限");
			$("#roleName").val($("#modalname").val());
		}
		
		var data = "action=" + action;
		if(action == "update"){
			var roleId = $("#modalId").val();
			data = data + "&roleId=" + roleId;
		}
		
		 $.ajax({
			 url:"../../role/roleMenu",
			 data:data,
	    	 type:'post',
	    	 dataType:'json',
	    	 cache:false,
	    	 success:function(result){
	    		 if(result.status){
	    			var zNodes = result.data;
	    			zNodes = eval("(" + zNodes + ")");
	    			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	    		 }else{
	    		 }
	    	 },
	    	 error:function(XMLHttpRequest, textStatus, errorThrown){
	         }
	 
	  });
		 
		 $("#saverm").click(function(){
				
				var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
				checkNotes = zTree.getCheckedNodes(true);
				
				if(checkNotes.length <= 0){
					alert("请选择菜单");
					return;
				}
				
				var ns = "";
				for(var n = 0; n<checkNotes.length; n++){
					ns += checkNotes[n].id + ",";
				}
				var rolename = $("#roleName").val();
				if(rolename == ""){
					alert("角色名称不能为空");
					return;
				}
				var data = "ns=" + ns +"&action=" + action+"&rolename="+rolename;
				if(action == "update"){
					var roleId = $("#modalId").val();
					
					data = data + "&roleId=" + roleId;
				}
				 $.ajax({
			    	 url:"../../role/roleupdate",
			    	 data:data,
			    	 type:'post',
			    	 dataType:'json',
			    	 cache:false,
			    	 success:function(result){
			    		 if(result.status){
			    			 alert("保存成功");
			    			 $("#table_list_1").trigger("reloadGrid");
			    			 $('#myModal').modal('hide');
			    		 }else{
			    			 alert("保存失败");
			    		 }
			    	 },
			    	 error:function(XMLHttpRequest, textStatus, errorThrown){
			         }
			 
			  });
			});
			
    	
    	
    };
    
    var setting = {
			view: {
				selectedMulti: false
			},
			check: {
				enable: true
			},
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
			}
		};
	var zNodes;
	var action;
});	