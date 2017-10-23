import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import io.vavr.collection.List;

import java.io.IOException;

import static io.vavr.API.List;
import static com.mongodb.client.model.Filters.eq;


public class ShopDao {
  private final MongoCollection<Document> shopsCollection;
  private final ObjectMapper objectMapper;

  public ShopDao(MongoCollection<Document> shopsCollection,
                 ObjectMapper objectMapper) {
    this.shopsCollection = shopsCollection;
    this.objectMapper = objectMapper;
  }

  public void insert(Shop shop) {
    try {
      String json = objectMapper.writeValueAsString(shop);
      Document bson = Document.parse(json);
      shopsCollection.insertOne(bson);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException();
    }
  }

  public Shop readByName(String shopName) {
    Document doc = shopsCollection.find(eq("name", shopName)).first();
    return jsonToShop(doc.toJson());
  }

  public List<Shop> readShopsWithProduct(String productId) {
    return List.ofAll(shopsCollection.find(eq("products", productId)))
            .map(Document::toJson)
            .map(this::jsonToShop);
  }

  private Shop jsonToShop(String json) {
    try {
      return objectMapper.readValue(json, Shop.class);
    } catch (IOException e) {
      throw new IllegalStateException();
    }
  }
}
