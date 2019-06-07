package com.example.spring.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.spring.entity.Roles;
import com.example.spring.entity.User;
import com.example.spring.form.UserForm;
import com.example.spring.service.UserService;
import com.example.spring.validation.group.Create;

@Controller
@RequestMapping(SignupController.URI_PREFIX)
public class SignupController {

	public static final String URI_PREFIX = "/signup";

	public static final String TPL_PREFIX = "signup/";

	public static final String PATH_CREATE = "";

	public static final String PAGE_CREATE = "index";

	@Autowired
	protected UserService service;

	@ModelAttribute("roles")
	public Roles[] roles() {

		return Roles.values();
	}

	/**
	 * create ページ用 (GET).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(PATH_CREATE)
	public String create(Model model, @ModelAttribute("form") UserForm form) {

		// 値の設定
		model.addAttribute("user", new User());
		return TPL_PREFIX + PAGE_CREATE;
	}

	/**
	 * create ページ用 (POST).
	 * 
	 * @param request リクエスト情報.
	 * @param form    入力フォーム.
	 * @param result  バリデーション結果
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@PostMapping(PATH_CREATE)
	public String create(
			Model model,
			@Validated(Create.class) @ModelAttribute("form") UserForm form, BindingResult result,
			RedirectAttributes redirect) {

		// エラー判定
		if (result.hasErrors()) {
			// 値の設定
			model.addAttribute("user", new User());
			return TPL_PREFIX + PAGE_CREATE;
		}

		// 登録処理
		User user = new User();
		BeanUtils.copyProperties(form, user);
		user = service.insert(user);

		// 更新結果をリダイレクト先に
		redirect.addFlashAttribute("success", "登録に成功しました。");
		return "redirect:/login";
	}

}
