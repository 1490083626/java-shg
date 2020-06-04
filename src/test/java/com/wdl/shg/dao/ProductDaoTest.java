package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.entity.Area;
import com.wdl.shg.entity.PersonInfo;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.ProductCategory;

import com.wdl.shg.entity.ProductImg;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.BaseTest;
import com.wdl.shg.dao.ProductDao;
import com.wdl.shg.dao.ProductImgDao;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductDaoTest extends BaseTest{

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Test
	public void testAInsertProduct() throws Exception {
		WechatUserDO owner = new WechatUserDO();
		owner.setOpenId("osO245XTZkaBMcMfZiJlHMVq280w");

		ProductCategory pc1 = new ProductCategory();
		pc1.setProductCategoryId(1L);
		
		Area area = new Area();
		area.setAreaId(1);
		
		//初始化三个商品实例并添加进userID为1的店铺里，
		//同时商品类别Id也为1

		Product product1 = new Product();
		product1.setProductName("测试1");
		product1.setProductDesc("测试Desc1");
		product1.setImgAddr("test1");
		product1.setPriority(0);
		product1.setEnableStatus(1);
		product1.setCreateTime(new Date());
		product1.setLastEditTime(new Date());
		product1.setOwner(owner);
		product1.setProductCategory(pc1);
		product1.setArea(area);
		product1.setPageView(1);
		product1.setLinkman("老吴");
		product1.setContactPhone("13542200000");
		product1.setContactWechat("wxabc");
		
//		Product product2 = new Product();
//		product2.setProductName("测试2");
//		product2.setProductDesc("测试Desc2");
//		product2.setImgAddr("test2");
//		product2.setPriority(0);
//		product2.setEnableStatus(0);
//		product2.setCreateTime(new Date());
//		product2.setLastEditTime(new Date());
//		product2.setOwner(owner);
//		product2.setProductCategory(pc1);
//		
//		Product product3 = new Product();
//		product3.setProductName("测试3");
//		product3.setProductDesc("测试Desc3");
//		product3.setImgAddr("test3");
//		product3.setPriority(0);
//		product3.setEnableStatus(1);
//		product3.setCreateTime(new Date());
//		product3.setLastEditTime(new Date());
//		product3.setOwner(owner);
//		product3.setProductCategory(pc1);
		//判断是否成功
		int effectedNum = productDao.insertProduct(product1);
		assertEquals(1, effectedNum);
//		effectedNum = productDao.insertProduct(product2);
//		assertEquals(1, effectedNum);
//		effectedNum = productDao.insertProduct(product3);
//		assertEquals(1, effectedNum);
	}
	@Test
	public void testBQueryProductList() throws Exception {
		Product product = new Product();
		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(1L);
		product.setProductCategory(productCategory);
		List<Product> productList = productDao.queryProductList(product, 0, 4);
		
		for(Product product2 : productList) {
			System.out.println(product2.getProductId());
			System.out.println("ProductImgList:" + product2.getProductImgList());
		}
		assertEquals(4, productList.size());
		
//		int count = productDao.queryProductCount(product);
//		assertEquals(28, count);
//		//使用商品名称模糊查询
//		product.setProductName("正式的商品");
//		productList = productDao.queryProductList(product, 0, 3);
//		assertEquals(1, productList.size());
//		count = productDao.queryProductCount(product);
//		assertEquals(1, count);
//		WechatUserDO owner = new WechatUserDO();
//		owner.setOpenId("ooooo");
//		product.setOwner(owner);
//		productList = productDao.queryProductList(product, 0, 3);
//		assertEquals(1, productList.size());
//		count = productDao.queryProductCount(product);
//		assertEquals(2, count);
//		System.out.println("AreaId:" + productList.size());	
//		System.out.println("AreaId:" + productList.get(0).getProductCategory().getProductCategoryId());
//		System.out.println("????");
		
		
	}

	@Test
	public void testCQueryProductByProductId() throws Exception {
		long productId = 2;
		//初始化两个商品详情图实例作为productId为1的商品下的详情图片
		//批量插入到商品详情图表中
		ProductImg productImg1 = new ProductImg();
		productImg1.setImgAddr("图片1");
		productImg1.setImgDesc("测试图片1");
		productImg1.setPriority(1);
		productImg1.setCreateTime(new Date());
		productImg1.setProductId(productId);
		ProductImg productImg2 = new ProductImg();
		productImg2.setImgAddr("图片2");
		productImg2.setPriority(1);
		productImg2.setCreateTime(new Date());
		productImg2.setProductId(productId);
		List<ProductImg> productImgList = new ArrayList<ProductImg>();
		productImgList.add(productImg1);
		productImgList.add(productImg2);
		int effectedNum = productImgDao.batchInsertProductImg(productImgList);
		assertEquals(2, effectedNum);
		//查询productId为1的商品信息并校验返回的详情图实例列表size是否为2
		Product product = productDao.queryProductById(productId);
		assertEquals(2, product.getProductImgList().size());
		//删除新增的这两个商品详情图实例
		effectedNum = productImgDao.deleteProductImgByProductId(productId);
		assertEquals(2, effectedNum);
	}
	
	@Test
	public void testDUpdateProduct() throws Exception {
		WechatUserDO owner = new WechatUserDO();
		owner.setOpenId("ooooo");
		Product product = new Product();
		product.setOwner(owner);
		product.setProductId(50L);

		ProductCategory productCategory = new ProductCategory();
		productCategory.setProductCategoryId(3L);
		product.setProductCategory(productCategory);
		Area area = new Area();
		area.setAreaId(2);
		product.setArea(area);
		product.setProductName("第一个产品");
		product.setPriority(100);

		product.setEnableStatus(0);
		int effectedNum = productDao.updateProduct(product);
		assertEquals(1, effectedNum);
	}
	@Test
	public void testEUpdateProductCategoryToNull() {
		//将productCategoryId为2的商品类别下面的商品的商品类别置为空
		int effectedNum = productDao.updateProductCategoryToNull(3L);
		assertEquals(1, effectedNum);
	}
	@Test
	public void testProductByhot() {
		//将productCategoryId为2的商品类别下面的商品的商品类别置为空
		Product product = new Product();
		List<Product> productList = productDao.queryProductListByHot(product, 0, 5);
		assertEquals(5, productList.size());
	}
}
