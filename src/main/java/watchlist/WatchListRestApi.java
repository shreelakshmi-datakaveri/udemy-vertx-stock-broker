package watchlist;

import assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class WatchListRestApi {
  private static final Logger LOG = LoggerFactory.getLogger(AssetsRestApi.class);

  public static void attach(final Router parent)
  {
    final String path = "/account/watchlist/:accountId";
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<>();
//    defining multiple endpoints like
//    /account/watchlist
    parent.get(path)
      .handler(context ->
      {
//        LOG.debug message is added to see if the
//        path was called
//        passing context.normalizedPath() to know
//        which path was passed
//        2nd parameter is the AccountId which is the part
//        of path parameter
        var accountId = context.pathParam("accountId");
          LOG.debug("{} for account {} ", context.normalizedPath(), accountId);
       var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));
        if(watchList.isEmpty())
        {
          context.response()
            .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
//          ending the response through .end
            .end(new JsonObject()
              .put("message", "watchlist for account " + accountId + "not available !")
              .put(" path ", context.normalizedPath())
              .toBuffer());
          return;
        }
      });

//    http put endpoint to create a new WatchList resource
//    And also to update the WatchLit resource
//    Add a path parameter accountId with the colon before that
    parent.put(path)
      .handler(context -> {
        var accountId = context.pathParam("accountId");
        LOG.debug("{} for account {} ", context.normalizedPath(), accountId);
//        Parse the accountId
//        Parse the body of the request
        var json = context.getBodyAsJson();
//        Mapping this object to WatchList class
        var watchList = json.mapTo(WatchList.class);
        watchListPerAccount.put(UUID.fromString(accountId), watchList);
        context.response().end(json.toBuffer());
      });

//    DELETE Endpoint
    parent.delete(path)
      .handler(context -> {
        var accountId = context.pathParam("accountId");
        LOG.debug("{} for account {} ", context.normalizedPath(), accountId);
       final WatchList deleted =  watchListPerAccount.remove(UUID.fromString(accountId));
       LOG.info("Deleted : {} , Remaining : {}", deleted, watchListPerAccount.values());
        context.response()
          .end(deleted.toJsonObject().toBuffer());
      });
  }
}
