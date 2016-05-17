package com.hoperun.tms.service.customer;

import java.util.Date;
import java.util.List;

import com.hoperun.framework.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hoperun.tms.bean.base.Carrier;
import com.hoperun.tms.bean.customer.Waybill;
import com.hoperun.tms.bean.customer.WaybillExample;
import com.hoperun.tms.bean.customer.WaybillPowercar;
import com.hoperun.tms.bean.customer.WaybillPowercarExample;
import com.hoperun.tms.bean.customer.extend.ExWaybill;
import com.hoperun.tms.mapper.customer.WaybillPowercarMapper;

@Service
public class WaybillPowercarService {

    @Autowired
    private WaybillPowercarMapper wayBillPowercarMapper;

    public List<WaybillPowercar> selectWaybillPowercarByWno(WaybillPowercarExample waybillEx) {
        return wayBillPowercarMapper.selectByExample(waybillEx);
    }

    public void insertWaybillPowercar(WaybillPowercar waybillPowercar) throws Exception {
        waybillPowercar.setCreatedAt(new Date());
        waybillPowercar.setCreatedBy(SessionUtils.getUserInfo());
        waybillPowercar.setUpdatedAt(new Date());
        waybillPowercar.setUpdatedBy(SessionUtils.getUserInfo());
        wayBillPowercarMapper.insertSelective(waybillPowercar);
    }

    public void updateWaybillPowerCar(WaybillPowercar waybillPowercar
            , WaybillPowercarExample waybillPowercarExample) throws Exception {
        waybillPowercar.setUpdatedAt(new Date());
        waybillPowercar.setUpdatedBy(SessionUtils.getUserInfo());
        wayBillPowercarMapper.updateByExampleSelective(waybillPowercar, waybillPowercarExample);
    }
}
