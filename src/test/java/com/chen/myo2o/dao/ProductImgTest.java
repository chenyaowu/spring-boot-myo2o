package com.chen.myo2o.dao;


import com.chen.myo2o.entity.ProductImg;
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
public class ProductImgTest  {

    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testBQueryProductImgList(){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(2);
        assertEquals(2,productImgList.size());
    }
    @Test
    public void testABatchInsertProductImg(){
        ProductImg productImg = new ProductImg();
        productImg.setImgAddr("图片1");
        productImg.setImgDesc("测试图片1");
        productImg.setPriority(1);
        productImg.setCreateTime(new Date());
        productImg.setProductId(2l);
        ProductImg productImg2 = new ProductImg();
        productImg2.setImgAddr("图片2");
        productImg2.setImgDesc("测试图片2");
        productImg2.setPriority(2);
        productImg2.setCreateTime(new Date());
        productImg2.setProductId(2L);
        List<ProductImg> productImgList = new ArrayList<ProductImg>();
        productImgList.add(productImg);
        productImgList.add(productImg2);
        int effectedNum = productImgDao.batchInsertProductImg(productImgList);
        assertEquals(2,effectedNum);
    }
    @Test
    public void testCDeleteProductImgByProductId(){
        long productId = 2;
        int effectedNum= productImgDao.deleteProductImgByProductId(productId);
        assertEquals(2,effectedNum);
    }
}
