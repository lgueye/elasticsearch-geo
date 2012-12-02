/**
 *
 */
package org.diveintojee.poc.steps;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.diveintojee.poc.SearchStationsResource;
import org.diveintojee.poc.Station;
import org.hamcrest.Matchers;
import org.jbehave.core.annotations.*;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.OutcomesTable;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author louis.gueye@gmail.com
 */
public class GeoSearchSteps extends BackendBaseSteps {

    private static final String SEARCH_URI = UriBuilder.fromPath("/backend")
            .path(SearchStationsResource.class).build().toString();

    /**
     * @param exchange
     */
    public GeoSearchSteps(Exchange exchange) {
        super(exchange);
    }


    @When("I search the closest stations to \"$address\"")
    public void searchStationByName(@Named("address") String address) {
        this.exchange.getRequest().setBody("address="+address);
        this.exchange.getRequest().setType(MediaType.APPLICATION_FORM_URLENCODED);
        this.exchange.getRequest().setRequestedType(MediaType.APPLICATION_XML);
        this.exchange.getRequest().setUri(SEARCH_URI);
        this.exchange.findEntityByCriteria();

    }


    @Then("I should get the following stations: $table")
    public void theValuesReturnedAre(ExamplesTable table) throws UnsupportedEncodingException, DecoderException {
        List<Station> stations = this.exchange.stationsFromResponse();
        assertEquals(table.getRowCount(), stations.size());
        URLCodec urlCodec = new URLCodec();
        for (int i = 0; i < stations.size(); i++) {
            Map<String, String> actualRow = actualRow(stations.get(i), urlCodec);
            OutcomesTable outcomes = new OutcomesTable();
            Map<String, String> expectedRow = table.getRow(i);
            for (String key : expectedRow.keySet()) {
                outcomes.addOutcome(key, actualRow.get(key), Matchers.equalTo(expectedRow.get(key)));
            }
            outcomes.verify();
        }

    }

    private Map<String, String> actualRow(Station station, URLCodec urlCodec) throws DecoderException, UnsupportedEncodingException {
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        builder.put("id", String.valueOf(station.getId()));
        String name = urlCodec.decode(String.valueOf(station.getName()), "UTF-8");
        builder.put("name", name);
        builder.put("type", String.valueOf(station.getType()));
        return builder.build();
    }

}
