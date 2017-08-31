package com.cox.fscm.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class AuthInterceptor implements Filter {
	public static final String AUTHENTICATION_HEADER = "Authorization";
	private Properties authProps;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filter)
			throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {

			if (authProps == null) {
				ServletContext servletContext = request.getServletContext();
				WebApplicationContext webApplicationContext = WebApplicationContextUtils
						.getWebApplicationContext(servletContext);
				authProps = (Properties) webApplicationContext.getBean("authProps");
			}

			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

			AuthenticationService authenticationService = new AuthenticationService();

			AuthResponse authResponse = authenticationService.authenticate(authCredentials, authProps);

			if (authResponse.getResultCode() == 0) {
				filter.doFilter(request, response);
			} else {
				ObjectWriter ow = new ObjectMapper().writer();
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					httpServletResponse.setContentType("application/json");
					PrintWriter writer = httpServletResponse.getWriter();
					writer.write(ow.writeValueAsString(authResponse));
					writer.close();
				}
			}
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		return;
	}

	@Override
	public void destroy() {
		return;
	}

}
