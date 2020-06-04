package com.wdl.shg.web.superadmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.entity.LocalAuth;
import com.wdl.shg.service.AuthService;
import com.wdl.shg.util.HttpServletRequestUtil;
import com.wdl.shg.util.TokenUtil;

@Controller
@RequestMapping("/auth")
@ResponseBody
public class AuthController {
	@Autowired
	private AuthService authService;
	
    @RequestMapping(value = "/verifyauth", method = { RequestMethod.POST})
	private Map<String,Object> verifyAuth(@RequestBody LocalAuth localAuth, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> modelMap = new HashMap<String,Object>();

		if(localAuth != null && localAuth.getUserName() != null && localAuth.getPassword() != null) {
			String authName = localAuth.getUserName();
			String password = localAuth.getPassword();
			Map<String,Object> modelMap2 = authService.getAuth(authName, password);
			if((boolean) modelMap2.get("success")) {
				String token = TokenUtil.token(authName,password);
				modelMap2.put("token", token);
				
				return modelMap2;
			} else {
				modelMap.put("success",false);
				modelMap.put("errMsg", "查询失败");
			}
		} else {
			modelMap.put("success",false);
			modelMap.put("errMsg", "用户名或密码为空");
		}
		
//        response.setHeader("Access-Control-Allow-Origin", "*");  
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");  
//        response.setHeader("Access-Control-Max-Age", "3600");  
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");

		return modelMap;
	}
}
