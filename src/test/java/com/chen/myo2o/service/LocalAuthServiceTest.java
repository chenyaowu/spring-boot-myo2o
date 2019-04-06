package com.chen.myo2o.service;

import com.chen.myo2o.dto.LocalAuthExecution;
import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.enums.LocalAuthStateEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest{

    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testBindLocalAuth(){
        //新增一条平台账号
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        String username = "testusername";
        String password = "testpassword";
        //给平台账号设置用户信息
        //给用户设置上用户Id,标明是某个用户创建的账号
        personInfo.setUserId(1L);
        //给平台账号设置用户信息，标明是与哪个用户绑定
        localAuth.setPersonInfo(personInfo);
        //设置账号，密码
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        //绑定账号
        LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(),lae.getState());
        //通过userId找到新增的localAuth
        localAuth  = localAuthService.getLocalAuthByUserId(1l);
        //打印用户名和密码
        System.out.println("用户账号："+localAuth.getUsername());
        System.out.println("用户密码："+localAuth.getPassword());

    }

    @Test
    public void testModifyLocalAuth(){
        String username = "testusername";
        String password = "testpassword";
        LocalAuthExecution lae = localAuthService.modifyLocalAuth(1l,username,password,password+"new");
        assertEquals(LocalAuthStateEnum.SUCCESS.getState(),lae.getState());
    }

}
