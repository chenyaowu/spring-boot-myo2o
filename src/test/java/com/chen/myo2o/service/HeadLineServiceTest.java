package com.chen.myo2o.service;

import com.chen.myo2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineServiceTest  {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private CacheService cacheService;


    @Test
    public void testGetHeadLineList() throws IOException {
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        List<HeadLine> headLineList = headLineService.getHeadLineList(headLineCondition);
        assertEquals(2,headLineList.size());
        cacheService.removeFromCache(headLineService.HEADLINELIST);

    }
}
