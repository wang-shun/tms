package com.hoperun.tms.util;

import com.dascom.DL210.SmartPrint;

public class Print{
	public static void main(String[] a){
		SmartPrint dll= new SmartPrint();
		String portname=dll.DSEnumPrinter();
		System.out.println("打印机="+portname.substring(portname.indexOf("@")));
		dll.DSOpenPrinter(portname.substring(portname.indexOf("@")+1));
		printtest(dll);
//		printtest(dll);
//		printtest(dll);
	}
		public static void  printtest(SmartPrint dll){

			dll.DSZPLStart();//发开始标志
			dll.DSZPLSetPageLength(7.3);			//设置页长
			dll.DSZPLPrintLine(0.25,0.2,1,3.8, 0.01);//横线
			dll.DSZPLPrintText(1.65,0.3, 3,3 ,"沪西");
			dll.DSZPLPrintLine(0.25,0.7,1,3.8, 0.01);//横线
			dll.DSZPLPrintText(0.35,0.75, 2,2 ,"上海分拨中心");
//			dll.DSZPLPrintLine(2.8,0.7,2,0.4, 0.01);//竖线
//			dll.DSZPLSetBarcodeDefaults(2, 3);
//			dll.DSZPLPrintCode128(2.9,0.75,1,2,0.2,false,false,"210901");
//			dll.DSZPLPrintText(2.9,0.97, 1,1 ,"2 1 0 9 0 1");//条码注释
			dll.DSZPLPrintLine(0.25,1.1,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.25,2,1,2.85, 0.01);//横线
			dll.DSZPLPrintLine(0.22,2.5,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.45,1.1,2,1.4, 0.01);//竖线
			dll.DSZPLPrintLine(3.1,1.1,2,1.4, 0.01);//竖线
			dll.DSZPLPrintLine(3.1,1.3,1,0.9, 0.01);//横线
			dll.DSZPLPrintText(0.25,1.3, 1,1 ,"收");
			dll.DSZPLPrintText(0.25,1.5, 1,1 ,"件");
			dll.DSZPLPrintText(0.55,1.2, 1,1 ,"测试13917110554");
			dll.DSZPLPrintText(0.55,1.4, 1,1 ,"上海市长宁区 测试单");
			dll.DSZPLPrintText(0.55,2.1, 1,1 ,"一米市集400-655-1212");
			dll.DSZPLPrintText(0.25,2.1, 1,1 ,"寄");
			dll.DSZPLPrintText(0.25,2.3, 1,1 ,"件");
			dll.DSZPLPrintText(0.55,2.3, 1,1 ,"上海市长宁区 天山西路789号");
			dll.DSZPLPrintText(3.45,1.15, 1,1 ,"服务");
			dll.DSZPLPrintText(3.15,1.35, 1,1 ,"付款方式：");
			dll.DSZPLPrintText(3.15,1.5, 1,1 ,"服务方式：");
			dll.DSZPLPrintText(3.15,1.65, 1,1 ,"到单返还：");
			dll.DSZPLPrintText(3.15,1.85, 2,1 ,"代收金额：");
			dll.DSZPLSetBarcodeDefaults(4, 3);
			dll.DSZPLPrintCode128(1.1,2.65,1,2,0.4,false,false,"806641423527");
			dll.DSZPLPrintText(1.4,3.07, 1,1 ,"8 0 6 6 4 1 4 2 3 5 2 7");
			dll.DSZPLPrintLine(0.2,3.3,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(2.33,3.3,2,0.9, 0.01);//竖线
			dll.DSZPLPrintLine(3.3,3.3,2,0.9, 0.01);//竖线
			dll.DSZPLPrintText(0.25,3.4, 1,1 ,"快递描述收件人地址，收件人或者寄");
			dll.DSZPLPrintText(0.25,3.55, 1,1 ,"件人允许签收，视为描述：您的签字");
			dll.DSZPLPrintText(0.25,3.7, 1,1 ,"代表您已验收此包裹，并确认商品完");
			dll.DSZPLPrintText(0.25,3.85, 1,1 ,"好无损，没有划痕，没有破损等质量");
			dll.DSZPLPrintText(0.25,4, 1,1 ,"问题。");
			dll.DSZPLPrintText(2.45,3.4, 2,1 ,"签收人：");
			dll.DSZPLPrintText(2.45,3.8, 2,1 ,"时间：");
			dll.DSZPLPrintQrCode(3.35,3.35,1,3 ,"收件：测试13917110554,上海上海市长宁区 测试单");
			
			dll.DSZPLSetBarcodeDefaults(2, 3);
			dll.DSZPLPrintCode128(2.6,4.4,1,2,0.3,false,false,"806641423527");
			dll.DSZPLPrintText(2.75,4.72, 1,1 ,"806641423527");
			dll.DSZPLPrintLine(0.2,5.5,1,3.0, 0.01);//横线
			dll.DSZPLPrintLine(0.2,6.0,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.4,5.0,2,1, 0.01);//竖线
			dll.DSZPLPrintLine(3.2,5.0,2,1, 0.01);//竖线
			dll.DSZPLPrintText(0.25,5.1, 1,1 ,"收");
			dll.DSZPLPrintText(0.25,5.3, 1,1 ,"件");
			dll.DSZPLPrintText(0.5,5.05, 1,1 ,"测试13917110554");
			dll.DSZPLPrintText(0.5,5.2, 1,1 ,"上海上海市长宁区 测试单");
			dll.DSZPLPrintText(0.25,5.6, 1,1 ,"寄");
			dll.DSZPLPrintText(0.25,5.8, 1,1 ,"件");
			dll.DSZPLPrintText(0.5,5.55, 1,1 ,"一米市集400-655-1212");
			dll.DSZPLPrintText(0.5,5.7, 1,1 ,"上海上海市长宁区 天山西路789号");
			dll.DSZPLPrintQrCode(3.25,5.1,1,3 ,"收件：测试13917110554,上海上海市长宁区 测试单");
			dll.DSZPLPrintText(3.7,6.5, 1,1 ,"已验视");
			dll.DSZPLEnd();//发结束标志，开始打印

			
	}
}
