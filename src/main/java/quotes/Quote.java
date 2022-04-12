package quotes;

import assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;


/**
 * @Value annotation from project lombok will automatically add all the
 * boiler-plate code such as getters, constructors for all the variables or fields
 * defined in the class
 *
 *
 */
@Value
@Builder
public class Quote {

  Asset asset;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
  BigDecimal volume;



}
