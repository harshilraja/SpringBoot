package com.example;

import com.example.simple.domain.Customer;
import com.example.simple.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository repository;
    @Autowired
    private TestEntityManager testEntityManager;

    // 비영속성 데이터
    private Customer getPersistenceContextCustomer() {
        return new Customer("heo won chul", "010-xxxx-xxxx", "developer");
    }

    @Test
    public void test_update() {
        Customer customer = repository.save(getPersistenceContextCustomer());
        customer.changeBigo("Developer");

        assertNotEquals(repository.save(customer).getBigo(), "developer");
    }
}