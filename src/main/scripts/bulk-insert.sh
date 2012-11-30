#! /bin/bash

# bulk index
DATA_LOCATION=${project.build.outputDirectory}/insert-stations
curl -s -XPOST 'http://localhost:9200/stations/_bulk' --data-binary @$DATA_LOCATION;
