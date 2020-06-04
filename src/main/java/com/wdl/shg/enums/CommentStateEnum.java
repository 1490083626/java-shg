package com.wdl.shg.enums;

public enum CommentStateEnum {
	OFFLINE(-1, "非法评论"), SUCCESS(0, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(
			-1001, "操作失败"),EMPTY(-1002, "评论为空");

	private int state;

	private String stateInfo;

	private CommentStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public static CommentStateEnum stateOf(int index) {
		for (CommentStateEnum state : values()) {
			if (state.getState() == index) {
				return state;
			}
		}
		return null;
	}
}
