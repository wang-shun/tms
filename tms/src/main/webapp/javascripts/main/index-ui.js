define(function(require, exports, module){
	var $=require("jquery");
	//var public=require("public");
	var RightTit="";
	var fnList={
		activeFn:function(){
			$("#nav-list-id .pLi").each(function(index, el) {
				$(this).click(function(){
					$("#pageFrame").attr("src","");
					$("#nav-list-id .pLi").removeClass('activeLi');
					$("#nav-list-id .cLi").removeClass('activeLi');
					$(this).addClass('activeLi');
					var arrowIcon = $(this).find(".submenu");
					
					if(arrowIcon.length!=0){
						if($(this).find(".submenu").is(":hidden")){
							$(".submenu").hide();
							$(this).find(".submenu").show();
						}else{
							$(this).find(".submenu").hide();
						}
					}else{
						$(".submenu").hide();
					}

					var targetSrc=$(this).attr("targetSrc");
					if(targetSrc!=""){
						$("#pageFrame").attr("src",targetSrc);
						fnList.setIframeHeight("pageFrame");
					}
					var MdmName= $('<li class="active"></li>');
					RightTit = $('<li class="active"></li>');
					RightTit.html($(this).find(".menu-text").html());
					$(".breadcrumb").html(RightTit);
			
				})

			});
				$("#nav-list-id .cLi").each(function(index, el) {
				$(this).click(function(event){
					$("#nav-list-id .cLi").removeClass('activeLi');
					$("#nav-list-id .pLi").removeClass('activeLi');
					$(this).parent().parent(".pLi").removeClass('activeLi');
					$(this).addClass('activeLi');
					var  e = event || window.event;
					 if(e.stopPropagation) { //W3C阻止冒泡方法  
				        e.stopPropagation();  
				    } else {  
				        e.cancelBubble = true; //IE阻止冒泡方法  
				    }  
					
					var targetSrc=$(this).attr("targetSrc");
					if(targetSrc!=""){
						$("#pageFrame").attr("src",targetSrc);
					}
					var RightTitChild="";
					RightTitChild = $('<li class="active"></li>');
					RightTitChild.html($(this).find(".menu-text").html());
					
					$(".breadcrumb").html(RightTit).append(RightTitChild);
				})
			});
		},
		setIframeHeight:function(id){
			try{
			    var iframe = document.getElementById(id);
			    if(iframe.attachEvent){
			      iframe.attachEvent("onload", function(){
			        iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
			      });
			      return;
			    }else{
			      iframe.onload = function(){
			        //iframe.height = iframe.contentDocument.body.scrollHeight;
			         iframe.height =  iframe.contentWindow.document.documentElement.scrollHeight;
			      };
			      return;				 
			    }	 
			  }catch(e){
			    throw new Error('setIframeHeight Error');
			  }
		},
	}


	$(document).ready(function() {
//		fnList.activeFn();
		 var iframe = document.getElementById("pageFrame");
		iframe.height = iframe.contentWindow.document.documentElement.scrollHeight;

	});
	exports.fnList=fnList;
});