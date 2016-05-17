package com.hoperun.tms.service.customer;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.customer.DeliveryBillTmp;
import com.hoperun.tms.bean.customer.DeliveryBillTmpExample;
import com.hoperun.tms.mapper.customer.DeliveryBillTmpMapper;

@Service
public class DeliveryBillTmpService {

	@Autowired
	private DeliveryBillTmpMapper deliveryBillTmpMapper;
	
	
	public List<DeliveryBillTmp> getDeliveryBillTmpList(DeliveryBillTmpExample r){
         List<DeliveryBillTmp> rlist = null;
		try {
			rlist = deliveryBillTmpMapper.selectByExample(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
         return rlist;
	}
	
	public Map getDeliveryBillTmpListPaginate(DeliveryBillTmpExample r,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<DeliveryBillTmp> deliveryBillTmpList =getDeliveryBillTmpList(r);
		Long totalrecords = startPage.getTotal();
		map.put("deliveryBillTmpList", deliveryBillTmpList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}


	public long delDeliveryBillTmpById(DeliveryBillTmpExample d) {
		try {
			long status = deliveryBillTmpMapper.deleteByExample(d);
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void updateDeliveryBillTmp(DeliveryBillTmp deliveryBillTmp) {
		try {
			deliveryBillTmpMapper.updateDeliveryBillTmp(deliveryBillTmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertDeliveryBillTmp(DeliveryBillTmp deliveryBillTmp) {
		try {
			deliveryBillTmpMapper.insertDeliveryBillTmp(deliveryBillTmp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
