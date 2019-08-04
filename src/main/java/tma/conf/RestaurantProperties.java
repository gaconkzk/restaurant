package tma.conf;

import lombok.Data;
import tma.conf.DatabaseConfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("restaurant")
@Data
public class RestaurantProperties {
  String version = "1.0.0";
  String host = "localhost";
  Integer port = 8080;
  Boolean https = false;
  String apiVersion = "1";
  DatabaseConfiguration database = new DatabaseConfiguration();
}
