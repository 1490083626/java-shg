package com.wdl.shg.dao;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.entity.LocalAuth;

public interface AuthDao {
	/*
	 * 用户登录验证
	 */
	LocalAuth queryAuth(@Param("authName") String authName, @Param("password") String password);
	/**
	 * 更新用户信息
	 * @param localAuth
	 * @return
	 */
//	int updateAuth(LocalAuth localAuth);
}
