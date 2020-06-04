package com.wdl.shg.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdl.shg.enums.ProductCategoryStateEnum;
import com.wdl.shg.dao.ProductCategoryDao;
import com.wdl.shg.dao.ProductDao;
import com.wdl.shg.dto.ProductCategoryExecution;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.exceptions.ProductCategoryOperationException;
import com.wdl.shg.service.ProductCategoryService;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	@Autowired 
	private ProductDao productDao;

	@Override
	public List<ProductCategory> getProductCategoryList() {
		return productCategoryDao. queryProductCategoryList();
	}

	@Override
	@Transactional
	public ProductCategoryExecution batchAddProductCategory(
			List<ProductCategory> productCategoryList) throws ProductCategoryOperationException {
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				int effectedNum = productCategoryDao
						.batchInsertProductCategory(productCategoryList);
				if (effectedNum <= 0) {
					throw new ProductCategoryOperationException("商品类别失败");
				} else {

					return new ProductCategoryExecution(
							ProductCategoryStateEnum.SUCCESS);
				}

			} catch (Exception e) {
				throw new ProductCategoryOperationException("batchAddProductCategory error: "
						+ e.getMessage());
			}
		} else {
			return new ProductCategoryExecution(
					ProductCategoryStateEnum.INNER_ERROR);
		}

	}

	@Override
	@Transactional
	public ProductCategoryExecution deleteProductCategory(
			long productCategoryId) throws ProductCategoryOperationException {
		//将此商品类别下的商品的类别Id置为空
		//先解除tb_product里的商品与该producategoryld的关联
		try {
			int effectedNum = productDao
					.updateProductCategoryToNull(productCategoryId);
			if (effectedNum < 0) {
				throw new RuntimeException("商品类别更新失败");
			}
		} catch (Exception e) {
			throw new RuntimeException("deleteProductCategory error: "
					+ e.getMessage());
		}
		//类别删除
		try {
			int effectedNum = productCategoryDao.deleteProductCategory(
					productCategoryId);
			if (effectedNum <= 0) {
				throw new ProductCategoryOperationException("店铺类别删除失败");
			} else {
				return new ProductCategoryExecution(
						ProductCategoryStateEnum.SUCCESS);
			}

		} catch (Exception e) {
			throw new RuntimeException("deleteProductCategory error: "
					+ e.getMessage());
		}
	}
	
	

}
