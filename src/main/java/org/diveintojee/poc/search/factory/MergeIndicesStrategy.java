package org.diveintojee.poc.search.factory;

import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class MergeIndicesStrategy {

    public void execute(Client client, String configFormat) {
        throw new UnsupportedOperationException("Not yet supported");
    }
}
