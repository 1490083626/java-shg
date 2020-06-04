package com.wdl.shg.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wdl.shg.dto.CommentsVO;
import com.wdl.shg.entity.Comment;

public interface CommentDao {
	/**
	 * 插入评论
	 * @param product
	 * @return
	 */
	int insertComment(Comment comment);
	
	/**
	 * 查询评论列表并分页
	 * @param comment
	 * @param rowIndex
	 * @param pageSize
	 * @return
	 */
	List<CommentsVO> queryCommentList(
			@Param("productId") int productId,
			@Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);
	
	/**
	 * 查询对应商品的评论总数
	 * @param productId
	 * @return
	 */
	int queryCommentsCount(@Param("productId") int productId);
	
	/**
	 * 获取用户未读留言消息
	 * @param openId
	 * @return
	 */
	List<CommentsVO> queryUnreadCommentListByOpenId(
			@Param("openId") String openId);
	
	/**
	 * 获取用户已读留言消息
	 * @param openId
	 * @return
	 */
	List<CommentsVO> queryReadCommentListByOpenId(
			@Param("openId") String openId);
	
	/**
	 * 未读转已读
	 * @param commentId
	 * @return
	 */
	int updateCommentToRead(Long commentId);
	
	/**
	 * 修改留言
	 * @param comment
	 * @return
	 */
	int updateComment(Comment comment);
	/**
	 * 删除留言
	 * @param wechatUserDOId
	 * @return
	 */
	int deleteComemnt(@Param("commentId") long commentId);

}
