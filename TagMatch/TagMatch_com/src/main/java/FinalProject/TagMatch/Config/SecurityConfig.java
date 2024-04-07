package FinalProject.TagMatch.Config;


import FinalProject.TagMatch.Reference.Role;
import FinalProject.TagMatch.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .headers(
                        (headerConfig) -> headerConfig.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::disable
                        )
                )
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/") // 로그인 페이지의 URL 설정
                                .permitAll() // 로그인 페이지는 모든 사용자에게 허용
                )
                .authorizeHttpRequests((authorizeRequest) -> authorizeRequest
//                                .requestMatchers("/mypage").hasRole(Role.USER.name())     // 일반로그인 시 마이페이지로 안들어가져서 지움. 나중에 security와 일반로그인을 합칠 때 수정해줄 것.
//                        .requestMatchers("/enterpage", "/mainpage","/tagpage").permitAll()
                                .anyRequest().permitAll()
                )
                .logout( // 로그아웃 성공 시 /mainpage 주소로 이동
                        (logoutConfig) -> logoutConfig
                                .logoutSuccessUrl("/mainpage")
                                .invalidateHttpSession(true)
                )

//                // OAuth2 로그인 기능에 대한 여러 설정
//                .oauth2Login(Customizer.withDefaults()); // 아래 코드와 동일한 결과

                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService))
                        .defaultSuccessUrl("/mainpage", true));


        return http.build();
    }
}