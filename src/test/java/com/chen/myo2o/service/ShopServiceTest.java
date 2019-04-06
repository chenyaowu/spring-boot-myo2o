package com.chen.myo2o.service;

import com.chen.myo2o.dto.ImageHolder;
import com.chen.myo2o.dto.ShopExecution;
import com.chen.myo2o.entity.Area;
import com.chen.myo2o.entity.PersonInfo;
import com.chen.myo2o.entity.Shop;
import com.chen.myo2o.entity.ShopCategory;
import com.chen.myo2o.enums.ShopStateEnum;
import com.chen.myo2o.exception.ShopOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws FileNotFoundException {

        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1l);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1l);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺1");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setShopImg("test");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("E:\\image\\916492631372800465.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution se =  shopService.addShop(shop,new ImageHolder(shopImg.getName(),is));
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }

    @Test
    public void testModifyShop() throws ShopOperationException,FileNotFoundException{
        Shop shop = new Shop() ;
        shop.setShopId(1l);
        shop.setShopName("修改后的店铺名称");
        File shopImg = new File("e://AOF7.jpg");
        InputStream is = new FileInputStream(shopImg);
        ShopExecution shopExecution = shopService.modifyShop(shop,new ImageHolder("test.jpg",is));
        System.out.println(shopExecution.getShop().getShopImg());
    }
    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(3L);
        shopCondition.setShopCategory(shopCategory);
        ShopExecution shopExecution = shopService.getShopList(shopCondition,1,2);
        System.out.println(shopExecution.getShopList().size());
        System.out.println(shopExecution.getCount());
    }
}
