package com.chen.myo2o.service;

import com.chen.myo2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryServiceTest {
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Test
    public void testGetShopCategoryList(){
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(1l);

        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setParent(parent);
        List<ShopCategory> shopCategoryList =shopCategoryService.getShopCategoryList(shopCategory);
        assertEquals(1,shopCategoryList.size());
    }
}
