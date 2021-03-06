$(function() {
	//获取商品列表的URL
	var listUrl = '/shg/useradmin/getproductlist?pageIndex=1&pageSize=999';
	//商品下架URL
	var deleteUrl = '/shg/useradmin/modifyproduct';

	/**
	 * 获取此店铺下的商品列表
	 */
	function getList() {
		//从后台获取此店铺的商品列表
		$.getJSON(listUrl, function(data) {
			if (data.success) {
				var productList = data.productList;
				var tempHtml = '';
				//遍历每条商品信息，拼接成一行显示，列信息包括：
				//商品名称，优先级，上架\下架（含productIp，编辑按钮（含productId）
				//预览（含productId）
				productList.map(function(item, index) {
					var textOp = "下架";
					var contraryStatus = 0;
					if (item.enableStatus == 0) {
						//若状态值为0，表明是已下架的商品，操作变为上架（即点击上架按钮上架相关商品）
						textOp = "上架";
						contraryStatus = 1;
					} else {
						contraryStatus = 0;
					}
					//拼接每件商品的行信息
					tempHtml += '' + '<div class="row row-product">'
							+ '<div class="col-30">'
							+ item.productName
							+ '</div>'
							+ '<div class="col-20">'
							+ item.priority
							+ '</div>'
							+ '<div class="col-50">'
							+ '<a href="#" class="edit" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">编辑</a>'
							+ '<a href="#" class="delete" data-id="'
							+ item.productId
							+ '" data-status="'
							+ contraryStatus
							+ '">'
							+ textOp
							+ '</a>'
							+ '<a href="#" class="preview" data-id="'
							+ item.productId
							+ '" data-status="'
							+ item.enableStatus
							+ '">预览</a>'
							+ '</div>'
							+ '</div>';
				});
				$('.product-wrap').html(tempHtml);
			}
		});
	}

	getList();

	function deleteItem(id, enableStatus) {
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定么?', function() {
			$.ajax({
				url : deleteUrl,
				type : 'POST',
				data : {
					productStr : JSON.stringify(product),
					statusChange : true
				},
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.toast('操作成功！');
						getList();
					} else {
						$.toast('操作失败！');
					}
				}
			});
		});
	}
	//将class为product-wrap里面的a标签绑定上点击的事件
	$('.product-wrap')
			.on(
					'click',
					'a',
					function(e) {
						var target = $(e.currentTarget);
						//如果有class edit则点击就进入店铺信息编辑页面，并带有productId参数
						if (target.hasClass('edit')) {
							window.location.href = '/shg/useradmin/productoperation?productId='
									+ e.currentTarget.dataset.id;
						} else if (target.hasClass('delete')) {
							
							deleteItem(e.currentTarget.dataset.id,
									e.currentTarget.dataset.status);
						} else if (target.hasClass('preview')) {
							window.location.href = '/myo2o/frontend/productdetail?productId='
									+ e.currentTarget.dataset.id;
						}else if(target.hasClass('status')){
							//如果有class status则调用后台功能上y下架相关商品，并带有productld参数
							changeItemStatus(e.currentTarget.dataset.id,e.currentTarget.dataset.status);
						}
					});
	function changeItemStatus(id,enableStatus){
		//定义product json对象并添加productid以及状态（上架/下架）
		var product = {};
		product.productId = id;
		product.enableStatus = enableStatus;
		$.confirm('确定吗？',function(){
			//上下架相关商品
			$.ajax({
				url:statusUrl,
				type:'POST',
				data:{
					productStr:JSON.stringify(product),
					statusChange:true
				},
				dataType:'json',
				success:function(data){
					if(data.success){
						$.toast('操作成功!');
						getList();
					}else{
						$.toast('操作失败！');
					}
				}
			});
		});
	}
	$('#new').click(function() {
		window.location.href = '/o2o/shopadmin/addproduct';
	});
});