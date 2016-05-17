define(function(require, exports, module) {
  var $=require("jquery");
	var execute = require("execute");
	var fnList=require("./menuSet-ui").fnList;
	var Handlebars = require("handlebars");
	var initTreeDate= require("../tree.js").initTreeDate;
	require("bootstrap");
	var menuSet=[
	{
	
	menubinding:"饮料"
	
	
	},
	{
	menubinding:"烧烤"
	
	}
	
	
];
	
var zNodes =[
	{id:1, pId:0, name: "湖滨路点菜单模板"},
	{id:11, pId:1, name: "烧烤"},
	{id:12, pId:1, name: "带我走"},
	{id:13, pId:1, name: "好想你"},
	
];

	var treefn={
		treeFnBack:function(treeObj){
			console.log(treeObj);
		},
		
	}

	$(document).ready(function() {
		fnList.initData("#menuSet-tb-list-template",menuSet,"#menuSet-tb");
			initTreeDate(zNodes,treefn.treeFnBack);
		
	// $(".container-page").css("height",$(window).height());


	});
});