package ru.fella.learnSpringJMS.jms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.bind.Marshaller;
import java.util.HashMap;

/**
 * Created by efischenko on 30.03.2018.
 */
@Configuration
public class Jaxb2Config {

    @Bean
    public Jaxb2Marshaller getJaxb2Marshaller(@Value("fella.ru") String contextPath, @Value("xsd/Person.xsd") Resource schemaLocation) {
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
}
