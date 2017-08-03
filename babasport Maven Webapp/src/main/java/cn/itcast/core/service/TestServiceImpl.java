package cn.itcast.core.service;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.core.bean.TestTb;
import cn.itcast.core.dao.TestTbDao;

@Service
@Transactional
public class TestServiceImpl implements TestTbService{

	@Resource
	private TestTbDao testTbDao;
	
	
	public void addTestTb(TestTb testTb) {

		testTbDao.addTestTb(testTb);
		//throw new RuntimeException();
	}

}
