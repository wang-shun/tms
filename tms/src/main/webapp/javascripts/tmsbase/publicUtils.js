define(function (require, exports, module) {
	var $=require("jquery");
	require("sweetalert");
	var dataAction = {
			getDate: function(unixtime){
				var date =null;
				if(unixtime!=null){
					date=new Date(unixtime);
				}else{
					return "";
				}
				Y = date.getFullYear() + '-';
				M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
				D = (date.getDate() < 10 ? '0'+date.getDate(): date.getDate()) + ' ';
				return Y+M+D;
			},
			getTime:function(unixtime){
				var date =null;
				if(unixtime!=null){
					date=new Date(unixtime);
				}else{
					return "";
				}
				h = (date.getHours() < 10 ? '0'+date.getHours(): date.getHours())+ ':';
				m = (date.getMinutes() < 10 ? '0'+date.getMinutes(): date.getMinutes())+ ':';
				s = (date.getSeconds() < 10 ? '0'+date.getSeconds(): date.getSeconds());
				return h+m+s;
			},
			getLocalTime:function (unixtime){
				var date =null;
				if(unixtime!=null){
					date=new Date(unixtime);
				}else{
					return "";
				}
				Y = date.getFullYear() + '-';
				M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
				D = (date.getDate() < 10 ? '0'+date.getDate(): date.getDate()) + ' ';
				h = (date.getHours() < 10 ? '0'+date.getHours(): date.getHours())+ ':';
				m = (date.getMinutes() < 10 ? '0'+date.getMinutes(): date.getMinutes())+ ':';
				s = (date.getSeconds() < 10 ? '0'+date.getSeconds(): date.getSeconds());
				return Y+M+D+h+m+s;
			}
	};
	var urlAction = {
			getQueryString: function (name) {
		        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		        var r = window.location.search.substr(1).match(reg);  //获取url中"?"符后的字符串并正则匹配
		        var context = "";
		        if (r != null)
		            context = r[2];
		        reg = null;
		        r = null;
		        return context == null || context == "" || context == "undefined" ? "" : context;
		    }
	};
	
	

	exports.getDate=dataAction.getDate;
	exports.getTime=dataAction.getTime;
	exports.getLocalTime=dataAction.getLocalTime;
	exports.getQueryString=urlAction.getQueryString;
});
