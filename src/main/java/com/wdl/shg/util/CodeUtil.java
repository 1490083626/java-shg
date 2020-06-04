package com.wdl.shg.util;

import javax.servlet.http.HttpServletRequest;

import com.wdl.shg.util.HttpServletRequestUtil;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//实际验证码
		String verifyCodeExpected = (String) request.getSession().getAttribute(
				com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
		//用户输入验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request,
				"verifyCodeActual");
		if (verifyCodeActual == null
				|| !verifyCodeActual.equalsIgnoreCase(verifyCodeExpected)) {
			return false;
		}
		return true;
	}

}
