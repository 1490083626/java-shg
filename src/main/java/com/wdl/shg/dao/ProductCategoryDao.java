package com.wdl.shg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 查询店铺商品类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList();

	/**
	 * 批量新增商品类别
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory>productCategoryList);
	/**
	 * 删除指定商品类别
	 * @param prodoctCategoryId
	 * @param shopId
	 * @return
	 */

	int deleteProductCategory(
			@Param("productCategoryId") long productCategoryId);
}
