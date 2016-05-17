define(function(require, exports, module){
	var $=require("jquery");
	var execute = require("execute");
	var areaSelection = require("areaSelection");
    var publicValidate = require("publicValidate");
    var publicJs = require("publicJs");
	require("datepicker");
	require("bootstrapSuggest");
	require("sweetalert");

	var centerAction={
			request_waybill_info:function(body){
					var request = new Object();
					request.body=body;
					request.type="POST";
					request.url="../waybill/saveOrUpdate";
					request.handle = this.handle_waybill_info;
					execute.execute(request);		
			},
			handle_waybill_info:function(_data){
				if(_data.success)
				{
					$("#id").val(_data.data.id);
					$("#no").val(_data.data.no);
					$("#no").text(_data.data.no);
					$(top.window.document.body).animate({scrollTop:0}, 400);
					$('#ticketNo').focus();//设置焦点，避免点击alert确认后回到页底
					swal("", "保存成功!", "success");
				}else{
					alert(_data.errorMessage);
				}
			},
			loadSenderInfo : function(dId){
				$.ajax({
					async: false,
					url: "../../sender/loadSenderInfo",
					type : 'POST',// 请求方式
					dataType:'json',
					data : {'id' : dId},
					success : function(data){
						var sender = data.data.sender;
						$("#dId").val(sender.id);
						$("#dContact").val(sender.contact);
						$("#dTel").val(sender.tel);
						$("#dPoint").val(sender.point);
						$("#dProvince").val(sender.province);
						$("#dCity").val(sender.city);
						$("#dCounty").val(sender.county);
						$("#dArea").val(sender.area);
						$("#dAddress").val(sender.address);
					}
				});
			},
			loadReceiverInfo : function(rId) {
				$.ajax({
					async: false,
					url: "../../receiver/loadReceiverInfo",
					type : 'POST',// 请求方式
					dataType:'json',
					data : {'id' : rId},
					success : function(data){
						var receiver = data.data.receiver;
						$("#rId").val(receiver.id);
						$("#rContact").val(receiver.contact);
						$("#rTel").val(receiver.tel);
						$("#rPoint").val(receiver.point);
						$("#rProvince").val(receiver.province);
						$("#rCity").val(receiver.city);
						$("#rCounty").val(receiver.county);
						$("#rArea").val(receiver.area);
						$("#rAddress").val(receiver.address);
					}
				});
			}
	};
	
	$(document).ready(function() {
		$("#receiveAt").val(getDate(null));
		$("#deliveryDate").val(getDate(null));
		var senderList = {value : []};
		$.ajax({
			async: false,
			url: "../../sender/senderOptions",
			type : 'POST',// 请求方式
			dataType:'json',
			success : function(data){
				(data.data.senderList).forEach(function(sender){
					senderList.value.push({
						"发货方": sender["发货方"],
						"发货人": sender["发货人"],
						"发货方ID": sender.ID
					});
				});
			}
		});

		var receiverList = {value : []};
		$.ajax({
			async: false,
			url: "../../receiver/receiverOptions",
			type : 'POST',// 请求方式
			dataType:'json',
			success : function(data){
				(data.data.receiverList).forEach(function(receiver){
					receiverList.value.push({
						"收货方": receiver["收货方"],
						"收货人": receiver["收货人"],
						"收货方ID": receiver.ID
					});
				});
			}
		});

		$("#dName").bsSuggest({
			showHeader: true,
			indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
			indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
			data: senderList
		}).on('onSetSelectValue', function (e, keyword, data) {
			/*console.log('onSetSelectValue: ', keyword, data);*/
			var dId = data["发货方ID"];
			centerAction.loadSenderInfo(dId);
		});

		$("#rName").bsSuggest({
			showHeader: true,
			indexId: 0,  //data.value 的第几个数据，作为input输入框的内容
			indexKey: 0, //data.value 的第几个数据，作为input输入框的内容",
			data: receiverList
		}).on('onSetSelectValue', function (e, keyword, data) {
			/*console.log('onSetSelectValue: ', keyword, data);*/
			var rId = data["收货方ID"];
			centerAction.loadReceiverInfo(rId);
		}).on('onUnsetSelectValue', function () {
			$("#rId").val("");
		});

		$("#rName").change(function(){
			if($("#rName").val().trim() == ""){
				$("#rId").val("");
			}
		});

		$("#waybillInfo").focusin(function(event){
			if("INPUT" == event.target.tagName.toUpperCase()||
				"TEXTAREA" == event.target.tagName.toUpperCase()){
				event.target.select();
			}
		});

		$("#reset").click(function(){
			$(top.window.document.body).animate({scrollTop:0}, 400);
			$('#ticketNo').focus();//设置焦点，避免点击alert确认后回到页底
			$('#id').val("");
			$('#no').text("（待生成）");
			$('#ticketNo').val("");
			$('#orderNo').val("");
			$('#recipient').val("");
			$("#receiveAt").val(getDate(null));
			$("#deliveryDate").val(getDate(null));
			$('#remark').val("");
			$('#dName').val("");
			$('#dId').val("");
			$('#dContact').val("");
			$('#dTel').val("");
			$('#dPoint').val("");
			$('#dAddress').val("");
			$('#dCounty').val("");
			$('#rId').val("");
			$('#rTel').val("");
			$('#rName').val("");
			$('#rContact').val("");
			$('#rAddress').val("");
			$('#rArea').val("东区");
			$('#rCounty').val("");
			$('#detailName').val("");
			$('#detailWeight').val("");
			$('#feeType').val("1");
			$('#payType').val("1");
			$('#transportFee').val("");
			$('#goodMoney').val("");
			//$('#detailSize').val("");
			$('#detailHight').val("");
			$('#detailLong').val("");
			$('#detailWidth').val("");
			swal("","运单信息已重置","success");
		});

		$("#saveOrUpdate").click(function(){
			if(!checkData()){
				return;
			}
			var body = new Object();
			body.id = $('#id').val();
			body.no = $('#no').val();
			body.ticketNo = $('#ticketNo').val();
			body.source = $('#source').val();
			body.orderNo = $('#orderNo').val();
			body.recipient = $('#recipient').val();
			body.deliveryType = $('#deliveryType').val();
			body.receiveAt = new Date($('#receiveAt').val()+" 00:00:00");
			body.deliveryDate = new Date($('#deliveryDate').val()+" 00:00:00");
			body.deliveryTime = $('#deliveryTime').val();
			body.remark = $('#remark').val();
			body.dName = $('#dName').val();
			body.dId = $('#dId').val();
			body.dArea = $('#dArea').val();
			body.dContact = $('#dContact').val();
			body.dTel = $('#dTel').val();
			body.dPoint = $('#dPoint').val();
			body.dAddress = $('#dAddress').val();
			body.dCity = $('#dCity').val();
			body.dArea = $('#dArea').val();
			body.dCounty = $('#dCounty').val();
			body.rId = $('#rId').val();
			body.rTel = $('#rTel').val();
			body.rName = $('#rName').val();
			body.rArea = $('#rArea').val();
			body.rContact = $('#rContact').val();
			body.rAddress = $('#rAddress').val();
			body.rCity = $('#rCity').val();
			body.rCounty = $('#rCounty').val();
			body.detailName = $('#detailName').val();
			body.detailWeight = $('#detailWeight').val();
			if($('#detailLong').val()!="" 
					&& $('#detailWidth').val()!="" 
					&& $('#detailHight').val()!=""){
			body.detailSizeLwh = $('#detailLong').val()+"*"+$('#detailWidth').val()+"*"+$('#detailHight').val();
			}
			body.feeType = $('#feeType').val();
			body.payType = $('#payType').val();
			body.transportFee = $('#transportFee').val();
			body.goodMoney = $('#goodMoney').val();
			body.detailType = $('#detailType').val();
			
			centerAction.request_waybill_info(body);
		});

		$("#receiveAt").datepicker({
			startView: 0,
			maxViewMode: 0,
			minViewMode:0,
			autoclose : true,
			format : 'yyyy-mm-dd'
		});
		$("#deliveryDate").datepicker({
			startView: 0,
			maxViewMode: 0,
			minViewMode:0,
			autoclose : true,
			format : 'yyyy-mm-dd'
		});

		areaSelection.loadProvinces(["rProvince","dProvince"]);
		areaSelection.loadCities("上海", "rCity", "rCounty");
		areaSelection.loadCities("上海", "dCity", "dCounty");
	});
	
	function getDate(unixtime){
		var date = new Date();
		if(unixtime!=null){
			date= new Date(unixtime);
		}
		Y = date.getFullYear() + '-';
		M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
		D = date.getDate() + ' ';
		return Y+M+D;
	}
	//校验数据
	function checkData(){
		//基本信息校验
		var ticketNo = $('#ticketNo').val();
		if(!ticketNo){
			$('#ticketNo').focus();
			alert("请填写面单号。");
			return false;
		}
		
		var source = $('#source').val();
		if(!source){
			$('#source').focus();
			alert("请选择订单来源。");
			return false;
		}
		var deliveryType = $('#deliveryType').val();
		if(!deliveryType){
			$('#deliveryType').focus();
			alert("请选择运输方式。");
			return false;
		}
		var recipient = $('#recipient').val();
		if(!recipient){
			$('#recipient').focus();
			alert("请填写揽件人。");
			return false;
		}
		var receiveAt = $('#receiveAt').val();
		if(!receiveAt){
			$('#receiveAt').focus();
			alert("请填写揽件日期。");
			return false;
		}
		var deliveryDate = $('#deliveryDate').val();
		if(!deliveryDate){
			$('#deliveryDate').focus();
			alert("请填写配送日期。");
			return false;
		}
		var deliveryTime = $('#deliveryTime').val();
		if(!deliveryTime){
			$('#deliveryTime').focus();
			alert("请选择配送时间段。");
			return false;
		}
		var remark = $('#remark').val();
		if(remark && publicJs.getByteLen(remark) > 200){
			$('#remark').focus();
			alert("备注超长，不允许超过200字节或100个汉字");
            return false;
		}

		//发货方信息校验
		var dName = $('#dName').val();
		if(!dName){
			$('#dName').focus();
			alert("请填写发货方。");
			return false;
		}
		var dContact = $('#dContact').val();
		if(!dContact){
			$('#dContact').focus();
			alert("请填写发货人。");
			return false;
		}
		var dTel = $('#dTel').val();
		if(!dTel){
			$('#dTel').focus();
			alert("请填写发货人电话。");
			return false;
		}else if(!publicValidate.validatePhoneNum(dTel)){
			alert("发货方手机号不正确"); 
			$('#rTel').focus();
			return false;
		}
		var dPoint = $('#dPoint').val();
		if(!dPoint){
			$('#dPoint').focus();
			alert("请填写发货点。");
			return false;
		}
		var dProvince = $('#dProvince').val();
		if(!dProvince){
			$('#dProvince').focus();
			alert("请选择发货方省份。");
			return false;
		}
		var dCity = $('#dCity').val();
		if(!dCity){
			$('#dCity').focus();
			alert("请选择发货方城市。");
			return false;
		}
		var dCounty = $('#dCounty').val();
		if(!dCounty){
			$('#dCounty').focus();
			alert("请选择发货方区/县。");
			return false;
		}
		var dArea = $('#dArea').val();
		if(!dArea){
			$('#dArea').focus();
			alert("请选择发货方城区。");
			return false;
		}
		var dAddress = $('#dAddress').val();
		if(!dAddress){
			$('#dAddress').focus();
			alert("请填写发货地址。");
			return false;
		}else if(dAddress && publicJs.getByteLen(dAddress) > 200){
			$('#dAddress').focus();
			alert("发货地址超长，不允许超过200字节或100个汉字");
            return false;
		}

		//收货方信息校验
		var rName = $('#rName').val();
		if(!rName){
			$('#rName').focus();
			alert("请填写收货方。");
			return false;
		}
		var rContact = $('#rContact').val();
		if(!rContact){
			$('#rContact').focus();
			alert("请填写收货人。");
			return false;
		}
		var rTel = $('#rTel').val();
		if(!rTel){
			$('#rTel').focus();
			alert("请填写收货人电话。");
			return false;
		}else if(!publicValidate.validatePhoneNum(rTel)){
			alert("收货方手机号不正确"); 
			$('#rTel').focus();
			return false;
		}
		var rProvince = $('#rProvince').val();
		if(!rProvince){
			$('#rProvince').focus();
			alert("请选择收货方省份。");
			return false;
		}
		var rCity = $('#rCity').val();
		if(!rCity){
			$('#rCity').focus();
			alert("请选择收货方城市。");
			return false;
		}
		var rCounty = $('#rCounty').val();
		if(!rCounty){
			$('#rCounty').focus();
			alert("请选择收货方区/县。");
			return false;
		}
		var rArea = $('#rArea').val();
		if(!rArea){
			$('#rArea').focus();
			alert("请填写收货方城区。");
			return false;
		}
		var rAddress = $('#rAddress').val();
		if(!rAddress){
			$('#rAddress').focus();
			alert("请填写收货地址。");
			return false;
		}else if(rAddress && publicJs.getByteLen(rAddress) > 200){
			$('#rAddress').focus();
			alert("收货地址超长，不允许超过200字节或100个汉字");
            return false;
		}

		//运单明细校验
		var detailType = $('#detailType').val();
		if(!detailType){
			$('#detailType').focus();
			alert("请选择物品种类。");
			return false;
		}
		var detailName = $('#detailName').val();
		if(!detailName){
			$('#detailName').focus();
			alert("请填写物品名称。");
			return false;
		}
		var detailWeight = $('#detailWeight').val();
		if(!detailWeight || isNaN(detailWeight)){
			$('#detailWeight').focus();
			alert("请填写正确的物品重量。");
			return false;
		}
		var detailSize = $('#detailSize').val();
		if(detailSize && isNaN(detailSize)){
			$('#detailSize').focus();
			alert("请填写正确的物品体积。");
			return false;
		}
		var detailLong = $('#detailLong').val();
		var detailWidth = $('#detailWidth').val();
		var detailHight = $('#detailHight').val();
		if(detailLong || detailWidth || detailHight){
			if(!detailLong || isNaN(detailLong)){
				$('#detailLong').focus();
				alert("请填写正确的物品长度。");
				return false;
			}
			if(!detailWidth || isNaN(detailWidth)){
				$('#detailWidth').focus();
				alert("请填写正确的物品宽度。");
				return false;
			}
			if(!detailHight || isNaN(detailHight)){
				$('#detailHight').focus();
				alert("请填写正确的物品高度。");
				return false;
			}
		}


		//计费方式校验
		var transportFee = $('#transportFee').val();
		if(isNaN(transportFee)){
			$('#transportFee').focus();
			alert("运单费用请填数字。");
			return false;
		}
		var goodMoney = $('#goodMoney').val();
		if(isNaN(goodMoney)){
			$('#goodMoney').focus();
			alert("代收货款请填数字。");
			return false;
		}
		/*if(transportFee && goodMoney && transportFee<goodMoney){
			$('#goodMoney').focus();
			alert("代收货款不能大于运单费用。");
			return false;
		}*/
		
		return true;
	}
	
});