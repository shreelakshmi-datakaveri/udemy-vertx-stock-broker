package assets;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {
/**
@parent Router is the restApi variable defined in the
 start method of MainVerticle

 Add a LOGGER in the class

 Defining a list of valid assets
 **/

private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);
public static final List<String> ASSETS = Arrays.asList("AAPL","AMZN","GOOGL","TSLA","NFLX","META", "MSFT");

  public static void attach(Router parent)
  {
    parent.get("/assets").handler(context -> {
      //      JSon array is from Vertx
      final JsonArray response = new JsonArray();
      ASSETS.stream().map(Asset :: new).forEach(response :: add);
//      response
//        .add(new JsonObject().put("symbol", "AAPL"))
//        .add(new JsonObject().put("symbol", "AMZN"))
//        .add(new JsonObject().put("symbol", "GOOGL"))
//        .add(new JsonObject().put("symbol", "TSLA"))
//        .add(new JsonObject().put("symbol", "NFLX"));

      response
        .add(new Asset( "AAPL"))
        .add(new Asset( "AMZN"))
        .add(new Asset( "GOOGL"))
        .add(new Asset("TSLA"))
        .add(new Asset( "NFLX"));

      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response()
        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
//        creating a custm header -> A header is nothing else than a key-value pair
        .putHeader("my-header", "my-value")
        .end(response.toBuffer());
      //  Returning JSON is the defacto standard when using REST APIs
//  So it makes sense to use JSonArray & JSON Objects
//      response.encode() in the Logger will give us the String output from JSON
//      .decode() does the opposite

    });
    //define a handler
//    The handler is asynchronously called when a client calls
//    the GET endpoint
//    context is the routing context which gives us information about
//    Request related information

  }
}
