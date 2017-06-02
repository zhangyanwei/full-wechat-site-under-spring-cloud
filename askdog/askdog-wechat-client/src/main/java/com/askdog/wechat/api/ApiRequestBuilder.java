package com.askdog.wechat.api;

import com.askdog.wechat.api.wxclient.exception.WxClientException;
import com.askdog.wechat.api.wxpay.model.ReturnValue;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.io.*;
import java.net.URI;

import static com.askdog.wechat.api.wxclient.exception.WxClientException.Error.PAY_ERROR;
import static com.askdog.wechat.api.wxpay.model.ReturnValue.Code.FAIL;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.springframework.http.MediaType.TEXT_XML;

public final class ApiRequestBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestBuilder.class);

    private static final XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.setSerializationInclusion(NON_NULL);
        xmlMapper.setPropertyNamingStrategy(CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
    }

    public static <R extends WxObject> ApiRequest.ApiRequestWithoutBody<R> buildGetRequest(
            URI uri, Class<R> responseType) {

        DefaultApiRequest<R, WxVoid> apiExchange = new DefaultApiRequest<>();
        apiExchange.setMethod(HttpMethod.GET);
        apiExchange.setUri(uri);
        apiExchange.setResponseType(responseType);
        return apiExchange;
    }

    public static <R extends WxObject, B extends WxObject> ApiRequest.ApiRequestWithBody<R, B> buildPostRequest(
            URI uri, Class<R> responseType) {

        DefaultApiRequest<R, B> apiExchange = new DefaultApiRequest<>();
        apiExchange.setMethod(HttpMethod.POST);
        apiExchange.setUri(uri);
        apiExchange.setResponseType(responseType);
        return apiExchange;
    }

    public static <R extends WxObject, B extends WxObject> ApiRequest.ApiRequestWithBody<R, B> buildXmlPostRequest(
            URI uri, Class<R> responseType) {

        return buildXmlPostRequest(null, uri, responseType);
    }

    // HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
    public static <R extends WxObject, B extends WxObject> ApiRequest.ApiRequestWithBody<R, B> buildXmlPostRequest(
            ClientHttpRequestFactory requestFactory, URI uri, Class<R> responseType) {

        DefaultApiRequest<R, B> apiExchange = new DefaultApiRequest<>();
        apiExchange.setRequestFactory(requestFactory);
        apiExchange.setMethod(HttpMethod.POST);
        apiExchange.setUri(uri);
        apiExchange.setResponseType(responseType);
        apiExchange.setEntitySerializer(xmlEntitySerializer());
        apiExchange.setEntityDeserializer(xmlEntityDeserializer());
        apiExchange.setResponseChecker(responseChecker());
        return apiExchange;
    }

    private static DefaultApiRequest.ResponseChecker responseChecker() {
        return content -> {
            //noinspection unchecked
            ReturnValue returnValue = (ReturnValue) xmlEntityDeserializer().deserialize(content, ReturnValue.class);
            if (returnValue.getReturnCode() == FAIL) {
                throw new WxClientException(PAY_ERROR, returnValue.getReturnMsg());
            }

            if (!isNullOrEmpty(returnValue.getErrCode())) {
                throw new WxClientException(PAY_ERROR, returnValue.getErrCodeDes());
            }
        };
    }

    private static DefaultApiRequest.EntityDeserializer xmlEntityDeserializer() {
        return (content, responseType1) -> {
            MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter(xmlMapper);
            try {
                return converter.read(responseType1, new HttpInputMessage() {
                    @Override
                    public InputStream getBody() throws IOException {
                        return new BufferedInputStream(new ByteArrayInputStream(content.getBytes(Charsets.UTF_8)));
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setContentType(TEXT_XML);
                        return httpHeaders;
                    }
                });
            } catch (IOException e) {
                logger.error("can not convert the content in XML", e);
            }

            return null;
        };
    }

    private static DefaultApiRequest.EntitySerializer xmlEntitySerializer() {
        return entity -> {
            MappingJackson2XmlHttpMessageConverter converter = new MappingJackson2XmlHttpMessageConverter(xmlMapper);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                converter.write(entity, TEXT_XML, new HttpOutputMessage() {
                    @Override
                    public OutputStream getBody() throws IOException {
                        return byteArrayOutputStream;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setContentType(TEXT_XML);
                        return httpHeaders;
                    }
                });

                return byteArrayOutputStream.toString(Charsets.UTF_8.toString());
            } catch (IOException e) {
                logger.error("can not write the xml content", e);
            }

            return null;
        };
    }

}
