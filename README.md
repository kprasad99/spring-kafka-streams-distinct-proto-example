# Spring Cloud Kafka Streams Distinct Example(Protobuf)

An example project that filters out duplicate messages by key using kafka streams uses Protobuf format.

## How it works

While serializing class, class name is passed as header, thus while deserializing class name extracted from header to deserialize the data to object.  
## How to run

* Update the `application.yml` to point to your kafka cluster.
* Start the app.
```
mvn spring-boot:run
```

## How to test

* send a curl request(which in turn calls kafka producer and publishes message to `k-msg` topic)
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "a1",
    "type": "info",
    "message": "Hello from my subject"
}'
```
.output
```
2020-06-29 23:52:15.628  INFO 62283 --- [or-http-epoll-3] i.g.k.kafka.endpoint.EventRestService    : Sending message a1
2020-06-29 23:52:15.657  INFO 62283 --- [-StreamThread-1] i.g.k.k.c.StreamsConfigurations          : received event with id a1, with value id: "a1"
type: "info"
message: "Hello from my subject"


```

* send a second curl with different id
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "a2",
    "type": "info",
    "text": "Hello from my subject"
}'
```
.output
```
2020-06-29 23:53:43.850  INFO 62283 --- [or-http-epoll-3] i.g.k.kafka.endpoint.EventRestService    : Sending message a2
2020-06-29 23:53:43.854  INFO 62283 --- [-StreamThread-1] i.g.k.k.c.StreamsConfigurations          : received event with id a2, with value id: "a2"
type: "info"
message: "Hello from my subject"


```

* Now send a curl with same id, Now you can notice logs only sender is logged and not receiver.
```
curl --location --request POST 'http://localhost:8080/api/notify' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id": "a1",
    "type": "info",
    "text": "Hello from my subject"
}'
```
.output
```
2020-06-29 23:55:07.099  INFO 62283 --- [or-http-epoll-3] i.g.k.kafka.endpoint.EventRestService    : Sending message a1
```