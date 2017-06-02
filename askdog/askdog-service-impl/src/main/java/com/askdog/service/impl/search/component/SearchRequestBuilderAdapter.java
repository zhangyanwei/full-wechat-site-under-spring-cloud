package com.askdog.service.impl.search.component;

import org.elasticsearch.action.search.SearchAction;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.Scroll;

public class SearchRequestBuilderAdapter extends SearchRequestBuilder {

    private SearchScrollRequestBuilder adaptee;

    @SuppressWarnings("ConstantConditions")
    public SearchRequestBuilderAdapter(Client client, SearchScrollRequestBuilder adaptee) {
        super(client, SearchAction.INSTANCE);
        this.adaptee = adaptee;
    }

    @Override
    public SearchRequestBuilder setScroll(Scroll scroll) {
        adaptee.setScroll(scroll);
        return this;
    }

    @Override
    public SearchResponse get() {
        return adaptee.get();
    }

}
