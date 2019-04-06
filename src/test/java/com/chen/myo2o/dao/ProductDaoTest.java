package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Product;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest {
    @Autowired
    private ProductDao productDao;

    @Test
    public void testInsertProduct(){
        Shop shop = new Shop();
        shop.setShopId(1L);
        ProductCategory pc1 = new ProductCategory();
        pc1.setProductCategoryId(2L);
        //初始化三个商品类别并添加进shopId为1的店铺里
        //同时商品类别Id也为1
        Product product = new Product();
        product.setProductName("测试1");
        product.setProductDesc("测试desc");
        product.setImgAddr("test1");
        product.setPriority(1);
        product.setEnableStatus(1);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        product.setShop(shop);
        product.setProductCategory(pc1);
        Product product1 = new Product();
        product1.setProductName("测试2");
        product1.setProductDesc("测试desc2");
        product1.setImgAddr("test2");
        product1.setPriority(2);
        product1.setEnableStatus(0);
        product1.setCreateTime(new Date());
        product1.setLastEditTime(new Date());
        product1.setShop(shop);
        product1.setProductCategory(pc1);

        Product product2 = new Product();
        product2.setProductName("测试3");
        product2.setProductDesc("测试desc3");
        product2.setImgAddr("test3");
        product2.setPriority(3);
        product2.setEnableStatus(0);
        product2.setCreateTime(new Date());
        product2.setLastEditTime(new Date());
        product2.setShop(shop);
        product2.setProductCategory(pc1);
        //判断是否成功
        int effectNum = productDao.insertProduct(product);
        assertEquals(1,effectNum);
        effectNum = productDao.insertProduct(product1);
        assertEquals(1,effectNum);
        effectNum = productDao.insertProduct(product2);
        assertEquals(1,effectNum);

    }
    @Test
    public void testqueryProductById(){
        productDao.queryProductById(2l);
    }
    @Test
    public void testUpdateProduct(){
        Product product = new Product();
        ProductCategory productCategory = new ProductCategory();
        Shop shop = new Shop();
        shop.setShopId(1l);
        productCategory.setProductCategoryId(2l);
        product.setProductId(2l);
        product.setProductName("测试商品2");
        product.setProductCategory(productCategory);
        product.setShop(shop);
        int effectNum = productDao.updateProduct(product);
        assertEquals(1,effectNum);
    }

    @Test
    public void testQueryProductList(){
        Product productCondition = new Product();
        List<Product> productList = productDao.queryProductList(productCondition,0,3);
        assertEquals(3,productList.size());

        int count = productDao.queryProductCount(productCondition);
        assertEquals(5,count);

        productCondition.setProductName("测试");
        productList = productDao.queryProductList(productCondition,0,3);
        assertEquals(3,productList.size());

        count = productDao.queryProductCount(productCondition);
        assertEquals(3,count);
    }

    @Test
    public void testUpdateProductCategoryToNull(){
        int effectNum = productDao.updateProductCategoryToNull(11);
        assertEquals(1,effectNum);
    }
}
