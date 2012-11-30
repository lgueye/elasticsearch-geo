curl -XGET 'http://localhost:9200/stations/station/_search?size=5&pretty=true' -d '{"sort":[{"_geo_distance":{"location":[48.8583, 2.2945], "order":"asc", "unit":"km"}}], "query":{"filtered":{"query":{"match_all":{}},"filter":{"bool":{"must_not":{"term":{"type":"bus"}}}}}}}'

