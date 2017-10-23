import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static io.vavr.API.List;
import static java.lang.System.out;

public class Examples {

  public static void main(String[] args) {
    MongoClient mongoClient = new MongoClient("192.168.99.100", 27017);
    MongoDatabase mongoDatabase = mongoClient.getDatabase("admin");
    MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("shops");

    FindIterable<Document> results = mongoCollection.find();
    List().appendAll(results).forEach(doc -> System.out.println(doc));
  }
}
