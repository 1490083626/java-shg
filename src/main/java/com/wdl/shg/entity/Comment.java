package com.wdl.shg.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Comment {
	/**
	 * 评论id
	 */
	private Long commentId;
	/**
	 * 被留言评论id
	 */
	private Long fatherCommentId;
	/**
	 * 被留言者
	 */
	private String toUserId;
	private int productId;
	/**
	 * 留言者id
	 */
	private String fromUserId;
	private String comment;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	private int enableStatus;
	private int read;
	public Long getCommentId() {
		return commentId;
	}
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}
	public Long getFatherCommentId() {
		return fatherCommentId;
	}
	public void setFatherCommentId(Long fatherCommentId) {
		this.fatherCommentId = fatherCommentId;
	}
	public String getToUserId() {
		return toUserId;
	}
	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(int enableStatus) {
		this.enableStatus = enableStatus;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
	
	
}
