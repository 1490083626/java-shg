package com.wdl.shg.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wdl.shg.dao.CommentDao;
import com.wdl.shg.dao.ProductDao;
import com.wdl.shg.dto.CommentExecution;
import com.wdl.shg.dto.CommentsVO;
import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.Comment;
import com.wdl.shg.entity.Product;
import com.wdl.shg.enums.CommentStateEnum;
import com.wdl.shg.enums.WechatUserStateEnum;
import com.wdl.shg.service.CommentService;
import com.wdl.shg.util.PageCalculator;
import com.wdl.shg.util.TimeAgoUtils;

@Service
public class CommentServiceImpl implements CommentService{
	@Autowired CommentDao commentDao;
	@Autowired ProductDao productDao;
	
	@Override
	@Transactional
	public CommentExecution addComment(Comment comment) throws RuntimeException {
		//空值判断
		if(comment != null && comment.getComment() != null && comment.getFromUserId() != null
				&& comment.getProductId() > 0) {
			comment.setCreateTime(new Date());
			try {
				int effectedNum = commentDao.insertComment(comment);
				if(effectedNum <= 0) {
					throw new RuntimeException("创建评论失败");
				}
				// 更新商品表留言数
				int count = commentDao.queryCommentsCount(comment.getProductId());
				if(count != -1L && comment.getProductId() != -1L) {
					Product product = new Product();
					product.setProductId((long)comment.getProductId());
					product.setMessageAmount((long)count);
					try {
						productDao.undateProductMessageAmount(product);
					} catch (Exception e) {
						throw new RuntimeException("更新留言数失败:" + e.toString());
					}
					
				}
			} catch (Exception e) {
				throw new RuntimeException("创建评论失败:" + e.toString());
			}
			return new CommentExecution(CommentStateEnum.SUCCESS, comment);
		}
		return new CommentExecution(CommentStateEnum.EMPTY);
	}

	@Override
	@Transactional
	public CommentExecution getAllComments(Integer productId, Integer pageIndex, Integer pageSize) {
		//页码转换成数据库的行码，并调用dao层取回指定页码的商品列表
		int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
		List<CommentsVO> commentList = commentDao.queryCommentList(productId, rowIndex, pageSize);
		
		for(CommentsVO c : commentList) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}
		int count = commentDao.queryCommentsCount(productId);
		
		CommentExecution ce = new CommentExecution();
		ce.setCommentsVOList(commentList);
		ce.setCount(count);
		return ce;
	}

	@Override
	public CommentExecution getUnreadComments(String openId) throws RuntimeException {
		List<CommentsVO> commentList = commentDao.queryUnreadCommentListByOpenId(openId);
		for(CommentsVO c : commentList) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}
		CommentExecution ce = new CommentExecution();
		ce.setCommentsVOList(commentList);
		return ce;
	}
	
	@Override
	public CommentExecution getReadComments(String openId) throws RuntimeException {
		List<CommentsVO> commentList = commentDao.queryReadCommentListByOpenId(openId);
		for(CommentsVO c : commentList) {
			String timeAgo = TimeAgoUtils.format(c.getCreateTime());
			c.setTimeAgoStr(timeAgo);
		}
		CommentExecution ce = new CommentExecution();
		ce.setCommentsVOList(commentList);
		return ce;
	}

	@Override
	public CommentExecution modifyCommentToRead(Long commentId) throws RuntimeException {
		if(commentId != -1L) {
			int effectedNum = commentDao.updateCommentToRead(commentId);
			if(effectedNum <= 0 ) {
				throw new RuntimeException("更新留言失败");
			}
			return new CommentExecution(CommentStateEnum.SUCCESS);
		}
		return new CommentExecution(CommentStateEnum.EMPTY);
	}

	@Override
	public CommentExecution modifyComment(Comment comment) throws RuntimeException {
		if(comment != null && comment.getCommentId() != -1L) {
			int effectedNum = commentDao.updateComment(comment);
			if(effectedNum <= 0 ) {
				throw new RuntimeException("更新留言失败");
			}
			return new CommentExecution(CommentStateEnum.SUCCESS);
		}
		return new CommentExecution(CommentStateEnum.EMPTY);
	}

	@Override
	@Transactional
	public CommentExecution deleteComment(long commentId) {
		if(commentId != -1L) {
			try {
				int effectedNum = commentDao.deleteComemnt(commentId);
				if(effectedNum <= 0) {
					throw new RuntimeException("删除留言失败");
				} else {
					return new CommentExecution(CommentStateEnum.SUCCESS);
				}
			} catch (Exception e) {
				throw new RuntimeException("deleteComment error: "
						+ e.getMessage());
			}
		}
		return new CommentExecution(CommentStateEnum.EMPTY);
	}
	
}
