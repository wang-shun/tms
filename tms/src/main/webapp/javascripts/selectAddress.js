define(function(require, exports, module){
	var $=require("jquery");
	var execute = require("execute");
	var publicJs=require("publicJs");
	var Handlebars = require("handlebars");
	
	

	var tempfn;
	function baindlist()
	{
		
		$(".area-name").click(function(){
					var pid=$(this).attr("data");
					if(shengshiStr!="")
					{
						shengshiStr=shengshiStr+"-"+$(this).html();
					}else{
						shengshiStr=$(this).html();
					}
					
					var tempdata=[];
					for(var i=0;i<data.length;i++)
					{
						if(pid==data[i].pid)
						{
							
							tempdata.push(data[i]);
						}
						
					}
				 if(tempdata.length==0)
				 {
					 //shengshiStr=shengshiStr+"-"+$(this).html();
					 $(domWrap).html(shengshiStr);
					 $(domWrap).attr("data",pid);
					  if(typeof(tempfn)=="function")
					 {
						tempfn.apply(this,[pid]); 
					 }
					 publicJs.closeShield();
					
				  }
				 newAddressEvent.setListData(tempdata);
			});
	}
	var newAddressEvent = {
		//初始化探出框
		initAlert: function (_data) {
			var detailHTML = publicJs.buildTemplate("area-template", "");

			detailHTML=detailHTML.replace(/&lt;/g, "<").replace(/&gt;/g, ">");
			publicJs.createShield(detailHTML,baindlist,1,"#list-template", _data, "#area-list");
		},
		//区域列表赋值
		setListData: function(_data) {    
	        var source = $("#list-template").html();
	        var template = Handlebars.compile(source);
	        var context = {
	            data: _data
	        };
	        var html = template(context);
	        $("#area-list").html(html);
			baindlist();
	    }
	};
	var newaddrAction={
			request_basegeo_list:function(){
						var request=new Object();
						var body=new Object();
						request.body=body;
						request.type="GET"
						request.url="basegeo/list";
						request.handle=this.handle_basegeo_list;
						execute.execute(request);
				},
			handle_basegeo_list:function(_data){
						if(_data.success)
						{
							data=[];
							for(var i=0;i<_data.data.length;i++)
							{
								var obj=new Object();
								obj.id=_data.data[i].geo_id;
								obj.pid=_data.data[i].parent_id;
								obj.name=_data.data[i].geo_name;
								data.push(obj);
							}
							$(".body-wrap").show();
							
						}else{
							alert(_data.errorMessage);	
						}
			},
	}

	var domWrap=null;
	function targetClick(domClick,domWrap1,fn){
		tempfn=fn;
		domWrap=domWrap1;
		$(domClick).click(function(){
			var tempdata=[];
			shengshiStr="";
			for(var i=0;i<data.length;i++)
			{
				if(null==data[i].pid)
				{
					tempdata.push(data[i]);
				}
				
			}
			
			newAddressEvent.initAlert(tempdata);
			
		});
		
		//fn();
	}
	

	$(document).ready(function() {
		newaddrAction.request_basegeo_list();
	});
	exports.targetClick=targetClick;
});