package app.endpoints;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.http.*;

public class APIendpoints {

  public static final String ACCESS_TOKEN_SESSION_KEY = "accessToken";

  public static HttpProtocolBuilder withAuthenticationHeader(HttpProtocolBuilder protocolBuilder) {
    return protocolBuilder.header(
        "Authorization",
        session ->
            session.contains(ACCESS_TOKEN_SESSION_KEY)
                ? session.getString(ACCESS_TOKEN_SESSION_KEY)
                : "");
  }

  public static final HttpRequestActionBuilder session =
      http("session").get("/session").queryParam("key", "value");

  public static final HttpRequestActionBuilder authenticate =
      http("authenticate")
          .post("/login")
          .formParam("username", "#{dynamic_username_key}")
          .formParam("password", "#{dynamic_password_key}")
          .check(jmesPath("accessToken").saveAs(ACCESS_TOKEN_SESSION_KEY));

  public static final HttpRequestActionBuilder list =
      http("list").get("/filter").queryParam("key", "value");

  public static final HttpRequestActionBuilder search =
      http("search").get("/search").queryParam("key", "#{dynamic_value}");

  public static final HttpRequestActionBuilder addToCart = http("add_to_cart").post("/add-to-cart");

  public static final HttpRequestActionBuilder fetchCartItems =
      http("fetch_cart_items").get("/fetch-cart-items").queryParam("key", "value");

  public static final HttpRequestActionBuilder order = http("order").post("/order");
}
