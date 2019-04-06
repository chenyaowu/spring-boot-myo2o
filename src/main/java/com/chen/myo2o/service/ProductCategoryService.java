package com.chen.myo2o.service;

import com.chen.myo2o.dto.ProductCategoryExecution;
import com.chen.myo2o.entity.ProductCategory;
import com.chen.myo2o.exception.ProductCategoryOperationException;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategory> getProductCategoryList(long shopId);

    ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategoryOperationException;

    ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategoryOperationException;

    List<ProductCategory> getByShopId(long shopId);


}
