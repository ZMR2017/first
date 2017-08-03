package cn.itcast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.itcast.common.junit.SpringJunitTest;
import cn.itcast.core.bean.TestTb;
import cn.itcast.core.service.TestTbService;


public class TestTestTb extends SpringJunitTest{
	
	@Autowired
	private TestTbService testTbService;

	@Test
	public void testAdd() throws Exception{
		TestTb testTb = new TestTb();
		testTb.setName("lbx咯");
		
		testTbService.addTestTb(testTb);
	}
}
