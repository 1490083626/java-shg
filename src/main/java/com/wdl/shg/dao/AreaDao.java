package com.wdl.shg.dao;

import java.util.List;

import com.wdl.shg.entity.Area;

public interface AreaDao {
	/**
	 * 列出地域列表
	 * 
	 * @param areaCondition
	 * @return 
	 */
	List<Area> queryArea();
}
