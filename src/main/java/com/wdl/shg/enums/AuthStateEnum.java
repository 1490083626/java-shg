package com.wdl.shg.enums;

public enum AuthStateEnum {
	OFFLINE(-1, "非法用户"), SUCCESS(0, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
			-1001, "操作失败"),EMPTY(-1002, "用户为空");

	private int state;

	private String stateInfo;

	private AuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static AuthStateEnum stateOf(int index) {
		for (AuthStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
