package com.wdl.shg.service;

import java.util.Map;

import com.wdl.shg.dto.AuthExecution;
import com.wdl.shg.entity.LocalAuth;

public interface AuthService {
	Map<String,Object> getAuth(String authName, String password) throws Exception;
	
//	AuthExecution modifyAuth(LocalAuth localAuth) throws Exception;
}
