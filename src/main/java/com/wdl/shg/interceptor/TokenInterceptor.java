package com.wdl.shg.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.wdl.shg.util.TokenUtil;

public class TokenInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		response.setCharacterEncoding("utf-8");
		String token = request.getHeader("Authorization");
		// token不存在
		if(token != null) {
			boolean result = TokenUtil.verify(token);
			if(result) {
				return true;
			}
		}
		Map<String, Object> map =  new HashMap<String, Object>();
		map.put("data", "token is null");
		map.put("code", "401");
		response.getWriter().write(JSONObject.toJSONString(map));
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
