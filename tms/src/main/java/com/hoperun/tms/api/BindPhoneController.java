package com.hoperun.tms.api;

import com.hoperun.framework.utils.HtmlUtil;
import com.hoperun.tms.bean.base.Driver;
import com.hoperun.tms.bean.base.DriverExample;
import com.hoperun.tms.bean.base.DriverExample.Criteria;
import com.hoperun.tms.service.base.DriverService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/api/bindPhone"})
public class BindPhoneController
{

  @Autowired
  DriverService driverService;

  @RequestMapping(value={"/bindPhone/{phone}/{openid}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public void query(@PathVariable String phone, @PathVariable String openid, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DriverExample e = new DriverExample();
    e.createCriteria().andContactEqualTo(phone);
    try {
      List rlist = this.driverService.getDriverList(e);
      if (rlist.isEmpty()) {
        HtmlUtil.writerFailJsonp(request, response, "请联系管理员维护手机号！");
        return;
      }
      Driver driver = (Driver)rlist.get(0);
      driver.setWxOpenId(openid);
      this.driverService.updateMutiDrivers(driver, e);
      HtmlUtil.writerJsonp(request, response, driver);
    } catch (Exception ex) {
      ex.printStackTrace();
      HtmlUtil.writerFailJsonp(request, response, ex.getMessage());
      return;
    }
  }
}