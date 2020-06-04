package com.wdl.shg.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CommentsVO {
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
	/**
	 * 评论内容
	 */
	private String comment;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	
    private String faceImage;
    private String nickname;
    private String toNickname;
    private String timeAgoStr;
    private String imgAddr;
    private int enableStatus;
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
	public String getFaceImage() {
		return faceImage;
	}
	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getToNickname() {
		return toNickname;
	}
	public void setToNickname(String toNickname) {
		this.toNickname = toNickname;
	}
	public String getTimeAgoStr() {
		return timeAgoStr;
	}
	public void setTimeAgoStr(String timeAgoStr) {
		this.timeAgoStr = timeAgoStr;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public int getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(int enableStatus) {
		this.enableStatus = enableStatus;
	}

	
	
}
