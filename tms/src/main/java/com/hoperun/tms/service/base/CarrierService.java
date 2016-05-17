package com.hoperun.tms.service.base;
 
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.CarrierExample;
import com.hoperun.tms.bean.base.extend.ExCarrier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.base.Carrier;
import com.hoperun.tms.mapper.base.CarrierMapper;

@Service
public class CarrierService {

	@Autowired
	private CarrierMapper carrierMapper;

	public List<Carrier> getCarrierList(CarrierExample e){
         return carrierMapper.selectByExample(e);
	}
	
	public Map getCarrierListPaginate(CarrierExample e,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<Carrier> carrierList =getCarrierList(e);
		Long totalrecords = startPage.getTotal();
		map.put("carrierList", carrierList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}

	/**
	 * carrier必须包含主键ID
	 * @param carrier
	 * @throws Exception
	 */
	public void updateById(Carrier carrier) throws Exception {
		if (null == carrier.getId()) {
			throw new Exception("承运商信息更新失败，未正确获取承运商ID");
		}
		uniqueValicate(carrier);
		carrier.setUpdatedBy("ceshi");
		carrier.setUpdatedAt(new Date());
		carrierMapper.updateByPrimaryKeySelective(carrier);
	}

	public void updateMultiCarriers(Carrier carrier, CarrierExample e) throws Exception {
		carrierMapper.updateByExampleSelective(carrier, e);
	}

	/**
	 * 承运商作废
	 * carrier必须包含主键ID
	 * @throws Exception
	 */
	public void disable(String[] ids) throws Exception {
		for (String id : ids) {
			Carrier carrier = new Carrier();
			carrier.setId(Long.parseLong(id));
			carrier.setStatus(ExCarrier.STATUS_DISABLED);
			carrier.setUpdatedBy("ceshi");
			carrier.setUpdatedAt(new Date());
			if (null == carrier.getId()) {
				throw new Exception("承运商信息删除失败，未正确获取承运商ID");
			}
			carrierMapper.updateByPrimaryKeySelective(carrier);
		}
	}

	public void insertCarrier(Carrier carrier) throws Exception {

		carrier.setCreatedBy("ceshi");
		carrier.setCreatedAt(new Date());
		carrier.setUpdatedBy("ceshi");
		carrier.setUpdatedAt(new Date());
		carrierMapper.insertSelective(carrier);
	}

	private void uniqueValicate(Carrier carrier) throws Exception {
		CarrierExample e = new CarrierExample();
		CarrierExample.Criteria c = e.createCriteria();
		c.andNameEqualTo(carrier.getName()).andStatusEqualTo(ExCarrier.STATUS_ENABLED);
		if (null != carrier.getId()) {
			c.andIdNotEqualTo(carrier.getId());
		}
		List<Carrier> carriers = carrierMapper.selectByExample(e);
		if (!carriers.isEmpty()) {
			throw new Exception("承运商:" + carrier.getName() + " 在列表中已存在。");
		}
	}
}
