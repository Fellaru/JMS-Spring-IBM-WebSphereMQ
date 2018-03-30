package ru.fella.learnSpringJMS.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.support.converter.MarshallingMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Created by efischenko on 29.03.2018.
 */
@Data
@Configuration
public class JmsConfigure {
    @Value("${ibm.mq.host}")
    private String host;
    @Value("${ibm.mq.port}")
    private Integer port;
    @Value("${ibm.mq.queueManager}")
    private String queueManager;
    @Value("${ibm.mq.channel}")
    private String channel;
    @Value("${ibm.mq.username}")
    private String username;
    @Value("${ibm.mq.password}")
    private String password;

    //Настройка подключения к IBM WebSphere MQ в соответствии с источником
    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName(host);
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            // mqQueueConnectionFactory.setCCSID(1208);
            mqQueueConnectionFactory.setChannel(channel);
            mqQueueConnectionFactory.setPort(port);
            mqQueueConnectionFactory.setQueueManager(queueManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mqQueueConnectionFactory;
    }

    //Настройка подключения (лоогин, пароль) к IBM WebSphere MQ в соответствии с источником
    @Bean
    UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter(MQQueueConnectionFactory mqQueueConnectionFactory) {
        UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter = new UserCredentialsConnectionFactoryAdapter();
        userCredentialsConnectionFactoryAdapter.setUsername(username);
        userCredentialsConnectionFactoryAdapter.setPassword(password);
        userCredentialsConnectionFactoryAdapter.setTargetConnectionFactory(mqQueueConnectionFactory);
        return userCredentialsConnectionFactoryAdapter;
    }

    // Настройка CachingConnectionFactory из Spring документации
    @Bean
    @Primary
    public CachingConnectionFactory cachingConnectionFactory(UserCredentialsConnectionFactoryAdapter userCredentialsConnectionFactoryAdapter) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setTargetConnectionFactory(userCredentialsConnectionFactoryAdapter);
        cachingConnectionFactory.setSessionCacheSize(50);
        cachingConnectionFactory.setReconnectOnException(true);
        return cachingConnectionFactory;
    }

    @Bean
    public MarshallingMessageConverter getMarshallingMessageConverter(Jaxb2Marshaller marshaller) {
        //Подставляем в реализацию наш сгенерированный Jaxb2Marshaller
        return new MarshallingMessageConverter(marshaller);
    }


}
