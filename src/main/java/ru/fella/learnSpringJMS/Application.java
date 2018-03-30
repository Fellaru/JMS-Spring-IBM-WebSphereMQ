package ru.fella.learnSpringJMS;

import fella.ru.Person;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.fella.learnSpringJMS.jms.PersonProducer;

@Data
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    PersonProducer personProducer;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        Person person = new Person();
        person.setFirstname("Ella");
        person.setLastname("Fischenko");

        personProducer.send(person);
    }
}
