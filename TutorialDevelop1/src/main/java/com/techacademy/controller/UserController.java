package com.techacademy.controller;

import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult; 
import org.springframework.validation.annotation.Validated; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.entity.User;
import com.techacademy.service.UserService;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    /** 一覧画面を表示 */
    @GetMapping("/list")
    public String getList(Model model) {
        // 全件検索結果をModelに登録
        model.addAttribute("userlist", service.getUserList());
        // user/list.htmlに画面遷移
        return "user/list";
    }

    /** User登録画面を表示 */
    @GetMapping("/register")
    public String getRegister(@ModelAttribute User user) {
        // User登録画面に遷移
        return "user/register";
    }

    /** User登録処理 */
    @PostMapping("/register")
    public String postRegister(@Validated User user, BindingResult res, Model model) {
        if(res.hasErrors()) {
            // エラーあり
            return getRegister(user);
        }
        // User登録
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }

    // ----- 修正ここから -----

    /** User更新画面を表示 */
    // PathVariable id を必須ではない (required = false) に変更
    // postUser() から呼ばれる際に id=null で呼ばれる可能性があるため
    @GetMapping({"/update/{id}/", "/update/"}) // パスに /update/ も追加
    public String getUser(@PathVariable(name = "id", required = false) Integer id, @ModelAttribute User user, Model model) {
        // 【ヒントによる処理の切り分け】

        // idがnullではない：一覧画面から遷移
        if (id != null) {
            // Modelにはサービスから取得したUserをセットする
            model.addAttribute("user", service.getUser(id));
        } 
        // idがnull：postUser()から遷移 (ModelAttributeでuserオブジェクトが渡されている)
        // else の場合は、@ModelAttribute で受け取った user (エラー情報を持つ) がそのまま model にセットされます。

        // User更新画面に遷移
        return "user/update";
    }

    /** User更新処理 */
    @PostMapping("/update/{id}/")
    public String postUser(@PathVariable("id") Integer id, @Validated @ModelAttribute User user, BindingResult res, Model model) {
        
        // PathVariableからIDをUserオブジェクトに設定
        user.setId(id);

        if(res.hasErrors()) {
            // エラーがある場合、getUser() メソッドを呼び出す
            // その際、引数の id に null を設定し、postUser()から呼ばれたことを getUser() 側で判断させる
            // @ModelAttribute で渡された user はエラー情報を持ったまま getUser() へ渡る
            return getUser(null, user, model);
        }
        
        // User更新
        service.saveUser(user);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }
    // ----- 修正ここまで -----

    /** User削除処理 */
    @PostMapping(path="list", params="deleteRun")
    public String deleteRun(@RequestParam(name="idck") Set<Integer> idck, Model model) {
        // Userを一括削除
        service.deleteUser(idck);
        // 一覧画面にリダイレクト
        return "redirect:/user/list";
    }
}