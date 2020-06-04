package com.wdl.shg.service;


import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.dto.ImageHolder;
import com.wdl.shg.dto.ProductExecution;
import com.wdl.shg.entity.PersonInfo;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.ProductStateEnum;
import com.wdl.shg.exceptions.ProductOperationException;


public class ProductServiceTest extends BaseTest{
	@Autowired
	private ProductService productService;
	
	@Test
	public void testAddProduct() throws FileNotFoundException,ProductOperationException{
		//创建shopld为1且productCategoryld为1的商品实例并给其成员变量赋值
		Product product = new Product();
		WechatUserDO owner = new WechatUserDO();
		owner.setId(1L);
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(3L);
		product.setOwner(owner);
		product.setProductCategory(pc);
		product.setProductName("测试商品");
		product.setProductDesc("测试商品1");
		product.setPriority(20);
		product.setCreateTime(new Date());
//TODO----------------------------------------------------------------		
		product.setEnableStatus(ProductStateEnum.SUCCESS.getState());
		//创建缩略图文件流
		File thunbnailFile = new File("e:/testImages/test2.png");
		InputStream is = new FileInputStream(thunbnailFile);
		ImageHolder thumbnail = new ImageHolder(thunbnailFile.getName(), is);
		//创建两个商品详情图文件流并将他们添加到详情图列表中
		File productImg1 = new File("e:/testImages/test2.png");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("e:/testImages/shu.png");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		//添加商品并验证
		ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
	@Test
	public void testModifyProduct() throws ProductOperationException,FileNotFoundException{
		//创建shopId为1且productCategoryId为1的商品实例并给其成员变量赋值
		Product product =new Product();
		WechatUserDO owner = new WechatUserDO();
		owner.setId(1L);
		ProductCategory pc =new ProductCategory();
		pc.setProductCategoryId(4L);
		product.setProductId(2L);
		product.setOwner(owner);
		product.setProductCategory(pc);
		product.setProductName("正式的商品");
		product.setProductDesc("正式的商品");
		product.setPriority(50);
		//创建缩略图文件流
		File thumbnailFile = new File("e:/testImages/test2.png");
		InputStream is = new FileInputStream(thumbnailFile);
		ImageHolder thumbnail = new ImageHolder(thumbnailFile.getName(), is);
		//创建两个商品详情图文件流并将他们添加到详情图列表中
		File productImg1 = new File("e:/testImages/test3.jpg");
		InputStream is1 = new FileInputStream(productImg1);
		File productImg2 = new File("e:/testImages/shu.png");
		InputStream is2 = new FileInputStream(productImg2);
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		productImgList.add(new ImageHolder(productImg1.getName(), is1));
		productImgList.add(new ImageHolder(productImg2.getName(), is2));
		//添加商品并验证
		ProductExecution pe = productService.modifyProduct(product, thumbnail, productImgList);
		assertEquals(ProductStateEnum.SUCCESS.getState(), pe.getState());
	}
}
