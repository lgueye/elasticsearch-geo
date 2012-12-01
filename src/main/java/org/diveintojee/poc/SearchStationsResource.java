package org.diveintojee.poc;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author louis.gueye@gmail.com
 */
@Component
@Path(SearchStationsResource.RESOURCE_PATH)
@Scope("request")
public class SearchStationsResource {

    public static final String RESOURCE_PATH = "/stations/search";

    @Autowired
    private Client elasticSearch;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private Geocoder googleGeocoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchStationsResource.class);

    @POST
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response find(@FormParam("address") final String address) throws Throwable {

        QueryBuilder queryBuilder = queryBuilder();

        GeoDistanceSortBuilder sortBuilder = sortBuilder(address);

        SearchResponse searchResponse = response(queryBuilder, sortBuilder);

        List<Station> stations = searchResponseToStations(searchResponse);

        final GenericEntity<List<Station>> entity = new GenericEntity<List<Station>>(stations) {
        };

        if (stations.isEmpty()) {
            SearchStationsResource.LOGGER.info("No results found");
        }

        return Response.ok(entity).build();

    }

    private List<Station> searchResponseToStations(SearchResponse searchResponse) {
        List<Station> stations = Lists.newArrayList();
        if (searchResponse != null) {
            SearchHits searchHits = searchResponse.getHits();
            for (SearchHit searchHit : searchHits) {
                byte[] accountAsBytes = searchHit.source();
                Station Station = convert(accountAsBytes);
                stations.add(Station);
            }
        }
        return stations;
    }

    private GeoDistanceSortBuilder sortBuilder(String address) {
        GeocoderResult result = geocodeProvidedAddress(address);
        LatLng location = result.getGeometry().getLocation();
        BigDecimal lat = location.getLat();
        BigDecimal lng = location.getLng();
        return SortBuilders
                .geoDistanceSort("location")
                .point(lng.doubleValue(), lat.doubleValue())
                .unit(DistanceUnit.KILOMETERS)
                .order(SortOrder.ASC);
    }

    private SearchResponse response(QueryBuilder queryBuilder, GeoDistanceSortBuilder sortBuilder) {
        return elasticSearch
                    .prepareSearch("stations")
                    .setTypes("station")
                    .setQuery(queryBuilder)
                    .addSort(sortBuilder)
                    .execute().actionGet();
    }

    private QueryBuilder queryBuilder() {
        BoolFilterBuilder filterBuilder = FilterBuilders.
                boolFilter().
                mustNot(FilterBuilders.termFilter("type", StationType.bus.toString()));
        return new FilteredQueryBuilder(new MatchAllQueryBuilder(), filterBuilder);
    }

    private GeocoderResult geocodeProvidedAddress(String address) {
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
        GeocodeResponse geocoderResponse = googleGeocoder.geocode(geocoderRequest);
        List<GeocoderResult> results = geocoderResponse.getResults();
        if (CollectionUtils.isEmpty(results)) {
            String message = "The geocoding service found no match for address [{}]";
            LOGGER.error(message,  address);
            throw new RuntimeException("geocode.no.results");
        }
        int countResults = results.size();
        if (countResults > 1) {
            String message = "The geocoding service found {} matches for addresses [{}]";
            LOGGER.error(message, countResults, address);
            throw new RuntimeException("geocode.too.many.results");
        }

        return results.iterator().next();
    }

    private Station convert(byte[] source) {
        if (source == null || source.length == 0) return null;
        try {
            this.jsonMapper.getDeserializationConfig()
                    .without(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
            final Station station = this.jsonMapper.readValue(source, Station.class);
            return station;
        } catch (final IOException ignored) {
            throw new IllegalStateException(ignored);
        }
    }
}
