package com.wdl.shg.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.entity.Area;

public class AreaServiceTest extends BaseTest{
	@Autowired
    private AreaService areaService;
	@Test
    public void testGetAreaList() {
	    List<Area> areaList = areaService.getAreaList();
	    assertEquals("丁香苑",areaList.get(0).getAreaName());
	  
    }
}
