#Литература
- [Доки Spring. Использование JMS](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#jms)
- [Maven plugins для jaxb2. Какие есть чем различаются](https://stackoverflow.com/questions/2432859/difference-of-maven-jaxb-plugins)
- [Документация выбранного maven-jaxb2-plugin](http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/)

#Краткое содержание по Spring JMS Documentation

JmsTemplate - базовый класс в Spring JMS изпользуется для отправки сообщений и синхронного приема.(Короче только отправки)

The JmsTemplate requires a reference to a ConnectionFactory!! Т.е ссылка на реализацию javax.jms.ConnectionFactory обязательна()

ConnectionFactory -это интерфейс  используемый клиентским приложением как фабрика для создания соединения с JMS provider и инкапсулирует в себе различные конфигурационные параметры для соединения с провайдером.

Spring Реализации ConnectionFactory(по документации) :
+ SingleConnectionFactory
```
that will return the same Connection on all createConnection() calls and ignore calls to close().This is useful for testing and standalone environments so that the same connection can be used for multiple JmsTemplate calls that may span any number of transactions. SingleConnectionFactory takes a reference to a standard ConnectionFactory that would typically come from JNDI.
```
+ CachingConnectionFactory
```
The CachingConnectionFactory extends the functionality of SingleConnectionFactory and adds the caching of Sessions, MessageProducers, and MessageConsumers
```

В выбраную spring реализацию интерфейса ConnectionFactory вставляем специфичную для нашего JMS провайдера javax.jms.ConnectionFactory  при помощи метода setTargetConnectionFactory

TODO ПОЧИТАТЬ ПОЧЕМУ НЕЛЬЗЯ ПРОСТО ВСТАВИТЬ СРАЗУ ЭТУ  специфичную для нашего JMS провайдера javax.jms.ConnectionFactory ВЕДЬ КОД ПОЗВОЛЯЕТ ЧЕМ ТАК ХОРОШИ SPRING РЕАЛИЗАЦИИ ЧТО БЫ В НИХ ОБОРАЧИВАТЬ

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

## Не было использованно в данном проекте но полезно знать:
+ Destination Management
