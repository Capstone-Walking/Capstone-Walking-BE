package com.walking.api.config;

import com.walking.member.api.config.MemberApiConfig;
import org.springframework.context.annotation.Import;

@Import({MemberApiConfig.class})
public class ApiConfig {}
