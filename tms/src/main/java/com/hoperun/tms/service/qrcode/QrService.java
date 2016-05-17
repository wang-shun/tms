package com.hoperun.tms.service.qrcode;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.hoperun.tms.constants.TwoDimensionCode;



@Service("qrService.java")
public class QrService {


	public void getQRCode(HttpServletResponse response, String content) throws Exception{
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");

		BufferedImage bi = new TwoDimensionCode().qRCodeCommon(content, "jpg", 7);
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
	}

}
