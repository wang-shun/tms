define(function (require, exports, module) {
	var $=require("jquery");
	require("sweetalert");

	function validatePhoneNum(value, colname){
		alert(arguments.length);
		var _emp = /^\s*|\s*$/g;
		text = value.replace(_emp, "");
		var _d = /^1[3578][01379]\d{8}$/g;//电信手机号码
		var _l = /^1[34578][01256]\d{8}$/g;//联通手机号码
		var _y = /^(134[012345678]\d{7}|1[34578][012356789]\d{8})$/g;//移动手机号码
		if (!_d.test(value) && !_l.test(value) && !_y.test(value)) {
			if(arguments.length == 1) {
				return false;
			}else{
				var message = "不是完整的11位手机号或者手机号不正确";
				alert(message); 
				return [false,message]; 
			}
		 } else{
			 if(arguments.length == 1) {
					return true;
				}else{
					return[true,""];
				}
		 }
	}
	function validateCard(value, colname){
		var flag=validateCardValue(value);
		if(!flag){ 
			var message = "身份证输入不合法";
			alert(message); 
			return [false,message]; 
		} return[true,""]
	}
	function validateCardValue(value){
		var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
		if(!reg.test(value)){ 
			return false;
		}else{ 
			return true;
		}
	}
	

	exports.validatePhoneNum=validatePhoneNum;
	exports.validateCardValue=validateCardValue;
	exports.validateCard=validateCard;
});
