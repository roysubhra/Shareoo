package roy.subhra.shareoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Supplier;

@Component
public class RestURIHelper {

    @Autowired
    DiscoveryClient discoveryClient;
    public Supplier<URI> uriSupplier = () -> discoveryClient.getInstances("shareoo-repo").get(0).getUri();
}
