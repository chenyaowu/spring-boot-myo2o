package com.chen.myo2o.dao;

import com.chen.myo2o.entity.LocalAuth;
import com.chen.myo2o.entity.PersonInfo;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {

    @Autowired
    private LocalAuthDao localAuthDao;
    private String username = "test";
    private String password = "123456";

    @Test
    public void testBQueryLocalByUserNameAndPwd(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(username,password);
        assertNotNull(localAuth);
    }

    @Test
    public void testCQueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1l);
        assertNotNull(localAuth);

    }

    @Test
    public void testAInsertLocalAuth(){
        // 新增一条平台账号
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1l);
        //给平台账号绑定上用户信息
        localAuth.setPersonInfo(personInfo);
        //设置上用户名和密码
        localAuth.setUsername(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        int effectNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1,effectNum);
    }

    @Test
    public void testDUpdateLocalAuth(){
        Date now = new Date();
        int effectNum = localAuthDao.updateLocalAuth(1l,username,password,password+"new",now);
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1l);
        assertEquals(localAuth.getPassword(),password+"new");
    }
}
