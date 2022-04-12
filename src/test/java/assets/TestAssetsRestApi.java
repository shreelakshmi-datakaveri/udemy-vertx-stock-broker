package assets;

import com.shree.udemy.broker.MainVerticle;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
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
public class TestAssetsRestApi {
private static final Logger LOG = LoggerFactory.getLogger(TestAssetsRestApi.class);

@BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws ParameterResolutionException {
//    testContext.completeNow();
//  Creating Vertx Web client
//    Vertx web client is an asynchronous HTTP client which is quite powerful
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
//    pass webclient options as the second parameter
    client.get("/assets")
      .send()
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonArray();
        LOG.info("Response : {}", json);
        assertEquals("[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"GOOGL\"},{\"name\":\"TSLA\"},{\"name\":\"NFLX\"},{\"name\":\"META\"},{\"name\":\"MSFT\"},{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"GOOGL\"},{\"name\":\"TSLA\"},{\"name\":\"NFLX\"}]", json.encode());
        assertEquals(200, response.statusCode());
//      Verifying header in this next assertEquals
        assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
        assertEquals("my-value", response.getHeader("my-header"));
        testContext.completeNow();
      }));

//client has an API to call different HTTP end points
//    .send will return us the future containing HTTP Response object
//    .onComplete() method gives us an asynchronous result as the return parameter
  }
}
