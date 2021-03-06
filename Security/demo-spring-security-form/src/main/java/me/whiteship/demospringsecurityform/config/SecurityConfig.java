package me.whiteship.demospringsecurityform.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import me.whiteship.demospringsecurityform.account.AccountService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
//	@Autowired
//	AccountService accountService; // 이걸 명시적으로 선언
	
	public AccessDecisionManager accessDecisionManager() {
		
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
		
		DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
		handler.setRoleHierarchy(roleHierarchy);
		
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(handler);
		List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
		
		return new AffirmativeBased(voters);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().mvcMatchers("/favicon.ico");
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()
				.mvcMatchers("/admin").hasRole("ADMIN")
				.mvcMatchers("/user").hasRole("USER")
				.anyRequest().authenticated()
				.accessDecisionManager(accessDecisionManager())
				.and()
			.formLogin()
				.and()
			.httpBasic();
		
		http.logout()
				.logoutSuccessUrl("/");
		
		// 현재 쓰레드에서 하위쓰레드로 공유 가능
		SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	}
	
//	@Override // 명시적으로 선언 -> 근데 빈이 등록만 되어 있으면 알아서 갔다 써서 필요 없음
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.userDetailsService(accountService); // 이걸 써서 유저 정보를 가져온다.
//	}
	
// DB를 사용해서 인증을 하기때문에 이제 이 부분이 필요 없음
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.inMemoryAuthentication()
//				.withUser("Jiyeon").password("{noop}123").roles("USER").and()
//				.withUser("admin").password("{noop}!@#").roles("ADMIN");
//	}
}
