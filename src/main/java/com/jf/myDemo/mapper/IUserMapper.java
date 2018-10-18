package com.jf.myDemo.mapper;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Wangjie
 * @Date: 2018-08-21 17:06
 * @Description: 存储过程调用测试Mapper
 * To change this template use File | Settings | File and Templates.
 */
@Component
public interface IUserMapper {
    Map<String,Object> getUserCount(Map<String,Object> map);
}
