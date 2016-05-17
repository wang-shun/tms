define(function(require, exports, module) {
    var $ = require("jquery");
    var execute = require("execute");
    var fnList=require("./index-ui").fnList;
	//var modalUI=require("../customplugins/modal-list-selection");
	
	var data={
			titlelist:["Domain","Price","Clicks","Update"],
			datalist:[
				{
					"Domain":"ace.com",
					"Price":"$45",
					"Clicks":"3,330",
					"Update":"Feb 12",
				},
				{
					"Domain":"ace.com",
					"Price":"$45",
					"Clicks":"3,330",
					"Update":"Feb 12",
				},
				{
					"Domain":"ace.com",
					"Price":"$45",
					"Clicks":"3,330",
					"Update":"Feb 12",
				},
				{
					"Domain":"ace.com",
					"Price":"$45",
					"Clicks":"3,330",
					"Update":"Feb 12",
				},
				{
					"Domain":"ace.com",
					"Price":"$45",
					"Clicks":"3,330",
					"Update":"Feb 12",
				}
			]
		};
	//modalUI.modal_select_Init(data,1,4);
	//modalUI.modalShow("modal-table");
	$(document).ready(function() {
		$("#pageFrame").css("min-height",$(window).height()-126);

		$("#logout").click(function(){
			$.ajax({
				url: "../../logout",
				data: data,
				type: 'post',
				dataType: 'json',
				cache: false,
				success: function (data) {
					window.location.href = "/shinho" + data.data.url;
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
//		           alert(errorThrown);
				}
			});
		});
		
		 $.ajax({
	    	 url:"../../controlSide",
	    	 type:'post',
	    	 dataType:'json',
	    	 cache:false,
	    	 success:function(result){
	    		 if(result.status){
	    			 if(result.data != null){
	    				 var length = result.data.length;
	    				 var html  = '';
	    				 if(length > 0){
	    					 for(var x = 0; x<length;x++){
	    						 var dat = result.data[x];
	    						 	
	    						 var list = dat.menuPageList;
	    							 html += '<li class="pLi">';
	    						 html += '<a href="#" class="dropdown-toggle">';
	    						 html += '<i class="'+dat.pclass+'"></i>';
	    						 html += ' <span class="menu-text">'+dat.name+'</span></a>';
	    						 
	    						 var menuhtml = "";
	    						 	 if(list != null && list.length > 0){
	    						 		
	    						 		 menuhtml += '<ul class="submenu">';
	    						 		 for(var y = 0;y < list.length; y++){
	    						 			var menu = list[y];
	    						 			menuhtml += ' <li class="cLi" targetSrc="'+menu.url+'"><a href="#" class="menu-text">'+menu.name+'</a></li>'
	    						 		 }
	    						 		menuhtml += '</ul>';
	    						 	 }
	    						  html += menuhtml;
	    			              
	    			              html += '</li>';
	    					 }
	    				 }
	    				 
	    				 $("#nav-list-id").html(html);
	    				 fnList.activeFn();
	    			 }
	    		 }else{
	    			 
	    		 }
	    	 },
	    	 error:function(XMLHttpRequest, textStatus, errorThrown){
	         }
	 
	  });
	});
});
