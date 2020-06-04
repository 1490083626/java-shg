package com.wdl.shg.util;

public class PageCalculator {
	/**
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return 从数据库的第几条开始
	 */
	public static int calculateRowIndex(int pageIndex,int pageSize) {
		//pageIndex:第几页 ， pageSize每页的条数
		//例如，pageIndex（哪一页的数据）=1,从数据库的第0条数据开始，pageIndex=2，从数据库的第pageSize条数据开始
		return (pageIndex > 0) ? (pageIndex-1) * pageSize:0;
	}
	public static int calculatePageCount(int totalCount, int pageSize) {
		int idealPage = totalCount / pageSize;
		int totalPage = (totalCount % pageSize == 0) ? idealPage
				: (idealPage + 1);
		return totalPage;
	}
	

}
