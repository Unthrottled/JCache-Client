package space.cyclic.reference;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.cache.impl.HazelcastClientCachingProvider;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.lang.Thread.sleep;

public class Main {

    private static final Duration TEN_SEC = new Duration(TimeUnit.SECONDS, 10);

    /**
     * Be sure to have a hazelcast server set up and running with
     * the javax.cache:cache-api:1.0.0 in the server's class path, with
     * @param args
     * @throws IOException
     */
    public static void main(String... args) throws IOException {
        ClientConfig clientConfig = new ClientConfig();
        ClientNetworkConfig networkConfig = new ClientNetworkConfig();
        networkConfig.setAddresses(Collections.singletonList("127.0.0.1:9001"));
        clientConfig.setNetworkConfig(networkConfig);
        GroupConfig groupConfig = new GroupConfig();
        groupConfig.setName("cacheo");
        groupConfig.setPassword("cacheo-pass");
        clientConfig.setGroupConfig(groupConfig);
        HazelcastInstance hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        try (CachingProvider cachingProvider = HazelcastClientCachingProvider.createCachingProvider(hazelcastInstance);
             CacheManager cacheManager = cachingProvider.getCacheManager()) {

            Cache<String, Integer> cacho = cacheManager.createCache("Pooper", new MutableConfiguration<String, Integer>().setStoreByValue(true)
                    .setTypes(String.class, Integer.class)
                    .setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(TEN_SEC))
                    .setStatisticsEnabled(false));
            Stream.iterate(1, a -> a + 1).limit(30).forEach(i -> cacho.put("key" + i, i));
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cacho.forEach(System.out::println);
        }
    }

}