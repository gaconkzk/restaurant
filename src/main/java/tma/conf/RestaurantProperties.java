package tma.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("restaurant")
@Data
public class RestaurantProperties {
  String version = "1.0.0";
  String host = "localhost";
  Integer port = 8080;
  DatabaseConfiguration database = new DatabaseConfiguration();
}
