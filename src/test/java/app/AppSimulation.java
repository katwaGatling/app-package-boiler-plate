package app;

import static app.config.Utils.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import app.endpoints.APIendpoints;
import app.groups.ScenarioGroups;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class AppSimulation extends Simulation {

  private static final HttpProtocolBuilder httpProtocolWithAuthentication =
      APIendpoints.withAuthenticationHeader(
          http.baseUrl(baseUrl)
              .acceptHeader(
                  "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7")
              .inferHtmlResources()
              .acceptEncodingHeader("gzip, deflate")
              .acceptLanguageHeader("fr-FR,fr;q=0.9,en-US;q=0.8,en;q=0.7")
              .userAgentHeader(
                  "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.70 Safari/537.36")
              .silentResources());

  private static final ScenarioBuilder scenario1 =
      scenario("Scenario 1")
          .exitBlockOnFail()
          .on(
              group("fr")
                  .on(
                      ScenarioGroups.authenticate,
                      ScenarioGroups.browse,
                      ScenarioGroups.incomplete_checkout),
              group("us")
                  .on(
                      ScenarioGroups.authenticate,
                      ScenarioGroups.browse,
                      ScenarioGroups.incomplete_checkout))
          .exitHereIfFailed();

  private static final PopulationBuilder getTypeOfLoadTestSc(String type) {
    return switch (type) {
      case "capacity" ->
          scenario1.injectOpen(
              incrementUsersPerSec(users)
                  .times(4)
                  .eachLevelLasting(duration)
                  .separatedByRampsLasting(4)
                  .startingFrom(10));
      case "stress" -> scenario1.injectOpen(stressPeakUsers(users).during(duration));
      case "soak" -> scenario1.injectOpen(constantUsersPerSec(users).during(duration));
      default -> scenario1.injectOpen(atOnceUsers(1));
    };
  }

  private static final Assertion getAssertion(String type) {
    return switch (type) {
      case "soak" -> global().responseTime().percentile(90.0).lt(500);
      case "stress" -> global().responseTime().percentile(90.0).lt(500);
      case "smoke" -> global().failedRequests().count().lt(1L);
      default -> global().responseTime().percentile(90.0).lt(500);
    };
  }

  {
    setUp(getTypeOfLoadTestSc(type))
        .assertions(getAssertion(Assertiontype))
        .protocols(httpProtocolWithAuthentication);
  }
}
