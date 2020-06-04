package com.wdl.shg.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.dao.WechatUserDao;
import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.WechatUserStateEnum;

public class WechatUserServiceTest extends BaseTest{
	@Autowired
	private WechatUserService wechatUserService;
	
	@Test
	public void testRegister() {
		String openId = "adfghhh";
		String token = "shg";
		WechatUserDO wechatUserDO = new WechatUserDO();
		wechatUserDO.setOpenId(openId);
		wechatUserDO.setNickname("name...");
		wechatUserDO.setToken(token);
		WechatUserExecution wue = wechatUserService.register(wechatUserDO);
		assertEquals(WechatUserStateEnum.SUCCESS.getState(), wue.getState());
		wechatUserDO = wechatUserService.getWechatUserByOpenId(openId);
		System.out.println(wechatUserDO.getNickname());	
	}
	
	@Test
	public void testGetWechatUserByOpenId() {
		WechatUserDO wechatUserDO = wechatUserService.getWechatUserByOpenId("oODL84rFCFKGTp_X4y3wdCMETg2c");
		System.out.println(wechatUserDO.getOpenId());
	}
}
