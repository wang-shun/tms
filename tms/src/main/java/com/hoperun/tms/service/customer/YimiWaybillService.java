package com.hoperun.tms.service.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.SessionUtils;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillExample.Criteria;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.WaybillLogExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.constants.ExWaybillLogConstants;
import com.hoperun.tms.mapper.customer.extend.ExWaybillLogMapper;
import com.hoperun.tms.mapper.customer.extend.ExWaybillMapper;
import com.hoperun.tms.util.StringUtil;

/**
 * Created by 16030117 on 2016/4/6.
 */
@Service
public class YimiWaybillService {
	@Autowired
	private ExWaybillMapper exWaybillMapper;
	
	@Autowired
	private ExWaybillLogMapper exWaybillLogMapper;

    public Map getWaybillListPaginate(WaybillExample waybillExample, int pageNumber, int pageSize){
        Map<String,Object> map = new HashMap<String,Object>();
        Page startPage = PageHelper.startPage(pageNumber, pageSize);
        List<Waybill> waybillList = exWaybillMapper.selectByExample(waybillExample);
        Long totalrecords = startPage.getTotal();
        map.put("waybillList", waybillList);
        map.put("currpage", pageNumber);
        map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
        map.put("totalrecords", totalrecords);
        return map;
    }

	/**一米导入运单
	 * 如果运单号导入过，就执行update操作
	 **/
	public void importWaybill(List<LinkedHashMap> waybillList) throws Exception {
		for (int i =0;i<waybillList.size();i++) {
			try {
				String json = JacksonJsonUtil.beanToJson(waybillList.get(i));
				ExWaybill waybill = (ExWaybill) JacksonJsonUtil.jsonToBean(json, new ExWaybill().getClass());
				dataValidation(waybill);
				String wLog = "运单已确认，发货方是【" + waybill.getdName() + "】发货人是【" + waybill.getdContact() + "】，联系电话【"
						+ waybill.getdTel() + "】，正从【" + waybill.getdAddress() + "】派车";
				WaybillExample waybillExample = new WaybillExample();
				Criteria criteria = waybillExample.createCriteria();
				criteria.andNoEqualTo(waybill.getNo());
				List<ExWaybill> wbs = exWaybillMapper.selectByExample(waybillExample);
				waybill.setTicketNo(waybill.getOrderNo());//将一米的客户订单号放到面单号这个字段。
				waybill.setSource("1");
				waybill.setStatus(ExWaybill.STATUS_CONFIRMED);
				waybill.setScanStatus(ExWaybill.UNSCANED);
				waybill.setUpdatedBy(SessionUtils.getUserInfo());
				waybill.setUpdatedAt(new Date());
				if (wbs != null && wbs.size() != 0) {
					ExWaybill exWaybill = wbs.get(0);
					if (!ExWaybill.STATUS_CONFIRMED.equals(exWaybill.getStatus())) {
						throw new Exception("运单【" + exWaybill.getNo() + "】状态不在确认状态。");
					}
					waybill.setId(exWaybill.getId());
					exWaybillMapper.updateByPrimaryKeySelective(waybill);
					updasteWaybillLog(waybill, wLog, ExWaybillLogConstants.W_STATUS_CONFIRMED);
				} else {
					waybill.setCreatedBy(SessionUtils.getUserInfo());
					waybill.setCreatedAt(new Date());
					exWaybillMapper.insertSelective(waybill);
					insertWaybillLog(waybill, wLog, ExWaybillLogConstants.W_STATUS_CONFIRMED);
				}
			}catch (Exception e) {
				throw new Exception("第" + (i + 1) + "行数据导入出错：" + e.getMessage());
			}
		}
	}
	private void insertWaybillLog(ExWaybill waybill,String wlog,String wStatus){
		WaybillLog waybillLog = new WaybillLog();
		waybillLog.setStatus("1");
		waybillLog.setwId(waybill.getId());
		waybillLog.setwNo(waybill.getNo());
		waybillLog.setwLog(wlog); 
		waybillLog.setwStatus(wStatus); 
		waybillLog.setCreatedAt(new Date());
		waybillLog.setCreatedBy(SessionUtils.getUserInfo());
		waybillLog.setUpdatedAt(new Date());
		waybillLog.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybillLogMapper.insertSelective(waybillLog);
	}
	private void updasteWaybillLog(ExWaybill waybill,String wlog,String wStatus){
		WaybillLogExample waybillLogExample = new WaybillLogExample();
		WaybillLogExample.Criteria criteria = waybillLogExample.createCriteria();
		criteria.andWNoEqualTo(waybill.getNo());
		criteria.andWStatusEqualTo(wStatus);
		WaybillLog waybillLog = new WaybillLog();
		waybillLog.setStatus("1");
		waybillLog.setwId(waybill.getId());
		waybillLog.setwNo(waybill.getNo());
		waybillLog.setwLog(wlog);
		waybillLog.setwStatus(wStatus);
		waybillLog.setCreatedAt(new Date());
		waybillLog.setCreatedBy(SessionUtils.getUserInfo());
		waybillLog.setUpdatedAt(new Date());
		waybillLog.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybillLogMapper.updateByExampleSelective(waybillLog,waybillLogExample);
	}

	private void dataValidation(ExWaybill waybill) throws Exception{
		if(StringUtil.isNullOrEmpty(waybill.getOrderNo())){
			throw new Exception("客户订单号为空");
		}
		/*if(StringUtil.isNullOrEmpty(waybill.getRecipient())){
			throw new Exception("揽件人为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getReceiveAt())){
			throw new Exception("揽件时间为空");
		}*/
		if(StringUtil.isNullOrEmpty(waybill.getDeliveryType())){
			throw new Exception("运输方式为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getDeliveryDate())){
			throw new Exception("配送日期为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getDeliveryTime())){
			throw new Exception("配送时间段为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdName())){
			throw new Exception("发货人为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdTel())){
			throw new Exception("发货人电话为空");
		}else if(!StringUtil.isPhone(waybill.getdTel())){
			throw new Exception("发货人电话【"+waybill.getdTel()+"】不正确");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdCity())){
			throw new Exception("发货方城市为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdCounty())){
			throw new Exception("发货方区/县为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdArea())){
			throw new Exception("发货城区为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getdAddress())){
			throw new Exception("发货地址为空");
		}
		if(waybill.getdAddress().length()>200){
			throw new Exception("发货地址超长，不允许超过200字节或100个汉字");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrName())){
			throw new Exception("收货人为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrTel())){
			throw new Exception("收货人电话为空");
		}else if(!StringUtil.isPhone(waybill.getrTel())){
			throw new Exception("收货人电话【"+waybill.getrTel()+"】不正确");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrCity())){
			throw new Exception("收货方城市为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrCounty())){
			throw new Exception("收货方区/县为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrArea())){
			throw new Exception("收货城区为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getrAddress())){
			throw new Exception("收货地址为空");
		}
		if(waybill.getrAddress().length()>200){
			throw new Exception("收货地址超长，不允许超过200字节或100个汉字");
		}
		if(StringUtil.isNullOrEmpty(waybill.getDetailName())){
			throw new Exception("物品名称为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getDetailType())){
			throw new Exception("物品种类为空");
		}
		if(StringUtil.isNullOrEmpty(waybill.getDetailWeight())){
			throw new Exception("物品重量为空");
		}

	}
}
