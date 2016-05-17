define(function(require, exports, module) {
    var $ = require("jquery");
	require("jquery.ztree.core-3.5.js");


	

	 var setting = {
            view: {
				showLine:true,
				showIcon: false,
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
            },
            check: {
                enable: false
                ,chkStyle: 'checkbox'
                ,radioType: "level",
				chkboxType:{ "Y" : "", "N" : "" }
            },
            data: {
                simpleData: {
                    enable: true,
                }
            },
            callback: {
            	beforeDrag: beforeDrag,
				onClick: zTreeOnClick,
				//onRightClick:OnRightClick,
				//onRename:OnRename,
				beforeEditName: beforeEditName,
				beforeRemove: beforeRemove,
				//beforeRename: beforeRename,
				//onRemove: onRemove,
            },
            edit: {
           
				enable: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: showRenameBtn,
				removeTitle: "删除",
				renameTitle: "编辑"
            }
        };
        function beforeEditName(treeId, treeNode) {
			//className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeEditName ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			//zTree.selectNode(treeNode);
			editFn.apply(this,[treeNode]);
			return false;
			//return confirm("进入节点 -- " + treeNode.name + " 的编辑状态吗？");
		}
		function beforeRemove(treeId, treeNode) {
			deletFn.apply(this,[treeNode]);
			//className = (className === "dark" ? "":"dark");
			//showLog("[ "+getTime()+" beforeRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			//var zTree = $.fn.zTree.getZTreeObj("treeDemo");
			//zTree.selectNode(treeNode);
			//return confirm("确认删除 节点 -- " + treeNode.name + " 吗？");
		}
		function onRemove(e, treeId, treeNode) {
			//deletFn.apply(this,[treeNode]);
			//showLog("[ "+getTime()+" onRemove ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name);
			
		}
		function beforeRename(treeId, treeNode, newName, isCancel) {
			className = (className === "dark" ? "":"dark");
			//showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" beforeRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
			if (newName.length == 0) {
				alert("节点名称不能为空.");
				var zTree = $.fn.zTree.getZTreeObj("treeDemo");
				setTimeout(function(){zTree.editName(treeNode)}, 10);
				return false;
			}
			return true;
		}
		function onRename(e, treeId, treeNode, isCancel) {
			//showLog((isCancel ? "<span style='color:red'>":"") + "[ "+getTime()+" onRename ]&nbsp;&nbsp;&nbsp;&nbsp; " + treeNode.name + (isCancel ? "</span>":""));
		}

		var flag = 0;//添加0 修改1
		function OnRename(){
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getSelectedNodes();
			var treeNode = nodes[0];
			editFn.apply(this,[treeNode]);
			
			
		};
	
		function onBodyMouseDown(event){
			if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
				rMenu.css({"visibility" : "hidden"});
			}
		}
        var zNodes =[];

		var treeNodecclickobj=null;
		function zTreeOnClick(event, treeId, treeNode) {
           
			//menuMasterAction.request_dishesInfo_list(1);
   			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var node = treeObj.getNodeByTId(treeNode.tId);
			$(".right-menu-name").html(node.name);

			fn.apply(this,[treeNode]);
	
		}
        function dropPrev(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropInner(treeId, nodes, targetNode) {
			if (targetNode && targetNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!targetNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropNext(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}

        var log, className = "dark", curDragNodes, autoExpandNode;
        function beforeDrag(treeId, treeNodes) {
	        curDragNodes = null;
	        return false;

        }
       
        function onExpand(event, treeId, treeNode) {
            if (treeNode === autoExpandNode) {
                className = (className === "dark" ? "":"dark");
              //  showLog("[ "+getTime()+" onExpand ]&nbsp;&nbsp;&nbsp;&nbsp;" + treeNode.name);
            }
        }

        function setTrigger() {
            var zTree = $.fn.zTree.getZTreeObj("treeDemo");
            zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
        }
        function setCheck() {
            //setting.check.chkStyle = $("#r1").attr("checked")? "checkbox":"radio";
            setting.check.chkStyle = "checkbox";
            setting.check.enable = (!$("#disablechk").attr("checked"));
            $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        }
       
        function showRemoveBtn(treeId, treeNode) {//显示删除按钮
			//return !treeNode.isFirstNode;
			 if(treeNode.pId==0||treeNode.pId==null){
			 	return false;
			 }else if(treeNode.pId==""){
			 	return true;
			 }else if(treeNode.pId!=""&&treeNode.pId!=0&&treeNode.pId!=null){
			 	return true;
			 }
    

		}
		function showRenameBtn(treeId, treeNode) {//显示编辑按钮
			//return !treeNode.isLastNode;
			if(treeNode.pId==0||treeNode.pId==null){
			 	return false;
			 }else if(treeNode.pId==""){
			 	return true;
			 }else if(treeNode.pId!=""&&treeNode.pId!=0&&treeNode.pId!=null){
			 	return true;
			 }
		}

        var newCount = 1;
        function addHoverDom(treeId, treeNode) {

            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='添加' onfocus='this.blur();'></span>";
           
             if(treeNode.pId==0||treeNode.pId==null||treeNode.pId==-1){
			 	 sObj.after(addStr);
			 }
            var btn = $("#addBtn_"+treeNode.tId);
            /*添加、新增*/
           if (btn) btn.bind("click", function(){
                var zTree = $.fn.zTree.getZTreeObj("treeDemo");
                addFn.apply(this,[zTree,treeNode,treeId]);
                //zTree.addNodes(treeNode, {id:(100 + newCount), pId:treeNode.id, name:"新节点" + (newCount++)});
                //return false;
            });
        };
        function removeHoverDom(treeId, treeNode) {
            $("#addBtn_"+treeNode.tId).unbind().remove();
        };


	var menuMasterAction = {
		getTid: function(obj) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getSelectedNodes();
			if (obj == "obj") {
				return nodes[0];
			}
			return nodes[0].id;
		},
		getPid: function(obj) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var nodes = treeObj.getSelectedNodes();
			if (obj == "obj") {
				return nodes[0];
			}
			return nodes[0].pId;
		},
		getChildren: function(targetNode) {
			var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
			var node = targetNode;
			var ParentNode = node.getParentNode();
			return ParentNode.children;

		},
	}


	
var zTree,fn,addFn,editFn,deletFn,treedata; 

	function initTreeDate(data,fun,addfun,editfun,deletfun){
		if(addfun==undefined||addfun=='undefined'){
			setting.view.addHoverDom=null;
			setting.edit.showRemoveBtn=false;
			setting.edit.showRenameBtn=false;
		}else{
			setting.view.addHoverDom=addHoverDom;
			setting.edit.showRemoveBtn=showRemoveBtn;
			setting.edit.showRenameBtn=showRenameBtn;
		}
		$.fn.zTree.init($("#treeDemo"), setting, data);
		fn=fun;
		addFn=addfun;
		editFn=editfun;
		deletFn=deletfun;
		treedata=data;
	}
	exports.initTreeDate=initTreeDate;
});
