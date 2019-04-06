package com.chen.myo2o.service;

import com.chen.myo2o.dto.ImageHolder;
import com.chen.myo2o.dto.ProductExecution;
import com.chen.myo2o.entity.Product;
import com.chen.myo2o.exception.ProductOperationException;

import java.util.List;

public interface ProductService {
    /**
     * 添加商品已经图片处理
     * @param product
     * @param imageHolder
     * @param imageHolderList
     * @return
     * @throws ProductOperationException
     */
    ProductExecution addProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException;

    Product getProductById(long id);

    ProductExecution modifyProduct(Product product, ImageHolder imageHolder, List<ImageHolder> imageHolderList) throws ProductOperationException;

    /**
     * 查询商品列表并分页，可以输入条件有：商品名称（模糊），商品状态，店铺Id,商品类别
     * @param productCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ProductExecution getProductList(Product productCondition, int pageIndex, int pageSize);



}
