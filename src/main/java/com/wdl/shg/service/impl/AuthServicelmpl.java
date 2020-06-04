package com.wdl.shg.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdl.shg.dao.AuthDao;
import com.wdl.shg.dto.AuthExecution;
import com.wdl.shg.entity.LocalAuth;
import com.wdl.shg.enums.AuthStateEnum;
import com.wdl.shg.service.AuthService;

@Service
public class AuthServicelmpl implements AuthService{
	private static Logger log = LoggerFactory
			.getLogger(WechatUserServiceImpl.class);
	
	@Autowired
	private AuthDao authDao;
	
	@Override
	public Map<String, Object> getAuth(String authName, String password) throws Exception {
		Map<String,Object> modelMap = new HashMap<String,Object>();
		
		try {
			LocalAuth auth = authDao.queryAuth(authName, password);
			if(auth != null) {
				modelMap.put("success", true);
				modelMap.put("auth", auth);
			} else {
				modelMap.put("success", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			modelMap.put("success",false);
			modelMap.put("errMsg",e.toString());
		}
		
		return modelMap;
	}

//	@Override
//	public AuthExecution modifyAuth(LocalAuth localAuth) throws Exception {
//		if(localAuth != null && localAuth.getLocalAuthId() != null) {
//			localAuth.set
//			try {
//				int effectedNum = authDao.updateAuth(localAuth);
//				if(effectedNum <= 0) {
//					throw new RuntimeException("更新用户信息失败");
//				}
//				return new AuthExecution(AuthStateEnum.SUCCESS, localAuth);
//			} catch (Exception e) {
//				throw new RuntimeException("更新用户信息失败:" + e.toString());
//			}
//		} else {
//			return new AuthExecution(AuthStateEnum.EMPTY);
//		}
//	}

}
