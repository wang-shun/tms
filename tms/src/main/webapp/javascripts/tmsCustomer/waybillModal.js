define(function (require, exports, module) {
    var $ = require("jquery");
	var publicUtils = require("publicUtils");
    exports.loadModalData = function() {
        $.ajax({
            async: false,
            url: "../../waybill/queryDetail",
            type: 'POST',// 请求方式
            dataType: 'json',
            data: {"id": $("#modalId").val()},
            success: function (_data) {
                var waybill = _data.data.waybill;
                reflshData(waybill);
            }
        });
    };

    function reflshData(waybill) {
        $('#noDetl').html(waybill.no);
        $('#ticketNoDetl').html(waybill.ticketNo);
        $('#sourceDetl').html(waybill.sourceDesc);
        $('#orderNoDetl').html(waybill.orderNo);
        $('#recipientDetl').html(waybill.recipient);
        $('#deliveryTypeDetl').html(waybill.deliveryTypeDesc);
        $('#receiveAtDetl').html(publicUtils.getDate(waybill.receiveAt));
        $('#deliveryDateDetl').html(waybill.deliveryDateStr);
        $('#deliveryTimeDetl').html(waybill.deliveryTimeDesc);
        $('#remarkDetl').html(waybill.remark);
        $('#dNameDetl').html(waybill.dName);
        $('#dContactDetl').html(waybill.dContact);
        $('#dIdDetl').html(waybill.dId);
        $('#dTelDetl').html(waybill.dTel);
        $('#dPointDetl').html(waybill.dPoint);
        $('#dProvinceDetl').html(waybill.dProvince);
        $('#rTelDetl').html(waybill.rTel);
        $('#rNameDetl').html(waybill.rName);
        $('#rContactDetl').html(waybill.rContact);
        $('#rProvinceDetl').html(waybill.rProvince);
        $('#detailNameDetl').html(waybill.detailName);
        $('#detailWeightDetl').html(waybill.detailWeight);
        $('#detailSizeDetl').html(waybill.detailSizeLwh);
        $('#feeTypeDetl').html(waybill.feeTypeDesc);
        $('#payTypeDetl').html(waybill.payTypeDesc);
        $('#transprotFeeDetl').html(waybill.transportFee);
        $('#goodMoneyDetl').html(waybill.goodMoney);

        $('#dAddressDetl').html((typeof waybill.dCity == "string"?waybill.dCity:"")
            + " " + (typeof waybill.dCounty == "string"?waybill.dCounty:"")
            + " " + (typeof waybill.dAddress == "string"?waybill.dAddress:"") );
        $('#rAddressDetl').html((typeof waybill.rCity == "string"?waybill.rCity:"")
            + " " + (typeof waybill.rCounty == "string"?waybill.rCounty:"")
            + " " + (typeof waybill.rAddress == "string"?waybill.rAddress:"") );
    }

    function addZero(str, length) {
        return new Array(length - str.length + 1).join("0") + str;
    }

});