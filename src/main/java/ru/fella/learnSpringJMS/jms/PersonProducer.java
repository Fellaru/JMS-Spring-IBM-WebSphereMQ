package ru.fella.learnSpringJMS.jms;

import fella.ru.Person;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by efischenko on 30.03.2018.
 */
@Data
@Component
public class PersonProducer {

    //Используется для отправки и синхронного приема сообщения
    // ля использования необходимо настроить ConnectionFactory и MessageConverter(если необходимо sendAndConvert)
    @Autowired
    private JmsTemplate jmsTemplate;

    @Value("${queu.name}")
    private String destinationName;

    public void send(Person person){
        jmsTemplate.convertAndSend(destinationName, person);
    }
}
