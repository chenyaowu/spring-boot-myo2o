package com.chen.myo2o.dao;

import com.chen.myo2o.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopDao {
    /**
     * 通过shop id查询店铺
     * @param shopId
     * @return
     */
    Shop queryByShopId(long shopId);
    /**
     * 新增店铺
     * @param shop
     * @return
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺
     * @param shop
     * @return
     */
    int updateShop(Shop shop);


    //分页查询店铺，可输入的条件有店铺名（模糊），店铺状态，店铺类别，区域Id,owner
    /*
    rowIndex 从第几行开始取，
    pageSize 返回条数
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIntdex, @Param("pageSize") int pageSize);

    /*
        返回queryShopList总数
     */
    int queryShopCount(@Param("shopCondition") Shop shopConditon);

}
