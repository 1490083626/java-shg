package com.wdl.shg.web.wechat;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dto.WechatLoginRequest;
import com.wdl.shg.service.WechatService;

@Controller
@RequestMapping("/wechat/login")
@ResponseBody
public class WeChatLoginController {
    @Resource
//	@Autowired
    WechatService wechatService;
    
    @RequestMapping(value = "/save", method = { RequestMethod.POST})
    public Map<String, Object> login(
            @Validated @RequestBody WechatLoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String, Object> userInfoMap = wechatService.getUserInfoMap(loginRequest);
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
        	System.out.println(cookies);
        }else {
			System.out.println("cookie is null");
			Cookie cookie = new Cookie("JSESSIONID", request.getSession().getId());
			cookie.setMaxAge(60 * 60 * 6);
			response.addCookie(cookie);
        }
        String openId = (String) userInfoMap.get("openId");
        Long userId = (Long) userInfoMap.get("userId");
        request.getSession().setAttribute("openId", openId);
        request.getSession().setAttribute("userId", userId);
//        userInfoMap.remove("openId");
        userInfoMap.remove("userId");
        return userInfoMap;
    }
}
