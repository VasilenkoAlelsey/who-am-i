package com.eleks.academy.whoami.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.eleks.academy.whoami.networking.server.Server;
import com.eleks.academy.whoami.networking.server.ServerImpl;

@Configuration
@PropertySource("application.properties")
public class ContextConfig {

	@Bean
	ServerProperties serverProperties(@Value("${server.port}") Integer port,
			@Value("${game.players}") Integer players) {
		return new ServerProperties(port, players);
	}
	
	@Bean
	Server server(ServerProperties serverProperties) throws IOException {
		return new ServerImpl(serverProperties.getPort(), serverProperties.getPlayers());
	}

}
