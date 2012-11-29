class GenerateStations {

    def run() {
        new File("target/classes/insert-stations").newOutputStream().withWriter("UTF-8") { writer ->
            final File ratpStationsFile = new File(getClass().getResource("ratp-stations.csv").getFile());
            ratpStationsFile.splitEachLine("#") {fields ->
                def id = fields[0]
                def lat = fields[1]
                def lng = fields[2]
                def name = URLEncoder.encode(fields[3])
                def township = URLEncoder.encode(fields[4])
                def type = fields[5]
//                def postedContent = "'{\"id\": $id, \"name\": \"$name\", \"township\": \"$township\", \"type\": \"$type\", \"location\": {\"lat\": \"$lat\", \"lon\": \"$lng\"}}'"
//                writer.write "curl -XPOST 'http://localhost:9200/stations/station/$id' -d $postedContent\n"
                def metadata = "{ \"index\" : { \"_index\" : \"stations\", \"_type\" : \"station\", \"_id\" : \"$id\" } }\n"
                writer.write metadata
                def content = "{\"id\": $id, \"name\": \"$name\", \"township\": \"$township\", \"type\": \"$type\", \"location\": {\"lat\": \"$lat\", \"lon\": \"$lng\"}}\n"
                writer.write content
            }
        }
    }

    static main(args) {
        new GenerateStations().run()
    }

}

