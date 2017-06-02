package com.askdog.sync.index.writer;

import com.askdog.sync.exception.SyncException;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.askdog.sync.common.Json.toBytes;
import static java.lang.String.valueOf;

class IndexClient<T> {

    private static final Logger logger = LoggerFactory.getLogger(ObjectIndexWriter.class);

    private final Client client;

    IndexClient(Client client) {
        this.client = client;
    }

    void delete(String indexName, String indexType, Long target) throws SyncException {
        client
                .prepareDelete(indexName, indexType, valueOf(target))
                .get();
        logger.info("deleted the index for type {}, target is {}", indexType, target);
    }

    void write(String indexName, String indexType, Long target, T index) throws SyncException {
        client
                .prepareIndex(indexName, indexType, valueOf(target))
                .setSource(toBytes(index))
                .get();
        logger.info("created the new index for type {}, target is {}", indexType, target);
    }

    void update(String indexName, String indexType, Long target, T index) throws SyncException {
        client
                .prepareUpdate(indexName, indexType, valueOf(target))
                .setDoc(toBytes(index))
                .get();
        logger.info("updated the index for type {}, target is {}", indexType, target);
    }

}
