package com.hoperun.tms.mapper.base.extend;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.mapper.base.ReceiverMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * DAO
 *
 * @author 15120071
 *         2016-3-22
 */
@Component
public interface ExReceiverMapper extends ReceiverMapper {
    @DataSource("master")
    public List<Map> selectForOptions();

}
 