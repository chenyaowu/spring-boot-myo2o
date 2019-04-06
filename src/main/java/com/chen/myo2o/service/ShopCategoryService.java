package com.chen.myo2o.service;

import com.chen.myo2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {
    String SCLISTKEY = "shopcategorylist";

    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
