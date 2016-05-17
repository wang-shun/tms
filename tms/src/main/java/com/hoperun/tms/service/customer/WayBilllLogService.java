package com.hoperun.tms.service.customer;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.customer.*;
import com.hoperun.tms.constants.ExWaybillLogConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.tms.mapper.customer.extend.ExWaybillLogMapper;

@Service
public class WayBilllLogService {
	@Autowired
	private ExWaybillLogMapper waybillLogMapper;
	@Autowired
	WaybillService waybillService;
	@Autowired
	DeliveryBillService deliveryBillService;

	public List<WaybillLog> selectWaybillLogByWno(WaybillLogExample waybillEx) {
		return waybillLogMapper.selectByExample(waybillEx);
	}

	public Map getLogsInfo(String wNo) throws Exception {
		Map resultMap = new HashMap();
		WaybillLogExample e = new WaybillLogExample();
		e.createCriteria().andWNoEqualTo(wNo).andStatusEqualTo("1");
		e.setOrderByClause("ID");
		List<WaybillLog> waybillLogs = selectWaybillLogByWno(e);
		if(waybillLogs.isEmpty()){
			throw new Exception("运单("+wNo+")尚无任何跟踪信息。");
		}
		resultMap.put("waybillLogList", waybillLogs);

		Waybill waybill = waybillService.getWaybillByNo(wNo);
		resultMap.put("waybill", waybill);

		String wStatus = waybillLogs.get(waybillLogs.size()-1).getwStatus();
		if(!ExWaybillLogConstants.W_STATUS_CONFIRMED.equals(wStatus)
				&& !ExWaybillLogConstants.W_STATUS_CART_RECEIVE.equals(wStatus)
				&& !ExWaybillLogConstants.W_STATUS_CANCEL.equals(wStatus)
				&& !StringUtil.isEmpty(waybill.getDeliveryNo())){
			DeliveryBillExample dbEx = new DeliveryBillExample();
			dbEx.createCriteria().andNoEqualTo(waybill.getDeliveryNo());
			List<DeliveryBill> deliveryBillList = deliveryBillService.getDeliveryBillList(dbEx);
			if(!deliveryBillList.isEmpty()){
				resultMap.put("deliveryBill", deliveryBillList.get(0));
			}
		}
		return resultMap;
	}
}
