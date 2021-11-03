package com.board.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mybatis.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor{

	//private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("===============================================");
		log.debug("==================== BEGIN ====================");
		log.debug("Request URI ===> " + request.getRequestURI());
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.debug("==================== END ======================");
		log.debug("===============================================");
	}



}
