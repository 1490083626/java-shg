package com.wdl.shg.web.useradmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dao.CollectDao;
import com.wdl.shg.dto.CollectExecution;
import com.wdl.shg.entity.Collect;
import com.wdl.shg.enums.CollectStateEnum;
import com.wdl.shg.enums.CommentStateEnum;
import com.wdl.shg.service.CollectService;
import com.wdl.shg.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/useradmin")
@ResponseBody
public class CollectManagementController {
	@Autowired CollectService collectService;
	
	@RequestMapping(value = "/addCollect", method = { RequestMethod.POST})
	public Map<String, Object> addCollect(@RequestBody Collect  collect) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		if(collect.getOpenId()!= null && collect.getProductId() != -1L) {
			// 是否已经添加收藏
			// 是则更新
			Collect collect2 = collectService.queryCollectByProductIdAndOpenId(collect);
			if(collect2 != null && collect2.getCollectId() != -1) {
				return upadteCollect(collect2);
			}
			// 未收藏则添加入库
			try {
				collect.setEnableStatus(1);
				CollectExecution ce = collectService.addCollect(collect);
				if(ce.getState() == CollectStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
					modelMap.put("isCollect", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "openId 或 productId 为空");
		}
		return modelMap;
	}
	
//	@RequestMapping(value = "/upadteCollect", method = { RequestMethod.POST})
//	@ResponseBody
	public Map<String, Object> upadteCollect(Collect collect) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
//		Long collectId = HttpServletRequestUtil.getLong(request, "collectId");
		
		if(collect != null && collect.getCollectId() != -1L) {
			if(collect.getEnableStatus() == 0) {
				collect.setEnableStatus(1);
				modelMap.put("isCollect", true);
			} else if (collect.getEnableStatus() == 1) {
				collect.setEnableStatus(0);
				modelMap.put("isCollect", false);
			}
			CollectExecution ce = collectService.updateCollect(collect);
			if(ce.getState() == CommentStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ce.getStateInfo());
			}
			
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty openId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/isCollect", method = { RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> isCollect(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Collect collect = new Collect();
		String openId = HttpServletRequestUtil.getString(request, "openId");
		
		Long productId = HttpServletRequestUtil.getLong(request, "productId");
		
		collect.setOpenId(openId);
		collect.setProductId(productId);
		if(collect != null && collect.getOpenId() != null && collect.getProductId() != -1) {
			Collect collect2 = collectService.queryCollectByProductIdAndOpenId(collect);
			if(collect2 != null && collect2.getCollectId() != -1) {
				modelMap.put("success", true);
				if(collect2.getEnableStatus() == 0) {
					modelMap.put("isCollect", false);
				} else if (collect2.getEnableStatus() == 1) {
					modelMap.put("isCollect", true);
				}
			} else {
				modelMap.put("success", true);
				modelMap.put("isCollect", false);
			}	
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty");
		}


		return modelMap;
	}
	
}
