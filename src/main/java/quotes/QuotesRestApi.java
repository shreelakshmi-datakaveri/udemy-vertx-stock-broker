package quotes;

import assets.Asset;
import assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(QuotesRestApi.class);
  public static void attach(Router parent)
  {

//    Resuse the static list of Assets in QuotesRestApi
    final Map<String, Quote> cachedQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol -> {
      cachedQuotes.put(symbol, initRandomQuote(symbol));
    });



    parent.get("/quotes/:asset").handler(context -> {
      //      JSon array is from Vertx
//      /quotes/:asset is the path parameter
//      the "asset" parameter will be dynamic
//      when we define a handler, we will get routingContext
//      this routingContext now contains different parameter
      final String assetParam = context.pathParam("asset");
//        with context.pathParam we can pass the name of the parameter
//      in our case the parameter is asset
//      to see the parameter we are adding a new log message
//      we are logging what has been passed to the rest API endpoint

      LOG.debug("Asset parameter : {} ", assetParam);



//     var quote = initRandomQuote(assetParam);
//  fetching the quotes from cachedQuotes HashMap
      var maybeQuote = Optional.ofNullable(cachedQuotes.get(assetParam));
      if(maybeQuote.isEmpty())
      {
        context.response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
//          ending the response through .end
          .end(new JsonObject()
          .put("message", "quote for asset " + assetParam + "not available !")
          .put(" path ", context.normalizedPath())
          .toBuffer());
        return;
      }

      final JsonObject response = new JsonObject();
      LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
      //  Returning JSON is the defacto standard when using REST APIs
//  So it makes sense to use JSonArray & JSON Objects
//      response.encode() in the Logger will give us the String ou22tput from JSON
//      .decode() does the opposite


      final JsonArray responses = new JsonArray();
//      response
//        .add(new JsonObject().put("symbol", "AAPL"))
//        .add(new JsonObject().put("symbol", "AMZN"))
//        .add(new JsonObject().put("symbol", "GOOGL"))
//        .add(new JsonObject().put("symbol", "TSLA"))
//        .add(new JsonObject().put("symbol", "NFLX"));

      responses
        .add(new Asset( "AAPL"))
        .add(new Asset( "AMZN"))
        .add(new Asset( "GOOGL"))
        .add(new Asset("TSLA"))
        .add(new Asset( "NFLX"));



    });
    //define a handler
//    The handler is asynchronously called when a client calls
//    the GET endpoint
//    context is the routing context which gives us information about
//    Request related information

  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));

  }

  /**
   * Converts Quote object easily to JsonObject
   * @return JsonObject
   */
  public JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }
}
