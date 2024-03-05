package app.config;

public class Utils {

  public static final String FALLBACK_ENV = "dev1";
  public static final String TARGET_ENV =
      System.getProperty("PROP_TARGET_ENV", FALLBACK_ENV).toUpperCase();

  public static final int users = Integer.getInteger("USERS", 10);
  public static final int duration = Integer.getInteger("DURATION", 10);

  public static final String type = System.getProperty("TYPE", "smoke");
  public static final String Assertiontype = System.getProperty("ASSERTION_TYPE", "smoke");

  public static final TargetEnvResolver.EnvInfo envInfo =
      TargetEnvResolver.resolveEnvironmentInfo(TARGET_ENV);
  public static final String baseUrl = envInfo.apiUrl();
  public static final String usersFeederFile = envInfo.usersFeederFile();
}
