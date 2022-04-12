package watchlist;

import assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.*;

import java.util.List;

/**
 * lombok's @Value Annotation will get us an immutable
 * object
 * If we have an immutable object, only the mapping from
 * immutable object to JSON object works
 * But the mapping from JSON Object to immutable object does not work
 *
 * That's why, removing @Value annotation and adding
 * the other annotation
 */
@Data
@AllArgsConstructor
@NoArgsConstructor

public class WatchList {
private List<Asset> assets;


  public JsonObject toJsonObject()
  {
    return JsonObject.mapFrom(this);
  }


}
