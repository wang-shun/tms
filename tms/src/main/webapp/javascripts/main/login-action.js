define(function(require, exports, module) {
    var $ = require("jquery");
	require("plugins/base/jquery.backstretch.min.js");

    $(function(){

		$.backstretch("../../images/backgrounds/login_bg2.jpg");

    	$("#sign").click(function(){
    	
    		$("#sign").attr("disabled","disabled");
    		$("#sign_mess").css("visibility","hidden");
    		
    		var name = $("#name").val();
    		var pass = $("#pass").val();
    		if(name == null || name == ""){
    			$("#sign_mess").text("用户名不能为空");
    			$("#sign_mess").css("visibility","visible");
    			return;
    		}
    		if(pass == null || pass == ""){
    			$("#sign_mess").text("密码不能为空");
    			$("#sign_mess").css("visibility","visible");
    			return;
    		}
    		var data = "name="+name+"&pass="+pass;
    		$("#sign").text("登录中...");
    			$.ajax({
		    	 url:"../../authLogin",
		    	 data:data,
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    		 	window.location.href="index.html";
		    		 	
		    		 }else{
		    		 	 $("#sign").removeAttr("disabled");
		    		 	 $("#sign").text("登录");
		    			 $("#sign_mess").text(result.data);
    					 $("#sign_mess").css("visibility","visible");
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
//		           alert(errorThrown);
		         }
		 
		  });
    		
    	});
    	
    });
    
    $(document).keypress(function(e) { 
   	 if (e.which == 13) {
   		 $("#sign").click(); 
   	 } 
    }); 
});
