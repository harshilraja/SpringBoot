package com.tistory.heowc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootLogStashApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(SpringBootLogStashApplicationTests.class);
	
	@Test
	public void test_log() {
		logger.info("test1");
		logger.info("test2");
		logger.info("test3");
		logger.info("test4");
		logger.info("test5");
	}
}
