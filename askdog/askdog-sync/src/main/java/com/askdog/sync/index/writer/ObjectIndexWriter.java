package com.askdog.sync.index.writer;

import com.askdog.sync.exception.SyncException;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

abstract class ObjectIndexWriter<T> implements IndexWriter {

    @Autowired
    private Client client;

    private IndexClient<T> indexWriter;

    @PostConstruct
    private void init() {
        indexWriter = new IndexClient<>(client);
    }

    @Override
    public void write(String indexName, String indexType, Long target) throws SyncException {
        T index = toIndex(target);
        indexWriter.write(indexName, indexType, target, index);
    }

    @Override
    public void update(String indexName, String indexType, Long target) throws SyncException {
        T index = toIndex(target);
        indexWriter.update(indexName, indexType, target, index);
    }

    @Override
    public void delete(String indexName, String indexType, Long target) throws SyncException {
        indexWriter.delete(indexName, indexType, target);
    }

    abstract T toIndex(Long target);
}
