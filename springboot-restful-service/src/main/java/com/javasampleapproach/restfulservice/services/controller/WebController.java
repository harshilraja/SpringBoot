package com.javasampleapproach.restfulservice.services.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javasampleapproach.restfulservice.model.Customer;

@RestController
public class WebController {
	
	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public String post(@RequestBody Customer cust) {
		System.out.println("/POST request, cust: " + cust.toString());
		return "/Post Successful!";
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Customer get(@RequestParam("id") long id, @RequestParam("name") String name, @RequestParam("age") int age) {
		String info = String.format("/GET info: id=%d, name=%s, age=%d", id, name, age);
		System.out.println(info);
		return new Customer(id, name, age);
	}

	@RequestMapping(value= "/put/{id}", method = RequestMethod.PUT)
	public void put(@PathVariable(value = "id") long id, @RequestBody Customer cust) {
		String info = String.format("id = %d, custinfo = %s", id, cust.toString());
		System.out.println("/PUT info " + info);
	}
	
	@RequestMapping(value= "/delete/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable(value = "id") long id) {
		String info = String.format("/Delele info: id = %d", id);
		System.out.println(info);
	}

}
