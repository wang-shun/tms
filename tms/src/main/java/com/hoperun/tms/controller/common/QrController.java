/**
 * 打印二维码
 *
 * @author 15120071
 * 2016-4-20
 */
package com.hoperun.tms.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hoperun.tms.service.qrcode.QrService;



@Controller
public class QrController {

    @Autowired
    QrService qrService;

	@RequestMapping(value="/qrcode")
	public void mtqrcode(@RequestParam("qrinfo") String qrinfo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		qrService.getQRCode(response, qrinfo);
	}
}
