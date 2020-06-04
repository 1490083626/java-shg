package com.wdl.shg.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.entity.ProductImg;
import com.wdl.shg.entity.WechatUserDO;


public class Product {
	private static final long serialVersionUID = -349433539553804024L;
	private Long productId;
	private String productName;
	private String productDesc;
	private String imgAddr;// 简略图
	private String normalPrice;
	private String promotionPrice;
	private Integer priority;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date lastEditTime;
	private Integer enableStatus;//0.下架1.在前端展示系统展示
	private List<ProductImg> productImgList;
	private ProductCategory productCategory;
//	private PersonInfo owner;
	private WechatUserDO owner;
	private Area area;
	private Integer pageView;
	private Long messageAmount; //留言数
	private String linkman; //联系人
	private String contactPhone; //联系电话
	private String contactWechat; //联系微信
	private String openId;
	
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getImgAddr() {
		return imgAddr;
	}
	public void setImgAddr(String imgAddr) {
		this.imgAddr = imgAddr;
	}
	public String getNormalPrice() {
		return normalPrice;
	}
	public void setNormalPrice(String normalPrice) {
		this.normalPrice = normalPrice;
	}
	public String getPromotionPrice() {
		return promotionPrice;
	}
	public void setPromotionPrice(String promotionPrice) {
		this.promotionPrice = promotionPrice;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(Date lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public Integer getEnableStatus() {
		return enableStatus;
	}
	public void setEnableStatus(Integer enableStatus) {
		this.enableStatus = enableStatus;
	}
	public List<ProductImg> getProductImgList() {
		return productImgList;
	}
	public void setProductImgList(List<ProductImg> productImgList) {
		this.productImgList = productImgList;
	}
	public ProductCategory getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

//	public PersonInfo getOwner() {
//		return owner;
//	}
//	public void setOwner(PersonInfo owner) {
//		this.owner = owner;
//	}
	
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}
	
	public Integer getPageView() {
		return pageView;
	}
	public void setPageView(Integer pageView) {
		this.pageView = pageView;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getMessageAmount() {
		return messageAmount;
	}
	public void setMessageAmount(Long messageAmount) {
		this.messageAmount = messageAmount;
	}
	public WechatUserDO getOwner() {
		return owner;
	}
	public void setOwner(WechatUserDO owner) {
		this.owner = owner;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactWechat() {
		return contactWechat;
	}
	public void setContactWechat(String contactWechat) {
		this.contactWechat = contactWechat;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	
}
