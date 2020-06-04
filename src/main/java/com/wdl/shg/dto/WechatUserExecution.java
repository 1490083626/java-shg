package com.wdl.shg.dto;

import java.util.List;

import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.WechatUserStateEnum;

public class WechatUserExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	private int count;

	private WechatUserDO wechatUserDO;

	private List<WechatUserDO> wechatUserDOList;
	
	public WechatUserExecution() {
	}
	
	// 失败的构造器
	public WechatUserExecution(WechatUserStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功的构造器
	public WechatUserExecution(WechatUserStateEnum stateEnum, WechatUserDO wechatUserDO) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.wechatUserDO = wechatUserDO;
	}

	// 成功的构造器
	public WechatUserExecution(WechatUserStateEnum stateEnum,
			List<WechatUserDO> wechatUserDOList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.wechatUserDOList = wechatUserDOList;
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

	public WechatUserDO getWechatUserDO() {
		return wechatUserDO;
	}

	public void setWechatUserDO(WechatUserDO wechatUserDO) {
		this.wechatUserDO = wechatUserDO;
	}

	public List<WechatUserDO> getWechatUserDOList() {
		return wechatUserDOList;
	}

	public void setWechatUserDOList(List<WechatUserDO> wechatUserDOList) {
		this.wechatUserDOList = wechatUserDOList;
	}

	
}
