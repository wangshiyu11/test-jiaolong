package com.test.dao;

import com.test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:24
 */
public interface UserDao extends JpaRepository<User,Long> {

    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public User findByLoginName(String loginName);


}
