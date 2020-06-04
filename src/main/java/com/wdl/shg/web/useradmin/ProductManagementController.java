package com.wdl.shg.web.useradmin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wdl.shg.dto.ProductCategoryExecution;

import com.wdl.shg.dto.ImageHolder;
import com.wdl.shg.dto.ProductExecution;
import com.wdl.shg.dto.Result;
import com.wdl.shg.entity.PersonInfo;
import com.wdl.shg.entity.Product;
import com.wdl.shg.entity.ProductCategory;
import com.wdl.shg.entity.WechatUserDO;
import com.wdl.shg.enums.ProductCategoryStateEnum;
import com.wdl.shg.enums.ProductStateEnum;
import com.wdl.shg.exceptions.ProductCategoryOperationException;
import com.wdl.shg.util.CodeUtil;
import com.wdl.shg.util.HttpServletRequestUtil;
import com.wdl.shg.util.TimeAgoUtils;
import com.wdl.shg.service.ProductCategoryService;
import com.wdl.shg.service.ProductService;
import com.wdl.shg.service.WechatUserService;

@Controller
@RequestMapping("/useradmin")
@ResponseBody
public class ProductManagementController {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired WechatUserService wechatUserService;

	//支持上传商品详情图的最大数量
	private static final int IMAGEMAXCOUNT = 6;
	private List<ImageHolder> productImgListFromRequest = new ArrayList<ImageHolder>();

    @RequestMapping(value = "/uploadProduction", method = { RequestMethod.POST})
    public Map<String, Object> addProduction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("进入post方法！");
        StringBuffer stringBuffer = request.getRequestURL();
        System.out.println(stringBuffer);
        

        System.out.println(request.getSession().getAttribute("sessionId"));
        request.getSession().setAttribute("sessionId", request.getSession().getId());
        
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //商品信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		//前端传递的图片数量
		int imgLength = HttpServletRequestUtil.getInt(request, "imgLength");
		String currentImg = (String)request.getSession().getAttribute("currentImg");

//		请求的数量由图片的数量确定（一图片对应一请求），先加入session，再整合入库
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = null;
		if (currentImg == null) {
			request.getSession().setAttribute("currentImg", "s");
			currentImg = (String)request.getSession().getAttribute("currentImg");
		} else if(currentImg != null && currentImg.length() <= imgLength){
			request.getSession().setAttribute("currentImg", currentImg + "s");
			currentImg = (String)request.getSession().getAttribute("currentImg");
		}
		// 将图片放入session
		try {
			//若请求中存在文件流，则取出相关的文件（详情图）
			if (multipartResolver.isMultipart(request)) {
				multipartRequest = (MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				for (int i = 0; i < imgLength; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						//若取出的第i个详情图片文件流不为空，则将其加入session列表，以便后续合并
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						request.getSession().setAttribute("productImg" + i, productImg);
						request.getSession().getAttribute("productImg" + i);
						
						if(i == 0) {
							thumbnail = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
							request.getSession().setAttribute("thumbnail", thumbnail);
						}
						
						System.out.println(request.getSession().getAttribute("productImg" + i));
					}
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		if(currentImg.length() == imgLength) {
			request.getSession().setAttribute("currentImg", null);
			System.out.println("开始入库");
			for (int i = 0; i < imgLength; i++) {
				ImageHolder imageHolder = (ImageHolder)request.getSession().getAttribute("productImg" + i);
				productImgList.add(imageHolder);
				//缩略图
				if(i == 0) {
					thumbnail = (ImageHolder)request.getSession().getAttribute("thumbnail");
				}
			}
			System.out.println(productImgList);
			
			ObjectMapper mapper = new ObjectMapper();
			Product product = new Product();
			try {
				//尝试获取前端传过来的表单string流并将其转换成Product实体类
				product = mapper.readValue(productStr, Product.class);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
	        product.setPriority(0);
	        product.setCreateTime(new Date());
	        product.setLastEditTime(new Date());
	        
			//若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
			if (product != null  && productImgList.size() > 0) {
				try {
	// -------------------------从session中获取当前的Id并赋值给product，减少对前端数据的依赖
					WechatUserDO owner = new WechatUserDO();
					String openId = (String) request.getSession().getAttribute("openId");
					if(openId == null) {
						openId = HttpServletRequestUtil.getString(request, "openId");
						if(openId == null) {
							modelMap.put("success", false);
							modelMap.put("errMsg", "openId 为空");
							return modelMap;
						}
					}
					owner.setOpenId(openId);
					// 获取userId用于创建图片路径
					Long userId = (Long) request.getSession().getAttribute("userId");
					if(openId != null) {
						WechatUserDO wechatUserDO = wechatUserService.getWechatUserByOpenId(openId);
						if (wechatUserDO.getId() != -1L) {
							userId = wechatUserDO.getId();
							owner.setId(userId);
						} else {
							modelMap.put("success", false);
							modelMap.put("errMsg", "userId 为空");
							return modelMap;
						}
					}
					request.getSession().setAttribute("currentOwner", owner);
					WechatUserDO currentOwner = (WechatUserDO) request.getSession().getAttribute(
							"currentOwner");

					product.setOwner(currentOwner);
					//执行添加操作
					ProductExecution pe = productService.addProduct(product,
							thumbnail, productImgList);
					if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pe.getStateInfo());
					}
				} catch (RuntimeException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入商品信息");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "图片未上传全部");
		}

	    return modelMap;
    }
  
	@RequestMapping(value = "/modifyProduction", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduction(HttpServletRequest request) {

        System.out.println(request.getSession().getAttribute("sessionId"));
        request.getSession().setAttribute("sessionId", request.getSession().getId());
        
        Map<String, Object> modelMap = new HashMap<String, Object>();
        //商品信息
		String productStr = HttpServletRequestUtil.getString(request, "productStr");
		
		//前端传递的图片数量
		int imgLength = HttpServletRequestUtil.getInt(request, "imgLength");
		String currentImg = (String)request.getSession().getAttribute("currentImg");

//		请求的数量由图片的数量确定（一图片对应一请求），先加入session，再整合入库
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		MultipartHttpServletRequest multipartRequest = null;
		if (currentImg == null) {
			request.getSession().setAttribute("currentImg", "s");
			currentImg = (String)request.getSession().getAttribute("currentImg");
		} else if(currentImg != null && currentImg.length() <= imgLength){
			request.getSession().setAttribute("currentImg", currentImg + "s");
			currentImg = (String)request.getSession().getAttribute("currentImg");
		}
		// 将图片放入session
		try {
			//若请求中存在文件流，则取出相关的文件（详情图）
			if (multipartResolver.isMultipart(request)) {
				multipartRequest = (MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				for (int i = 0; i < imgLength; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						//若取出的第i个详情图片文件流不为空，则将其加入session列表，以便后续合并
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						request.getSession().setAttribute("productImg" + i, productImg);
						request.getSession().getAttribute("productImg" + i);
						
						if(i == 0) {
							thumbnail = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
							request.getSession().setAttribute("thumbnail", thumbnail);
						}
						
						System.out.println(request.getSession().getAttribute("productImg" + i));
					}
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		
		if(currentImg.length() == imgLength) {
			request.getSession().setAttribute("currentImg", null);
			System.out.println("开始入库");
			for (int i = 0; i < imgLength; i++) {
				ImageHolder imageHolder = (ImageHolder)request.getSession().getAttribute("productImg" + i);
				productImgList.add(imageHolder);
				// 缩略图
				if(i == 0) {
					thumbnail = (ImageHolder)request.getSession().getAttribute("thumbnail");
				}
			}
			System.out.println(productImgList);
			
			ObjectMapper mapper = new ObjectMapper();
			Product product = new Product();
			try {
				// 尝试获取前端传过来的表单string流并将其转换成Product实体类
				product = mapper.readValue(productStr, Product.class);
				
			} catch (Exception e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}
	        product.setPriority(0);
	        product.setCreateTime(new Date());
	        product.setLastEditTime(new Date());
	        
			//若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
			if (product != null  && productImgList.size() > 0) {
				try {
	// -------------------------从session中获取当前的Id并赋值给product，减少对前端数据的依赖
					WechatUserDO owner = new WechatUserDO();
					String openId = (String) request.getSession().getAttribute("openId");
					Long userId = (Long) request.getSession().getAttribute("userId");
					
					if(openId == null) {
						openId = HttpServletRequestUtil.getString(request, "openId");
						if(openId != null) {
							WechatUserDO wechatUserDO = wechatUserService.getWechatUserByOpenId(openId);
							if (wechatUserDO.getId() != -1L) {
								userId = wechatUserDO.getId();
							} else {
								modelMap.put("success", false);
								modelMap.put("errMsg", "userId 为空");
								return modelMap;
							}
						}
						if(openId == null) {
							modelMap.put("success", false);
							modelMap.put("errMsg", "openId 为空");
							return modelMap;
						}
					}
					owner.setId(userId);
					owner.setOpenId(openId);

					request.getSession().setAttribute("currentOwner", owner);
					WechatUserDO currentOwner = (WechatUserDO) request.getSession().getAttribute(
							"currentOwner");

					product.setOwner(currentOwner);
					//执行添加操作
					ProductExecution pe = productService.modifyProduct(product,
							thumbnail, productImgList);
					if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
						modelMap.put("success", true);
					} else {
						modelMap.put("success", false);
						modelMap.put("errMsg", pe.getStateInfo());
					}
				} catch (RuntimeException e) {
					modelMap.put("success", false);
					modelMap.put("errMsg", e.toString());
					return modelMap;
				}

			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "请输入商品信息");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "图片未上传全部");
		}

	    return modelMap;
	}
	
    //TODO remove
	@RequestMapping(value = "/uploadImage1", method = { RequestMethod.POST})
    public  Map<String, Object> weChatImage(
    		@Validated HttpServletRequest request) throws Exception {
        System.out.println("进入post方法！");  
        
        StringBuffer stringBuffer = request.getRequestURL();
        System.out.println(stringBuffer);
        
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0) {
        	for(Cookie cookie : cookies) {
        		String name = cookie.getValue();
        		System.out.println(name);
        	}
        }else {
			System.out.println("cookie is null");
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
			Cookie cookie = new Cookie("lastTime", "2020年03月13日");
			cookie.setMaxAge(60 * 60);
		}
        
        System.out.println(request.getSession().getAttribute("abc"));
        request.getSession().setAttribute("abc", "aaaaaa");
        
        Map<String, Object> modelMap = new HashMap<String, Object>();
		String productStr = HttpServletRequestUtil.getString(request,
				"productStr");
		int imgLength = HttpServletRequestUtil.getInt(request,
				"imgLength");
		String currentImg = (String)request.getSession().getAttribute("currentImg");
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		System.out.println(sessionId);
//		Cookie 
		if(currentImg == null) {
			request.getSession().setAttribute("currentImg", "1");
			currentImg = (String)request.getSession().getAttribute("currentImg");
			//TODO
		}else {
			int currentImgCount = Integer.parseInt(currentImg);
			request.getSession().setAttribute("currentImg", currentImgCount++);
			currentImg = (String)request.getSession().getAttribute("currentImg");
			if(currentImgCount == imgLength) {
				currentImgCount = 0;
			}
		}
		
//		response.setHeader("set-cookie", sessionId);
//      response.addCookie(cookie);
		if(imgLength >= 0) return null;
		
		ObjectMapper mapper = new ObjectMapper();
		Product product = new Product();
        product = mapper.readValue(productStr, Product.class);
        product.setPriority(0);
        product.setCreateTime(new Date());
        product.setLastEditTime(new Date());
        String s = product.getProductName();
        
        


//		if(thumbnailFromRequest == null || productImgListFromRequest.size() != 2) return null;
		
//TODO		
		PersonInfo owner = new PersonInfo();
		owner.setUserId(1L);
		request.getSession().setAttribute("currentOwner", owner);
		WechatUserDO currentOwner = (WechatUserDO) request.getSession().getAttribute(
				"currentOwner");
//		Product product1 = new Product();
		product.setOwner(currentOwner);
		
		//执行添加操作
		ImageHolder thumbnail = null;
		ProductExecution pe = productService.addProduct(product,
				thumbnail, productImgListFromRequest);
		
        MultipartHttpServletRequest multipartRequest = null;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		
		if (multipartResolver.isMultipart(request)) {
			multipartRequest = (MultipartHttpServletRequest) request;
			ImageHolder productImg = null;
			
			for (int i = 0; i < 3; i++) {
				if(i == 0) {
					//获取刚插入商品id，以插入图片
					request.getSession().setAttribute("currentProduct", pe.getProduct());
				}
				Product product2 = (Product)request.getSession().getAttribute("currentProduct");
				System.out.println(product2.getProductId());
				CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
						.getFile("productImg" + i);
				if(productImgFile != null) {
					request.getSession().setAttribute("currentProductImg" + i, productImgFile);
					CommonsMultipartFile productImgFile2 = (CommonsMultipartFile)request.getSession().getAttribute("currentProductImg" + i);
					System.out.println(productImgFile2.getName());
				}
				
				
				if (productImgFile != null) {
					//若取出的第i个详情图片文件流不为空，则将其加入详情图列表
					productImg = new ImageHolder(productImgFile.getOriginalFilename(),
							productImgFile.getInputStream());
					productImgListFromRequest.add(productImg);
				}else {
					//若取出的第i个详情图片文件流为空
					System.out.println("productImg" + i + "为空");
				}
			}
		}
		//取出刚插入的商品id
//		ProductExecution getPe = productService.getProductList(product, 0, 1);
//		Product nowProduct = getPe.getProductList().get(0);
//		System.out.println(nowProduct.getProductId());
        
		if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", pe.getStateInfo());
		}
	 
	        System.out.println(modelMap);
	        return modelMap;
    }

	
	@RequestMapping(value = "/getproductlist", method = RequestMethod.GET)
	@ResponseBody
	//http://localhost:8080/shg/useradmin/getProductList?pageIndex=1&pageSize=999
	private Map<String, Object> getProductList(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//获取前台传过来的用户id
		String openId= HttpServletRequestUtil.getString(request, "openId");
		WechatUserDO currentOwner = new WechatUserDO();
	    if(openId != null) {
	    	currentOwner.setOpenId(openId);
	    } else {
	    	//从当前session中获取用户信息
	    	currentOwner = (WechatUserDO) request.getSession().getAttribute("currentOwner");
	    	if(currentOwner != null) {
	    		openId = currentOwner.getOpenId();
	    	}
		}
		
		//控制判断
		if ((pageIndex > -1) && (pageSize > -1)) {
			//获取传入的需要检索的条件，包括是否需要从某个商品类别以及模糊查找商品名去筛选商品列表
			//筛选的条件可以进行排列组合
			long productCategoryId = HttpServletRequestUtil.getLong(request,
					"productCategoryId");
			String productName = HttpServletRequestUtil.getString(request,
					"productName");
			int enableStatus = HttpServletRequestUtil.getInt(request,
					"enableStatus");
			Product productCondition = compactProductCondition(
					openId, productCategoryId, productName, enableStatus);
			//传入查询条件以及分页信息进行查询，返回相应商品列表以及总数
			ProductExecution pe = productService.getProductList(
					productCondition, pageIndex, pageSize);
			//移出已删除商品
			List<Product> productList = pe.getProductList();
			Iterator<Product> iterator = productList.iterator();
			while (iterator.hasNext()) {
				Product product = iterator.next();
				if(product.getEnableStatus() == 3) {
					iterator.remove();
				}
			}
			pe.setProductList(productList);
			
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/getproductlistByCollect", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getproductlistByCollect(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//获取前台传过来的页码
		int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取前台传过来的每页要求返回的商品数上限
		int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
		//获取前台传过来的用户id
		String openId= HttpServletRequestUtil.getString(request, "openId");
		WechatUserDO currentOwner = new WechatUserDO();
	    if(openId != null) {
	    	currentOwner.setOpenId(openId);
	    } else {
	    	//从当前session中获取用户信息
	    	currentOwner = (WechatUserDO) request.getSession().getAttribute("currentOwner");
	    	if(currentOwner != null) {
	    		openId = currentOwner.getOpenId();
	    	}
		}
		
		//控制判断
		if ((pageIndex > -1) && (pageSize > -1)) {
//			int enableStatus = HttpServletRequestUtil.getInt(request,
//					"enableStatus");
			ProductExecution pe = productService.queryProductByCollect(
					openId, pageIndex, pageSize);
			modelMap.put("productList", pe.getProductList());
			modelMap.put("count", pe.getCount());
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty pageSize or pageIndex or shopId");
		}
		return modelMap;
	}
	
	@RequestMapping(value ="/getproductcategorylist", method=RequestMethod. GET)
	@ResponseBody
	private Result<List<ProductCategory>> getProductCategoryList(HttpServletRequest request) {
//To Be removed-----------------------------------------------------
//		PersonInfo owner = new PersonInfo();
//		owner.setUserId(1L);
//		request.getSession().setAttribute("currentOwner", owner);
//		
//		PersonInfo currentOwner = (PersonInfo) request.getSession().getAttribute(
//					"currentOwner");
		List<ProductCategory> list =null;
		list = productCategoryService.getProductCategoryList();
		return new Result<List<ProductCategory>>(true,list);
//		if(currentOwner != null && currentOwner.getUserId() > 0) {
//			list = productCategoryService.getProductCategoryList();
//			return new Result<List<ProductCategory>>(true,list);
//		}else {
//			ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
//			return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
//		}
	}
	
	// web端
	@RequestMapping(value = "/addproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProduct(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//验证码校验
//		if (!CodeUtil.checkVerifyCode(request)) {
//			modelMap.put("success", false);
//			modelMap.put("errMsg", "输入了错误的验证码");
//			return modelMap;
//		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		String productStr = HttpServletRequestUtil.getString(request,
				"productStr");
		MultipartHttpServletRequest multipartRequest = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		try {
			//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
			if (multipartResolver.isMultipart(request)) {
				multipartRequest = (MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
				thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), thumbnailFile.getInputStream());
				for (int i = 0; i < IMAGEMAXCOUNT; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						//若取出的第i个详情图片文件流不为空，则将其加入详情图列表
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						productImgList.add(productImg);
					}else {
						//若取出的第i个详情图片文件流为空，则终止循环
						break;
					}
				}
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "上传图片不能为空");
				return modelMap;
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			//尝试获取前端传过来的表单string流并将其转换成Product实体类
			product = mapper.readValue(productStr, Product.class);
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		//若Product信息，缩略图以及详情图列表为非空，则开始进行商品添加操作
		if (product != null && thumbnail != null && productImgList.size() > 0) {
			try {
//TODO -------------------------从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				WechatUserDO wechatUserDO = new WechatUserDO();
				wechatUserDO.setId(7L);
				// web端 为管理员上传
				wechatUserDO.setOpenId("admin");
				request.getSession().setAttribute("currentOwner", wechatUserDO);

				product.setOwner(wechatUserDO);
				//执行添加操作
				ProductExecution pe = productService.addProduct(product,
						thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
	
	@RequestMapping(value = "/addproductcategorys", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> addProductCategorys(
			@RequestBody List<ProductCategory> productCategoryList,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
//		Shop currentShop = (Shop) request.getSession().getAttribute(
//				"currentShop");
//		for (ProductCategory pc : productCategoryList) {
//			pc.setShopId(currentShop.getShopId());
//		}
		if (productCategoryList != null && productCategoryList.size() > 0) {
			try {
				ProductCategoryExecution pe = productCategoryService
						.batchAddProductCategory(productCategoryList);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS
						.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (ProductCategoryOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少输入一个商品类别");
		}
		return modelMap;
	}
	

	@RequestMapping(value = "/removeproductcategory", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> removeProductCategory(Long productCategoryId,
			HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (productCategoryId != null && productCategoryId > 0) {
			try {
//				Shop currentShop = (Shop) request.getSession().getAttribute(
//						"currentShop");
				ProductCategoryExecution pe = productCategoryService
						.deleteProductCategory(productCategoryId);
				if (pe.getState() == ProductCategoryStateEnum.SUCCESS
						.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请至少选择一个商品类别");
		}
		return modelMap;
	}
	/**
	 * 通过商品id获取商品信息
	 * @param productId
	 * @return
	 */
	@RequestMapping(value = "/getproductbyid", method = RequestMethod.GET)
	@ResponseBody
	private Map<String, Object> getProductById(@RequestParam Long productId) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//非空判断
		if (productId > -1) {
			//获取商品信息
			Product product = productService.getProductById(productId);
			//获取商品类别列表
			String timeAgo = TimeAgoUtils.format(product.getLastEditTime());
			modelMap.put("timeAgo", timeAgo);
			List<ProductCategory> productCategoryList = productCategoryService
					.getProductCategoryList();
			modelMap.put("product", product);
			modelMap.put("productCategoryList", productCategoryList);
			modelMap.put("success", true);
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "empty productId");
		}
		return modelMap;
	}
	
	// web端
	@RequestMapping(value = "/modifyproduct", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> modifyProduct(HttpServletRequest request) {
		//是商品编辑时候调用还是上下架操作的时候调用
		//若为前者则进行验证码判断，后者则跳过验证码判断
//		boolean statusChange = HttpServletRequestUtil.getBoolean(request,
//				"statusChange");
		Map<String, Object> modelMap = new HashMap<String, Object>();
//		//验证码判断
//		if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
//			modelMap.put("success", false);
//			modelMap.put("errMsg", "输入了错误的验证码");
//			return modelMap;
//		}
		//接收前端参数的变量的初始化，包括商品，缩略图，详情图列表实体类
		ObjectMapper mapper = new ObjectMapper();
		Product product = null;
		ImageHolder thumbnail = null;
		List<ImageHolder> productImgList = new ArrayList<ImageHolder>();
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		//若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
		try {
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				//取出缩略图并构建ImageHolder对象
				CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest
						.getFile("thumbnail");
				if(thumbnailFile != null) {
					thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(), 
							thumbnailFile.getInputStream());
				}
				//取出详情图列表并构建List<lmageHolder>列表对象，最多支持六张图片上传
				for (int i = 0; i < IMAGEMAXCOUNT; i++) {
					CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest
							.getFile("productImg" + i);
					if (productImgFile != null) {
						//若取出的第i个详情图片文件流不为空，则将其加入详情图列表
						ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),
								productImgFile.getInputStream());
						productImgList.add(productImg);
					}else {
						//若取出的第i个详情图片文件流为空，则终止循环
						break;
					}
				}
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}
		try {
			String productStr = HttpServletRequestUtil.getString(request, "productStr");
			//尝试获取前端传过来的表单string流并将其转换成Procduct实体类
			product = mapper.readValue(productStr, Product.class);
		}catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		//非空判断
		if (product != null) {
			try {
//TODO --------------从session中获取当前店铺的Id并赋值给product，减少对前端数据的依赖
				WechatUserDO currentOwner = new WechatUserDO();
				currentOwner.setOpenId("admin");
				currentOwner.setId(7L);
				product.setOwner(currentOwner);
				//开始进行商品信息变更操作
				ProductExecution pe = productService.modifyProduct(product,
						thumbnail, productImgList);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入商品信息");
		}
		return modelMap;
	}
	
	
	@RequestMapping(value = "/updateProductPageView", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> updateProductPageView(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		try {
			long productId = HttpServletRequestUtil.getLong(request, "productId");
			ProductExecution pe = productService.updateProductPageView(productId);
			if(pe.getState() == ProductStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", pe.getStateInfo());
			}
		} catch (Exception e) {
			modelMap.put("success", false);
			modelMap.put("errMsg", e.toString());
			return modelMap;
		}

		return modelMap;
	}
	
	@RequestMapping(value = "/updateProductEnableStatus", method = RequestMethod.POST)
	@ResponseBody
	private Map<String, Object> updateProductEnableStatus(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Long productId = HttpServletRequestUtil.getLong(request, "productId");
		Boolean deleteProduct = HttpServletRequestUtil.getBoolean(request, "deleteProduct");
		Product product = new Product();
		String openId = "";
		if(productId != -1L) {
			product.setProductId(productId);
			
			Product product2 = productService.getProductById(productId);
			openId = product2.getOwner().getOpenId();
			
			if(deleteProduct) {
				product.setEnableStatus(3);
			} else {
				if(product2.getEnableStatus() == 1) {
					product.setEnableStatus(0);
				} else {
					product.setEnableStatus(1);
				}
			}

		}
		
		//若Product为非空，则开始进行商品更新操作
		if (product != null) {
			try {
// -------------------------从session中获取当前的Id并赋值给product，减少对前端数据的依赖
				WechatUserDO owner = new WechatUserDO();
				
				if(openId == null) {
					openId = (String) request.getSession().getAttribute("openId");
					if(openId == null) {
						openId = HttpServletRequestUtil.getString(request, "openId");
						if(openId == null) {
							modelMap.put("success", false);
							modelMap.put("errMsg", "openId 为空");
							return modelMap;
						}
					}

				}
				owner.setOpenId(openId);
				product.setOwner(owner);
				
				
				
				//执行更新操作
				ProductExecution pe = productService.modifyProduct(product,
						null, null);
				if (pe.getState() == ProductStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				} else {
					modelMap.put("success", false);
					modelMap.put("errMsg", pe.getStateInfo());
				}
			} catch (RuntimeException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.toString());
				return modelMap;
			}

		}

		return modelMap;
	}
	
	private Product compactProductCondition(String openId,
			long productCategoryId, String productName,
			int enableStatus) {
		Product productCondition = new Product();
		if(openId != null) {
			WechatUserDO owner = new WechatUserDO();
			owner.setOpenId(openId);
			productCondition.setOwner(owner);
		}
		
		//若有指定类别的要求则添加进去
		if (productCategoryId != -1L) {
			ProductCategory productCategory = new ProductCategory();
			productCategory.setProductCategoryId(productCategoryId);
			productCondition.setProductCategory(productCategory);
		}
		//若有状态的要求则添加进去
		if (enableStatus != -1L) {
			productCondition.setEnableStatus(enableStatus);
		}
		//若有商品名模糊查询的要求则添加进去
		if (productName != null) {
			productCondition.setProductName(productName);
		}
		return productCondition;
	}

}
