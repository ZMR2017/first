package cn.itcast;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.common.junit.SpringJunitTest;
import cn.itcast.core.bean.TestTb;
import cn.itcast.core.bean.product.Brand;
import cn.itcast.core.query.product.BrandQuery;
import cn.itcast.core.service.TestTbService;
import cn.itcast.core.service.product.BrandService;


public class TestBrand extends SpringJunitTest{
	
	@Autowired
	private BrandService brandService;

	@Test
	public void testGet() throws Exception{
		BrandQuery brandQuery = new BrandQuery();
		//brandQuery.setFields("id");
		//brandQuery.setNameLike(true);
		//brandQuery.setName("金");
		brandQuery.orderById(false);
		brandQuery.setPageNo(2);
		brandQuery.setPageSize(2);
		List<Brand> brandList = brandService.getBrandList(brandQuery);
		
		for (Brand brand : brandList) {
			System.out.println(brand.toString());
		}
			
	}
}
