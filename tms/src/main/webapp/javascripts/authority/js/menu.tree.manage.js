define(function (require, exports, module) {
	var $=require("jquery");
    require("sweetalert");
    require("jquery.ztree.core-3.5");
    require("jqueryztreeexcheck");
    require("jqueryztreeexedit");
    
    exports.loadModalData = function(){
    	
    };

var setting = {
			view: {
				selectedMulti: false
			},
			edit: {
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: false
			},
			data: {
				keep: {
					parent:true,
					leaf:true
				},
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeDrag: beforeDrag,
				onRemove: onRemove,
				onClick: zTreeOnClick
			}
		};

		var zNodes;
		var log, className = "dark";
		function beforeDrag(treeId, treeNodes) {
			return false;
		}
		function beforeRemove(treeId, treeNode) {
			className = (className === "dark" ? "":"dark");
			return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
		}
		function onRemove(e, treeId, treeNode) {
		}
		function beforeRename(treeId, treeNode, newName) {
			if (newName.length == 0) {
				alert("节点名称不能为空.");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			return true;
		}
		
		function zTreeOnClick(event, treeId, treeNode) {
		    $("#currNoteName").val(treeNode.name);
		    $("#currNoteDecr").val(treeNode.description);
		    $("#currLinkAddr").val(treeNode.url);
		    $("#pId").val(treeNode.pId);
		    $("#currNoteId").val(treeNode.id);
		    $("#pclass").val(treeNode.pclass);
		}

		var newCount = 1;
		function add(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			isParent = e.data.isParent,
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			
			
			
			if (treeNode && treeNode.isParent == true) {
				var data = "pId="+treeNode.id+"&name=" + "new node" + newCount++ +"&isParent="+isParent;
				 $.ajax({
			    	 url:"../../menu/menuAdd",
			    	 data:data,
			    	 type:'post',
			    	 dataType:'json',
			    	 cache:false,
			    	 success:function(result){
			    		 if(result.status){
			    			 alert("增加成功");
			    			 if (treeNode) {
			 					treeNode = zTree.addNodes(treeNode, {id:result.data, pId:treeNode.id, isParent:isParent, name:"new node" + (newCount++)});
			 				} else {
			 					treeNode = zTree.addNodes(null, {id:result.data, pId:0, isParent:isParent, name:"new node" + (newCount++)});
			 				}
			    		 }
			    	 },
			    	 error:function(XMLHttpRequest, textStatus, errorThrown){
			         }
			 
			  });
			} else {
				alert("请选择目录");
			}
		};
		function edit() {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (nodes.length == 0) {
				alert("请先选择一个节点");
				return;
			}
//			zTree.editName(treeNode);
			
			
			
			
			var nName = $("#currNoteName").val();
			var ndesc = $("#currNoteDecr").val();
			var nlink = $("#currLinkAddr").val();
			var  npid = $("#pId").val();
			var nid = $("#currNoteId").val();
			var pclass = $("#pclass").val();
			
			var data = "name="+nName+"&desc="+ndesc+"&link="+nlink+"&id="+nid+"&pclass="+pclass;
			
			 $.ajax({
		    	 url:"../../menu/menuUpdate",
		    	 data:data,
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    			 alert("修改成功");
		    			 treeNode.name = nName;
		    			 treeNode.description = ndesc;
		    			 treeNode.url = nlink;
		    			 treeNode.pId = npid;
		    			 treeNode.id = nid;
		    			 treeNode.pclass = pclass;
		    			 zTree.updateNode(treeNode);
		    		 }else{
		    			 alert("修改失败");
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
//		           alert(errorThrown);
		         }
		 
		  });
			
			
		}
		function remove(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (nodes.length == 0) {
				alert("请先选择一个节点");
				return;
			}
			if(treeNode.pId == null){
				alert("不能删除根结点");
				return;
			}
			if(!confirm("确认删除 节点 -- " + treeNode.name + " 吗？")){
				return;
			}
			
			var data = "id="+treeNode.id;
			 $.ajax({
		    	 url:"../../menu/menuDelAll",
		    	 data:data,
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    			 alert("删除成功");
		    			 zTree.removeNode(treeNode, true);
		    		 }else{
		    			 alert("删除失败");
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
		         }
		 
		  });
			
		};
		function clearChildren(e) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
			nodes = zTree.getSelectedNodes(),
			treeNode = nodes[0];
			if (nodes.length == 0 || !nodes[0].isParent) {
				alert("请先选择一个目录");
				return;
			}
			
			if(treeNode.pId == null){
				alert("不能删除根结点");
				return;
			}
			
			if(!confirm("确认删除 节点 -- " + treeNode.name + " 吗？")){
				return;
			}
			
			var data = "id="+treeNode.id;
			 $.ajax({
				 url:"../../menu/menuDelchildren",
		    	 data:data,
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    			 alert("删除成功");
		    			 zTree.removeChildNodes(treeNode);
		    		 }else{
		    			 alert("删除失败");
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
		         }
		 
		  });
			
		};
		
		$(function(){
			
			 $.ajax({
				 url:"../../menu/getAll",
		    	 type:'post',
		    	 dataType:'json',
		    	 cache:false,
		    	 success:function(result){
		    		 if(result.status){
		    			var zNodes = result.data;
		    			zNodes = eval("(" + zNodes + ")");
		    			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		    		 }else{
		    		 }
		    	 },
		    	 error:function(XMLHttpRequest, textStatus, errorThrown){
		         }
		 
		  });
			
			
			$("#addParent").bind("click", {isParent:true}, add);
			$("#addLeaf").bind("click", {isParent:false}, add);
			$("#edit").bind("click", edit);
			$("#remove").bind("click", remove);
			$("#clearChildren").bind("click", clearChildren);
		});
});