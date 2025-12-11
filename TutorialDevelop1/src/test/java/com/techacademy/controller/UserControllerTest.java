package com.techacademy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.techacademy.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    private MockMvc mockMvc;

    private final WebApplicationContext webApplicationContext;

    UserControllerTest(WebApplicationContext context) {
        this.webApplicationContext = context;
    }

    @BeforeEach
    void beforeEach() {
        // Spring Securityを有効にする
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity()).build();
    }

    @Test
    @DisplayName("User更新画面")
    @WithMockUser
    void testGetUser() throws Exception {
        // HTTPリクエストに対するレスポンスの検証
        MvcResult result = mockMvc.perform(get("/user/update/1/")) // URLにアクセス
            .andExpect(status().isOk()) // ステータスを確認
            .andExpect(model().attributeExists("user")) // Modelの内容を確認
            .andExpect(model().hasNoErrors()) // Modelのエラー有無の確認
            .andExpect(view().name("user/update")) // viewの確認
            .andReturn(); // 内容の取得

        // userの検証
        // Modelからuserを取り出す
        User user = (User)result.getModelAndView().getModel().get("user");
        assertEquals(1, user.getId());
        assertEquals("キラメキ太郎", user.getName());
    }
    /**
	 * getList() メソッドに対するテスト (testGetList) DBから取得したデータに基づいて課題の検証を行います。
	 */
	@Test
	@DisplayName("userリストがDBから取得され、正しく表示される事")
	@WithMockUser
	public void testGetList() throws Exception { // メソッド名を testGetList に変更

		// 実行 (Act)
		MvcResult result = mockMvc.perform(get("/user/list"))

				// 1. HTTPステータスが200 OKであること
				.andExpect(status().isOk())

				// 4. viewの名前が user/list であること
				.andExpect(view().name("user/list"))

				// 2. Modelにuserlistが含まれていること
				.andExpect(model().attributeExists("userlist"))

				// 3. Modelにエラーが無いこと
				.andExpect(model().hasNoErrors()).andReturn();

		List<User> ul = (List<User>) result.getModelAndView().getModel().get("userlist");
		assertEquals(3, ul.size());
		User us = ul.get(0);
		assertEquals(1, us.getId());
		assertEquals("キラメキ太郎", us.getName());
		
		User us1 = ul.get(1);
		assertEquals(2, us1.getId());
		assertEquals("キラメキ次郎", us1.getName());
		
		User us2 = ul.get(2);
		assertEquals(3, us2.getId());
		assertEquals("キラメキ花子", us2.getName());

	}
}





