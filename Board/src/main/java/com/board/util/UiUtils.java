package com.board.util;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.board.constant.Method;

@Controller
public class UiUtils {

	public String showMessageWithRedirect(@RequestParam(value="message", required = false) String message
										,@RequestParam(value="redirectUri", required = false)String redirectUri
										,@RequestParam(value="method", required = false)Method method
										,@RequestParam(value="params", required = false)Map<String, Object> params, Model model) {
		model.addAttribute("message", message);
		//사용자에게 전달할 메세지
		model.addAttribute("redirectUri", redirectUri);
		//리다이렉트 할 URI를 의미하며
		model.addAttribute("method", method);
		//Method Enum 클래스에 선언한 HTTP 요청 메서드입니다.
		model.addAttribute("params", params);
		//화면(View)으로 전달할 파라미터입니다.

		return "utils/message-redirect";
	}
}
