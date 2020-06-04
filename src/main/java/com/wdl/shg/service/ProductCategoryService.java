package com.wdl.shg.service;

import java.util.List;

import com.wdl.shg.dto.ProductCategoryExecution;
import com.wdl.shg.entity.ProductCategory;


public interface ProductCategoryService {

	/**
	 * 查询所有商品类别信息
	 * @param 
	 * @return
	 */
	List<ProductCategory> getProductCategoryList();
	
	/**
	 * 批量插入商品信息
	 * @param productCategoryList
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution batchAddProductCategory(List<ProductCategory>productCategoryList)
			throws RuntimeException;
	/**
	 * 将此类别下的商品里的类别id置空，再删除掉该商品类别
	 * @param productCategoryId
	 * @param shopId
	 * @return
	 * @throws ProductCategoryOperationException
	 */
	ProductCategoryExecution deleteProductCategory(long productCategoryId)
			throws RuntimeException;
	
	
}
