package CoffeeApp.storageservice.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public record GoodsWrapperForWriteOff(ConcurrentHashMap<String, String> ingredientResults,
                                      ConcurrentHashMap<String, String> itemResults) {

}
