define(function(require, exports, module) {
    var $ = require("jquery"),
        Handlebars = require("handlebars");



    /** 全选/全不选 */
    function seleAll() {
        $("#checkAll").click(function() {
            $("input[name='chk_list']").prop("checked", $(this).prop("checked"));
        });
    }

    $("input[name='chk_list']").click(function() {
        var bln = true;
        $("input[name='chk_list']").each(function() {
            if (this.checked == false) {
                bln = false;
                return;
            }
        });
        if (bln) {
            $("#checkAll").prop("checked", true);
        } else {
            $("#checkAll").prop("checked", false);
        }
    });
/*选中返回本条记录的id*/
    function backMsg(fn){
        $("input[name='chk_list']").each(function(index, el) {
            $(this).click(function(){
                var id=$(this).attr("id");
                fn.apply(this,[id]);
            })
        });
    }

    /** 是否有选中项 */
    function blnCheck() {
        var bln = false;
        $("input[name='chk_list']").each(function() {
            if (this.checked == true) {
                bln = true;
                return;
            }
        });
        return bln;
    }

    /** 获取选中几项 */
    function getCheckNum() {
        var num = 0;
        $("input[name='chk_list']").each(function() {
            if (this.checked == true) {
                num++;
            }
        });
        return num;
    }

    /** 获取选中项数据 */
    function getCheckData(_data) {
        var json = [];
        var num = 0;
        $("input[name='chk_list']").each(function() {
            if (this.checked == true) {
                json.push(_data[num]);
            }
            num++;
        });
        console.log(json);
        return json;
    }

    /** 判断是否可以修改 */
    function blnModify() {
        var num = getCheckNum();
        var bln = false;
        if (num == 0) {
            alert("请选择一个选项进行修改。");
            bln = false;
        } else if (num == 1) {
            bln = true;
        } else {
            alert("只能选择一个选项进行修改。");
            bln = false;
        }
        return bln;
    }

    /** 手机校验 */
    function isMobile(object) {
        if (object == "") {
            // alert("请输入手机号码");
            return true;
        }
        var s = object;
        var reg2 = /^1\d{10}$/;
        var reg3 = /^0\d{11}$/;
        var my = false;
        if (reg2.test(s)) my = true;
        if (reg3.test(s)) my = true;
        if (!my) {
            alert("您的手机号码格式不正确");
            return false;
        }
        return true;
    }

    /** 邮箱校验 */
    function isMail(object) {
        if (object == "") {
            return true;
        }
        var reg = /^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/;
        if (!reg.test(object)) {
            alert("您的邮箱格式不正确");
            return false;
        }
        return true;
    }

    /** 校验：只能输入数字、字母、汉字、下滑线 */
    function isInput(object) {
        if (object == "") {
            return true;
        }
        var reg = /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
        if (!reg.test(object)) {
            return false;
        }
        return true;
    }

    function isIp(object) {
        if (object == "") {
            return true;
        }
        var reg = /((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
        if (!reg.test(object)) {
            return false;
        }
        return true;
    }

    module.exports.blnCheck = blnCheck;
    module.exports.getCheckNum = getCheckNum;
    module.exports.getCheckData = getCheckData;
    module.exports.blnModify = blnModify;
    module.exports.seleAll = seleAll;
    module.exports.isMobile = isMobile;
    module.exports.isMail = isMail;
    module.exports.isInput = isInput;
    module.exports.isIp = isIp;
    module.exports.backMsg = backMsg;
    
});
