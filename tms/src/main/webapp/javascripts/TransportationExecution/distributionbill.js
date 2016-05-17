define(function (require, exports, module) {
    var $ = require("jquery");
    exports.loadModalData = function() {
        $.ajax({
            async: false,
            url: "../../deliveryBill/queryDetail",
            type: 'POST',// 请求方式
            dataType: 'json',
            data: {"id": $("#modalId").val()},
            success: function (_data) {
                var deliveryBill = _data.data.deliveryBill;
                reflshData(deliveryBill);
                
                var  waybillList = _data.data.waybillList;
                if(waybillList != null){
                	reflshwayData(waybillList);
                }
            }
        });
    };

    function reflshData(deliveryBill) {
    	$('#deliveryBillNoDetl').html(deliveryBill.no);
        $('#createdAtDetl').html(formatDate(new Date()));
        $('#vNoDetl').html(deliveryBill.vNo);
        $('#dNameDetl').html(deliveryBill.dName);
        $('#deliveryOriginDetl').html(deliveryBill.deliveryOrigin);
        $('#deliveryTermDetl').html(deliveryBill.deliveryTerm);
        $('#billqr').attr("src","../../qrcode?qrinfo="+deliveryBill.no);
    }

    function formatDate(date) {
        if (date instanceof Date) {
            return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" +
                date.getDate() + " " + date.getHours() + ":" + addZero(date.getMinutes().toString(), 2);
        }
    }

    function addZero(str, length) {
        return new Array(length - str.length + 1).join("0") + str;
    }
    
    function reflshwayData(waybillList){
    	var i = 0;
    	waybillList.forEach(function(waybill){
    		$("#bill").append("<tr><td>"
                + i++ +"</td><td>"
                + waybill.no +"</td><td>"
                + waybill.dArea +"</td><td>"
                + formatDate(new Date(waybill.deliveryDate)) +"</td><td>"
                + waybill.remark +"</td></tr>");
    		
    	});
    }
});