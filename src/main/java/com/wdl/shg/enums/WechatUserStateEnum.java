package com.wdl.shg.enums;


public enum WechatUserStateEnum {
	LOGINFAIL(-1, "openId输入有误"), SUCCESS(0, "操作成功"), EMPTY(1, "操作失败"), NULL_AUTH_INFO(-1006,
			"注册信息为空");

	private int state;

	private String stateInfo;

	private WechatUserStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static WechatUserStateEnum stateOf(int index) {
		for (WechatUserStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}

}
