package com.hoperun.tms.service.customer;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.framework.base.BaseBean;
import com.hoperun.framework.utils.SessionUtils;
import com.hoperun.framework.utils.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.customer.DeliveryBill;
import com.hoperun.tms.bean.customer.DeliveryBillExample;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.extend.ExDeliveryBill;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.mapper.customer.DeliveryBillMapper;
import com.hoperun.tms.mapper.customer.WaybillLogMapper;
import com.hoperun.tms.mapper.customer.WaybillMapper;
import com.hoperun.tms.mapper.customer.extend.ExDeliveryBillMapper;

@Service
public class DeliveryBillService {

	@Autowired
	private DeliveryBillMapper deliveryBillMapper;
	
	@Autowired
	private ExDeliveryBillMapper exDeliveryBillMapper;
	
	@Autowired
	private WaybillLogMapper waybillLogMapper;
	
	@Autowired
	private WaybillMapper waybillMapper;
	
	public List<DeliveryBill> getDeliveryBillList(DeliveryBillExample r)throws Exception{
         List<DeliveryBill> rlist = deliveryBillMapper.selectByExample(r);
         return rlist;
	}
	
	public Map getDeliveryBillListPaginate(DeliveryBillExample r,int pageNumber,int pageSize)throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<DeliveryBill> deliveryBillTmpList = getDeliveryBillList(r);
		Long totalrecords = startPage.getTotal();
		map.put("deliveryBillList", deliveryBillTmpList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}

	public long delDeliveryBillById(DeliveryBillExample d)throws Exception {
		long status = deliveryBillMapper.deleteByExample(d);
		return status;
	}
	
	public void insertDeliveryBill(DeliveryBill deliveryBill)throws Exception {
		deliveryBill.setCreatedBy(SessionUtils.getUserInfo());
		deliveryBill.setCreatedAt(new Date());
		deliveryBill.setUpdatedBy(SessionUtils.getUserInfo());
		deliveryBill.setUpdatedAt(new Date());
		deliveryBillMapper.insertSelective(deliveryBill);
	}
	
	public void updateDeliveryBill(DeliveryBill deliveryBill,DeliveryBillExample deliveryBillExample) throws Exception{
		deliveryBill.setUpdatedBy(SessionUtils.getUserInfo());
		deliveryBill.setUpdatedAt(new Date());
		deliveryBillMapper.updateByExample(deliveryBill,deliveryBillExample);
	}
	
	public void updateDeliveryBillForApi(DeliveryBill deliveryBill,DeliveryBillExample deliveryBillExample,List<WaybillLog> list)throws Exception {
		deliveryBill.setUpdatedBy(SessionUtils.getUserInfo());
		deliveryBill.setUpdatedAt(new Date());
		deliveryBillMapper.updateByExample(deliveryBill,deliveryBillExample);
		for(WaybillLog waybillLog:list){
			waybillLog.setCreatedBy(SessionUtils.getUserInfo());
			waybillLog.setCreatedAt(new Date());
			waybillLog.setUpdatedBy(SessionUtils.getUserInfo());
			waybillLog.setUpdatedAt(new Date());
			waybillLogMapper.insertSelective(waybillLog);
		}
	}
	
	/**
	 * 创建配送单(回写运单,写日志)
	 * @param 
	 */
	public void createDeliveryBillEx(
			ExDeliveryBill exDeliveryBill, ExWaybill exWaybill,
			WaybillExample waybillExample, List<WaybillLog> list) {
		exDeliveryBill.setCreatedBy(SessionUtils.getUserInfo());
		exDeliveryBill.setCreatedAt(new Date());
		exDeliveryBill.setUpdatedBy(SessionUtils.getUserInfo());
		exDeliveryBill.setUpdatedAt(new Date());
		exDeliveryBillMapper.insertSelective(exDeliveryBill);

		exWaybill.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybill.setUpdatedAt(new Date());
		waybillMapper.updateByExampleSelective(exWaybill, waybillExample);
		for(WaybillLog waybillLog:list){
			waybillLog.setCreatedBy(SessionUtils.getUserInfo());
			waybillLog.setCreatedAt(new Date());
			waybillLog.setUpdatedBy(SessionUtils.getUserInfo());
			waybillLog.setUpdatedAt(new Date());
			waybillLogMapper.insertSelective(waybillLog);
		}	
	}
	/**
	 * 生成预分配配送单
	 * @param 
	 * @throws Exception 
	 */
	public void createPredistributionDeliveryBillEx(ExDeliveryBill exDeliveryBill, String[] waybillIds) throws Exception {
		exDeliveryBill.setCreatedBy(SessionUtils.getUserInfo());
		exDeliveryBill.setCreatedAt(new Date());
		exDeliveryBill.setUpdatedBy(SessionUtils.getUserInfo());
		exDeliveryBill.setUpdatedAt(new Date());
		exDeliveryBillMapper.insertSelective(exDeliveryBill);
		for (String id : waybillIds) {
			Waybill waybill = waybillMapper.selectByPrimaryKey(Long.parseLong(id));
			if(ExWaybill.STATUS_PREDISTRIBUTION.equals(waybill.getStatus())||!StringUtil.isEmpty(waybill.getDeliveryNo())){
				throw new Exception("运单【"+waybill.getNo()+"】已分配");
			}
			waybill.setDeliveryId(exDeliveryBill.getId());
	        waybill.setDeliveryNo(exDeliveryBill.getNo());
			waybill.setUpdatedBy(SessionUtils.getUserInfo());
			waybill.setUpdatedAt(new Date());
			waybillMapper.updateByPrimaryKeySelective(waybill);
		}
	}
	public DeliveryBill getDeliveryBillById(Long id){
		DeliveryBill deliveryBill = null;
		try {
			deliveryBill = deliveryBillMapper.selectByPrimaryKey(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deliveryBill;
	}
}
