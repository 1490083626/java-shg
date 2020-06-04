package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.BaseTest;
import com.wdl.shg.dao.ProductCategoryDao;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest{
	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	public void testBQueryProductCategory() throws Exception {
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList();
		assertEquals(2, productCategoryList.size());
		System.out.println( productCategoryList.get(1).getProductCategoryName());

	}
	@Test
	public void testABatchInsertProductCategory() throws Exception {
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryName("商品类别1");

		productCategory.setPriority(10);
		productCategory.setCreateTime(new Date());


		ProductCategory productCategory2 = new ProductCategory();
		productCategory2.setProductCategoryName("商品类别2");

		productCategory2.setPriority(12);
		productCategory2.setCreateTime(new Date());


		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
		productCategoryList.add(productCategory);
		productCategoryList.add(productCategory2);
		int effectedNum = productCategoryDao
				.batchInsertProductCategory(productCategoryList);
		assertEquals(2, effectedNum);
	}
	@Test
	public void testCDeleteProductCategory() throws Exception {
		List<ProductCategory> productCategoryList = productCategoryDao
				.queryProductCategoryList();
		int effectedNum = productCategoryDao.deleteProductCategory(
				productCategoryList.get(0).getProductCategoryId());
		assertEquals(1, effectedNum);
//		effectedNum = productCategoryDao.deleteProductCategory(
//				productCategoryList.get(1).getProductCategoryId());
//		assertEquals(1, effectedNum);
	}
	
}
