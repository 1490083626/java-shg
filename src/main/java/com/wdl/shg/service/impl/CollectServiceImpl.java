package com.wdl.shg.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdl.shg.dao.CollectDao;
import com.wdl.shg.dto.CollectExecution;
import com.wdl.shg.entity.Collect;
import com.wdl.shg.enums.CollectStateEnum;
import com.wdl.shg.service.CollectService;

@Service
public class CollectServiceImpl implements CollectService{
	@Autowired CollectDao collectDao;
	
	@Override
	@Transactional
	public CollectExecution addCollect(Collect collect) throws RuntimeException {
		if(collect != null && collect.getOpenId() != null && collect.getProductId() != -1L) {
			collect.setCreateTime(new Date());
			
			try {
				int effectedNum =  collectDao.insertCollect(collect);
				if(effectedNum <= 0) {
					throw new RuntimeException("创建收藏失败");
				}
			} catch (Exception e) {
				throw new RuntimeException("创建收藏失败:" + e.toString());
			}
			return new CollectExecution(CollectStateEnum.SUCCESS, collect);
		}
		return new CollectExecution(CollectStateEnum.EMPTY);
	}

	@Override
	public CollectExecution getAllCommentss(Integer productId, Long openId) throws RuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollectExecution updateCollect(Collect collect) throws RuntimeException {
		if(collect != null) {
			int effectedNum = collectDao.updateCollect(collect);
			if(effectedNum <= 0) {
				throw new RuntimeException("更新收藏失败");
			}
			return new CollectExecution(CollectStateEnum.SUCCESS, collect);
		}
		return new CollectExecution(CollectStateEnum.EMPTY);
	}

	@Override
	public Collect queryCollectByProductIdAndOpenId(Collect collect) throws RuntimeException {
		return collectDao.queryCollectByProductIdAndOpenId(collect);
	}

}
