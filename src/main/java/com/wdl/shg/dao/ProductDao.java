package com.wdl.shg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.entity.Product;

public interface ProductDao {
	/**
	 * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，用户Id,商品类别，浏览量，区域
	 * 
	 * @param productCondition
	 * @param beginIndex
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	List<Product> queryProductListByHot(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	List<Product> queryProductListByTime(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	List<Product> queryProductListByComment(
			@Param("productCondition") Product productCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	/**
	 * 查询对应的商品总数
	 * 
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	
	/**
	 * 插入商品
	 * 
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);	
	/**
	 * 通过productId查询唯一的商品信息
	 * @param productld
	 * @return
	 */
	Product queryProductById(long productId);
	/**
	*更新商品信息
	*
	*@param product
	*@return
	*/
	int updateProduct(Product product);
	/**
	 * 删全商品类别之前，将商品类别ID置为空
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
	
	/**
	 * 更新浏览量
	 * @return
	 */
	int undateProductPageView(long productId);
	
	/**
	 * 更新留言数
	 * @param product
	 * @return
	 */
	int undateProductMessageAmount(Product product);
	
	List<Product> queryProductByCollect(
			@Param("openId") String openId,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
}
