package com.wdl.shg.service;

import com.wdl.shg.dto.CommentExecution;
import com.wdl.shg.entity.Comment;


public interface CommentService {
	/**
	 * 添加评论
	 * @param comment
	 * @return
	 * @throws RuntimeException
	 */
	CommentExecution addComment(Comment comment) throws RuntimeException;
	/**
	 * 查询商品下的评论
	 * @param productId
	 * @param page
	 * @param pagesize
	 * @return
	 */
	CommentExecution getAllComments(Integer productId, Integer page, Integer pagesize) throws RuntimeException;
	
	CommentExecution getUnreadComments(String openId) throws RuntimeException;
	
	CommentExecution getReadComments(String openId) throws RuntimeException;
	
	CommentExecution modifyCommentToRead(Long commentId) throws RuntimeException;
	
	CommentExecution modifyComment(Comment comment) throws RuntimeException;
	
	CommentExecution deleteComment(long commentId);
}
