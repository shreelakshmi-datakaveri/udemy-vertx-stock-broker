package assets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

//add the lomok annotation
//@Value annotation will create an immutaable class for us
//It adds a constructor, getters for all fields
//that means no other code needs to be defined
//so the Asset class looks very empty

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {
//  final String name;
//
//  public Asset(String name) {
//    this.name = name;
//  }
//
//  public String getName() {
//    return this.name;
//  }

   String name;
}
