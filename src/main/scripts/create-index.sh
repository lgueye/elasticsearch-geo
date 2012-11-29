#! /bin/bash

# drop index
curl -XDELETE 'http://localhost:9200/stations'

# create index with settings
SETTINGS_LOCATION=${project.build.outputDirectory}/elasticsearch/stations/_settings.json
curl -XPOST 'http://localhost:9200/stations' -d@$SETTINGS_LOCATION

# put mapping
MAPPING_LOCATION=${project.build.outputDirectory}/elasticsearch/stations/station.json
curl -XPUT 'http://localhost:9200/stations/station/_mapping' -d@$MAPPING_LOCATION
