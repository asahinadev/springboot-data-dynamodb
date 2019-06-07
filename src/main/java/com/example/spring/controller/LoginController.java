package com.example.spring.controller;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.example.spring.form.UserForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

	/**
	 * login ページ用（正常時）.
	 * 
	 * @param request リクエスト情報
	 * @param form    入力フォーム
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping()
	public String login(@ModelAttribute("form") UserForm form) {

		log.debug("start");
		return "login";
	}

	/**
	 * login ページ用（異常時）.
	 * 
	 * @param request   リクエスト情報
	 * @param form      入力フォーム
	 * @param exception 認証エラー
	 * @return 画面表示用ワード（テンプレート、リダイレクト）.
	 */
	@GetMapping(params = "error")
	public String login(
			Model model,
			@ModelAttribute("form") UserForm form,
			@SessionAttribute(name = WebAttributes.AUTHENTICATION_EXCEPTION, required = false) Exception exception) {

		if (exception == null) {
			model.addAttribute("message", "エラーが発生しました。");
		} else {

			if (exception instanceof DisabledException) {
				model.addAttribute("message", "アカウントが無効です。");
			} else if (exception instanceof LockedException) {
				model.addAttribute("message", "アカウントが凍結中です。");
			} else if (exception instanceof AccountExpiredException) {
				model.addAttribute("message", "アカウントの有効期限が切れています。");
			} else if (exception instanceof CredentialsExpiredException) {
				model.addAttribute("message", "パスワードの有効期限が切れています。");
			} else if (exception instanceof BadCredentialsException) {
				model.addAttribute("message", "アカウントまたはパスワードが一致しませんでした。");
			} else if (exception instanceof UsernameNotFoundException) {
				model.addAttribute("message", "アカウントまたはパスワードが一致しませんでした。");
			} else {
				model.addAttribute("message", "アカウントまたはパスワードが一致しませんでした。");
			}
		}
		return "login";
	}

	@GetMapping(params = "logout")
	public String logout(Model model) {

		model.addAttribute("message", "ログアウトが成功しました。");
		return "login";
	}
}
