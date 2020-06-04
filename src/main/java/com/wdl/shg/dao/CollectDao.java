package com.wdl.shg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.entity.Collect;

public interface CollectDao {
	/**
	 * 添加收藏
	 * @param product
	 * @return
	 */
	int insertCollect(Collect collect);
	
	/**
	 * @param collectId
	 * @return
	 */
	Collect queryCollectByProductIdAndOpenId(Collect collect);
	
	List<Collect> queryCollectList(
			@Param("productId") Long productId,
			@Param("openId") String openId);
	
	int updateCollect(Collect collect);

}
