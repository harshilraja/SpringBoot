package com.springjava4dev.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.javasampleapproach.restfulservice.SpringbootRestServiceApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringbootRestServiceApplication.class)
@WebAppConfiguration
public class SpringbootRestServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

}
