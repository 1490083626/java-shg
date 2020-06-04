package com.wdl.shg.web.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dto.ProductExecution;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.service.ProductService;

@Controller
@RequestMapping("/frontend")
public class ReportController {
	@Autowired ProductService productService;
	
	@RequestMapping(value = "/getProductCountByCate", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductCountByCate() {
//		1	生活用品
//		3	体育用品
//		4	电子数码
//		5	书籍
//		6	衣服配饰
//		7	其它
		Map<String, Object> modedlMap = new HashMap<String, Object>();
		
		List<String> cateName = new ArrayList<String>();
		cateName.add("生活用品");
		cateName.add("体育用品");
		cateName.add("电子数码");
		cateName.add("书籍");
		cateName.add("衣服配饰");
		cateName.add("其它");
		
		List<Integer> cateCount = new ArrayList<Integer>();
		
		Map<String, Object> cateMap = new HashMap<String, Object>();
		
		
		Product productCondition = new Product();
		ProductCategory pc = new ProductCategory();
		pc.setProductCategoryId(1L);
		productCondition.setProductCategory(pc);
		ProductExecution pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("生活用品", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		pc.setProductCategoryId(3L);
		productCondition.setProductCategory(pc);
		pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("体育用品", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		pc.setProductCategoryId(4L);
		productCondition.setProductCategory(pc);
		pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("电子数码", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		pc.setProductCategoryId(5L);
		productCondition.setProductCategory(pc);
		pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("书籍", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		pc.setProductCategoryId(6L);
		productCondition.setProductCategory(pc);
		pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("衣服配饰", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		
		pc.setProductCategoryId(7L);
		productCondition.setProductCategory(pc);
		pe = productService.queryProductCountByCate(productCondition);
		cateCount.add(pe.getCount());
//		cateMap.put("其它", pe.getCount());
//		cateList.add(cateMap);
		System.out.println(pe.getCount());
		
		modedlMap.put("nameData", cateName);
		modedlMap.put("countData", cateCount);
		modedlMap.put("success", true);
		return modedlMap;
		
	}
}
