package com.wdl.shg.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.wdl.shg.BaseTest;
import com.wdl.shg.dto.CommentExecution;
import com.wdl.shg.dto.CommentsVO;
import com.wdl.shg.entity.Comment;
import com.wdl.shg.enums.CommentStateEnum;

public class CommentServiceTest extends BaseTest{
	@Autowired CommentService commentService;
	
	@Test
	public void testAddComment() throws Exception {
		Comment comment = new Comment();
		comment.setComment("哈哈哈哈哈哈哈哈哈");
		comment.setFromUserId("openid...");
		comment.setProductId(10);
		
		comment.setFatherCommentId(3L);
		comment.setToUserId("openid...");
		CommentExecution ce = commentService.addComment(comment);
		assertEquals(CommentStateEnum.SUCCESS.getState(), ce.getState());
	}
	
	@Test
	public void testQueryCommentList() throws Exception {
		CommentsVO commentsVO = new CommentsVO();
		commentsVO.setProductId(10);
		CommentExecution ce = commentService.getAllComments(commentsVO.getProductId(), 2, 3);
		ce.getCommentsVOList();
		System.out.println(ce.getCount());
		assertEquals(2, ce.getCommentsVOList().size());
	}
}
