package com.wdl.shg.service;

import java.util.Map;

import com.wdl.shg.dto.WechatLoginRequest;

public interface WechatService {
	Map<String, Object> getUserInfoMap(WechatLoginRequest loginRequest) throws Exception;

}
