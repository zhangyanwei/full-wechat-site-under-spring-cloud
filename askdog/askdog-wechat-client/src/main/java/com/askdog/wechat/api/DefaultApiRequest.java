package com.askdog.wechat.api;

import com.askdog.wechat.api.ApiRequest.ApiRequestWithBody;
import com.askdog.wechat.api.ApiRequest.ApiRequestWithoutBody;
import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxclient.impl.RestTemplate;
import com.askdog.wechat.api.wxclient.model.WxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.net.URI;

import static com.askdog.common.utils.Json.readValue;
import static com.askdog.common.utils.Json.writeValueAsString;
import static com.google.common.collect.Lists.newArrayList;
import static java.nio.charset.StandardCharsets.UTF_8;

public class DefaultApiRequest<R extends WxObject, B extends WxObject>
        implements ApiRequest, ApiRequestWithoutBody<R>, ApiRequestWithBody<R, B> {

    private static final Logger logger = LoggerFactory.getLogger(DefaultApiRequest.class);

    private HttpMethod method;
    private URI uri;
    private Class<R> responseType;
    private ClientHttpRequestFactory requestFactory;
    private EntitySerializer entitySerializer;
    private EntityDeserializer entityDeserializer;
    private ResponseChecker responseChecker;

    void setMethod(HttpMethod method) {
        this.method = method;
    }

    void setUri(URI uri) {
        this.uri = uri;
    }

    void setResponseType(Class<R> responseType) {
        this.responseType = responseType;
    }

    public void setRequestFactory(ClientHttpRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    void setEntitySerializer(EntitySerializer entitySerializer) {
        this.entitySerializer = entitySerializer;
    }

    void setEntityDeserializer(EntityDeserializer entityDeserializer) {
        this.entityDeserializer = entityDeserializer;
    }

    public void setResponseChecker(ResponseChecker responseChecker) {
        this.responseChecker = responseChecker;
    }

    @Override
    public R request() throws WxClientException {
        return request(null);
    }

    @Override
    public R request(B body) throws WxClientException {
        RestTemplate template = getRestTemplate();

        String requestBody = serialize(body);
        logger.info("requested data is: ");
        logger.info(requestBody);

        HttpEntity<String> entity = new HttpEntity<>(requestBody);
        ResponseEntity<String> responseEntity = template.exchange(uri, method, entity, String.class);
        String content = responseEntity.getBody();

        logger.info("response data is: ");
        logger.info(content);

        checkResponse(content);
        return deserialize(content, responseType);
    }

    private RestTemplate getRestTemplate() {
        RestTemplate template;
        if (this.requestFactory != null) {
            template =  new RestTemplate(this.requestFactory);
            template.setMessageConverters(newArrayList(new StringHttpMessageConverter(UTF_8)));
        } else {
            template = new RestTemplate(newArrayList(new StringHttpMessageConverter(UTF_8)));
        }
        return template;
    }

    private void checkResponse(String content) throws WxClientException {
        if (this.responseChecker != null) {
            this.responseChecker.check(content);
        } else {
            WxResult wxResult = deserialize(content, WxResult.class);
            if (wxResult != null && wxResult.getCode() != 0) {
                throw new WxClientException(wxResult);
            }
        }
    }

    private String serialize(B entity) {
        if (this.entitySerializer != null) {
            //noinspection unchecked
            return this.entitySerializer.serialize(entity);
        }

        return writeValueAsString(entity);
    }

    private <T> T deserialize(String entity, Class<T> responseType) {
        if (this.entityDeserializer != null) {
            //noinspection unchecked
            return (T) this.entityDeserializer.deserialize(entity, responseType);
        }

        return readValue(entity, responseType);
    }

    interface EntitySerializer<T> {
        String serialize(T entity);
    }

    interface EntityDeserializer<T> {
        T deserialize(String content, Class<T> responseType);
    }

    interface ResponseChecker {
        void check(String content) throws WxClientException;
    }

}
