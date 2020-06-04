package com.wdl.shg.service;

import java.util.List;

import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.WechatUserDO;

public interface WechatUserService {
	/**
	 * 查询用户列表并分页
	 * @param wechatUserDOCondition
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	WechatUserExecution getWechatInfoList(WechatUserDO wechatUserDOCondition,int pageIndex,int pageSize);
	/**
	 * 通过openId获取平台对应的微信账号
	 * @param openId
	 * @return
	 */
	WechatUserDO getWechatUserByOpenId(String openId);
	
	/**
	 * 注册
	 * @param wechatUserDO
	 * @return
	 * @throws RuntimeException
	 */
	WechatUserExecution register(WechatUserDO wechatUserDO) throws RuntimeException;
	
	/**
	 * 更新
	 * @param wechatUserDO
	 * @return
	 * @throws RuntimeException
	 */
	WechatUserExecution modifyWechatUser(WechatUserDO wechatUserDO) throws RuntimeException;
	/**
	 * 删除
	 * @param wechatUserDOId
	 * @return
	 */
	WechatUserExecution deleteWechatUser(long wechatUserDOId);
}
