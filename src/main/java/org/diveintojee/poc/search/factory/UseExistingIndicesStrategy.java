package org.diveintojee.poc.search.factory;

import org.elasticsearch.client.IndicesAdminClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author louis.gueye@gmail.com
 */
@Component
public class UseExistingIndicesStrategy {

    String resolveNewIndexName(String oldIndexName, final String indexRootName) {
        if (oldIndexName.equals(indexRootName)) return indexRootName;
        return oldIndexName.endsWith("-a") ? indexRootName + "-b" : indexRootName + "-a";
    }

    String resolveOldIndexName(IndicesAdminClient indicesAdminClient, String indexRootName) {
        String oldIndexName;
        final boolean indexAExists = indicesAdminClient.prepareExists(indexRootName + "-a").execute().actionGet().exists();
        final boolean indexBExists = indicesAdminClient.prepareExists(indexRootName + "-b").execute().actionGet().exists();
        if (!indexAExists && !indexBExists) {
            oldIndexName = indexRootName;
        } else if (indexAExists && indexBExists) {
            throw new IllegalStateException("Only 1 " + indexRootName + " index should exist at a time");
        } else if (!indexAExists && !indexBExists) {
            oldIndexName = indexRootName + "-b";
        } else if (indexAExists) {
            oldIndexName = indexRootName + "-a";
        } else {
            oldIndexName = indexRootName + "-b";
        }
        return oldIndexName;
    }

    public void execute(IndicesAdminClient indicesAdminClient, String indexRootName, Map<String, Object> index) {
        String oldIndexName = resolveOldIndexName(indicesAdminClient, indexRootName);
        String newIndexName = resolveNewIndexName(oldIndexName, indexRootName);
        index.put("write-index", newIndexName);
    }
}
