package com.wdl.shg.dto;

import java.util.List;

import com.wdl.shg.entity.Comment;
import com.wdl.shg.enums.CommentStateEnum;

public class CommentExecution {
	//结果状态
	private int state;
	
	//状态标识
	private String stateInfo;
	
	//评论数量
	private int count;
	
	// 操作的评论（增删改评论的时候用）
	private Comment comment;
	
	private CommentsVO commentsVO;
	
	//获取的comment列表(查询评论列表的时候用)
	private List<Comment> commentList;
	
	private List<CommentsVO> commentsVOList;
	
	public CommentExecution() {
	}
	
	//失败的构造器
	public CommentExecution(CommentStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	
	// 成功的构造器
	public CommentExecution(CommentStateEnum stateEnum, Comment comment) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.comment = comment;
	}
	
	public CommentExecution(CommentStateEnum stateEnum, CommentsVO commentsVO) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.commentsVO = commentsVO;
	}
	
	// 成功的构造器
//	public CommentExecution(CommentStateEnum stateEnum, List<Comment> commentList) {
//		this.state = stateEnum.getState();
//		this.stateInfo = stateEnum.getStateInfo();
//		this.commentList = commentList;
//	}
	
	public CommentExecution(CommentStateEnum stateEnum, List<CommentsVO> commentsVOList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.commentsVOList = commentsVOList;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Comment getComment() {
		return comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public CommentsVO getCommentsVO() {
		return commentsVO;
	}

	public void setCommentsVO(CommentsVO commentsVO) {
		this.commentsVO = commentsVO;
	}

	public List<CommentsVO> getCommentsVOList() {
		return commentsVOList;
	}

	public void setCommentsVOList(List<CommentsVO> commentsVOList) {
		this.commentsVOList = commentsVOList;
	}
	
	
}
