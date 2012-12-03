How to run the project?
===
* make sure you have elasticsearch running on http://localhost:9200 (download .deb [here] (https://github.com/elasticsearch/elasticsearch/downloads)
* edit src/main/resources/elasticsearch/_settings.json and replace cluster.name=lgueye to the suitable value (defaults to 'elasticsearch' if you didn't edit anything in it in /etc/elasticsearch/elasticsearch.yml
* mvn clean verify
