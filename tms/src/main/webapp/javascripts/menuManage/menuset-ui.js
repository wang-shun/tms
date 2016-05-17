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
			fnList.bindingFn();
		

        },
        editFn:function(){
			
				$("#edit").click(function(){					
					$('#myModal').modal('show');
					
				})
		
		},
		

		addFn:function(){
			$("#add").click(function(event) {
				
				$('#myModal').modal('show');
				
				
			});
		},
		bindingFn:function(){
			$("#binding").click(function(event) {
				$('#myModal_binding').modal('show');
			});
		}
		
		


	}


	$(document).ready(function() {
	
	    configure.backMsg(function(id){
	    	  console.log(id);
	    })

	});
	exports.fnList=fnList;
});