#! /bin/bash

# bulk index
curl -s -XPOST 'http://localhost:9200/stations/_bulk' --data-binary @target/classes/insert-stations;
