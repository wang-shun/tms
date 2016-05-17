define(function (require, exports, module) {
	var $=require("jquery");
	require("sweetalert");
	function updateData(grid_selector, confirmMsg){
		var ids = $(grid_selector).jqGrid('getGridParam','selarrrow');
		if(ids==''){
			alert('请先选择修改数据!');
			return;
		}
		sweetConfirm(confirmMsg||"确定修改？", function(){
			var isSuccess = true;
			var successCount=0;
			var failCount = 0;
			var errorMessage = "";
			var failArray = new Array();
			var successArray = new Array();
			//先将所有的数据保存
			for(var i=0;i<ids.length;i++){
				$(grid_selector).jqGrid('saveRow', ids[i],{
					successfunc: function(response) {
						var responseJson = response.responseJSON;
						if(responseJson!=undefined &&  !responseJson.success){
							isSuccess=false;
							failCount=failCount+1;
							failArray.push(ids[i]);
							errorMessage=errorMessage+"第"+(failCount)+"条更新失败原因："+responseJson.errorMessage+"<br>"
							return false;
						}else{
							successArray.push(ids[i]);
							successCount=successCount+1;
							//sweetAlert.close();
							return true;
						}
					}
				});
			}
			if(isSuccess && successCount!=0){
				$(grid_selector).trigger("reloadGrid");
				sweetAlert.close();
				return;
			}else if(!isSuccess && failCount!=0){
				for(var i=0;i<successArray.length;i++){
					$(grid_selector).jqGrid('setSelection', successArray[i], true);
				}
				for(var i=0;i<failArray.length;i++){
					$("#"+failArray[i]).find("td").css("background-color", "#dff0d8");
						$(grid_selector).jqGrid('setSelection', failArray[i], true);
					}
				alert("更新成功【"+successCount+"】条记录，失败【"+failCount+"】。具体：<br>"+errorMessage);
				return;
			}
		});
	}
	
	function updateDataBatch(grid_selector, confirmMsg,url,datas){
		sweetConfirm(confirmMsg, function(){
			$.ajax({
				async: false,
				url: url,
				type : 'POST',// 请求方式
				dataType:'json',
				data:{"dataList":JSON.stringify(datas)},
				success : function(data){
					if(!data.success){
						alert(data.errorMessage);
						return false;
					}else{
						$(grid_selector).trigger("reloadGrid");
						sweetAlert.close();
						return true;
					}
				}
			});
		});
	}
	
	function deleteData(grid_selector, confirmMsg){
		var ids = $(grid_selector).jqGrid('getGridParam','selarrrow');
		if(ids==''){
			alert('请先选择删除数据!');
			return;
		}
		$(grid_selector).jqGrid('delGridRow', ids, {
			afterSubmit: function (response,postdata){
				return afterSubmitDel(response,postdata);
			}
		});
		$(grid_selector).jqGrid("setGridParam").trigger("reloadGrid");
	}
	
	function cancelOrConfirmData(grid_selector, confirmMsg,url,action,successMessage){
		var ids = $(grid_selector).jqGrid('getGridParam', 'selarrrow');
	    if (ids == '') {
	        alert('请先选择数据');
	        return;
	    }
		sweetConfirm(confirmMsg, function(){
			$.ajax({
				async: false,
				url: url,
				type : 'POST',// 请求方式
				dataType:'json',
				data:{
					'id':ids.toString(),
					'oper':action
				},
				success : function(data){
					if(!data.success){
						alert(data.errorMessage);
						return false;
					}else{
						swal("", successMessage, "success");
						return true;
					}
				}
			});
			$(grid_selector).jqGrid("setGridParam").trigger("reloadGrid");
		});
	}

	function afterSubmitAdd(response,postdata){
		var json = response.responseJSON;
		if(json!=undefined && json.success == false ){
			alert("新增失败,原因:" + json.errorMessage);
			return [false, json.errorMessage];
		}
		return [true, ""];
	}

	function afterSubmitDel(response,postdata){
		var json = response.responseJSON;
		if(json!=undefined && json.success == false ){
			alert("删除失败,原因:" + json.errorMessage);
			return [true, json.errorMessage];
		}
		return [true, ""];
	}

	function onSelectAllWithDate(aRowids,status,pickdates,grid_selector){
		if(status==true){
			for(var i=0;i<aRowids.length;i++){
				$(grid_selector).jqGrid('editRow', aRowids[i], true,pickdates);
			}
		}else{
			for(var i=0;i<aRowids.length;i++){
				$(grid_selector).jqGrid('restoreRow',aRowids[i]);
			}
		}
	}
	function onSelectAll(aRowids,status,grid_selector){
		if(status==true){
			for(var i=0;i<aRowids.length;i++){
				$(grid_selector).jqGrid('editRow', aRowids[i], true);
			}
		}else{
			for(var i=0;i<aRowids.length;i++){
				$(grid_selector).jqGrid('restoreRow',aRowids[i]);
			}
		}
	}
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

	exports.updateData=updateData;
	exports.updateDataBatch=updateDataBatch;
	exports.deleteData=deleteData;
	exports.afterSubmitAdd=afterSubmitAdd;
	exports.afterSubmitDel=afterSubmitDel;
	exports.onSelectAllWithDate=onSelectAllWithDate;
	exports.onSelectAll=onSelectAll;
	exports.getDate=getDate;
	exports.cancelOrConfirmData=cancelOrConfirmData;
});
