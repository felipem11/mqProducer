		package com.felipe.mq;

		import com.felipe.mq.input.RespostaCreditoAPICaller;
		import com.felipe.mq.input.RespostaCreditoAPIResponse;
		import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
		import com.fasterxml.jackson.databind.PropertyNamingStrategy;
		import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
		import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.jms.*;
		import java.util.UUID;

		@SpringBootApplication
@RestController
@EnableJms
public class AppProducer {
	
	static Logger logger = Logger.getLogger(AppProducer.class);
	
	@Autowired
    private JmsTemplate jmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(AppProducer.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(MqspringApplication.class, args);
//		JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
	}
	
	@PutMapping("teste/{id}")
//	@ResponseStatus(HttpStatus.NOT_FOUND)
	RespostaCreditoAPIResponse send(@PathVariable String id,
//			@RequestBody RespostaCreditoAPICaller respostaCreditoAPICaller) throws JsonProcessingException{
                                    @RequestBody String respostaCreditoAPICaller) throws JsonProcessingException{
		System.out.println("Teste ID: " + id	);
		System.out.println("Teste Body: " + respostaCreditoAPICaller	);
		RespostaCreditoAPIResponse response = new RespostaCreditoAPIResponse(UUID.randomUUID().toString());
		System.out.println(response);
		return response;

	}
	@PostMapping("send")
	RespostaCreditoAPICaller send(@RequestBody RespostaCreditoAPICaller respostaCreditoAPICaller) throws JsonProcessingException{


		// Send a message with a POJO - the template reuse the message converter
//		System.out.println("Sending an email message.");
//		jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
		
	    try{
//	        jmsTemplate.convertAndSend("DEV.QUEUE.1", "{\"cliente\": \"felipe\"}");
	    	
	    	ObjectMapper objectMapper = new ObjectMapper();
//	    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//	    	objectMapper.setDateFormat(df);
	    	objectMapper.registerModule(new JavaTimeModule());
	    	objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

//	    	respostaCreditoAPICaller.setDataHoraDecisao(LocalDateTime.now());
	    	String carAsString = objectMapper.writeValueAsString(respostaCreditoAPICaller);
	    	System.out.println(carAsString);
	    	
//	    	jmsTemplate.send("DEV.QUEUE.1", (MessageCreator) session -> session.createTextMessage(carAsString));
	    	
	    	jmsTemplate.send("DEV.QUEUE.1", 
	    			(MessageCreator) mc -> {
	    				TextMessage message = mc.createTextMessage(carAsString);
	    				message.setStringProperty("prioridade", "alta");
	    				return message;
	    				});
	        
//	    	jmsTemplate.convertAndSend("DEV.QUEUE.1", respostaCreditoAPICaller);
//	    	jmsTemplate.send("DEV.QUEUE.1", carAsString);
//	        jmsTemplate.convertAndSend("DEV.QUEUE.1", new Email("Erro na gravacao", "Body by mqspring3"));

	        
	        //	        jmsTemplate.convertAndSend("DEV.QUEUE.1", new Email("email@app.001", "Body by mqspring3"));
//	        jmsTemplate.convertAndSend("mailbox", new Email("info@example.com", "Hello"));
	        logger.debug("SEND");
	        return respostaCreditoAPICaller;
	    }catch(JmsException ex){
	        ex.printStackTrace();
	    }
		return respostaCreditoAPICaller;
	}
	
	 public Message createMessage(Session session, String carAsString) throws JMSException {
         Queue replyQueue = session.createQueue("reply-queue");

         TextMessage message = session.createTextMessage(carAsString);

//         message.setJMSCorrelationID(correlationID.toString());
         message.setJMSReplyTo(replyQueue);

         return message;
     }
	
	@GetMapping("recv")
	String recv(){
	    try{
	    	Destination dados = null;
//			System.out.println(jmsTemplate.receive(dados));
	    	String recv = jmsTemplate.receiveAndConvert("DEV.QUEUE.1").toString();
	        System.out.println("recv App 001: "+ recv);
	    	return recv;
	    }catch(JmsException ex){
	        ex.printStackTrace();
	        return "FAIL";
	    }
	}

			@RequestMapping(value = "login", method = RequestMethod.POST)
			public String login() {

				String username = "felipe";
				String password = "senha";
				Integer TOKEN_EXPIRE_TIME = 60 * 2;

//        User curentUser = new User().selectOne(new EntityWrapper<User>()
//                .eq("username",username)
//                .eq("password",password)
//                .eq("del_flag",0));


				JSONObject result = new JSONObject();

					Jedis jedis = new Jedis("localhost", 6379);
					String token = "sdfrw523r23rsrsd";
					jedis.set(username, token);
//					jedis.expire(username, TOKEN_EXPIRE_TIME);
					jedis.expire(username, 5);
					jedis.set(token, username);
					jedis.expire(token, TOKEN_EXPIRE_TIME);
					Long currentTime = System.currentTimeMillis();
//					jedis.set(token + username, currentTime.toString());

				System.out.println(jedis);
				System.out.println(jedis.get(token));
				System.out.println(jedis.get(username));

					jedis.close();


				return "OK";

			}
	////////////////////////////////////////////////////////////////////////////////////////////
	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
													DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		// This provides all boot's default to this factory, including the message converter
		configurer.configure(factory, connectionFactory);
		// You could still override some of Boot's default if necessary.
		return factory;
	}

	@Bean // Serialize message content to json using TextMessage
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("cliente");
		return converter;
	}
}
