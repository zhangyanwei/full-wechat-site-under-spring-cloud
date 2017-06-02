package com.askdog.sync.configuration;

import com.askdog.sync.configuration.ElasticSearchPropertiesConfiguration.Host;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(ElasticSearchPropertiesConfiguration.class)
public class ElasticsearchConfig {

    @Autowired
    private ElasticSearchPropertiesConfiguration elasticSearchPropertiesConfiguration;

    @Bean
    public Client elasticSearchClientProduction() throws UnknownHostException {
        Settings settings = Settings.settingsBuilder().put(elasticSearchPropertiesConfiguration.getSettings()).build();
        TransportClient client = TransportClient.builder().settings(settings).build();

        for (Host host : elasticSearchPropertiesConfiguration.getHosts()) {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host.getAddress()), Integer.valueOf(host.getPort())));
        }

        return client;
    }
}
