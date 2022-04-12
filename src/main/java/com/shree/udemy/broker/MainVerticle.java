package com.shree.udemy.broker;

import assets.AssetsRestApi;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quotes.QuotesRestApi;
import watchlist.WatchListRestApi;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> {
//LOG output
    LOG.error("Unhandled :  ", error);
    });

//    Defining the Asynchronous result
//    If the ar (Asynchornous result) failed
//    LOG the error with the message
//    Failed to deploy ar.cause() + return
//    If the deployment succeeds
//    Do a LOG.info having message
//    Deployed Main Verticle!

//    vertx.deployVerticle(new MainVerticle(), ar -> {
//      if(ar.failed())
//      {
//        LOG.error("Failed to deploy  :  ", ar.cause());
//        return;
//      }
//      LOG.info("Deployed {} !", MainVerticle.class.getName());
//    });
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> LOG.error("Failed to deploy: ", err))
      .onSuccess(id -> {
        LOG.info("Deployed {} with id {}!", MainVerticle.class.getName(), id);
      });

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.deployVerticle(RESTAPIVerticle.class.getName(),
        new DeploymentOptions().setInstances(Runtime.getRuntime().availableProcessors()))
      .onFailure(startPromise :: fail)
      .onSuccess(id -> {
        LOG.info("Deployed {} wih id {} ", RESTAPIVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });



//    final Router restApi = Router.router(vertx);
//
//    restApi.route()
//        .handler(BodyHandler.create()
//            .setBodyLimit(1024)
//            .setHandleFileUploads(true)
//          )
//          .failureHandler(handleFailure());
//
//    AssetsRestApi.attach(restApi);
//    QuotesRestApi.attach(restApi);
//    WatchListRestApi.attach(restApi);
//
////    Failure Handler
//    restApi.route().failureHandler(handleFailure());
//
////    restApi.get("/assets").handler(context -> {
////      //      JSon array is from Vertx
////        final JsonArray response = new JsonArray();
////        response
////          .add(new JsonObject().put("symbol", "AAPL"))
////          .add(new JsonObject().put("symbol", "AMZN"))
////          .add(new JsonObject().put("symbol", "GOOGL"))
////          .add(new JsonObject().put("symbol", "TSLA"))
////          .add(new JsonObject().put("symbol", "NFLX"));
////        LOG.info("Path {} responds with {}", context.normalizedPath(), response.encode());
////        context.response().end(response.toBuffer());
////        //  Returning JSON is the defacto standard when using REST APIs
//////  So it makes sense to use JSonArray & JSON Objects
//////      response.encode() in the Logger will give us the String output from JSON
//////      .decode() does the opposite
////
////    });
//    //define a handler
////    The handler is asynchronously called when a client calls
////    the GET endpoint
////    context is the routing context which gives us information about
////    Request related information
//
////    vertx.createHttpServer()
////      .requestHandler(req -> {
////      req.response()
////        .putHeader("content-type", "text/plain")
////        .end("Hello from Vert.x!");
////    }).listen(8888, http -> {
////      if (http.succeeded()) {
////        startPromise.complete();
////        LOG.info("HTTP server started on port 8888");
////      } else {
////        startPromise.fail(http.cause());
////      }
////    });
//    vertx.createHttpServer()
//      .requestHandler(restApi)
//      .exceptionHandler(error -> {
//        LOG.error("HTTP Server error: ", error);
//      })
//      .listen(8888, http -> {
//        if (http.succeeded()) {
//          startPromise.complete();
//          LOG.info("HTTP server started on port 8888");
//        } else {
//          startPromise.fail(http.cause());
//        }
//      });
  }

  private Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
//        if the client stops the requests
        return;
      }
      LOG.error("Route Error : ", errorContext.failure());
//      response to the client
//      .end( ) is defining a body
      errorContext.response()
        .setStatusCode(500)
//        Setting the status code as 500 bcs we don't know
//        what happened
        .end(new JsonObject().put("message ", "Something went wrong :-| ").toBuffer());
    };
  }
}

