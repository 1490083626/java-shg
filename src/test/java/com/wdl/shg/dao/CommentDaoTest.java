package com.wdl.shg.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.dto.CommentsVO;
import com.wdl.shg.entity.Comment;

public class CommentDaoTest extends BaseTest{
	@Autowired CommentDao commentDao;
	
	@Test
	public void testInsertComment() throws Exception {
		Comment comment = new Comment();
		comment.setComment("李艳艳无限好");
		comment.setCreateTime(new Date());
		comment.setFatherCommentId(3L);
		comment.setFromUserId("osO245XTZkaBMcMfZiJlHMVq280w");
		comment.setProductId(10);
		comment.setToUserId("openid...");
		int effectedNum = commentDao.insertComment(comment);
		assertEquals(1, effectedNum);
	}
	
	@Test
	public void testQueryCommentList() throws Exception {
		CommentsVO commentsVO = new CommentsVO();
		
//		commentsVO.setProductId(10);
		List<CommentsVO> commentsVOList = commentDao.queryCommentList(commentsVO.getProductId(), 0, 2);
		for(CommentsVO comment2 : commentsVOList) {
			System.out.println(comment2.getFromUserId());
			System.out.println(comment2.getFaceImage());
			System.out.println(comment2.getNickname());
			System.out.println(comment2.getCreateTime());
		}
		System.out.println(commentsVOList.size());
		assertEquals(2, commentsVOList.size());
		
		commentsVO.setProductId(10);
		System.out.println("ccc:" + commentsVO.getProductId());
		int count = commentDao.queryCommentsCount(commentsVO.getProductId());
		
		System.out.println(count);
		assertEquals(count, 8);
	}
	
	@Test
	public void testQueryUnreadCommentListByOpenId() {
		CommentsVO commentsVO = new CommentsVO();
		
		commentsVO.setToUserId("openid...");
		
		List<CommentsVO> commentsVOList = commentDao.queryUnreadCommentListByOpenId(commentsVO.getToUserId());
		for(CommentsVO comment2 : commentsVOList) {
			System.out.println(comment2.getCommentId());
			System.out.println(comment2.getFromUserId());
			System.out.println(comment2.getFaceImage());
			System.out.println(comment2.getNickname());
			System.out.println(comment2.getImgAddr());
		}
	}
	
	@Test
	public void testUpdateComment() {
		Comment comment = new Comment();
		comment.setCommentId(3L);
		comment.setComment("留言改一改+1");
		comment.setEnableStatus(1);
		
		int effectedNum = commentDao.updateComment(comment);
		assertEquals(1, effectedNum);
	}
}
