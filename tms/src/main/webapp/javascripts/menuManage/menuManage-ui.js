define(function(require, exports, module){
	var $=require("jquery");
	var publicJs=require("publicJs");
	var Handlebars = require("handlebars");
	var configure = require("../../javascripts/checkboxSelect");

	var fnList={
		
		 initData: function(style, _data, list) {
            var source = $(style).html();
            var template = Handlebars.compile(source);
            var context = {
                data: _data
            };
            var html = template(context);
            $(list).html(html);
		 	fnList.editFn();
			fnList.addFn();
			fnList.detailsFn();

        },
        editFn:function(){
			$("table .menu_modify").each(function(){
				$(this).click(function(){					
					$('#myModal_modify').modal('show');
						
					trId=$(this).attr("trid");
				})
			});
		},
		detailsFn:function(){
			$("table .menu_details").each(function(){
				$(this).click(function(){					
					$('#myModal_details').modal('show');
						
					trId=$(this).attr("trid");
				})
			});
		},

		addFn:function(){
			$("#add").click(function(event) {
				$("form").find().val("");
				$('#myModal_add').modal('show');
			});
		},
		


	}


	$(document).ready(function() {
	
	    configure.backMsg(function(id){
	    	  console.log(id);
	    })

	});
	exports.fnList=fnList;
});