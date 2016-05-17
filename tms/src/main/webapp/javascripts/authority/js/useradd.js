	function valiform(loginname,Password,Passwordrepeat,Name,Mobile,Email,action){
		var data = "action="+action;
		
		if(isempty(loginname)){
			if(action == "add"){
				var da = "loginName="+loginname;
				var re = true;
				 $.ajax({
			    	 url:"userIsExists?callback=?",
			    	 data:da,
			    	 type:'post',
			    	 dataType:'json',
			    	 cache:false,
			    	 async:false,
			    	 success:function(result){
			    		 if(result.status){
			    			 alert("登录名已经存在");	
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
	$(function(){
		

		
		$("[data-mask]").inputmask();
		
		$("#saverm").click(function(){
			
			var loginname = $("#inputloginname").val();
			var Password = $("#inputPassword3").val();
			var Passwordrepeat = $("#inputPasswordrepeat").val();
			var Name = $("#inputName").val();
			var Mobile = $("#inputMobile").val();
			var Email = $("#inputEmail3").val();
			Mobile = Mobile.replace(/-/g,"");
			
			var action = $("#action").val();
			
			var data = valiform(loginname,Password,Passwordrepeat,Name,Mobile,Email,action);
			
			if(!data){
				return;
			}
			
			if(action == "update"){
				var userId = $("#userId").val();
				data = data + "&id="+userId;
			}
			 $.ajax({
		    	 url:"userAddOrUpdate?callback=?",
		    	 data:data,
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    			 alert("保存成功");
		    			 aa();
		    		 }else{
		    			 alert("保存失败");
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
		         }
		 
		  });
		});
		
		
		
	});
	
	
	
		
