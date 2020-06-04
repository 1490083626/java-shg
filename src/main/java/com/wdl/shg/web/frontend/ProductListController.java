package com.wdl.shg.web.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dto.ProductExecution;
import com.wdl.shg.entity.Area;
import com.wdl.shg.entity.PersonInfo;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.util.HttpServletRequestUtil;
import com.wdl.shg.service.ProductCategoryService;
import com.wdl.shg.service.ProductService;

@Controller
@RequestMapping("/frontend")
public class ProductListController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@RequestMapping(value = "/listproducts", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProducts(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		if ((pageIndex > -1) && (pageSize > -1)) {
//TODO-------------------------------------			
			long userId = HttpServletRequestUtil.getLong(request,
					"userId");
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request,
					"areaId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			Product productCondition = compactProductCondition4Search(userId,productCategoryId, productName,areaId);
			ProductExecution pe = productService.getProductList(
					productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listproductsbyhot", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByHot(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		if ((pageIndex > -1) && (pageSize > -1)) {
//TODO-------------------------------------			
			long userId = HttpServletRequestUtil.getLong(request,
					"userId");
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request,
					"areaId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			Product productCondition = compactProductCondition4Search(userId,productCategoryId, productName,areaId);
			ProductExecution pe = productService.getProductListByHot(
					productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listproductsbytime", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByTime(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		if ((pageIndex > -1) && (pageSize > -1)) {
//TODO-------------------------------------			
			long userId = HttpServletRequestUtil.getLong(request,
					"userId");
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request,
					"areaId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			Product productCondition = compactProductCondition4Search(userId,productCategoryId, productName,areaId);
			ProductExecution pe = productService.getProductListByTime(
					productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/listproductsbycomment", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> listProductsByComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

		if ((pageIndex > -1) && (pageSize > -1)) {
//TODO-------------------------------------			
			long userId = HttpServletRequestUtil.getLong(request,
					"userId");
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			int areaId = HttpServletRequestUtil.getInt(request,
					"areaId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			Product productCondition = compactProductCondition4Search(userId,productCategoryId, productName,areaId);
			ProductExecution pe = productService.getProductListByComment(
					productCondition, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}

	private Product compactProductCondition4Search(long userId, long productCategoryId, String productName,int areaId) {
		Product productCondition = new Product();
//TODO------------------------------------------用户
		if(userId != -1L) {
			WechatUserDO owner = new WechatUserDO();
			owner.setId(userId);
			productCondition.setOwner(owner);
		}
		
		if (productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		
		if (areaId != -1L) {
			Area area = new Area();
			area.setAreaId(areaId);
			productCondition.setArea(area);
		}
			
		if (productName != null && !productName.equals("undefined")) {
			productCondition.setProductName(productName);
		}
		
		productCondition.setEnableStatus(1);
		return productCondition;
	}
}
