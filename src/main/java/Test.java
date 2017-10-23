import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.vavr.collection.List;
import io.vavr.jackson.datatype.VavrModule;
import org.bson.Document;

import java.io.IOException;

import static com.fasterxml.jackson.databind.PropertyNamingStrategy.KEBAB_CASE;
import static com.mongodb.client.model.Filters.eq;
import static io.vavr.API.List;

public class Test {

  public static void main(String[] args) {
    List<String> products = List("piwo", "kabanosy", "hummus");
    Shop shop = new Shop("Biedronka", products);

    try {
      insertShop(shop);

    } catch (IOException e) {
      e.printStackTrace();
    }

    readByName("Biedronka");
  }

  public static void insertShop(Shop shop) throws IOException {
    ObjectMapper mapper = getMapper();
    String jsonShop = mapper.writeValueAsString(shop);

    MongoCollection<Document> mongoCollection = getCollection();
    mongoCollection.insertOne(Document.parse(jsonShop));
  }

  public static void readByName(String shopName) {
    MongoCollection<Document> mongoCollection = getCollection();

    //List().appendAll(mongoCollection.find(Document.parse("{\"name\": \"shopname\"}")))
    //       .forEach(System.out::println);

    List().appendAll(mongoCollection.find(eq("name", shopName)))
            .forEach(System.out::println);
  }

  private static MongoCollection<Document> getCollection() {
    MongoClient mongoClient = new MongoClient("192.168.99.100", 27017);
    MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("shops");
    return mongoCollection;
  }

  private static ObjectMapper getMapper() {
    return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(KEBAB_CASE)
            .registerModule(new VavrModule());
  }
}
