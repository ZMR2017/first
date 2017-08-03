package cn.itcast.core.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.itcast.common.web.session.SessionProvider;
import cn.itcast.core.bean.BuyCart;
import cn.itcast.core.bean.BuyItem;
import cn.itcast.core.bean.product.Sku;
import cn.itcast.core.bean.user.Addr;
import cn.itcast.core.bean.user.Buyer;
import cn.itcast.core.query.user.AddrQuery;
import cn.itcast.core.service.product.SkuService;
import cn.itcast.core.service.user.AddrService;
import cn.itcast.core.web.Constants;

/**
 * 购物车
 * @author Administrator
 *
 */

@Controller
public class CartController {

	//购买按钮
	@RequestMapping(value = "/shopping/buyCart.shtml")
	public String buyCart(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response,Integer skuId,Integer amount,Integer buyLimit,Integer productId){
		//第一步：sku
		
			
			ObjectMapper om = new ObjectMapper();
			om.setSerializationInclusion(Inclusion.NON_NULL);
			//声明购物车
			BuyCart buyCart = null;
			//判断Cookie是否有购物车
			//jessionid
			//buyCart_cookie 
			//
			Cookie[] cookies = request.getCookies();
			if(null != cookies && cookies.length>0){
				for (Cookie cookie : cookies) {
					if(Constants.BUYCART_COOKIE.equals(cookie.getName())){
						//如果有 就使用此购物车
						String value = cookie.getValue();
						//
						try {
							buyCart = om.readValue(value.toString(), BuyCart.class);
						
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}
			}
			
			//如果没有 创建购物车
			
			//购物车 //最后一款
			if(null == buyCart){
				
				buyCart = new BuyCart();
			}
			if(null != skuId ){
			Sku sku = new Sku();
			sku.setId(skuId);
			//添加购物限制
			if(null!=buyLimit){
				
				sku.setSkuUpperLimit(buyLimit);
			}
			
			//创建购物项
			BuyItem buyItem = new BuyItem();
			
			buyItem.setSku(sku);
			//数量
			buyItem.setAmount(amount);
			//添加购物项
			buyCart.addItem(buyItem);
			//最后一款商品ID
			if(null != productId){
				
				buyCart.setProductId(productId);
			}
			
			
			//流
			StringWriter str = new StringWriter();
			//对象转成Json 是一个写的过程 Json是字符串流
			try {
				om.writeValue(str, buyCart);
	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//购物车装进Cookie中  对象装成Json
			Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, str.toString());
 			//关闭浏览器 也要有Cookie
			//默认是-1 关闭浏览器就没了
			//销毁 0 马上就么有
			//expiry 秒
			cookie.setMaxAge(60*60*24);
			//路径
			//默认  /shopping
			//  /shopping
			//  /buyer/*shtml
			//
			cookie.setPath("/");
			//发送
			response.addCookie(cookie);
			
		}
			//装满购物车
			List<BuyItem> items = buyCart.getItems();
			for(BuyItem item: items){
				Sku s = skuService.getSkuByKey(item.getSku().getId());
				item.setSku(s);
				//小计	
			}

			modelMap.addAttribute("buyCart",buyCart);
		
		return "product/cart";
	}
	
	//清空购物车
	@RequestMapping(value = "/shopping/clearCart.shtml")
	public String clearCart(HttpServletRequest request,HttpServletResponse response){
		Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/shopping/buyCart.shtml";
	}
	
	//删除一个购物项
	@RequestMapping( value = "/shopping/deleteItem.shtml")
	public String deleteItem(HttpServletResponse response,HttpServletRequest request,Integer skuId){
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Inclusion.NON_NULL);
		//声明购物车
		BuyCart buyCart = null;
		//判断Cookie是否有购物车
		//jessionid
		//buyCart_cookie 
		//
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length>0){
			for (Cookie cookie : cookies) {
				if(Constants.BUYCART_COOKIE.equals(cookie.getName())){
					//如果有 就使用此购物车
					String value = cookie.getValue();
					//
					try {
						buyCart = om.readValue(value.toString(), BuyCart.class);
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		if(null != buyCart){
			Sku sku = new Sku();
			sku.setId(skuId);
			
			
			//创建购物项
			BuyItem buyItem = new BuyItem();
			buyItem.setSku(sku);
			
			buyCart.deleteItem(buyItem);
			
			//流
			StringWriter str = new StringWriter();
			//对象转成Json 是一个写的过程 Json是字符串流
			try {
				om.writeValue(str, buyCart);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//购物车装进Cookie中  对象装成Json
			Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, str.toString());
				//关闭浏览器 也要有Cookie
			//默认是-1 关闭浏览器就没了
			//销毁 0 马上就么有
			//expiry 秒
			cookie.setMaxAge(60*60*24);
			//路径
			//默认  /shopping
			//  /shopping
			//  /buyer/*shtml
			//
			cookie.setPath("/");
			//发送
			response.addCookie(cookie);
		}


		return "redirect:/shopping/buyCart.shtml";
	}
	//结算
	@RequestMapping(value = "/buyer/trueBuy.shtml")
	public String trueBUy(ModelMap modelMap,HttpServletResponse response,HttpServletRequest request){
		
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Inclusion.NON_NULL);
		//声明购物车
		BuyCart buyCart = null;
		//判断Cookie是否有购物车
		//jessionid
		//buyCart_cookie 
		//
		Cookie[] cookies = request.getCookies();
		if(null != cookies && cookies.length>0){
			for (Cookie cookie : cookies) {
				if(Constants.BUYCART_COOKIE.equals(cookie.getName())){
					//如果有 就使用此购物车
					String value = cookie.getValue();
					//
					try {
						buyCart = om.readValue(value.toString(), BuyCart.class);
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		//判断购物车中是否有商品
		if(null != buyCart){
			//判断购物车中商品是否还有库存
			List<BuyItem> items = buyCart.getItems();
			if(null != items && items.size()>0){
				//购物车中商品项
				Integer i = items.size();
				
				
				//判断购物车中的商品是否有库存
				for (BuyItem buyItem : items) {
					Sku sku = skuService.getSkuByKey(buyItem.getSku().getId());
					//判断库存
					if(sku.getStockInventory()< buyItem.getAmount()){
						//删除此商品
						buyCart.deleteItem(buyItem);
					}
				}
				//清理后商品项个数
				Integer l =items.size();
				//判断清理前后
				if(i>l){
					//修改Cookie中的购物车数据
					//流
					StringWriter str = new StringWriter();
					//对象转成Json 是一个写的过程 Json是字符串流
					try {
						om.writeValue(str, buyCart);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//购物车装进Cookie中  对象装成Json
					Cookie cookie = new Cookie(Constants.BUYCART_COOKIE, str.toString());
						//关闭浏览器 也要有Cookie
					//默认是-1 关闭浏览器就没了
					//销毁 0 马上就么有
					//expiry 秒
					cookie.setMaxAge(60*60*24);
					//路径
					//默认  /shopping
					//  /shopping
					//  /buyer/*shtml
					//
					cookie.setPath("/");
					//发送
					response.addCookie(cookie);
					return "redirct:/shopping/buyCart.shtml";
				}else{
					//收货地址加载
					Buyer buyer = (Buyer) sessionProvider.getAttribute(request, Constants.BUYER_SESSION);
					AddrQuery addrQuery = new AddrQuery();
					addrQuery.setBuyerId(buyer.getUsername());
					//默认是1
					addrQuery.setIsDef(1);
					List<Addr> addrs = addrService.getAddrList(addrQuery);
					
					modelMap.addAttribute("addr", addrs.get(0));
					//装满购物车
					List<BuyItem> its = buyCart.getItems();
					for(BuyItem item: its){
						Sku s = skuService.getSkuByKey(item.getSku().getId());
						item.setSku(s);
						//小计	
					}

					modelMap.addAttribute("buyCart",buyCart);
					
					//正常
					return "product/productOrder";
					
				}
			}else{
				return "redirct:/shopping/buyCart.shtml";
			}
		}else{
			return "redirct:/shopping/buyCart.shtml";
			
		}
	}
	
	@Autowired
	private SkuService skuService;
	@Autowired
	private AddrService addrService;
	@Autowired
	private SessionProvider sessionProvider;
}
