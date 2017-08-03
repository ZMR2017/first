<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/back_page/head.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>babasport-list</title>
<script type="text/javascript">
//修改按钮
function updateSku(skuId){
	//让市场价亮起来
	$("#m" +skuId).attr("disabled",false);
	//售价
	$("#p" +skuId).attr("disabled",false);
	//库存
	$("#i" +skuId).attr("disabled",false);
	//限购
	$("#l" +skuId).attr("disabled",false);
	//运费
	$("#f" +skuId).attr("disabled",false);
}
function addSku(skuId){
	//让市场价暗
	$("#m" +skuId).attr("disabled",true);
	//售价
	$("#p" +skuId).attr("disabled",true);
	//库存
	$("#i" +skuId).attr("disabled",true);
	//限购
	$("#l" +skuId).attr("disabled",true);
	//运费
	$("#f" +skuId).attr("disabled",true);
	
	var m = $("#m" +skuId).val();
	//售价
	var p = $("#p" +skuId).val();
	//库存
	var i = $("#i" +skuId).val();
	//限购
	var l = $("#l" +skuId).val();
	//运费
	var f = $("#f" +skuId).val();
	//保存
	var url = "/sku/add.do";
	var params =
	 {"marketPrice" : m,
	  "skuPrice" : p,
	  "stockInventory" : i,
	  "skuUpperLimit" : l,
	  "deliveFee" : f,
	  "id" : skuId,					
	};
	$.post(url,params,function(data){
		alert(data.message);
	},"json");	
	
	
}

</script>
</head>
<body>
<div class="box-positon">
	<div class="rpos">当前位置: 库存管理 - 列表</div>
	<div class="clear"></div>
</div>
<div class="body-box">
<form method="post" id="tableForm">
<table cellspacing="1" cellpadding="0" border="0" width="100%" class="pn-ltable">
	<thead class="pn-lthead">
		<tr>
			<th width="20"><input type="checkbox" onclick="Pn.checkbox('ids',this.checked)"/></th>
			<th>商品编号</th>
			<th>商品颜色</th>
			<th>商品尺码</th>
			<th>市场价格</th>
			<th>销售价格</th>
			<th>库       存</th>
			<th>购买限制</th>
			<th>运       费</th>
			<th>是否赠品</th>
			<th>操       作</th>
		</tr>
	</thead>
	<tbody class="pn-ltbody">
		<c:forEach items="${skus }" var="sku" >
			<tr bgcolor="#ffffff" onmouseover="this.bgColor='#eeeeee'" onmouseout="this.bgColor='#ffffff'">
				<td><input type="checkbox" name="ids" value="73"/></td>
				<td>${sku.id }--${pno }</td>
				<td align="center">${sku.color.name }</td>
				<td align="center">${sku.size }</td>
				<td align="center"><input type="text" id="m${sku.id }"  disabled="disabled" value="${sku.marketPrice }" size="10"/></td>
				<td align="center"><input type="text" id="p${sku.id }"  disabled="disabled" value="${sku.skuPrice }" size="10"/></td>
				<td align="center"><input type="text" id="i${sku.id }"  disabled="disabled" value="${sku.stockInventory }" size="10"/></td>
				<td align="center"><input type="text" id="l${sku.id }"  disabled="disabled" value="${sku.skuUpperLimit }" size="10"/></td>
				<td align="center"><input type="text" id="f${sku.id }"  disabled="disabled" value="${sku.deliveFee }" size="10"/></td>
				<td align="center">不是</td>
				<td align="center"><a href="javascript:updateSku(${sku.id })" class="pn-opt">修改</a> | <a href="javascript:addSku(${sku.id })" class="pn-opt">保存</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</form>
</div>
</body>
</html>