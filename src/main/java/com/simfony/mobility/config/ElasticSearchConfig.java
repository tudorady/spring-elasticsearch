package com.simfony.mobility.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by tudoraurelian on 15/09/2018.
 */
@Configuration
@EnableElasticsearchRepositories("com.simfony.mobility.repository.search")
//@ComponentScan(basePackages = { "com.baeldung.spring.data.es.service" })
public class ElasticSearchConfig {

//    @Value("${elasticsearch.home:/usr/local/Cellar/elasticsearch/5.6.0}")
//    private String elasticsearchHome;
//
//    @Value("${elasticsearch.cluster.name:elasticsearch}")
//    private String clusterName;

    @Bean
    public Client client() throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder()
//            .put("client.transport.sniff", true)
//            .put("path.home", "home")
            .put("cluster.name", "docker-cluster").build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings)
            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.106"), 9300));
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        return new ElasticsearchTemplate(client());
    }

}
