package com.hoperun.tms.util;
/*
 * 
 */
import com.dascom.DL210.SmartPrint;
import com.hoperun.tms.bean.customer.extend.ExWaybill;

public class PrintBill{
		public void  printBill(ExWaybill w)throws Exception{
			System.out.println("==================begin SmartPrint");
			SmartPrint dll= new SmartPrint();
			System.out.println("==================create SmartPrint complete");
			String portname=dll.DSEnumPrinter();
			System.out.println("=================init complete");
			
			dll.DSClosePrinter();
			dll.DSOpenPrinter(portname.substring(portname.indexOf("@")+1));

			dll.DSZPLStart();//发开始标志
			dll.DSZPLSetPageLength(7.3);			//设置页长
			dll.DSZPLPrintLine(0.35,0.2,1,3.8, 0.01);//横线
			dll.DSZPLPrintText(1.75,0.3, 3,3 ,"上海");
			dll.DSZPLPrintLine(0.35,0.7,1,3.8, 0.01);//横线
			dll.DSZPLPrintText(0.45,0.75, 2,2 ,"一米市集仓库");
//			dll.DSZPLPrintLine(2.8,0.7,2,0.4, 0.01);//竖线
//			dll.DSZPLSetBarcodeDefaults(2, 3);
//			dll.DSZPLPrintCode128(2.9,0.75,1,2,0.2,false,false,"210901");
//			dll.DSZPLPrintText(2.9,0.97, 1,1 ,"2 1 0 9 0 1");//条码注释
			dll.DSZPLPrintLine(0.35,1.1,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.35,2,1,2.85, 0.01);//横线
			dll.DSZPLPrintLine(0.32,2.5,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.55,1.1,2,1.4, 0.01);//竖线
			dll.DSZPLPrintLine(3.2,1.1,2,1.4, 0.01);//竖线
			dll.DSZPLPrintLine(3.2,1.3,1,0.9, 0.01);//横线
			dll.DSZPLPrintText(0.35,1.3, 1,1 ,"收");
			dll.DSZPLPrintText(0.35,1.5, 1,1 ,"件");
			dll.DSZPLPrintText(0.65,1.2, 1,1 ,w.getrContact() + " " + w.getrTel());//收件人
			dll.DSZPLPrintText(0.65,1.4, 1,1 ,w.getrCity() + " " + w.getrAddress());//收件地址
			dll.DSZPLPrintText(0.65,2.1, 1,1 ,w.getdContact() + " " + w.getdTel());//寄件人
			dll.DSZPLPrintText(0.35,2.1, 1,1 ,"寄");
			dll.DSZPLPrintText(0.35,2.3, 1,1 ,"件");
			dll.DSZPLPrintText(0.65,2.3, 1,1 ,w.getdCity() + " " + w.getdAddress());//寄件地址
			dll.DSZPLPrintText(3.55,1.15, 1,1 ,"服务");
			dll.DSZPLPrintText(3.25,1.35, 1,1 ,"付款方式：");
			dll.DSZPLPrintText(3.25,1.5, 1,1 ,"服务方式：");
			dll.DSZPLPrintText(3.25,1.65, 1,1 ,"到单返还：");
			dll.DSZPLPrintText(3.25,1.85, 2,1 ,"代收金额：");
			dll.DSZPLSetBarcodeDefaults(4, 3);
			dll.DSZPLPrintCode128(1.2,2.65,1,2,0.4,false,false,w.getNo());
			dll.DSZPLPrintText(1.5,3.07, 1,1 ,w.getNo());
			dll.DSZPLPrintLine(0.3,3.3,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(2.43,3.3,2,0.9, 0.01);//竖线
			dll.DSZPLPrintLine(3.4,3.3,2,0.9, 0.01);//竖线
			dll.DSZPLPrintText(0.35,3.4, 1,1 ,"快递描述收件人地址，收件人或者寄");
			dll.DSZPLPrintText(0.35,3.55, 1,1 ,"件人允许签收，视为描述：您的签字");
			dll.DSZPLPrintText(0.35,3.7, 1,1 ,"代表您已验收此包裹，并确认商品完");
			dll.DSZPLPrintText(0.35,3.85, 1,1 ,"好无损，没有划痕，没有破损等质量");
			dll.DSZPLPrintText(0.35,4, 1,1 ,"问题。");
			dll.DSZPLPrintText(2.55,3.4, 2,1 ,"签收人：");
			dll.DSZPLPrintText(2.55,3.8, 2,1 ,"时间：");
			dll.DSZPLPrintQrCode(3.45,3.35,1,3 ,w.getNo());//二维码
			
			dll.DSZPLSetBarcodeDefaults(2, 3);
			dll.DSZPLPrintCode128(2.7,4.4,1,2,0.3,false,false,w.getNo());
			dll.DSZPLPrintText(2.85,4.72, 1,1 ,w.getNo());
			dll.DSZPLPrintLine(0.3,5.5,1,3.0, 0.01);//横线
			dll.DSZPLPrintLine(0.3,6.0,1,3.8, 0.01);//横线
			dll.DSZPLPrintLine(0.5,5.0,2,1, 0.01);//竖线
			dll.DSZPLPrintLine(3.3,5.0,2,1, 0.01);//竖线
			dll.DSZPLPrintText(0.35,5.1, 1,1 ,"收");
			dll.DSZPLPrintText(0.35,5.3, 1,1 ,"件");
			dll.DSZPLPrintText(0.6,5.05, 1,1 ,w.getrContact() + " " + w.getrTel());
			dll.DSZPLPrintText(0.6,5.2, 1,1 ,w.getrCity() + " " + w.getrAddress());
			dll.DSZPLPrintText(0.35,5.6, 1,1 ,"寄");
			dll.DSZPLPrintText(0.35,5.8, 1,1 ,"件");
			dll.DSZPLPrintText(0.6,5.55, 1,1 ,w.getdContact() + " " + w.getdTel());
			dll.DSZPLPrintText(0.6,5.7, 1,1 ,w.getdCity() + " " + w.getdAddress());
			dll.DSZPLPrintQrCode(3.35,5.1,1,3 ,w.getNo());//二维码
			dll.DSZPLPrintText(3.8,6.5, 1,1 ,"已验视");
			dll.DSZPLEnd();//发结束标志，开始打印

			
			
	}
		
		public String appendSpace(String  para){  
	        int length = para.length();  
	        char[] value = new char[length << 1];  
	        for (int i=0, j=0; i<length; ++i, j = i << 1) {  
	            value[j] = para.charAt(i);  
	            value[1 + j] = ' ';  
	        }  
	        return new String(value);  
	    } 
		public String isEmpty(String content){
			if(content == null){
				return "";
			}
			return content;
		}
}