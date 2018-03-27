package com.strat.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class SpringRabbitSampleApplication {

	private static AmqpTemplate amqpTemplate;

	@Bean
	public MessageConverter jsonMessageConverter(){
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public AmqpTemplate userExchangeTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate result = new RabbitTemplate(connectionFactory);
		result.setMessageConverter(jsonMessageConverter());
		result.setExchange("test");
		return result;
	}

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringRabbitSampleApplication.class, args);
		amqpTemplate = context.getBean("userExchangeTemplate", AmqpTemplate.class);
	}

	@GetMapping
	@ResponseBody
	public String test() {
		amqpTemplate.convertAndSend("Hello world!");
		return "successful";
	}
}
