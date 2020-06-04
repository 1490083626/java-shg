package com.wdl.shg.service;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.wdl.shg.dto.ImageHolder;
import com.wdl.shg.dto.ProductExecution;
import com.wdl.shg.entity.Product;
import com.wdl.shg.exceptions.ProductOperationException;

public interface ProductService {
	/**
	 * 查询商品列表并分页，可输入的条件有：商品名（模糊），商品状态，用户Id，商品类别，地区，浏览量
	 * @param productCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList(Product productCondition,int pageIndex,int pageSize);
	
	ProductExecution getProductListByHot(Product productCondition,int pageIndex,int pageSize);
	
	ProductExecution getProductListByTime(Product productCondition,int pageIndex,int pageSize);
	
	ProductExecution getProductListByComment(Product productCondition,int pageIndex,int pageSize);
	/**
	 * 添加商品信息以及图片处理
	 * @param product
	 * @param thumbnail
	 * @param productImgs
	 * @return
	 * @throws RuntimeException
	 */
//	ProductExecution addProduct(Product product, CommonsMultipartFile thumbnail, List<CommonsMultipartFile> productImgs)
//			throws RuntimeException;
	ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList)
			throws RuntimeException;
	/**
	 * 通过商品Id查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product getProductById(long productId);
	
	ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgHolderList)
	throws ProductOperationException;
	
	ProductExecution updateProductPageView(long productId);
	
	ProductExecution undateProductMessageAmount(Product product);
	
	ProductExecution queryProductByCollect(String openId, int pageIndex,int pageSize);
	
	ProductExecution queryProductCountByCate(Product productCondition);
	
}
