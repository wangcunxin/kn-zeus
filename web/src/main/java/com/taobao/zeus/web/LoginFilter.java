package com.taobao.zeus.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.taobao.zeus.store.UserManager;
import com.taobao.zeus.store.mysql.persistence.ZeusUser;
/**
 * 登陆信息设置
 * @author zhoufang
 *
 */
public class LoginFilter implements Filter {
	
	
	private UserManager userManager;
	private SSOLogin login=new SSOLogin() {
		public String getUid(HttpServletRequest req) {
			return ZeusUser.ADMIN.getUid();
		}
		public String getPhone(HttpServletRequest req) {
			return ZeusUser.ADMIN.getPhone();
		}
		public String getName(HttpServletRequest req) {
			return ZeusUser.ADMIN.getName();
		}
		public String getEmail(HttpServletRequest req) {
			return ZeusUser.ADMIN.getEmail();
		}
	};
	@Override
	public void destroy() {
		// do nothing
	}
	
	
	public interface SSOLogin{
		String getUid(HttpServletRequest req);
		String getEmail(HttpServletRequest req);
		String getName(HttpServletRequest req);
		String getPhone(HttpServletRequest req);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		HttpServletResponse httpResponse=(HttpServletResponse)response;
		httpResponse.setCharacterEncoding("utf-8");

		//线上服务器检测
		if(httpRequest.getRequestURI().equals("/zeus.check")){
			response.getWriter().write("success");
			return;
		}

		// session校验
		HttpSession session = ((HttpServletRequest) request).getSession();
		try {
			ZeusUser user = (ZeusUser) session.getAttribute("user");
			if (null == user) {
				String uid = request.getParameter("uid");
				String password = request.getParameter("password");
				ZeusUser loginUser = userManager.checkUser(uid, password);
				if (null != loginUser) {
					session.setAttribute("user", loginUser);
					LoginUser.user.set(loginUser);
					chain.doFilter(request, response);
					return;
				} else {
					if (null == uid || null == password) {
						response.getWriter().write(createLoginPage(true));
					} else {
						response.getWriter().write(createLoginPage(false));
					}
					return;
				}
			} else {
				LoginUser.user.set(user);
				chain.doFilter(request, response);
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.getWriter().write(createLoginPage(true));

//		String uri=httpRequest.getRequestURI();
//		if(uri.endsWith(".taobao") || uri.endsWith(".js") || uri.endsWith(".css") || uri.endsWith(".gif") ||
//				uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith("dump.do")){
//			chain.doFilter(request, response);
//			return;
//		}
//
//		ZeusUser zeusUser=null;
//		String uid=login.getUid(httpRequest);
//		if(uid==null){
//			return;
//		}
//		zeusUser=new ZeusUser();
//		zeusUser.setEmail(login.getEmail(httpRequest));
//		zeusUser.setUid(login.getUid(httpRequest));
//		zeusUser.setName(login.getName(httpRequest));
//		zeusUser.setPhone(login.getPhone(httpRequest));
//		if(!uid.equals(httpRequest.getSession().getAttribute("user"))){
//			userManager.addOrUpdateUser(zeusUser);
//			httpRequest.getSession().setAttribute("user", zeusUser.getUid());
//		}
//		LoginUser.user.set(zeusUser);
//
//		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ApplicationContext applicationContext=WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		userManager=(UserManager) applicationContext.getBean("userManager");
		if(applicationContext.containsBean("ssoLogin")){
			login=(SSOLogin)applicationContext.getBean("ssologin");
		}
	}

	public String createLoginPage(boolean loginResult) {

		String htmlBefore =
				"<html>" +
						"<head>" +
						"<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">" +
				"<title>宙斯云梯任务平台</title>" +
				"<style type=\"text/css\">" +
				"html{" +
					"width: 100%;" +
					"height: 100%;" +
					"overflow: hidden;" +
					"font-style: sans-serif;" +
				"}" +
				"body{" +
					"width: 100%;" +
					"height: 100%;" +
					"font-family: 'Open Sans',sans-serif;" +
					"margin: 0;" +
					"background-color: #4A374A;" +
				"}" +
				"#login{" +
					"position: absolute;" +
					"top: 50%;" +
					"left:50%;" +
					"margin: -150px 0 0 -150px;" +
					"width: 300px;" +
					"height: 300px;" +
				"}" +
				"#login h1{" +
					"color: #fff;" +
					"text-shadow:0 0 10px;" +
					"letter-spacing: 1px;" +
					"text-align: center;" +
				"}" +
				"h1{" +
					"font-size: 2em;" +
					"margin: 0.67em 0;" +
				"}" +
				"input{" +
					"width: 300px;" +
					"height: 30px;" +
					"margin-bottom: 10px;" +
					"outline: none;" +
					"padding: 10px;" +
					"font-size: 13px;" +
					"color: #fff;" +
					"text-shadow:1px 1px 1px;" +
					"border-top: 1px solid #312E3D;" +
					"border-left: 1px solid #312E3D;" +
					"border-right: 1px solid #312E3D;" +
					"border-bottom: 1px solid #56536A;" +
					"border-radius: 4px;" +
					"background-color: #2D2D3F;" +
				"}" +
				".but{" +
					"width: 300px;" +
					"min-height: 20px;" +
					"display: block;" +
					"background-color: #4a77d4;" +
					"border: 1px solid #3762bc;" +
					"color: #fff;" +
					"padding: 9px 14px;" +
					"font-size: 15px;" +
					"line-height: normal;" +
					"border-radius: 5px;" +
					"margin: 0;" +
				"}" +
				"span{" +
					"margin-bottom: 10px;" +
					"outline: none;" +
					"font-size: 13px;" +
					"color: #FF0000;" +
					"text-shadow:1px 1px 1px;" +
					"border-top: 1px;" +
					"border-left: 1px;" +
					"border-right: 1px;" +
					"border-bottom: 1px;" +
					"border-radius: 4px;" +
				"}"+
				"</style>" +
						"</head>" +
						"<body>" +
						"<div id=\"login\">" +
						"<h1>大数据调度平台登录</h1>" +
						"<form method=\"post\" action=\"/zeus/\" >" +
						"<input type=\"text\" required=\"required\" placeholder=\"用户名\" name=\"uid\">" + "</input>" +
						"<input type=\"password\" required=\"required\" placeholder=\"密码\" name=\"password\">" + "</input>";

		String htmlCentor = "<span>用户名或密码不正确</span>";

		String htmlAfter = "<button class=\"but\" type=\"submit\">登录</button>" +
						"</form>" +
						"</div>" +
						"</body>" +
						"</html>";
		String htmlStr = htmlBefore + htmlAfter;
		if (!loginResult) {
			htmlStr = htmlBefore + htmlCentor + htmlAfter;
		}

		return htmlStr;
	}

}
