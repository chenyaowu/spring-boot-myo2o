package com.chen.myo2o.dao;

import com.chen.myo2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest{
    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testAQueryByShopId(){
        Long shopId = 1l;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        System.out.println(productCategoryList.size());
    }

    @Test
    public void testBBatchInsertProductCategory(){
        ProductCategory productCategory1 = new ProductCategory();
        ProductCategory productCategory2 = new ProductCategory();
        productCategory1.setPriority(1);
        productCategory1.setCreateTime(new Date());
        productCategory1.setShopId(1L);
        productCategory1.setProductCategoryName("商品类别1");

        productCategory2.setPriority(2);
        productCategory2.setCreateTime(new Date());
        productCategory2.setShopId(13L);
        productCategory2.setProductCategoryName("商品类别2");

        List<ProductCategory> list = new ArrayList<ProductCategory>();
        list.add(productCategory1);
        list.add(productCategory2);
        int effectNum = productCategoryDao.batchInsertProductCategory(list);
        assertEquals(effectNum,2);
    }
    @Test
    public void testCDelteProductCategory(){
        long shopId = 1L;
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory productCategory:productCategoryList){
            if(productCategory.getProductCategoryName().equals("店铺类别1") || productCategory.getProductCategoryName().equals("店铺类别2")){
                int effectedNum = productCategoryDao.deleteProductCategory(productCategory.getProductCategoryId(),shopId);
                assertEquals(1,effectedNum);
            }
        }

    }
}
