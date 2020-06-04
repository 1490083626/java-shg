package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.entity.LocalAuth;

public class AuthDaoTest extends BaseTest{
	@Autowired
	private AuthDao authDao;
	
	@Test
	public void testGetAuth() throws Exception{
//		LocalAuth localAuth = new LocalAuth();
//		localAuth.setUserName("admin");
//		localAuth.setPassword("123456");
//		int effectedNum = authDao.queryAuth(localAuth.getUserName(), localAuth.getPassword());
//		assertEquals(1, effectedNum);
		
		LocalAuth localAuth2 = new LocalAuth();
		localAuth2.setUserName("admindddd");
		localAuth2.setPassword("123456");
		LocalAuth localAuth3 = authDao.queryAuth(localAuth2.getUserName(), localAuth2.getPassword());
		System.out.println(localAuth3);
//		assertEquals(0, effectedNum2);
	}
}
