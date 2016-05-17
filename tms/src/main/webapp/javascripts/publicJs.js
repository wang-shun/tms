define(function(require, exports, module) {
    var $ = require("jquery");
    var Handlebars = require("handlbars");
	require("./jquery.pager");
    require("jquery-ui.min.js");
	

	Handlebars.registerHelper('compare', function(left, operator, right, options) {
         if (arguments.length < 3) {
           throw new Error('Handlerbars Helper "compare" needs 2 parameters');
         }
         var operators = {
           '==':     function(l, r) {return l == r; },
           '===':    function(l, r) {return l === r; },
           '!=':     function(l, r) {return l != r; },
           '!==':    function(l, r) {return l !== r; },
           '<':      function(l, r) {return l < r; },
           '>':      function(l, r) {return l > r; },
           '<=':     function(l, r) {return l <= r; },
           '>=':     function(l, r) {return l >= r; },
           'typeof': function(l, r) {return typeof l == r; }
         };

         if (!operators[operator]) {
           throw new Error('Handlerbars Helper "compare" doesn\'t know the operator ' + operator);
         }

         var result = operators[operator](left, right);

         if (result) {
           return options.fn(this);
         } 
         else {
           return options.inverse(this);
         }
     });





	/*
		分页封装 start
		pagerobj:控件承载对象
		current_page：当前页
		ALLpages:总页数
		fun:点击回调
	*/
	
	var pagination= function(pagerobj,current_page, ALLpages, fun) {
        $(pagerobj).pager({
            pagenumber: current_page,
            pagecount: ALLpages,
            buttonClickCallback: fun
        });

    };
	var getRadioValue=function(name)
	{
		var objs=document.getElementsByName(name);
		for(var i=0;i<objs.length;i++)
		{
			if(objs[i].checked)
			{
				return objs[i].value;
			}	
		}	
		return -1;
	};
	var setRadioValue=function(name,_value)
	{
		var objs=document.getElementsByName(name);
		for(var i=0;i<objs.length;i++)
		{
			if(objs[i].value==_value)
			{
				objs[i].checked=true;
				return;
			}	
		}	
	};
	var clearFormValues=function(obj)
	{
		$(obj).find("input[type='text']").val("");
		$(obj).find("textarea").val("");
		$(obj).find("input[type='password']").val("");
		//$(obj).find("input[type='checkbox']").attr("checked",false);
		//$(obj).find("input[type='radio']").attr("checked",false);
	};
	var clearcheckBox=function(obj)
	{
		$(obj).find("input[type='text']").val("");
		$(obj).find("textarea").val("");
		$(obj).find("input[type='password']").val("");
		$(obj).find("input[type='checkbox']").attr("checked",false);
		//$(obj).find("input[type='radio']").attr("checked",false);
	};
	/*
		分页封装 end
	*/
	/* window.alert=function(info,fun){
			var alertHtml='';
			alertHtml +='<div class="show-bg" style="position:fixed;left:0;top:0;width:100%;height:100%;background:rgba(0,0,0,0.6);z-index:9998"></div>'
			       +'<table align="center" valign="middle" width="100%" height="100%" class="alert-container-text" style="position:absolute;left:0;top:0;z-index:9999">'
				+'<tr>'
					+'<td>'
						+'<div class="alert-body">'
							+'<span class="text-area">'
								+'<span class="text-container"></span>'
							+'</span>'
						+'</div>'
					+'</td>'
				+'</tr>'
			+'</table>';
			
			$("body").append(alertHtml);
			
			//createShield(alertHtml,function(){
				 $(".text-container").html(info);				
				 $(".alert-body").css({"border-radius":"5px","width":"40%","overflow":"hidden","background":"#fff","margin":"0 auto"});
				 $(".text-area").css({"display":"block","width":"100%","overflow":"hidden"}); 
				 $(".text-container").css({"box-sizing":"border-box","color":"#484848","word-break":"break-all","display":"block","width":"100%","overflow":"hidden","display":"block","padding":"30px 30px","text-align":"left","font-size":"14px"});
				$(".body-wrap").css("pointer-events","none");
				
				 $(".alert-container-text").unbind().click(function(){
				
					$(".alert-container-text").remove();
					$(".show-bg").remove();
					if(typeof(fun)=="function"){
					 	fun();
					}
					$(".body-wrap").css("pointer-events","auto");
					return false;
				});
				 $(".alert-container-text").unbind().bind("touchstart",function(){
				
					$(".alert-container-text").remove();
					$(".show-bg").remove();
					if(typeof(fun)=="function"){
					 	fun();
					}
					$(".body-wrap").css("pointer-events","auto");
					return false;
				});
			 //})
		
			setTimeout(function(){
			 	$(".alert-container-text").remove();
					$(".show-bg").remove();
					if(typeof(fun)=="function"){
					 	fun();
					}
					$(".body-wrap").css("pointer-events","auto");
					return false;
			},2000);
    }*/
	
	 exports.startLoading=function(){
        if($(".crm-loading-background")[0]!=null)
            return;
        var background=$("<div/>").attr("class","crm-loading-background").appendTo("body");
        var img=$("<img/>").attr({"src":"../../images/pageloading.gif","class":"loading-icon"}).appendTo(background);
    }
    exports.endLoading=function(){
        $(".crm-loading-background").remove();
    }
    var pubFn={
    	//表格checkbox选中返回值
    	seleceCheckbox:function(obj){
    		var tempId=[];
    		$(obj).find("input[type=checkbox]").each(function(){
    			  if (this.checked == true) {
    			  	var temp=new Object();
    			  	temp.id=$(this).attr("dataid");
    			  	tempId.push(temp);

    			  }

    		});
    		 return tempId;
    	},
    	 intnum:function(thisval){//只能输入整数
	    	$(".inptNum").each(function(){
	    		$(this)[0].onkeyup=function(){
		            this.value=this.value.replace(/[^\d]/g,'');
		        }
	    	})	
	    },
	    floatnum:function(thisval){//输入整数和2位小数
	    	$(".inptFloat").each(function(){
	    		$(this)[0].onkeyup=function(){
		           		this.value=this.value.replace(/[^\d\.]/g,'');
	                     str=this.value.split(".");
	                     if(str.length>1){
	                      if(str[1].length>2)//小数部分大于2
	                        {
	                            this.value=str[0]+"."+str[1].substring(0,2);
	                          //alert(Math.round(num*100)/100);
	                            //alert("小数部分保留2位");
	                            return;
	                         
	                        }  
	                     }
		        }
	    	})	
	    },
		  /** 全选/全不选 */
		seleAll:function() {
			$("#grop-list .checkAll").each(function(){
				 $(this).click(function() {
					$(this).parent().parent().find("form").find("input[name='chk_list']").prop("checked", $(this).prop("checked"));
				});
			});      
		},
    }

	/*
     $.datepicker.regional['zh-CN'] = {
        clearText : '清除',
        clearStatus : '清除已选日期',
        closeText : '关闭',
        closeStatus : '不改变当前选择',
        prevText : '<上月',
        prevStatus : '显示上月',
        prevBigText : '<<',
        prevBigStatus : '显示上一年',
        nextText : '下月>',
        nextStatus : '显示下月',
        nextBigText : '>>',
        nextBigStatus : '显示下一年',
        currentText : '今天',
        currentStatus : '显示本月',
        monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月',
                '十月', '十一月', '十二月' ],
        monthNamesShort : [ '一', '二', '三', '四', '五', '六', '七', '八', '九', '十',
                '十一', '十二' ],
        monthStatus : '选择月份',
        yearStatus : '选择年份',
        weekHeader : '周',
        weekStatus : '年内周次',
        dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
        dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
        dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ],
        dayStatus : '设置 DD 为一周起始',
        dateStatus : '选择 m月 d日, DD',
        dateFormat : 'yy-mm-dd',
        firstDay : 1,
        initStatus : '请选择日期',
        isRTL : false
        };
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);*/
		
	exports.getLanguage=function()
	{
		var str=window.location.href;
		if(str.indexOf("/en/")>-1)
		{
			return "en";	
		}else{
			return "zh";	
		}
	};

	function getByteLen(str){
		var char = str.replace(/[^\x00-\xff]/g, '**');
		return char.length;
	}

	/**用loadjs替代require可无需修改引入的js（引用js必须独立不引用jquery等框架）*/
	/* 由jQuery提供的$.getScript()替代
	function loadjs(src){
		var script = document.createElement("script");
		script.type = "text/javascript";
		script.src = src;
		document.body.appendChild(script);
	}*/

	//函数节流，控制大量连续触发性事务（如onresize）的执行频率
	function throttle(method, args, context){
		clearTimeout(method.throttleId);
		method.throttleId = setTimeout(function () {
			method.call(context, args);
		}, 100);
	}

	pubFn.intnum();
	pubFn.floatnum();
	exports.pagination=pagination;
	exports.getRadioValue=getRadioValue;
	exports.setRadioValue=setRadioValue;
	exports.clearFormValues=clearFormValues;
	exports.pubFn=pubFn;
	exports.clearcheckBox=clearcheckBox;
	exports.getByteLen = getByteLen;
	//exports.loadjs = loadjs;
	exports.throttle=throttle;
});
