define(function(require, exports, module) {
  var $=require("jquery");
	var execute = require("execute");
	var fnList=require("./menuManage-ui").fnList;
	var Handlebars = require("handlebars");
	var initTreeDate= require("../tree.js").initTreeDate;
	require("bootstrap");
	var menu=[
	{
	
	menuno:"0001",
	menuname:"静安寺菜单",
	menucount:10,
	menuaction1:"编辑",
	menuaction2:"详情",
	menuaction3:"配置",
	
	},
	{
	menuno:"0002",
	menuname:"湖滨道菜单",
	menucount:8,
	menuaction1:"编辑",
	menuaction2:"详情",
	menuaction3:"配置",
	
	}
	
	
];
	var details=[
	{
		detailsno:"0001",
		detailsname:"烤羊腿"
	},
	{
		detailsno:"0002",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0003",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0004",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0001",
		detailsname:"烤羊腿"
	},
	{
		detailsno:"0002",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0003",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0004",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0001",
		detailsname:"烤羊腿"
	},
	{
		detailsno:"0002",
		detailsname:"红烧鸡翅膀"
	},
	{
		detailsno:"0003",
		detailsname:"红烧鸡翅膀"
	},
	
	];
	var zNodes =[
	{id:1, pId:0, name: "咖啡"},
	{id:11, pId:1, name: "拿铁咖啡"},
	{id:12, pId:1, name: "焦糖咖啡"},
	{id:13, pId:1, name: "酷酷咖啡"},
	{id:2, pId:0, name: "可乐"},
	{id:21, pId:2, name: "拿铁咖啡"},
	{id:22, pId:2, name: "焦糖咖啡"},
	{id:23, pId:2, name: "酷酷咖啡"},
	
];

	var treefn={
		treeFnBack:function(treeObj){
			console.log(treeObj);
		},
		
	}


	$(document).ready(function() {
		fnList.initData("#menu-list-template",menu,"#menu-tb");
		fnList.initData("#details-list-template",details,"#details-tb");
		initTreeDate(zNodes,treefn.treeFnBack);
		$("table .menu_configure").click(function(){
				window.location.href="../menuManage/menuset.html";
			});
	// $(".container-page").css("height",$(window).height());


	});
});
