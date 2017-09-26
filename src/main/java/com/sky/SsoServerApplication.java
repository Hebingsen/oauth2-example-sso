package com.sky;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * oauth2 授权服务与资源服务必须使用注解@EnableResourceServer与@EnableAuthorizationServer
 * @作者 乐此不彼
 * @时间 2017年8月30日
 * @公司 sky工作室
 */
@EnableResourceServer
@SpringBootApplication
public class SsoServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoServerApplication.class, args);
	}
	
	
	/**
	 * 暂时不开启普通access_token的生成
	 * @作者 乐此不彼
	 * @时间 2017年8月31日
	 * @公司 sky工作室
	 */
	//@Component
	//@Configuration
	//@EnableAuthorizationServer
	public static class Oauth2Config extends AuthorizationServerConfigurerAdapter{
		
		@Autowired
		AuthenticationManager authenticationManager;
		
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager);
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory()
			.withClient("lawyer_client_id")
			.secret("lawyer_client_secret")
			.authorizedGrantTypes("authorization_code","refresh_token","password")
			.scopes("user_info");
		}
		
	}
	
}
