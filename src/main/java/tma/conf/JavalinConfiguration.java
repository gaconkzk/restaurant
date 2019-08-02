package tma.conf;

import lombok.Data;

@Data
public final class JavalinConfiguration {
  String version = "1.0.0";
  String host = "localhost";
  Integer port = 8080;
  Boolean https = false;
  String apiVersion = "1";
  DatabaseConfiguration database = new DatabaseConfiguration();
}
