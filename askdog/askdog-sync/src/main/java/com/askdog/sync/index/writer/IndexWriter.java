package com.askdog.sync.index.writer;

import com.askdog.sync.exception.SyncException;

interface IndexWriter {
    void write(String indexName, String indexType, Long target) throws SyncException;
    void update(String indexName, String indexType, Long target) throws SyncException;
    void delete(String indexName, String indexType, Long target) throws SyncException;
}
