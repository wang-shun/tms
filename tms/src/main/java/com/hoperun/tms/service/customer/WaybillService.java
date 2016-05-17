package com.hoperun.tms.service.customer;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.framework.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillLog;
import com.hoperun.tms.bean.customer.WaybillLogExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.bean.customer.extend.WaybillTransportDetail;
import com.hoperun.tms.constants.ExWaybillLogConstants;
import com.hoperun.tms.mapper.customer.extend.ExWaybillLogMapper;
//import com.hoperun.tms.mapper.customer.extend.ExWaybillLogMapper;
import com.hoperun.tms.mapper.customer.extend.ExWaybillMapper;

@Service
public class WaybillService {
	@Autowired
	private ExWaybillMapper exWaybillMapper;
	
	@Autowired
	private ExWaybillLogMapper exWaybillLogMapper;
	
	public Map getWaybillListPaginate(WaybillExample waybillExample,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<ExWaybill> waybillList = exWaybillMapper.selectByExample(waybillExample);
		Long totalrecords = startPage.getTotal();
		map.put("waybillList", waybillList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}

	public ExWaybill getWaybillById(Long id) {
		return exWaybillMapper.selectByPrimaryKey(id);
	}

	/**
	 * 根据运单号获取运单信息（不存在或存在多条记录时抛出异常）
	 * @param no
	 * @return
	 * @throws Exception
     */
	public ExWaybill getWaybillByNo(String no) throws Exception{
		WaybillExample e = new WaybillExample();
		e.createCriteria().andNoEqualTo(no).andStatusNotEqualTo(ExWaybill.STATUS_CANCELED);
		List<ExWaybill> waybillList = exWaybillMapper.selectByExample(e);
		if(waybillList.isEmpty()){
			throw new Exception("运单号:"+no+"不存在。");
		}else if(waybillList.size()>1){
			throw new Exception("运单号:"+no+"异常！请联系系统管理员。");
		}else{
			return waybillList.get(0);
		}
	}

	public List<ExWaybill> getAll(WaybillExample waybillExample){
		List<ExWaybill> waybillList = exWaybillMapper.selectByExample(waybillExample);
		return waybillList;
	}

	public List<ExWaybill> selectByExample(WaybillExample waybillExample){
		return exWaybillMapper.selectByExample(waybillExample);
	}

	public Waybill selectWaybillById(Long id) {
		return exWaybillMapper.selectByPrimaryKey(id);
	}

	public WaybillTransportDetail selectWaybillTransportDetail(String no) {
		return exWaybillMapper.selectWaybillTransportDetail(no);
	}

	public List<Map<String, String>> queryDNamesBySource(String source){
		return exWaybillMapper.queryDNamesBySource(source);
	}

	public void updateWaybill(ExWaybill waybill)  throws Exception {
		if (null == waybill.getId()) {
			throw new Exception("运单信息更新失败，未正确获取运单ID");
		}
		waybill.setUpdatedBy(SessionUtils.getUserInfo());
		waybill.setUpdatedAt(new Date());
		exWaybillMapper.updateByPrimaryKeySelective(waybill);
	}

	public void updateWaybillAndLog(ExWaybill waybill,String wLog,String wStatus)  throws Exception {
		if (null == waybill.getId()) {
			throw new Exception("运单信息更新失败，未正确获取运单ID");
		}
		waybill.setUpdatedBy(SessionUtils.getUserInfo());
		waybill.setUpdatedAt(new Date());
		exWaybillMapper.updateByPrimaryKeySelective(waybill);

		insertWaybillLog(waybill,wLog, wStatus);
	}

	/**
	 * 修改记录
	 * @param waybill
	 * @throws Exception
	 */
	public void update(ExWaybill waybill, WaybillExample waybillExample)  throws Exception {
		waybill.setUpdatedAt(new Date());
		waybill.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybillMapper.updateByExampleSelective(waybill, waybillExample);
	}
	public void confirmSettlementStatus(String[] ids)  throws Exception {
		for (String id : ids) {
			ExWaybill waybill = getWaybillById(Long.parseLong(id));
			waybill.setId(Long.parseLong(id));
			if (ExWaybill.UNLIQUIDATED.equals(waybill.gettFeeStatus())) {
				waybill.setStatus(ExWaybill.STATUS_CONFIRMED);
			} else {
				throw new Exception("运单号【" + waybill.getNo() + "】当前状态无需确认。");
			}
			updateWaybill(waybill);
		}
	}

	public void insertWaybill(ExWaybill waybill) {
		waybill.setCreatedBy(SessionUtils.getUserInfo());
		waybill.setCreatedAt(new Date());
		waybill.setUpdatedBy(SessionUtils.getUserInfo());
		waybill.setUpdatedAt(new Date());
		exWaybillMapper.insertSelective(waybill);
	}

	public void insertOrUpdateWaybill(ExWaybill waybill) throws Exception {
		Long id = waybill.getId();
		waybill.setStatus(ExWaybill.STATUS_CREATED);//设置默认状态是00
		if(null==id){
			String waybillNo = exWaybillMapper.selectWaybillNo();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).replace("-", "");
			waybillNo="X"+dateStr.substring(2, dateStr.length())+waybillNo;
			waybill.setNo(waybillNo);
			waybill.setStatus("00");
			waybill.setCreatedBy(SessionUtils.getUserInfo());
			waybill.setCreatedAt(new Date());
			waybill.setUpdatedBy(SessionUtils.getUserInfo());
			waybill.setUpdatedAt(new Date());
			exWaybillMapper.insertSelective(waybill);
		}else{
			Waybill waybillDb = exWaybillMapper.selectByPrimaryKey(id);
			if(ExWaybill.STATUS_CREATED.equals(waybillDb.getStatus())){
				waybill.setUpdatedBy(SessionUtils.getUserInfo());
				waybill.setUpdatedAt(new Date());
				exWaybillMapper.updateByPrimaryKey(waybill);
			}else{
				throw new Exception("这个运单不在创建状态。");
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

	public void confirmWaybills(String[] ids)  throws Exception {
		for (String id : ids) {
			ExWaybill waybill = getWaybillById(Long.parseLong(id));
			waybill.setId(Long.parseLong(id));
			if (ExWaybill.STATUS_CREATED.equals(waybill.getStatus())) {
				waybill.setStatus(ExWaybill.STATUS_CONFIRMED);
			} else {
				throw new Exception("运单号【" + waybill.getNo() + "】当前状态不需要确认。");
			}
			String log = "运单已确认，发货方是【" + waybill.getdName() + "】发货人是【" + waybill.getdContact() + "】，联系电话【"
					+ waybill.getdTel() + "】，正从【" + waybill.getdAddress() + "】派车";
			updateWaybillAndLog(waybill, log, ExWaybillLogConstants.W_STATUS_CONFIRMED);
		}
	}
    
   /**
    * 扫码操作
    * @param waybill
    * @throws Exception
    */
	public void sacnUpdate(ExWaybill waybill, WaybillExample waybillExample,WaybillLog waybillLog)  throws Exception {
		update(waybill, waybillExample);

		waybillLog.setCreatedAt(new Date());
		waybillLog.setCreatedBy(SessionUtils.getUserInfo());
		waybillLog.setUpdatedAt(new Date());
		waybillLog.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybillLogMapper.insertSelective(waybillLog);
	}

	/**
	 * 扫码回退
	 *
	 * @param waybill
	 * @throws Exception
	 */
	public void sacnRollBack(ExWaybill waybill, WaybillExample waybillExample, WaybillLogExample waybillLogExample) throws Exception {
		waybill.setUpdatedAt(new Date());
		waybill.setUpdatedBy(SessionUtils.getUserInfo());
		exWaybillMapper.updateByExampleSelective(waybill, waybillExample);
		exWaybillLogMapper.deleteByExample(waybillLogExample);//删除日志
	}

	/**
	 * 统计运单总数
	 * @param 
	 */
	public int countWayBill(WaybillExample waybillExample) {
		return exWaybillMapper.countByExample(waybillExample);
	}

	public void disableWaybills(String[] ids)  throws Exception {
		for (String id : ids) {
			ExWaybill waybill = getWaybillById(Long.parseLong(id));
			waybill.setId(Long.parseLong(id));
			if (ExWaybill.STATUS_CONFIRMED.equals(waybill.getStatus())
					|| ExWaybill.STATUS_CREATED.equals(waybill.getStatus())) {
				waybill.setStatus(ExWaybill.STATUS_CANCELED);
			} else if (ExWaybill.STATUS_CANCELED.equals(waybill.getStatus())) {
				throw new Exception("运单号【" + waybill.getNo() + "】当前状态已经作废。");
			} else {
				throw new Exception("运单号【" + waybill.getNo() + "】当前状态不允许作废。");
			}
			updateWaybillAndLog(waybill, ExWaybillLogConstants.W_LOG_CANCEL,
					ExWaybillLogConstants.W_STATUS_CANCEL);
		}
	}

	public long delWaybillById(Long id) {
		return exWaybillMapper.deleteByPrimaryKey(id);
	}

	public void saveTransportFee(String[] ids, BigDecimal transportFee) throws Exception {
		for (String id : ids) {
			ExWaybill waybill = getWaybillById(Long.parseLong(id));
			waybill.setId(Long.parseLong(id));
			waybill.setTransportFee(transportFee);
			if (ExWaybill.UNLIQUIDATED.equals(waybill.gettFeeStatus())) {
				waybill.setStatus(ExWaybill.STATUS_CONFIRMED);
			} else {
				throw new Exception("运单号【" + waybill.getNo() + "】当前状态无需确认。");
			}
			updateWaybill(waybill);
		}
	}

	public Map getWaybillList(WaybillExample waybillExample) {
		Map<String,Object> map = new HashMap<String,Object>();
		List<ExWaybill> waybillList = exWaybillMapper.selectByExample(waybillExample);
		map.put("waybillList", waybillList);
		return null;
	}
}
