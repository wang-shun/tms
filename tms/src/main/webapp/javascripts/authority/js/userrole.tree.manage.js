define(function (require, exports, module) {
	var $=require("jquery");
    require("sweetalert");
    exports.loadModalData = function(){
    	$("#usna").html($("#modalname").val());
    	
    	var id = $("#modalId").val(); 
     var data = "id="+id;
   	 $.ajax({
		 url:"../../user/userRoleOper",
		 data:data,
    	 type:'post',
    	 dataType:'json',
    	 cache:false,
    	 success:function(result){
    		 if(result.status){
    			var rlList = result.data;
    			var html = "";
    			for(var i = 0; i< rlList.length;i++){
    				var role = rlList[i];
    				if(i%3==0 || i == 0){
    					html += "<tr>";
   	 				}
    				
    				html += "<td style='text-align:left'>";
    				html += "<input name='role' type='checkbox' "+role.checked+" value="+role.id+" >"+role.name;  
    				html += "</td>";
    				
    				if((i+1)%3==0){
    					html += "</tr>";
    				}
    			}
    			
    			$("#usro").html(html);
    		 }else{
    		 }
    	 },
    	 error:function(XMLHttpRequest, textStatus, errorThrown){
         }
 
  });
   	 
 	$("#saverm").click(function(){
 		
		var role = "";
		var obj = $('input[name="role"]:checked');
		if(obj.size() == 0){
			alert("请选择权限");
			return;
		}
		obj.each(function(){
			role = role + $(this).val() + ",";
		});
				var userId = id;
				
				var data = "userId=" + userId +"&role=" + role;
				 $.ajax({
			    	 url:"../../user/userroleupdate?callback=?",
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
    


		

});
		