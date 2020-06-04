package com.wdl.shg.web.frontend;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdl.shg.dto.HeadLineExecution;
import com.wdl.shg.dto.ImageHolder;
import com.wdl.shg.util.HttpServletRequestUtil;
import com.wdl.shg.entity.HeadLine;
import com.wdl.shg.enums.HeadLineStateEnum;
import com.wdl.shg.service.HeadLineService;



@Controller
@RequestMapping("/frontend")
public class MainPageController {
//	@Autowired
//	private ShopCategoryService shopCategoryService;
	@Autowired
	private HeadLineService headLineService;
	@Autowired
//	private ProductCategoryService productCategoryService;
	/**
	 * 初始化前端展示系统的主页信息
	 * @return
	 */
	@RequestMapping(value = "/getMainPageHead", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getMainPageHead() {
		Map<String, Object> modelMap = new HashMap<String, Object>();
//		List<ProductCategory> productCategoryList = new ArrayList<ProductCategory>();
//		try {
//			//获取分类列表
//			productCategoryList = productCategoryService.getProductCategoryList();
//			modelMap.put("shopCategoryList", productCategoryList);
//		} catch (Exception e) {
//			
//			modelMap.put("success", false);
//			modelMap.put("errMsg", e.getMessage());
//			return modelMap;
//		}
		List<HeadLine> headLineList = new ArrayList<HeadLine>();
		try {
			//获取状态为可用（1）的头条列表
			HeadLine headLineCondition = new HeadLine();
			headLineCondition.setEnableStatus(1);
			headLineList = headLineService.getHeadLineList(headLineCondition);
			modelMap.put("headLineList", headLineList);
		} catch (Exception e) {
			e.printStackTrace();
			HeadLineStateEnum s = HeadLineStateEnum.INNER_ERROR;
			modelMap.put("success", false);
			modelMap.put("errMsg", s.getStateInfo());
			return modelMap;
		}
		modelMap.put("success", true);
		return modelMap;
	}
	
	@RequestMapping(value = "/addheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addHeadLine(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		String headLineStr = HttpServletRequestUtil.getString(request,
				"headLineStr");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile thumbnail = (CommonsMultipartFile) multipartRequest
				.getFile("headTitleManagementAdd_lineImg");
		ImageHolder imageHolder = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && thumbnail != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName((headLine.getLineName() == null) ? null
						: URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				HeadLineExecution ae = headLineService.addHeadLine(headLine,
						imageHolder);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}

	@RequestMapping(value = "/modifyheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyHeadLine(HttpServletRequest request) throws IOException {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		HeadLine headLine = null;
		String headLineStr = HttpServletRequestUtil.getString(request,
				"headLineStr");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile thumbnail = (CommonsMultipartFile) multipartRequest
				.getFile("headTitleManagementEdit_lineImg");
		ImageHolder imageHolder = new ImageHolder(thumbnail.getOriginalFilename(), thumbnail.getInputStream());
		try {
			headLine = mapper.readValue(headLineStr, HeadLine.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		if (headLine != null && headLine.getLineId() != null) {
			try {
				// decode可能有中文的地方
				headLine.setLineName((headLine.getLineName() == null) ? null
						: URLDecoder.decode(headLine.getLineName(), "UTF-8"));
				HeadLineExecution ae = headLineService.modifyHeadLine(headLine,
						imageHolder);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/removeheadline", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLine(Long headLineId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (headLineId != null && headLineId > 0) {
			try {
				HeadLineExecution ae = headLineService
						.removeHeadLine(headLineId);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入头条信息");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/removeheadlines", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeHeadLines(String headLineIdListStr) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(
				ArrayList.class, Long.class);
		List<Long> headLineIdList = null;
		try {
			headLineIdList = mapper.readValue(headLineIdListStr, javaType);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
		}
		if (headLineIdList != null && headLineIdList.size() > 0) {
			try {
				HeadLineExecution ae = headLineService
						.removeHeadLineList(headLineIdList);
				if (ae.getState() == HeadLineStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ae.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入区域信息");
		}
		return modelMap;
	}
}
