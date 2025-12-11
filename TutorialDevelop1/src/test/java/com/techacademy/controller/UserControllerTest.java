package com.techacademy.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.techacademy.entity.User;
import com.techacademy.entity.User.Gender; // UserエンティティのGender Enumをインポート
import com.techacademy.service.UserService;

// UserControllerを対象としたテストであることを指定
@WebMvcTest(UserController.class) 
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private UserService userService;

    /**
     * テスト用のUserリストを作成するヘルパーメソッド
     * Userエンティティに合わせてセッターを使用してUserオブジェクトを作成します。
     */
    private List<User> createTestUserList() {
        List<User> userList = new ArrayList<>();
        
        // 1件目: キラメキ太郎
        User user1 = new User(); 
        user1.setId(1);
        user1.setName("キラメキ太郎");
        // GenderはEnum型なので、User.Gender.男性 を使用します
        user1.setGender(User.Gender.男性); 
        user1.setAge(27);
        user1.setEmail("taro.kirameki@mail.com");
        userList.add(user1);

        // 2件目: キラメキ次郎
        User user2 = new User();
        user2.setId(2);
        user2.setName("キラメキ次郎");
        user2.setGender(User.Gender.男性);
        user2.setAge(22);
        user2.setEmail("jiro.kirameki@mail.com");
        userList.add(user2);

        // 3件目: キラメキ花子
        User user3 = new User();
        user3.setId(3);
        user3.setName("キラメキ花子");
        user3.setGender(User.Gender.女性);
        user3.setAge(25);
        user3.setEmail("hanako.kirameki@mail.com");
        userList.add(user3);

        return userList;
    }

    /**
     * getList() メソッドに対するテスト (testGetList)
     * 課題で指定されたすべての検証を行います。
     */
    @Test
    public void testGetList() throws Exception {
        // 準備 (Arrange)
        List<User> expectedList = createTestUserList();
        
        // モックの設定: userService.getUserList() が呼ばれたら、テストデータ(expectedList) を返す
        when(userService.getUserList()).thenReturn(expectedList);

        // 実行と検証 (Act & Assert)
        mockMvc.perform(get("/user/list")) // /user/list に GET リクエストを送信
                
                // 1. HTTPステータスが200 OKであること
                .andExpect(status().isOk())
                
                // 4. viewの名前が user/list であること
                .andExpect(view().name("user/list"))
                
                // 2. Modelにuserlistが含まれていること
                .andExpect(model().attributeExists("userlist"))
                
                // 3. Modelにエラーが無いこと
                .andExpect(model().hasNoErrors()) 
                
                // 5. Modelからuserlistを取り出す件数が3件であること
                .andExpect(model().attribute("userlist", hasSize(3)))
                
                // 6. userlistから1件ずつ取り出し、idとnameを検証する
                .andExpect(model().attribute("userlist", contains(
                        // 1件目
                        allOf(
                            hasProperty("id", is(1)), 
                            hasProperty("name", is("キラメキ太郎"))
                        ),
                        // 2件目
                        allOf(
                            hasProperty("id", is(2)), 
                            hasProperty("name", is("キラメキ次郎"))
                        ),
                        // 3件目
                        allOf(
                            hasProperty("id", is(3)), 
                            hasProperty("name", is("キラメキ花子"))
                        )
                )));

        // サービスのgetUserListメソッドが1回呼ばれたことを検証
        verify(userService, times(1)).getUserList();
    }
}