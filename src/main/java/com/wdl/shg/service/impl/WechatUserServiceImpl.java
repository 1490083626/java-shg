package com.wdl.shg.service.impl;

import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdl.shg.dao.WechatUserDao;
import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.WechatUserStateEnum;
import com.wdl.shg.service.WechatUserService;
import com.wdl.shg.util.PageCalculator;

@Service
public class WechatUserServiceImpl implements WechatUserService {
	private static Logger log = LoggerFactory
			.getLogger(WechatUserServiceImpl.class);
	@Autowired
	private WechatUserDao wechatUserDao;
	
	@Override
	public WechatUserDO getWechatUserByOpenId(String openId) {
		WechatUserDO wechatUserDO = wechatUserDao.queryWechatInfoByOpenId(openId);
		return wechatUserDO;
	}

	@Override
	@Transactional
	public WechatUserExecution register(WechatUserDO wechatUserDO) throws RuntimeException {
		if(wechatUserDO == null || wechatUserDO.getOpenId() == null) {
			return new WechatUserExecution(WechatUserStateEnum.NULL_AUTH_INFO);
		}
		try {
			wechatUserDO.setCreatedAt(new Date());
			//TODO
//			WechatUserDO.setEnableStatus(1);
			int effectedNum = wechatUserDao.insertWechatUser(wechatUserDO);
			if(effectedNum <= 0) {
				throw new RuntimeException("添加用户失败");
			}else {
				return new WechatUserExecution(WechatUserStateEnum.SUCCESS,
						wechatUserDO);
			}
		} catch (Exception e) {
			log.error("insertWechatUser error" + e.toString());
			throw new RuntimeException("insertWechatUser error" + e.getMessage());
		}
	}

	@Override
	public WechatUserExecution getWechatInfoList(WechatUserDO wechatUserDOCondition, int pageIndex, int pageSize) {
		//页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<WechatUserDO> wechatUserDOList = wechatUserDao.queryWechatInfoList(wechatUserDOCondition, rowIndex, pageSize);
		int count = wechatUserDao.queryWechatUserDOCount(wechatUserDOCondition);
		WechatUserExecution we = new WechatUserExecution();
		we.setWechatUserDOList(wechatUserDOList);
		we.setCount(count);
		return we;
	}

	@Override
	@Transactional
	public WechatUserExecution modifyWechatUser(WechatUserDO wechatUserDO) throws RuntimeException {
		if (wechatUserDO != null && wechatUserDO.getId() != -1L) {
			wechatUserDO.setUpdatedAt(new Date());
			try {
				int effectedNum = wechatUserDao.updateWechatUser(wechatUserDO);
				if(effectedNum <= 0) {
					throw new RuntimeException("更新用户信息失败");
				}
				return new WechatUserExecution(WechatUserStateEnum.SUCCESS, wechatUserDO);
			} catch (Exception e) {
				throw new RuntimeException("更新用户信息失败:" + e.toString());
			}
		} else {
			return new WechatUserExecution(WechatUserStateEnum.EMPTY);
		}
	}

	@Override
	@Transactional
	public WechatUserExecution deleteWechatUser(long wechatUserDOId) {
		if(wechatUserDOId != -1L) {
			try {
				int effectedNum = wechatUserDao.deleteWechatUser(wechatUserDOId);
				if(effectedNum <= 0) {
					throw new RuntimeException("删除用户失败");
				} else {
					return new WechatUserExecution(WechatUserStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new RuntimeException("deleteWechatUser error: "
						+ e.getMessage());
			}
		}
		return new WechatUserExecution(WechatUserStateEnum.EMPTY);
	}

}
