#! /bin/bash

while read line
do
    IFS='#'; read -a array <<< "$line"; 
    echo "curl -XPOST 'http://localhost:9200/stations/station' -d '{\"id\": ${array[0]}, \"name\": \"${array[3]}\", \"township\": \"${array[4]}\", \"type\": \"${array[5]}\", \"location\": {\"lat\": \"${array[1]}\", \"lon\": \"${array[2]}\"}}'"
    #echo "${array[@]}"
    #echo -e "$line"
done < ratp_arret_graphique.csv

#curl -XPOST "http://localhost:9200/stations/station" -d '{"id": 1843, "name": "Gare du Nord", "township": "PARIS-10EME", "type": "rer", "location": {"lat": "2.53440609425408", "lon": "48.935489860864"}}'
