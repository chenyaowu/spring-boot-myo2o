package com.chen.myo2o.service;

import com.chen.myo2o.entity.Area;

import java.util.List;

public interface AreaService {
    String AREALISTKEY = "arealist";
    List<Area> getAreaList();
}
