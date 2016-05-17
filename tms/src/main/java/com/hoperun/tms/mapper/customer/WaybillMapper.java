package com.hoperun.tms.mapper.customer; 

import java.util.List;
import java.util.Map;

import com.hoperun.framework.annotation.DataSource;
import com.hoperun.framework.base.BaseMapper;
import org.springframework.stereotype.Component;

/**
 *  DAO
 * @author 15120071
 * 2016-3-22
 */
@Component
public interface WaybillMapper extends BaseMapper{
  @DataSource("master")
  public List<Map<String, String>> queryDNamesBySource(String source);
}
 