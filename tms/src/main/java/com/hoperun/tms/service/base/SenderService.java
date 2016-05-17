package com.hoperun.tms.service.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hoperun.tms.bean.base.Sender;
import com.hoperun.tms.bean.base.SenderExample;
import com.hoperun.tms.bean.base.extend.ExSender;
import com.hoperun.tms.mapper.base.extend.ExSenderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 16030117 on 2016/3/31.
 */
@Service
public class SenderService {
    @Autowired
    private ExSenderMapper exSenderMapper;

    public Sender queryById(Long id){
        return exSenderMapper.selectByPrimaryKey(id);
    }

    public List<Sender> queryByList(SenderExample senderExample){
        return exSenderMapper.selectByExample(senderExample);
    }

    public List<Map> queryForOptions(){
        return exSenderMapper.selectForOptions();
    }

    public Map getDriverListPaginate(SenderExample senderExample, int pageNumber, int pageSize){
        Map<String,Object> map = new HashMap<String,Object>();
        Page startPage = PageHelper.startPage(pageNumber, pageSize);
        List<Sender> senderList = queryByList(senderExample);
        Long totalrecords = startPage.getTotal();
        map.put("senderList", senderList);
        map.put("currpage", pageNumber);
        map.put("totalpages", totalrecords==0?1:(totalrecords-1)/pageSize+1);
        map.put("totalrecords", totalrecords);
        return map;
    }

    public void add(Sender sender) throws Exception{
        uniqueValidate(sender);
        sender.setCreatedBy("ceshi");
        sender.setCreatedAt(new Date());
        sender.setUpdatedBy("ceshi");
        sender.setUpdatedAt(new Date());
        exSenderMapper.insert(sender);
    }

    public void updateMultiSenders(Sender sender, SenderExample e) throws Exception{
        sender.setUpdatedBy("ceshi");
        sender.setUpdatedAt(new Date());
        exSenderMapper.updateByExampleSelective(sender, e);
    }

    /**
     * sender必须包含主键ID
     * @param sender
     * @throws Exception
     */
    public void updateById(Sender sender) throws Exception{
        if (null == sender.getId()) {
            throw new Exception("发货方更新失败，未正确获取发货人ID");
        }
        uniqueValidate(sender);
        sender.setUpdatedBy("ceshi");
        sender.setUpdatedAt(new Date());
        exSenderMapper.updateByPrimaryKeySelective(sender);
    }

    /**
     * 发货方作废
     * sender必须包含主键ID
     * @throws Exception
     */
    public void disable(String[] ids) throws Exception {
        for(String id : ids){
            if (null == id) {
                throw new Exception("发货方删除失败，未正确获取发货人ID");
            }
            Sender sender = new Sender();
            sender.setId(Long.parseLong(id));
            sender.setStatus(ExSender.STATUS_DISABLED);
            sender.setUpdatedBy("ceshi");
            sender.setUpdatedAt(new Date());
            exSenderMapper.updateByPrimaryKeySelective(sender);
        }
    }

    private void uniqueValidate(Sender sender) throws Exception{
        SenderExample e = new SenderExample();
        SenderExample.Criteria c = e.createCriteria();
        c.andNameEqualTo(sender.getName()).andStatusEqualTo(ExSender.STATUS_ENABLED);
        if (null != sender.getId()) {
            c.andIdNotEqualTo(sender.getId());
        }
        List<Sender> ExistedVehicleDrivers = exSenderMapper.selectByExample(e);
        if (!ExistedVehicleDrivers.isEmpty()) {
            throw new Exception("发货方"+sender.getName()+"的信息已存在。");
        }
    }
}
