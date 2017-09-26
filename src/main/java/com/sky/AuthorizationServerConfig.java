package com.sky;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	@Autowired
	AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
		endpoints.accessTokenConverter(accessTokenConverter());
		endpoints.tokenStore(tokenStore());
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("lawyer_client_id").secret("lawyer_client_secret")
				.authorizedGrantTypes("authorization_code", "refresh_token", "password").scopes("user_info");
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// TODO Auto-generated method stub
		super.configure(security);
	}

	/**
	 * token存储方式
	 * 
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		TokenStore jwtTokenStore = new JwtTokenStore(accessTokenConverter());
		return jwtTokenStore;
	}

	/**
	 * jwt的转换
	 * 
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {

			/**
			 * 重写增强token方法,用于自定义一些token返回的信息
			 */
			@Override
			public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

				String username = authentication.getName();
				User user = (User) authentication.getPrincipal();
				System.err.println(String.format("username=%s,user=%s", username, user.toString()));

				final Map<String, Object> authInfo = new HashMap<String, Object>();
				authInfo.put("username", username);
				authInfo.put("roles", user.getAuthorities());

				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(authInfo);

				// 加强后的token
				OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
				System.err.println("加强后的enhancedToken=" + enhancedToken.getValue());

				return enhancedToken;
			}

		};

		// 测试用,资源服务使用相同的字符达到一个对称加密的效果,生产时候使用RSA非对称加密方式
		accessTokenConverter.setSigningKey("123456");
		return accessTokenConverter;
	}
}
