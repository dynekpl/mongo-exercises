import io.vavr.collection.List;
import lombok.NonNull;
import lombok.Value;

@Value
public class Shop {
  @NonNull
  private final String name;
  @NonNull
  private final List<String> products;
}
