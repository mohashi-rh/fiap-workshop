package br.com.fiap.fuse.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoClient;

@Configuration
public class MongoClientBeanFactory {
	@Bean(name="mongoBean")
	public MongoClient createMongo() {
		return new MongoClient("192.168.99.1", 27017);
	}
}
