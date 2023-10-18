Kafka: docker-compose -f common.yml -f kafka_cluster.yml up
Elastic: docker-compose -f common.yml -f elastic_cluster.yml up

### KAFKA ###
```shell 
 docker-compose -f common.yml -f kafka_cluster.yml up
```

### ELASTICSEARCH ###
```shell 
 docker-compose -f common.yml -f elastic_cluster.yml up
```

### DOCKER PS (check active containers) ###
```shell 
 docker ps
```

### DOCKER PS (check active containers) ###
```shell 
 curl --location 'localhost:9200/twitter-index/_search?q=id:3484704647758181427'
```


docker-compose -f common.yml -f kafka_cluster.yml -f services.yml up