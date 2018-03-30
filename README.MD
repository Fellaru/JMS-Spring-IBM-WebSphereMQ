#Литература
- [Доки Spring. Использование JMS](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#jms)
- [Maven plugins для jaxb2. Какие есть чем различаются](https://stackoverflow.com/questions/2432859/difference-of-maven-jaxb-plugins)
- [Документация выбранного maven-jaxb2-plugin](http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/)

#Краткое содержание по Spring JMS Documentation

JmsTemplate ожидает обект типа Interface MessageConverter для метода convertAndSend()

Реализации интерфейса MessageConverter:
+ MappingJackson2MessageConverter 
```
Message converter that uses Jackson 2.x to convert messages to and from JSON. Maps an object to a BytesMessage, or to a TextMessage if the targetType is set to MessageType.TEXT. Converts from a TextMessage or BytesMessage to an object.
```
+ MarshallingMessageConverter
 ```
 Spring JMS MessageConverter that uses a Marshaller and Unmarshaller. Marshals an object to a BytesMessage, or to a TextMessage if the targetType is set to MessageType.TEXT. Unmarshals from a TextMessage or BytesMessage to an object.
 ```
+ MessagingMessageConverter 
Для конвертации с использованием и заголовков (Header) сообщения и тела(Body)

```
Convert a Message from the messaging abstraction to and from a Message using an underlying MessageConverter for the payload and a JmsHeaderMapper to map the JMS headers to and from standard message headers.
``` 
+ SimpleMessageConverter 
```
A simple message converter which is able to handle TextMessages, BytesMessages, MapMessages, and ObjectMessages. Used as default conversion strategy by JmsTemplate, for convertAndSend and receiveAndConvert operations. Converts a String to a TextMessage, a byte array to a BytesMessage, a Map to a MapMessage, and a Serializable object to a ObjectMessage (or vice versa).
```

