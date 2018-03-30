#Литература
- [Доки Spring. Использование JMS](https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#jms)
- [Maven plugins для jaxb2. Какие есть чем различаются](https://stackoverflow.com/questions/2432859/difference-of-maven-jaxb-plugins)
- [Документация выбранного maven-jaxb2-plugin](http://www.mojohaus.org/jaxb2-maven-plugin/Documentation/v2.2/)
- [Настройка ConnectionFactory для IBM WebSphere MQ](https://github.com/lzp4ever/IBM_WebSphere_MQ_Spring_Boot_JMS)

#Краткое содержание по Spring JMS Documentation

## SENDING A MESSAGE

JmsTemplate - базовый класс в Spring JMS изпользуется для отправки сообщений и синхронного приема.(Короче только отправки)

The JmsTemplate requires a reference to a ConnectionFactory!! Т.е ссылка на реализацию javax.jms.ConnectionFactory обязательна()

ConnectionFactory -это интерфейс  используемый клиентским приложением как фабрика для создания соединения с JMS provider и
 инкапсулирует в себе различные конфигурационные параметры для соединения с провайдером.

Spring Реализации ConnectionFactory(по документации) :
+ SingleConnectionFactory
```
that will return the same Connection on all createConnection() calls and ignore calls to close().This is useful for testing and
 standalone environments so that the same connection can be used for multiple JmsTemplate calls that may span any number of 
 transactions. SingleConnectionFactory takes a reference to a standard ConnectionFactory that would typically come from JNDI.
```
+ CachingConnectionFactory
```
The CachingConnectionFactory extends the functionality of SingleConnectionFactory and adds the caching of Sessions, 
MessageProducers, and MessageConsumers
```

В выбраную spring реализацию интерфейса ConnectionFactory вставляем специфичную для нашего JMS провайдера 
javax.jms.ConnectionFactory  при помощи метода setTargetConnectionFactory

TODO ПОЧИТАТЬ ПОЧЕМУ НЕЛЬЗЯ ПРОСТО ВСТАВИТЬ СРАЗУ ЭТУ  специфичную для нашего JMS провайдера javax.jms.ConnectionFactory 
ВЕДЬ КОД ПОЗВОЛЯЕТ ЧЕМ ТАК ХОРОШИ SPRING РЕАЛИЗАЦИИ ЧТО БЫ В НИХ ОБОРАЧИВАТЬ

JmsTemplate ожидает обект типа Interface MessageConverter для метода convertAndSend()

Реализации интерфейса MessageConverter:
+ MappingJackson2MessageConverter 
```
Message converter that uses Jackson 2.x to convert messages to and from JSON. Maps an object to a BytesMessage, 
or to a TextMessage if the targetType is set to MessageType.TEXT. Converts from a TextMessage or BytesMessage to an object.
```
+ MarshallingMessageConverter
 ```
 Spring JMS MessageConverter that uses a Marshaller and Unmarshaller. Marshals an object to a BytesMessage, 
 or to a TextMessage if the targetType is set to MessageType.TEXT. Unmarshals from a TextMessage or BytesMessage to an object.
 ```
+ MessagingMessageConverter 
Для конвертации с использованием и заголовков (Header) сообщения и тела(Body)

```
Convert a Message from the messaging abstraction to and from a Message using an underlying MessageConverter for the payload 
and a JmsHeaderMapper to map the JMS headers to and from standard message headers.
``` 
+ SimpleMessageConverter 
```
A simple message converter which is able to handle TextMessages, BytesMessages, MapMessages, and ObjectMessages.
 Used as default conversion strategy by JmsTemplate, for convertAndSend and receiveAndConvert operations.
  Converts a String to a TextMessage, a byte array to a BytesMessage, a Map to a MapMessage, and a Serializable object to 
  a ObjectMessage (or vice versa).
```

##RECEIVING A MESSAGE
Spring предоставляет способ написания message-driven POJOs (MDPs) , аналогичных message-driven beans(MDBs), но только в случае 
spring решения нам не нужно напрямую взаимодействовать с EJB container.

**Message Listener Container** используется для приема сообщений из очереди и запуска MessageListener'а который был инжектирован
 в него(Message Listener Container). Listener container ответственнен за все потоки приема сообщений и отправки в MessageListener
 для обработки этих сообщений. Message listener container посредник между MDP and a messaging provider, и берет на себя заботу о 
 регистрации для получения сообщений, участия в транзакциях, сбора и выпуска ресурсов, преобразования исключений и т. п.
 
 Две стандартные реализации интерфейса Message Listener Container:
- SimpleMessageListenerContainer

- DefaultMessageListenerContainer. 
(This message listener container is the one used in most cases.)

**Для ассинхроной отправки в Spring используется MDPs:**
Про написание MDPs [тут](https://docs.spring.io/spring/docs/5.0.5.BUILD-SNAPSHOT/spring-framework-reference/integration.html#jms-asynchronousMessageReception).
Кратко имплементируем MessageListener интерфейс и настраиваем Message Listener Container/ туда MDPs и настройки подключения и destination


Но можно во все это не влазить а просто воспользоваться аннотацией @JmsListener. Помечаем этой аннотацией метод(Наподобии 
оnMessage в Java EE), а Spring уже инжектирует его в куда нужно и создаст MessageListener и вставит его в Message Listener 
Container(!! ВАЖНО ЭТА СТРОКА ЭТО ТО КАК Я ЭТО ПОНЯЛА). destination Message Listener Container возьмет из параметров 
аннотации @JmsListener . А параметры подключения необходимо настроить в ConnectionFactory(читай выше в SENDING A MESSAGE
 как это делается)

- Аннотация [@JmsListener](https://docs.spring.io/spring/docs/5.0.5.BUILD-SNAPSHOT/spring-framework-reference/integration.html#jms-annotated)
1. To enable support for @JmsListener annotations add @EnableJms to one of your @Configuration classes.
//TODO дописать 

Метод с @JmsListener аннотацией может иметь довольно гибкую сигнатуру

**Основные элементы которые мы можем ввести в сигнатуру JMS listener endpoints(метод с @JmsListener аннотацией):**

- javax.jms.Message или любые классы имплементируещие Message.

- javax.jms.Session

- org.springframework.messaging.Message

- @Header - аннотированные аргументы метода для извлечения определенного значения заголовка, включая стандартные заголовки JMS.

- @Headers - аннотированный аргумент, который также должен быть java.util.Map для доступа ко всем заголовкам

- Не аннотированный элемент, который не является одним из поддерживаемых типов (например, Message и Session), считается payload.
 Вы можете сделать это явным путем аннотировать параметр с помощью @Payload. Вы также можете включить проверку, добавив дополнительную анотация @Valid



## Не было использованно в данном проекте но полезно знать:
+ Destination Management
//TODO попробовать с сервером приложений и JNDI
+Transaction Manager
//Пороботать с локальной и XA транзакциями и Spring JMS
