//package com.hoperun.shinho.action;
//
//import java.io.File;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.hoperun.framework.annotation.NotAuth;
//import com.hoperun.framework.utils.HtmlUtil;
//import com.hoperun.framework.utils.SpringContextUtil;
//import com.hoperun.framework.utils.StringUtil;
//import com.hoperun.shinho.bean.UserEntity;
//import com.hoperun.shinho.service.UserService;
//
///**
// * <br>
// * <b>功能：</b>LoginController<br>
// * <b>作者：</b>Wang Jiahao<br>
// * <b>日期：</b> Dec 7, 2015 <br>
// * <b>版权所有：<b>版权所有(C) 2015，www.hoperun.com<br>
// */
//@Controller
//@RequestMapping("/test")
//public class TestController {
//
//	private final static Logger LOGGER = Logger.getLogger(TestController.class);
//
//	@Autowired
//	private UserService userService;
//
//	@RequestMapping(value = "/forwardtest", method = RequestMethod.GET)
//	@NotAuth
//	public String forwardtest(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		LOGGER.debug("user login test");
//		request.setAttribute("name2", "value2");
//		return "forward:/api/login/forwardtest2";
//	}
//
//	@RequestMapping(value = "/forwardtest2", method = RequestMethod.GET)
//	@NotAuth
//	public String forwardtest2(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		LOGGER.debug("user login test");
//		//
//		System.out.println("name1:" + request.getParameter("name1"));
//		System.out.println("name2:" + request.getAttribute("name2"));
//		return "page1";
//	}
//
//	@RequestMapping(value = "/toupload", method = RequestMethod.GET)
//	@NotAuth
//	public String toupload(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		LOGGER.debug("user login test");
//		// return "upload";
//		LOGGER.debug("forward:" + request.getContextPath() + "/html/order/index.html");
//		return "forward:" + "/html/order/index.html";
//	}
//
//	/***
//	 * 上传文件 用@RequestParam注解来指定表单上的file为MultipartFile
//	 * 
//	 * @param file
//	 * @return
//	 */
//	@RequestMapping("filesUpload")
//	@NotAuth
//	public String filesUpload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
//		for (MultipartFile file : files) {
//			saveFile(file, request);
//		}
//		// 重定向
//		return "redirect:list";
//	}
//
//	private void saveFile(MultipartFile file, HttpServletRequest request) {
//		// 判断文件是否为空
//		if (!file.isEmpty()) {
//			try {
//				// 文件保存路径
//				String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/"
//						+ file.getOriginalFilename();
//				// 转存文件
//				file.transferTo(new File(filePath));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	/***
//	 * 读取上传文件中得所有文件并返回
//	 * 
//	 * @return
//	 */
//	@RequestMapping("list")
//	@NotAuth
//	public void list(HttpServletRequest request, HttpServletResponse response) {
//		String filePath = request.getSession().getServletContext().getRealPath("/") + "upload/";
//		File uploadDest = new File(filePath);
//		String[] fileNames = uploadDest.list();
//		for (int i = 0; i < fileNames.length; i++) {
//			// 打印出文件名
//			System.out.println(fileNames[i]);
//		}
//		HtmlUtil.writerSuccessJson(response, fileNames);
//	}
//
//	/**
//	 * 
//	 * @param request
//	 * @return
//	 */
//	@RequestMapping(value = "/getContext", method = RequestMethod.GET)
//	@NotAuth
//	public void getContext(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		System.out.println(SpringContextUtil.getApplicationContext());
//		WebApplicationContext applicationContext = (WebApplicationContext) SpringContextUtil.getApplicationContext();
//		applicationContext.getServletContext().getContext("/");
//		HtmlUtil.writerSuccessJson(response, applicationContext.getServletContext().getRealPath("/"));
//	}
//
//	@RequestMapping(value = "/toPage", method = RequestMethod.GET)
//	@NotAuth
//	public String toPage(HttpServletResponse response) throws Exception {
//		// 将生成的页面内容返回
//		return "PaySuccessful";
//	}
//
//	@RequestMapping(value = "/usertest1", method = RequestMethod.GET)
//	@NotAuth
//	public void getUserTest1(HttpServletResponse response) throws Exception {
//		UserEntity userEntity = userService.queryByCellPhone("123456");
//		HtmlUtil.writerSuccessJson(response, userEntity);
//	}
//
//	@RequestMapping(value = "/usertest2/{memberId}", method = RequestMethod.GET)
//	@NotAuth
//	public void getUserTest2(@PathVariable String memberId, HttpServletResponse response) throws Exception {
//		UserEntity userEntity = userService.queryById(memberId);
//		HtmlUtil.writerSuccessJson(response, userEntity);
//	}
//
//	@RequestMapping(value = "/usertest3", method = RequestMethod.GET)
//	@NotAuth
//	public void getUserTest3(HttpServletResponse response) throws Exception {
//		UserEntity userEntity = userService.queryByOpenId("openid");
//		HtmlUtil.writerSuccessJson(response, userEntity);
//	}
//
//	@RequestMapping(value = "/usertest4", method = RequestMethod.GET)
//	@NotAuth
//	public void createUserTest() throws Exception {
//		// userService.createUserByOpenId("openid");
//	}
//
//	@RequestMapping(value = "/usertest5/{memberId}", method = RequestMethod.GET)
//	@NotAuth
//	public void setUserLastTimeTest(@PathVariable String memberId, HttpServletResponse response) throws Exception {
//		userService.updateLastDate(memberId);
//	}
//
//	@RequestMapping(value = "/testSeq", method = RequestMethod.GET)
//	@NotAuth
//	public void testSeq(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		HtmlUtil.writerSuccessJson(response, "6" + StringUtil.fillZero(userService.queryCustomerIdSeq(), 9));
//	}
//}
