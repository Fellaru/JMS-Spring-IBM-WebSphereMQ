package ru.fella.learnSpringJMS.jms;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.Marshaller;
import java.util.HashMap;

/**
 * Created by efischenko on 29.03.2018.
 */
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

    @Bean
    public Jaxb2Marshaller marshall(@Value("fella.ru") String contextPath, @Value("xsd/Person.xsd") Resource schemaLocation) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        //Задаем package где искать сгенерированные классы
        marshaller.setContextPath(contextPath);
        //Xsd для валидации xml
        marshaller.setSchema(schemaLocation);

        HashMap<String, Object> properties = new HashMap<>();
        //Свойство задающее pretty print xml
        properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.setMarshallerProperties(properties);
        return marshaller;
    }


    //Настройка подключения к IBM WebSphere MQ в соответствии с источником
    @Bean
    public MQQueueConnectionFactory mqQueueConnectionFactory() {
        MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
        mqQueueConnectionFactory.setHostName(host);
        try {
            mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//            mqQueueConnectionFactory.setCCSID(1208);
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


}
