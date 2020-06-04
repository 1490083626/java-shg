package com.wdl.shg.web.superadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="useradmin",method={RequestMethod.GET})
public class ShopAdminController {
	@RequestMapping(value="/shopoperation")
	public String shopOperation() {
		//src/main/resources/spring下spring-web.xml：3.定义视图解析器
		return "shop/shopoperation";
	}
	@RequestMapping(value="/shoplist")
	public String shopList() {

		return "shop/shoplist";
	}
	@RequestMapping(value="/shopmanagement")
	public String shopManagement() {

		return "shop/shopmanagemnet";
	}
	
	@RequestMapping(value="/productoperation")
	public String productOperation() {
		//转发至商品添加/编辑页面
		return "shop/productoperation";
	}
	@RequestMapping(value="/productcategorymanagement")
	public String productCategoryManage() {
		//转发至商品类别管理页面
		return "shop/productcategorymanagement";
	}

	@RequestMapping(value="/productmanagement")
	public String productManagement() {
		//转发至商品管理页面
		return "shop/productmanagement";
	}

}
