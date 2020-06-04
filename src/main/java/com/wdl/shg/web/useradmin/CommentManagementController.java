package com.wdl.shg.web.useradmin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wdl.shg.dao.CommentDao;
import com.wdl.shg.dto.CommentExecution;
import com.wdl.shg.dto.WechatUserExecution;
import com.wdl.shg.entity.Comment;
import com.wdl.shg.enums.CommentStateEnum;
import com.wdl.shg.enums.WechatUserStateEnum;
import com.wdl.shg.service.CommentService;
import com.wdl.shg.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/useradmin")
@ResponseBody
public class CommentManagementController {
	@Autowired CommentService commentService;
	
	@RequestMapping(value = "/addComment", method = { RequestMethod.POST})
	public Map<String, Object> addComment(@RequestBody Comment comment, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		Long fatherCommentId =  HttpServletRequestUtil.getLong(request, "fatherCommentId");
		String toUserId =  HttpServletRequestUtil.getString(request, "toUserId");
//		String openId = (String) request.getSession().getAttribute("openId");
//		comment.setFromUserId(openId);
		
		if( toUserId!= null && toUserId != "") {
			comment.setToUserId(toUserId);
		}
		if(fatherCommentId > 1) {
			comment.setFatherCommentId(fatherCommentId);
		}
		
		if(comment != null && comment.getComment() != null && comment.getComment() != "") {
			try {
				CommentExecution ce = commentService.addComment(comment);
				if(ce.getState() == CommentStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入评论");
		}
		 
		return modelMap;
	}
	
	@RequestMapping(value = "/getCommentList", method = { RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getCommentList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		
		int productId = HttpServletRequestUtil.getInt(request, "productId");
		
//		if(pageIndex == null) {
//			pageIndex = 1;
//		}
//		if(pageSize == null) {
//			pageIndex = 10;
//		}
		
		if(pageIndex > -1 && pageSize > -1) {
			CommentExecution ce = commentService.getAllComments(productId, pageIndex, pageSize);
			modelMap.put("commentsList", ce.getCommentsVOList());
			modelMap.put("count", ce.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or productId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getUnreadComments", method = { RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getUnreadComments(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		String openId = HttpServletRequestUtil.getString(request, "openId");
		
//		if(pageIndex == null) {
//			pageIndex = 1;
//		}
//		if(pageSize == null) {
//			pageIndex = 10;
//		}
		
		if(openId != null) {
			CommentExecution ce = commentService.getUnreadComments(openId);
			modelMap.put("commentsList", ce.getCommentsVOList());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty openId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getReadComments", method = { RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> getReadComments(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		String openId = HttpServletRequestUtil.getString(request, "openId");
		
		if(openId != null) {
			CommentExecution ce = commentService.getReadComments(openId);
			modelMap.put("commentsList", ce.getCommentsVOList());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty openId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/upadteUnreadComment", method = { RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> upadteUnreadComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		
		Long commentId = HttpServletRequestUtil.getLong(request, "commentId");

		
		if(commentId != -1L) {
			CommentExecution ce = commentService.modifyCommentToRead(commentId);
			if(ce.getState() == CommentStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ce.getStateInfo());
			}
			
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty commentId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/upadteComment", method = { RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> upadteComment(@RequestBody Comment comment, HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if(comment != null && comment.getCommentId() != -1L) {
			CommentExecution ce = commentService.modifyComment(comment);
			if(ce.getState() == CommentStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", ce.getStateInfo());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty comment");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/deleteComment", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> deleteComment(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		long commentId = HttpServletRequestUtil.getLong(request, "commentId");
		if(commentId != -1L) {
			try {
				CommentExecution ce = commentService.deleteComment(commentId);
				if(ce.getState() == CommentStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", ce.getStateInfo());
				}
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "删除失败，留言id不能为空");
		}
		return modelMap;
	}
}
