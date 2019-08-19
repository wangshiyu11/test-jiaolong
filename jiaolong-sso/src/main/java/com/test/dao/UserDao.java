package com.test.dao;

import com.test.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author ：WSY
 * @description :TODO
 * @date ：Created in 2019/8/5 14:24
 */
public interface UserDao extends JpaRepository<User,Long> {

    @Query(value = "select * from base_user where loginName=?1",nativeQuery = true)
    public User findByLoginName(String loginName);

    @Query(value = "select * from base_user where userName=?1",nativeQuery = true)
    public User findUsername(String userName);

    //根据手机号查询用户
    @Query(value = "select * from base_user where tel=?1",nativeQuery = true)
    public User findUserByTel(String tel);

    //点击忘记密码输入邮箱 然后点击 然后通过邮箱邮件进行更改密码  输入新的密码 判断两次密码输入是否相同 修改密码成功
    //重新登录 根据最新的密码进行登录 （修改密码提交的时候需要用到md5加密）
    @Transactional
    @Modifying
    @Query(value = "update base_user u set u.password=?1 where u.userName=?2",nativeQuery = true)
    public void updatePassword(String password,String username);

    @Query(value = "select * from base_user where email=?1",nativeQuery = true)
    User getEmailCode(String email);
}
