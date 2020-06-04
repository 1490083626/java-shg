package com.wdl.shg.service;

import com.wdl.shg.dto.CollectExecution;
import com.wdl.shg.entity.Collect;

public interface CollectService {
	/**
	 * 添加收藏
	 * @param collect
	 * @return
	 * @throws RuntimeException
	 */
	CollectExecution addCollect(Collect collect) throws RuntimeException;
	CollectExecution updateCollect(Collect collect) throws RuntimeException;
	CollectExecution getAllCommentss(Integer productId, Long openId) throws RuntimeException;
	Collect queryCollectByProductIdAndOpenId(Collect collect) throws RuntimeException;
}
