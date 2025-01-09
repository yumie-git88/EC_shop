package jp.co.sss.shop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jp.co.sss.shop.filter.LoginFilter;

@Configuration
public class WebConfig {
	@Bean
	public FilterRegistrationBean<LoginFilter> loginFilter() {
		FilterRegistrationBean<LoginFilter> registrationBean= new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginFilter());
		registrationBean.addUrlPatterns("/*"); // 全URLに対してフィルタを適用
		return registrationBean;
	}
}
