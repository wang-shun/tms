package com.hoperun.tms.service.base;
 
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hoperun.tms.bean.base.ReceiverExample;
import com.hoperun.tms.bean.base.ReceiverExample.Criteria;
import com.hoperun.tms.bean.base.extend.ExReceiver;
import com.hoperun.tms.mapper.base.extend.ExReceiverMapper;
import com.hoperun.tms.util.ValidateUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.framework.base.BaseBean;
import com.hoperun.framework.utils.JacksonJsonUtil;
import com.hoperun.framework.utils.StringUtil;
import com.hoperun.tms.bean.base.Receiver;

@Service
public class ReceiverService {

	@Autowired
	private ExReceiverMapper exReceiverMapper;

	public List<Receiver> getReceiverList(ReceiverExample e){
         return exReceiverMapper.selectByExample(e);
	}
	
	public Map getReceiverListPaginate(ReceiverExample e,int pageNumber,int pageSize){
		Map<String,Object> map = new HashMap<String,Object>();
		Page startPage = PageHelper.startPage(pageNumber, pageSize);
		List<Receiver> receiverList =getReceiverList(e);
		Long totalrecords = startPage.getTotal();
		map.put("receiverList", receiverList);
		map.put("currpage", pageNumber);
		map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
		map.put("totalrecords", totalrecords);
		return map;
	}

	public List<Map> queryForOptions(){
		return exReceiverMapper.selectForOptions();
	}

	public Receiver queryById(Long id){
		return exReceiverMapper.selectByPrimaryKey(id);
	}

	public void disable(String[] ids) throws Exception{
		for (String id : ids) {
			if (null == id) {
				throw new Exception("收货方信息删除失败，未正确获取承运商ID");
			}
			Receiver receiver = new Receiver();
			receiver.setId(Long.parseLong(id));
			receiver.setStatus(ExReceiver.STATUS_DISABLED);
			receiver.setUpdatedBy("ceshi");
			receiver.setUpdatedAt(new Date());
			exReceiverMapper.updateByPrimaryKeySelective(receiver);
		}
	}

	public void updateMultiCarriers(Receiver receiver, ReceiverExample e) throws Exception {
		receiver.setUpdatedBy("ceshi");
		receiver.setUpdatedAt(new Date());
		exReceiverMapper.updateByExampleSelective(receiver, e);
	}
	public void updateBatch(List<LinkedHashMap> receiverList) throws Exception {
	        for (int i = 0;i<receiverList.size();i++) {
	        	String json = JacksonJsonUtil.beanToJson(receiverList.get(i));
	        	ExReceiver receiver = (ExReceiver) JacksonJsonUtil.jsonToBean(json, new ExReceiver().getClass());
	        	ReceiverExample e = new ReceiverExample();
	        	Criteria criteria = e.createCriteria();
	        	criteria.andIdNotEqualTo(receiver.getId());
	        	criteria.andTelEqualTo(receiver.getTel());
				List<ExReceiver> exReceiverList = exReceiverMapper.selectByExample(e);
				if(!exReceiverList.isEmpty()){
					throw new Exception("第"+(i+1)+"条数据，手机号码已存在");
				}
				ReceiverExample rex = new ReceiverExample();
				rex.createCriteria().andIdEqualTo(receiver.getId());
                receiver.setUpdatedBy("ceshi");
                receiver.setUpdatedAt(new Date());
	        	exReceiverMapper.updateByExampleSelective(receiver, rex);
			}
	}

	/**
	 * receiver必须包含主键ID
	 * @param receiver
	 * @throws Exception
	 */
	public void updateById(Receiver receiver) throws Exception {
		if (null == receiver.getId()) {
			throw new Exception("收货方信息修改失败，未正确获取收货方ID");
		}
		receiver.setUpdatedBy("ceshi");
		receiver.setUpdatedAt(new Date());
		exReceiverMapper.updateByPrimaryKey(receiver);
	}

	public void insertReceiver(Receiver receiver) {
		receiver.setCreatedBy("ceshi");
		receiver.setCreatedAt(new Date());
		exReceiverMapper.insertSelective(receiver);
	}

	public void exceltodb(MultipartFile buildInfo) throws Exception{
			InputStream in = buildInfo.getInputStream();
		
		List<Receiver> rList = new ArrayList<Receiver>();
		XSSFWorkbook wb = new XSSFWorkbook(in);
		 
		   HSSFCell cell = null;
		 
		   for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
		 
			   XSSFSheet st = wb.getSheetAt(sheetIndex);
		 
		       // 第一行为标题，不取
		 
		       for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
		 
		    	   XSSFRow  row = st.getRow(rowIndex);
		          String name = row.getCell(0) != null?row.getCell(0).getStringCellValue():"";
		          String city = row.getCell(1) != null?row.getCell(1).getStringCellValue():"";
		          String area = row.getCell(2) != null?row.getCell(2).getStringCellValue():"";
		          String contact =  row.getCell(3)!= null?row.getCell(3).getStringCellValue():"";
		          String tel = row.getCell(4)!= null?row.getCell(4).getStringCellValue():"";
		          String address = row.getCell(5)!= null?row.getCell(5).getStringCellValue():"";
		          String county =  row.getCell(6)!= null?row.getCell(6).getStringCellValue():"";
		          
		          if(!StringUtil.isValid(county) &&!StringUtil.isValid(name) && !StringUtil.isValid(city)  && !StringUtil.isValid(area)  && !StringUtil.isValid(contact)&& !StringUtil.isValid(tel)  && !StringUtil.isValid(address)){
		        	  continue;
		          }
		          
		          Receiver  receiver = new Receiver();
		          receiver.setName(name);
		          receiver.setCity(city);
		          receiver.setArea(area);
		          receiver.setContact(contact);
		          if(ValidateUtil.validatePhone(tel)){
		        	ReceiverExample rex = new ReceiverExample();
		        	Criteria criteria = rex.createCriteria();
		        	criteria.andTelEqualTo(tel);
					List<ExReceiver> exReceiverList = exReceiverMapper.selectByExample(rex);
					if(!exReceiverList.isEmpty()){
						throw new Exception("第"+rowIndex+"条数据中，手机号码已存在");
					}
		          }else{
		        	  throw new Exception("第"+rowIndex+"条数据中，手机号码不正确");
		          }
		          receiver.setTel(tel);
		          receiver.setAddress(address);
		          receiver.setCounty(county);
		          receiver.setStatus("1");
		          
		          rList.add(receiver);
		       }
		   }
		   in.close();
		   if(rList.size() > 0){
			   for(Receiver  receiver:rList){
				   insertReceiver(receiver);
			   }
		   }
		   
	}
}
