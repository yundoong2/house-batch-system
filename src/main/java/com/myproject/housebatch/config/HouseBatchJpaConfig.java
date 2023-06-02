package com.myproject.housebatch.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //DB에 생성 일시, 수정 일시를 어노테이션으로 설정해줄 수 있게 해줌
public class HouseBatchJpaConfig {

}
