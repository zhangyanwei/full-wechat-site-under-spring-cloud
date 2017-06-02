package com.askdog.wechat.api.wxclient.impl;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class RestTemplate extends org.springframework.web.client.RestTemplate {

    public RestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public RestTemplate(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    protected <T> ResponseExtractor<ResponseEntity<T>> responseEntityExtractor(Type responseType) {
        return String.class == responseType ? new StringResponseExtractor<>() : super.responseEntityExtractor(responseType);
    }

    private static class StringResponseExtractor<T> implements ResponseExtractor<ResponseEntity<T>> {

        @SuppressWarnings("unchecked")
        @Override
        public ResponseEntity<T> extractData(ClientHttpResponse response) throws IOException {
            String value = CharStreams.toString(new InputStreamReader(response.getBody(), Charsets.UTF_8));
            return new ResponseEntity(value, response.getHeaders(), response.getStatusCode());
        }
    }
}
