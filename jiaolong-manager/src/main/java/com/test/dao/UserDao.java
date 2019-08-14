package com.test.dao;

import com.test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:24
 */
@Mapper
@Component
public interface UserDao{

    List<User> userList(@Param(value = "name") String name, @Param(value = "dt1") String dt1, @Param(value = "dt2") String dt2, @Param(value = "xingbie") String xingbie);

    void del(Long id);

    int add(User user);

    int update(User user);

    int findByloginName(String loginName);
}
