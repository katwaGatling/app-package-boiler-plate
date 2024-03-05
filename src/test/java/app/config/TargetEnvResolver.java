package app.config;

public class TargetEnvResolver {

  public record EnvInfo(String apiUrl, String usersFeederFile) {}

  public static EnvInfo resolveEnvironmentInfo(String targetEnv) {
    return switch (targetEnv) {
      case "DEV1" ->
          new EnvInfo(
            "INSERT_BASE_URL",
              "users_dev1.json");
      default ->
          new EnvInfo(
              "INSERT_BASE_URL",
              "users_dev1.json");
    };
  }
}
