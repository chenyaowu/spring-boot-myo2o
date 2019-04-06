package com.chen.myo2o.dao;


import com.chen.myo2o.entity.Area;

import java.util.List;

public interface AreaDao {
    /*
    列出区域列表
    @Return areaList
     */
    List<Area> queryArea();
}
