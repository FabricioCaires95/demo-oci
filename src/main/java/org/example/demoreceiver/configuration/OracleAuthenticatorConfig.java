package org.example.demoreceiver.configuration;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.http.client.ProxyConfiguration;
import com.oracle.bmc.http.client.StandardClientProperties;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@Configuration
public class OracleAuthenticatorConfig {

    private static final String PROXY_URI = "localhost";
    private static final int PROXY_PORT = 8889;
    private static final String PROXY_USERNAME = "username";
    private static final String PROXY_PASSWORD = "password";

    @Value("${profile:DEFAULT}")
    private String profileOci;

    @Value("${configurationFilePath}")
    private String configFilePath;

    public ObjectStorage createObjectStorage() {
        try {

            final ConfigFileReader.ConfigFile configFileReader = ConfigFileReader.parse(configFilePath, profileOci);
            final AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFileReader);

            final ProxyConfiguration proxyConfiguration =
                    ProxyConfiguration.builder()
                            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_URI, PROXY_PORT)))
                            .username(PROXY_USERNAME)
                            .password(PROXY_PASSWORD.toCharArray())
                            .build();

            return ObjectStorageClient
                    .builder()
                    .region(Region.SA_SAOPAULO_1)
                    .clientConfigurator(httpClientBuilder -> httpClientBuilder.property(StandardClientProperties.PROXY, proxyConfiguration))
                    .build(provider);
        } catch (IOException e) {
            throw new RuntimeException("Error to load configuration file from oci: " + e.getMessage());
        }
    }

}
