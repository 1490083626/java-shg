package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.WechatUserDO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WechatUserDaoTest extends BaseTest{
	@Autowired
	private WechatUserDao wechatUserDao;
	
	@Test
	public void testAInsertWechatUser() throws Exception {
		WechatUserDO wechatUserDO = new WechatUserDO();
		wechatUserDO.setToken("shg");
		wechatUserDO.setNickname("张三");
		wechatUserDO.setAvatarUrl("wwwwwwww");
		wechatUserDO.setGender(1);
		wechatUserDO.setCountry("中国");
		wechatUserDO.setProvince("广东省");
		wechatUserDO.setCity("广州市");
		wechatUserDO.setMobile("13511111111");
		wechatUserDO.setOpenId("openid...");
		wechatUserDO.setCreatedAt(new Date());
		int effectedNum = wechatUserDao.insertWechatUser(wechatUserDO);
		assertEquals(1, effectedNum);
	}
	
	@Test 
	public void testBQueryWechatInfoByOpenId() {
		String openId = "oODL84rFCFKGTp_X4y3wdCMETg2c";
		WechatUserDO wechatUserDO = wechatUserDao.queryWechatInfoByOpenId(openId);
		wechatUserDO.setOpenId(openId);
		System.out.println(wechatUserDO.getNickname());
	}
	
	@Test 
	public void testCQueryWechatInfos() {
//		String openId = "openid..";
		String nickname = "甘";
		WechatUserDO wechatUserDOCondition = new WechatUserDO();
//		wechatUserDOCondition.setOpenId(openId);
		wechatUserDOCondition.setNickname(nickname);
		List<WechatUserDO> wechatUserDOList = wechatUserDao.queryWechatInfoList(wechatUserDOCondition, 0, 1);
		
		System.out.println(wechatUserDOList.size());
		for(WechatUserDO wechatUserDO : wechatUserDOList) {
			System.out.println(wechatUserDO.getNickname());
		}
	}
	
	@Test 
	public void testCdeleteWechatInfo() {
		WechatUserDO wechatUserDOCondition = new WechatUserDO();
//		wechatUserDOCondition.setOpenId(openId);
		wechatUserDOCondition.setId(5L);
		int effecedNum = wechatUserDao.deleteWechatUser(wechatUserDOCondition.getId());
		
		assertEquals(1, effecedNum);
	}
}
