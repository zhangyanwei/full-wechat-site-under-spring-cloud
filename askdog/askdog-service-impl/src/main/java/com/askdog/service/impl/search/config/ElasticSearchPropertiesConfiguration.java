package com.askdog.service.impl.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "askdog.elasticsearch")
public class ElasticSearchPropertiesConfiguration {

    @NotNull
    private Map<String, String> settings;
    @NotNull
    private Host[] hosts;

    public Map<String, String> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, String> settings) {
        this.settings = settings;
    }

    public Host[] getHosts() {
        return hosts;
    }

    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
    }

    public static class Host {

        @NotNull
        private String address;

        @NotNull
        private String port;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }

}
