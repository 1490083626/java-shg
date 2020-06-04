package com.wdl.shg.enums;

public enum CollectStateEnum {
	SUCCESS(0, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
			-1001, "操作失败"),EMPTY(-1002, "为空");

	private int state;

	private String stateInfo;

	private CollectStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static CollectStateEnum stateOf(int index) {
		for (CollectStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
