define(function(require, exports, module) {
	var $ = require("jquery");
	var execute = require("execute");
	var publicJs = require("publicJs");
	var wx = require("./jweixin-1.0.0.js");
	var zhifuAction = {
		
		request_pay_topay : function(orderId,ok,cancel) {
			var request = new Object();
			var body = new Object();
			body = "orderId=" + orderId;
			request.body = body;
			request.async = true;
			request.ok=ok;
			request.cancel=cancel;
			request.url = "pay/topay";
			request.handle = this.handle_pay_topay;
			execute.executePOST(request);
		},
		handle_pay_topay : function(data) {
			if (data.success) {
				onBridgeReady(data.data,this.ok,this.cancel);

			} else {
				alert(data.errorMessage);
			}
		}

	};

	var data1 = {
		appId : "wxb26a644daef3e61e",
		timestamp : "1306007401",
		nonceStr : "GEGksfLp3Wu7wGGF",
		signature : "05826E418A9AC9FF551ED9A72D5676ED",
		paySign : "ED1EE21CF2024B666E5511DEC6CED2F9",

	};
	function onBridgeReady(data,_ok,_cancel) {
		wx.config({
			debug : false,
			appId : data.appId, 
			timestamp : data.timeStamp,
			nonceStr : data.nonceStr, 
			signature : data.signature,
			jsApiList : [ 'chooseWXPay' ]
		// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
		});
		wx.ready(function() {
			
			wx.chooseWXPay({
				timestamp : data.timeStamp, 
				nonceStr : data.nonceStr, 
				"package":"prepay_id="+data.prepayId, 
				signType : "MD5",
				paySign : data.paySign, 
				complete : function(res) {
					//errMsg:chooseWXPay:ok
					//errMsg:chooseWXPay:cancel
					if (res.errMsg == "chooseWXPay:cancel") {
						alert("取消支付！");
						_cancel();
					}else if(res.errMsg == "chooseWXPay:ok"){
						alert("支付成功！");
						_ok();
					}else{
						alert("chooseWXPay returnMsg:" + res.errMsg);
					}
					
					
				}

			});
		});
	}
	exports.weinxinPay=zhifuAction;	
});