package com.example.web;

import com.example.domain.User;
import com.example.domain.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserRepository repository;

	@Test
	@WithMockUser(username = "heowc")
	public void test_unauthorized_findAll() throws Exception {
		given(repository.findAll()).willReturn(null);
		assertThat(mockMvc.perform(get("/user")).andReturn()
				.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
	public void test_authorized_findAll() throws Exception {
		given(repository.findAll()).willReturn(null);
		assertThat(mockMvc.perform(get("/user")).andReturn()
				.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
	}

	@Test
	@WithMockUser(username = "heowc")
	public void test_findOne() throws Exception {
		User user = new User("heowc", "1234", "won chul", "010-xxxx-xxxx", "https://heowc.github.io");
		String result = objectMapper.writeValueAsString(user);

		given(repository.findById(user.getId())).willReturn(user);
		assertThat(mockMvc.perform(get("/user/" + user.getId()))
				.andExpect(content().json(result)));
	}

	@Test
	@WithMockUser(username = "heowc")
	public void test_matches() throws Exception {
		User user = new User("heowc", "1234", "won chul", "010-xxxx-xxxx", "https://heowc.github.io");

		given(repository.findById(user.getId())).willReturn(user);
		assertThat(mockMvc.perform(get("/user/match/" + user.getId())).andReturn()
				.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
	}
}