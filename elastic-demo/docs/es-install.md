# Install ElasticSearch and Kibana
This demo uses [ElasticSearch 7.5.1](https://www.elastic.co/downloads/) backend.
* download [elasticsearch](https://www.elastic.co/downloads/elasticsearch)
* download [kibana](https://www.elastic.co/downloads/kibana)
* extract both downloaded *.tgz files
  ```
  tar xzvf elasticsearch-7.5.1-linux-x86_64.tar.gz
  tar xzvf kibana-7.5.1-linux-x86_64.tar.gz
  ```
* run elasticsearch server
  ```
  cd elasticsearch-7.5.1/bin
  ./elasticsearch
  ```
* run kibana server
  ```
  cd kibana-7.5.1-linux-x86_64/bin
  ./kibana
  ```
* elasticsearch REST API is available as 
  ```
  http://127.0.0.1:9200
  ```
* kibana web ui is available as 
  ```
  http://127.0.0.1:5601/app/kibana
  ```
  