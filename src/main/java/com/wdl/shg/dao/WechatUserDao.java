package com.wdl.shg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.WechatUserDO;

public interface WechatUserDao {
	/**
	 * 查询用户列表
	 * @param wechatUserDOCondition
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<WechatUserDO> queryWechatInfoList(
			@Param("wechatUserDOCondition") WechatUserDO wechatUserDOCondition,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	/**
	 * 查询对应的用户总数
	 * 
	 * @param wechatUserDOCondition
	 * @return
	 */
	int queryWechatUserDOCount(@Param("wechatUserDOCondition") WechatUserDO wechatUserDOCondition);	
	/**
	 * 通过openId查询对应本平台的微信账号
	 * @param openId
	 * @return
	 */
	WechatUserDO queryWechatInfoByOpenId(String openId);
	/**
	 * 添加对应本平台的微信账号
	 * @param wechatUserDO
	 * @return
	 */
	int insertWechatUser(WechatUserDO wechatUserDO);
	/**
	 * 更新对应本平台的微信账号
	 * @param wechatUserDO
	 * @return
	 */
	int updateWechatUser(WechatUserDO wechatUserDO);
	/**
	 * 删除用户
	 * @param wechatUserDOId
	 * @return
	 */
	int deleteWechatUser(@Param("wechatUserDOId") long wechatUserDOId);
}
