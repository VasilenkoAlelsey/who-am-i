package com.eleks.academy.whoami.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.eleks.academy.whoami.networking.server.Server;
import com.eleks.academy.whoami.networking.server.ServerImpl;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class ContextConfig {

	@Bean
	Server server(ServerProperties serverProperties) throws IOException {
		return new ServerImpl(serverProperties.getPort(), serverProperties.getPlayers());
	}

}
