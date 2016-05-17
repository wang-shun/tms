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
		
	function excel(){
		document.demo.action="exportExcel";
	    document.demo.submit();
	}
	
