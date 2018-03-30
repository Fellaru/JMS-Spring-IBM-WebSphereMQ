package ru.fella.learnSpringJMS;

import fella.ru.Person;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

@Data
@SpringBootApplication
public class Application implements CommandLineRunner {

    //Используется для отправки и синхронного приема сообщения
    // ля использования необходимо настроить ConnectionFactory и MessageConverter(если необходимо sendAndConvert)
    @Autowired
    JmsTemplate jmsTemplate;

    @Value("${queu.name}")
    String destinationName;




    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Person person = new Person();
        person.setFirstname("Ella");
        person.setLastname("Fischenko");
        jmsTemplate.convertAndSend(destinationName, person);

    }
}
