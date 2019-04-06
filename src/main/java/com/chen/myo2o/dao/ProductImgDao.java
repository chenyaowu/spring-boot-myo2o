package com.chen.myo2o.dao;

import com.chen.myo2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    //查询图片列表
    List<ProductImg> queryProductImgList(long productId);
    //批量插入商品图片
    int batchInsertProductImg(List<ProductImg> productImgList);
    //删除商品图片
    int deleteProductImgByProductId(long productId);
}
