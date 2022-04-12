package quotes;

import com.shree.udemy.broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuotesRestApi {
private static final Logger LOG = LoggerFactory.getLogger(TestQuotesRestApi.class);

@BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_quotes_for_asset(Vertx vertx, VertxTestContext testContext) throws ParameterResolutionException {
//    testContext.completeNow();
//  Creating Vertx Web client
//    Vertx web client is an asynchronous HTTP client which is quite powerful
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
//    pass webclient options as the second parameter

//   we are using /quotes/AMZN o test the behaviour
    client.get("/quotes/AMZN")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response : {}", json);
//        assertEquals("", json.getJsonObject("asset").encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      }));

//client has an API to call different HTTP end points
//    .send will return us the future containing HTTP Response object
//    .onComplete() method gives us an asynchronous result as the return parameter
  }


  @Test
  void returns_not_found_for_unknown_asset(Vertx vertx, VertxTestContext testContext) throws ParameterResolutionException {
//    testContext.completeNow();
//  Creating Vertx Web client
//    Vertx web client is an asynchronous HTTP client which is quite powerful
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
//    pass webclient options as the second parameter

//   we are using /quotes/AMZN o test the behaviour
    client.get("/quotes/AMZN")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response : {}", json);
        assertEquals("{}", json.encode());
        assertEquals(404, response.statusCode());
        testContext.completeNow();
      }));

//client has an API to call different HTTP end points
//    .send will return us the future containing HTTP Response object
//    .onComplete() method gives us an asynchronous result as the return parameter
  }
}
