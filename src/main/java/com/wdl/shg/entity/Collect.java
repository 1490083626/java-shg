package com.wdl.shg.entity;

import java.util.Date;

public class Collect {
	private Long collectId;
	private String openId;
	private Long productId;
	private Date createTime;
	private int enableStatus;
	public Long getCollectId() {
		return collectId;
	}
	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
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
	
}
