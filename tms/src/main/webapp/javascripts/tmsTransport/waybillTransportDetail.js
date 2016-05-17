define(function (require, exports, module) {
    var $ = require("jquery");
    require("sweetalert");
    var execute = require("execute");
    var publicUtils = require("publicUtils");
    var public = require("publicJs");
    require("http://ditu.google.cn/maps/api/js?sensor=false&language=zh-CN");

    var centerAction = {

        GetQueryString: function (name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);  //获取url中"?"符后的字符串并正则匹配
            var context = "";
            if (r != null)
                context = r[2];
            reg = null;
            r = null;
            return context == null || context == "" || context == "undefined" ? "" : context;
        },

        request_waybill_info: function (body) {
            var request = new Object();
            request.body = body;
            request.type = "POST";
            request.url = "../waybillTransport/queryDetail";
            request.handle = centerAction.handle_waybill_info;
            execute.execute(request);
        },

        handle_waybill_info: function (_data) {
            if (_data.success) {
                centerAction.reflshData(_data.data.waybillLogList);
                if (_data.data.waybillLogList.length >= 2) {
                    centerAction.setBigCar(_data.data.driver);
                }
                if (_data.data.waybillLogList.length >= 4) {
                    centerAction.setSmallCar(_data.data.waybill);
                }
                centerAction.showMap(_data.data.waybill);
            } else {
                alert(_data.errorMessage);
            }
        },

        showMap : function(waybill){
            if(waybill.latLng!=null&&waybill.latLng.indexOf("/")>0){
                var coords = waybill.latLng.split("/");
                var googleCoords = new google.maps.LatLng(coords[0], coords[1]);
                var mapOptions = {
                    zoom : 15,
                    center : googleCoords,
                    mapTypeId : google.maps.MapTypeId.ROADMAP
                }
                var mapDiv = document.getElementById("map");
                var map = new google.maps.Map(mapDiv, mapOptions);
                var title = "运单位置";
                var content = "最后更新时间：" +
                    publicUtils.getDate(waybill.updatedAt)+publicUtils.getTime(waybill.updatedAt);
                centerAction.addMapMarker(map, googleCoords, title, content);
            }
        },

        addMapMarker : function(map, latLng, title, content){
            var marderOpts = {
                position : latLng,
                map : map,
                title : title,
                clickable : true
            }
            var marker = new google.maps.Marker(marderOpts);

            var infoWindowOptions = {
                content: content,
                position: latLng
            };

            var infoWindow = new google.maps.InfoWindow(infoWindowOptions);
            google.maps.event.addListener(marker, 'click', function() {
                infoWindow.open(map);
            });
        },

        reflshData: function (waybillLogList) {
            $('#id').html(waybillLogList[0].wId);
            $('#no').html(waybillLogList[0].wNo);
            if ("9" == status) {
                //作废跟踪处理
                centerAction.showCancel(waybillLogList);
            } else {
                //正常运单跟踪
                centerAction.showWaybillLog(waybillLogList, status);
            }
        },

        /***显示作废跟踪**/
        showCancel: function (waybillLogList) {
            $('#status90').show();
            $('#status1').hide();
            $('#status2').hide();
            $('#status3').hide();
            $('#status4').hide();
            $('#status5').hide();
            $("#status-process").css({
                "width": "100%"
            });
            var lastData = waybillLogList[waybillLogList.length - 1];
            var firstData = waybillLogList[0];
            $("#status0").attr('class', 'order-status-text status-first status-done')
            $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="fa fa fa-suitcase"></i>' +
                publicUtils.getTime(firstData.createdAt) + '<br><small class="text-navy">'
                + publicUtils.getDate(firstData.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                + '运单确认</strong></p><p>' +
                firstData.wLog + '</p></div></div></div>');
            $("#status90").attr('class', 'order-status-text status-on');
            $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="fa fa-times"></i>' +
                publicUtils.getTime(lastData.createdAt) + '<br><small class="text-navy">'
                + publicUtils.getDate(lastData.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                + '运单作废</strong></p><p>' +
                lastData.wLog + '</p></div></div></div>');
        },

        /***正常运单跟踪**/
        showWaybillLog: function (waybillLogList, status) {
            var class1 = "fa fa-suitcase";
            var class2 = "fa fa-truck";
            var class3 = "fa fa-truck";
            var class4 = "fa fa-truck";
            var class5 = "fa fa-motorcycle";
            var class6 = "fa fa-check";
            //由于第一条就是最新数据
            for (var i = 0; i < waybillLogList.length; i++) {
                var waybillLog = waybillLogList[i];
                //展示上面进度
                if (i == waybillLogList.length - 1) {
                    $("#status-process").css({"width": (i * 25) + "%"});
                    $("#status" + i).attr('class', 'order-status-text status-on');
                } else if (i == 0) {
                    $("#status" + i).attr('class', 'order-status-text status-first status-done')
                } else {
                    $("#status" + i).attr('class', 'order-status-text status-done')
                }
                //展示下面进度
                if (i == 0) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class1 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '运单确认</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                } else if (i == 1) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class2 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '大车接单</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                } /*else if (i == 2) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class3 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '生成配送单</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                }*/ else if (i == 2) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class4 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '大车下车确认</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                } else if (i == 3) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class5 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '小车接单</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                } else if (i == 4) {
                    $("#waybillInfoDetail").prepend('<div class="timeline-item"><div class="row"><div class="col-xs-3 date"><i class="' +
                        class6 + '"></i>' +
                        publicUtils.getTime(waybillLog.createdAt) + '<br><small class="text-navy">'
                        + publicUtils.getDate(waybillLog.createdAt) + '</small> </div><div class="col-xs-7 content"><p class="m-b-xs"><strong>'
                        + '运单签收</strong></p><p>' +
                        waybillLog.wLog + '</p></div></div></div>');
                }

            }
        },

        setBigCar: function (driver) {
            if (driver != null) {
                $("#bigCar").prepend(
                    '<li class="list-group-item"><p><a class="text-success" >车牌号：</a>'
                    + '<input id="vNoBig" name = "vNoBig" readonly="readonly" value="' + driver.VNO + '"></p></li>' +

                    '<li class="list-group-item"><p><a class="text-success" >司机：</a>'
                    + '<input id="dNameBig" name = "dNameBig" readonly="readonly"  value="' + driver.NAME + '"></p></li>'
                    /*+ '<li class="list-group-item"> <p><a class="text-success" >配送集散点：</a> '
                    + '<input id="deliveryTerm" name = "deliveryTerm" readonly="readonly"  value="' + deliveryBill.deliveryTerm + '"></p></li>'*/
                    );
            }
        },

        setSmallCar: function (waybill) {
            if (waybill != null) {
                var statusSmall = null;
                if ('40' == waybill.status) {
                    statusSmall = "已签收";
                } else {
                    statusSmall = "未签收";
                }
                $("#smallCar").prepend('<li class="list-group-item"><p><a class="text-success" >小车车牌：</a> <input id="vNoSmall" name = "vNoSmall" readonly="readonly" value="' + (waybill.vNo==null?"":waybill.vNo) + '"></p></li>'
                    + '<li class="list-group-item"><p><a class="text-success" >是否签收：</a> <input id="statusSmall" name = "statusSmall" readonly="readonly" value="' + statusSmall + '"></p></li>'
                    + '<li class="list-group-item"><p><a class="text-success" >派送人：</a> <input id="dNameSmall" name = "dNameSmall" readonly="readonly" value="' + (waybill.vDriverName==null?"":waybill.vDriverName) + '"></p></li>'
                    + '<li class="list-group-item"><p><a class="text-success" >签收时间：</a> <input id="signTime" name = "signTime" readonly="readonly" value="' + publicUtils.getLocalTime(waybill.signTime) + '"></p></li>');
                /*$('#vNoSmall').val(waybill.vNo);
                 $('#dNameSmall').val(waybill.vDriverName);
                 $('#signTime').val(publicUtils.getLocalTime(waybill.signTime));*/
            }
        }
    };

    $(document).ready(function () {
        var body = new Object();
        body.id = centerAction.GetQueryString("id");
        centerAction.request_waybill_info(body);
    });
});