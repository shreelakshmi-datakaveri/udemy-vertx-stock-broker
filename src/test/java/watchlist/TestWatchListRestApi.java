package watchlist;

import assets.Asset;
import com.shree.udemy.broker.MainVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
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

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestWatchListRestApi {
private static final Logger LOG = LoggerFactory.getLogger(TestWatchListRestApi.class);

@BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_and_returns_watchlist_for_account(Vertx vertx, VertxTestContext testContext) throws ParameterResolutionException {
//    testContext.completeNow();
//  Creating Vertx Web client
//    Vertx web client is an asynchronous HTTP client which is quite powerful
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
//    pass webclient options as the second parameter
    var accountId = UUID.randomUUID();

//   we are using /quotes/AMZN o test the behaviour
    client.put("/account/watchlist/" + accountId.toString())
      .sendJsonObject(getBody())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        LOG.info("Response : {}", json);
//       assertEquals("", json.getJsonObject("asset").encode());
        assertEquals(200, response.statusCode());
        testContext.completeNow();
      })).compose(next -> {
        client.get("/account/watchlist/" + accountId.toString())
          .send()
          .onComplete(response -> {
            testContext.completeNow();
          });
        return Future.succeededFuture();
      });

//client has an API to call different HTTP end points
//    .send will return us the future containing HTTP Response object
//    .onComplete() method gives us an asynchronous result as the return parameter
  }


@Test
void adds_and_deletes_watchlist_for_account(Vertx vertx, VertxTestContext context)
{

  //    Vertx web client is an asynchronous HTTP client which is quite powerful
  var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
//    pass webclient options as the second parameter
  var accountId = UUID.randomUUID();

  client.put("/account/watchlist/" + accountId.toString())
    .sendJsonObject(getBody())
    .onComplete(context.succeeding(response -> {
      var json = response.bodyAsJsonObject();
      LOG.info("Response DELETE: {}", json);
//       assertEquals("", json.getJsonObject("asset").encode());
      assertEquals(200, response.statusCode());
      context.completeNow();
    })).compose(next -> {
      client.delete("/account/watchlist/" + accountId.toString())
        .send()
        .onComplete(response -> {
          context.completeNow();
        });
      return Future.succeededFuture();
    });

}

  private JsonObject getBody() {
    return new WatchList(
      Arrays.asList
      (
        new Asset("AMZN"),
        new Asset("TSLA")
      )
    )
      .toJsonObject();
  }

}
