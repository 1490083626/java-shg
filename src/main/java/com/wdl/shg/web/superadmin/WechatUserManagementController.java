package com.wdl.shg.web.superadmin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.WechatUserStateEnum;
import com.wdl.shg.service.WechatUserService;
import com.wdl.shg.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/auth")
@ResponseBody
public class WechatUserManagementController {
    @Autowired
    WechatUserService wechatUserService;
    
	@RequestMapping(value = "/getwechatuserlist", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getWechatUserList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pagenum");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pagesize");

		//控制判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			//筛选的条件可以进行排列组合
			String openId = HttpServletRequestUtil.getString(request,
					"openId");
			Integer gender = HttpServletRequestUtil.getInt(request,
					"gender");
			String avatarUrl = HttpServletRequestUtil.getString(request,
					"avatarUrl");
			String mobile = HttpServletRequestUtil.getString(request,
					"mobile");
			String nickname = HttpServletRequestUtil.getString(request,
					"query");
//			String nickname = HttpServletRequestUtil.getString(request,
//					"nickname");
			Integer enableStatus = HttpServletRequestUtil.getInt(request, "enableStatus");
			WechatUserDO wechatUserDOCondition = compactWechatUserDOCondition(
					openId, gender, avatarUrl, mobile, nickname, enableStatus);
			//传入查询条件以及分页信息进行查询，返回相应商品列表以及总数
			WechatUserExecution we = wechatUserService.getWechatInfoList(
					wechatUserDOCondition, pageIndex, pageSize);
			//移出已删除商品
			List<WechatUserDO> wechatUserDOList = we.getWechatUserDOList();

			we.setWechatUserDOList(wechatUserDOList);
			
			modelMap.put("wechatUserDOList", we.getWechatUserDOList());
			modelMap.put("count", we.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/modifyWechatUser", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyWechatUser(@RequestBody WechatUserDO wechatUserDO, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(wechatUserDO != null && wechatUserDO.getId() != -1L) {
			try {
				WechatUserExecution we = wechatUserService.modifyWechatUser(wechatUserDO);
				modelMap.put("success", true);
				modelMap.put("data", we.getWechatUserDO());
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", true);
			modelMap.put("errMsg", "更新失败，用户id不能为空");
		}
		
		return modelMap;
		
	}
	
	@RequestMapping(value = "/getWechatUserByOpenId", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getWechatUserByOpenId(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String openId = HttpServletRequestUtil.getString(request, "id");
		if(openId != null) {
			try {
				WechatUserDO wu = wechatUserService.getWechatUserByOpenId(openId);
				modelMap.put("success", true);
				modelMap.put("data", wu);
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", true);
			modelMap.put("errMsg", "查询失败，用户id不能为空");
		}
		
		return modelMap;
		
	}
	
	@RequestMapping(value = "/deleteWechatUser", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteWechatUser(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long id = HttpServletRequestUtil.getLong(request, "id");
		if(id != -1L) {
			try {
				WechatUserExecution we = wechatUserService.deleteWechatUser(id);
				if(we.getState() == WechatUserStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", we.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "删除失败，用户id不能为空");
		}
		return modelMap;
	}
	
	private WechatUserDO compactWechatUserDOCondition(String openId, Integer gender, 
			String avatarUrl, String mobile, String nickname, Integer enableStatus ) {
		WechatUserDO wechatUserDOCondition = new WechatUserDO();
		if(openId != null) {
			wechatUserDOCondition.setOpenId(openId);
		}
		if(gender != -1) {
			wechatUserDOCondition.setGender(gender);
		}
		if(avatarUrl != null) {
			wechatUserDOCondition.setAvatarUrl(avatarUrl);
		}
		if(mobile != null) {
			wechatUserDOCondition.setMobile(mobile);
		}
		if(nickname != null) {
			wechatUserDOCondition.setNickname(nickname);
		}
		if(enableStatus != -1) {
			wechatUserDOCondition.setEnableStatus(enableStatus);
		}
		return wechatUserDOCondition;
	}
}
