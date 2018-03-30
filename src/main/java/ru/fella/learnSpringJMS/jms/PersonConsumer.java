package ru.fella.learnSpringJMS.jms;

import fella.ru.Person;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Created by efischenko on 30.03.2018.
 */
//This class named in Spring Doc ->  Asynchronous reception: Message-Driven POJOs
@Component
public class PersonConsumer {

    // Annotation-driven listener endpoints
    @JmsListener(destination = "${queu.name}")
    public void onMessage(Person person) {
        System.out.println("received " + person);
    }
}
