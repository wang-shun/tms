package com.hoperun.tms.mapper.base.extend;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.framework.base.BaseBean;
import com.hoperun.framework.base.BaseMapper;
import com.hoperun.tms.mapper.base.SenderMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 16030117 on 2016/4/6.
 */
@Component
public interface ExSenderMapper extends SenderMapper {
    @DataSource("master")
    public List<Map> selectForOptions();
}
