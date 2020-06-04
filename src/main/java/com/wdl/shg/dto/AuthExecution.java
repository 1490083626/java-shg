package com.wdl.shg.dto;

import java.util.List;

import com.wdl.shg.entity.LocalAuth;
import com.wdl.shg.enums.AuthStateEnum;

public class AuthExecution {
	// 结果状态
	private int state;

	// 状态标识
	private String stateInfo;

	// 用户数量
	private int count;

	// 操作的用户（增删改的时候用）
	private LocalAuth localAuth;

	// 获取的用户列表(查询商品列表的时候用)
	private List<LocalAuth> localAuthList;

	public AuthExecution() {
	}
	
	// 失败的构造器
	public AuthExecution(AuthStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}
	
	// 成功的构造器
	public AuthExecution(AuthStateEnum stateEnum, LocalAuth localAuth) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.localAuth = localAuth;
	}

	// 成功的构造器
	public AuthExecution(AuthStateEnum stateEnum,
			List<LocalAuth> localAuthList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.localAuthList = localAuthList;
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

	public LocalAuth getLocalAuth() {
		return localAuth;
	}

	public void setLocalAuth(LocalAuth localAuth) {
		this.localAuth = localAuth;
	}

	public List<LocalAuth> getLocalAuthList() {
		return localAuthList;
	}

	public void setLocalAuthList(List<LocalAuth> localAuthList) {
		this.localAuthList = localAuthList;
	}
	
}
