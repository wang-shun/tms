define(function(require, exports, module) {
	var $ = require("jquery");
    var iScroll =require("./iscroll").iScroll;
 	var myScroll,
        pullDownEl, pullDownOffset,
        pullUpEl, pullUpOffset,
        generatedCount = 0;
        var newsID=-1;
        function pullUpAction (fn) {
            fn.apply(myScroll,[]); 
        }
    function initDiv(contain){
         var scrollerDiv =$('<div id="scroller"></div>');
        $('#wrapper').prepend(scrollerDiv);
        scrollerDiv.append(contain);
        var html="";
        html ='<div id="pullUp">'
            +'<span class="pullUpIcon"></span><span class="pullUpLabel">Pull up to refresh...</span>'
        +'</div>'
        scrollerDiv.append(html);
    }
    function loaded(fn) {
		
        
        pullUpEl = document.getElementById('pullUp');
        pullUpOffset = pullUpEl.offsetHeight;
        

        myScroll = new iScroll('wrapper', {
            useTransition: true,
            topOffset: pullDownOffset,
            onRefresh: function () {
                if (pullUpEl.className.match('loading')) {
                    pullUpEl.className = '';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载...';
                }
                 $("#pullUp").hide();
  
                },
			
            onScrollMove: function () {
				 if (this.y < (this.maxScrollY+40) && !pullUpEl.className.match('flip')) {
					  pullUpEl.querySelector('.pullUpLabel').innerHTML = '上拉加载...';
					 
                     $("#pullUp").show();
                     
                 }
                 if (this.y < (this.maxScrollY-40) && !pullUpEl.className.match('flip')) {
                     $("#pullUp").show();
                     pullUpEl.className = 'flip';
                     pullUpEl.querySelector('.pullUpLabel').innerHTML = '释放刷新...';
                     this.maxScrollY = this.maxScrollY;
                 }
                console.log(this.y+"======="+this.maxScrollY+"==="+pullUpEl.className)
           /* if (this.y < (this.maxScrollY - 40) && !pullUpEl.className.match('flip')) {
                pullUpEl.className = 'flip';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Release to refresh...';
                this.maxScrollY = this.maxScrollY;
            } else if (this.y > (this.maxScrollY + 40) && pullUpEl.className.match('flip')) {
                pullUpEl.className = '';
                pullUpEl.querySelector('.pullUpLabel').innerHTML = 'Pull up to load more...';
                this.maxScrollY = pullUpOffset;
            }*/

            },
            onScrollEnd: function () {
                if (pullUpEl.className.match('flip')) {
                    pullUpEl.className = 'loading';
                    pullUpEl.querySelector('.pullUpLabel').innerHTML = '正在刷新...';
                    pullUpAction(fn);	// Execute custom function (ajax call?)
                }
            }
        });
                               
        setTimeout(function () { document.getElementById('wrapper').style.left = '0'; }, 800);
		return myScroll;
    }
    //document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
   

//function initScroll(){
	 //document.addEventListener('DOMContentLoaded', function () { setTimeout(loaded, 500); }, false);
//}
exports.loaded=loaded;	
exports.initDiv=initDiv;
});