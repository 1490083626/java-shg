package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.dao.AreaDao;
import com.wdl.shg.entity.Area;
import com.wdl.shg.BaseTest;

public class AreaDaoTest extends BaseTest{
	@Autowired
	private AreaDao areaDao;
	
	@Test
	public void testQueryArea() {
		List<Area> areaList = areaDao.queryArea();
		assertEquals(4,areaList.size());
	}
}
