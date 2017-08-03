package cn.itcast;

import static org.junit.Assert.*;

import java.io.OutputStream;
import java.io.StringWriter;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Test;

import cn.itcast.core.bean.user.Buyer;

/**
 * 购物车对象  转成JSON
 * @author Administrator
 *
 */
public class TestCookie {

	@Test
	public void testCookie() throws Exception {
		
		Buyer buyer = new Buyer();
		buyer.setUsername("lbx");
		
		//springmvc
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Inclusion.NON_NULL);
		//流
		StringWriter str = new StringWriter();
		//对象转成Json 是一个写的过程 Json是字符串流
		om.writeValue(str, buyer);
		
		System.out.println(str.toString());
		
		//JSON 转对象
		Buyer readValue = om.readValue(str.toString(), Buyer.class);
		
		System.out.println(readValue);
	}
}
