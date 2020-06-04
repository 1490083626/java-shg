package com.wdl.shg.dto;

import java.util.List;

import com.wdl.shg.entity.Collect;
import com.wdl.shg.enums.CollectStateEnum;

public class CollectExecution {
		//结果状态
		private int state;
		
		//状态标识
		private String stateInfo;
		
		//评论数量
		private int count;
		
		// 操作的评论（增删改评论的时候用）
		private Collect collect;
		
		//获取的comment列表(查询评论列表的时候用)
		private List<Collect> collectList;
		
		
		public CollectExecution() {
		}
		
		//失败的构造器
		public CollectExecution(CollectStateEnum stateEnum) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
		}
		
		// 成功的构造器
		public CollectExecution(CollectStateEnum stateEnum, Collect collect) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.collect = collect;
		}

		
		public CollectExecution(CollectStateEnum stateEnum, List<Collect> collectList) {
			this.state = stateEnum.getState();
			this.stateInfo = stateEnum.getStateInfo();
			this.collectList = collectList;
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

		public Collect getCollect() {
			return collect;
		}

		public void setCollect(Collect collect) {
			this.collect = collect;
		}

		public List<Collect> getCollectList() {
			return collectList;
		}

		public void setCollectList(List<Collect> collectList) {
			this.collectList = collectList;
		}

	

}
