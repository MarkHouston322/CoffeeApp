package CoffeeApp.storageservice.interfaces;

import CoffeeApp.storageservice.exceptions.NullOrEmptyParamException;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.ingredient.Ingredient;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.repositories.ingredientRepository.IngredientRepository;
import CoffeeApp.storageservice.repositories.itemRepository.ItemRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

public interface ContainIngredients {

    default GoodsWrapperForWriteOff checkGoods(Map<String, String> goods, IngredientService ingredientService, ItemService itemService)
            throws InterruptedException, ExecutionException {

        ConcurrentHashMap<String, String> ingredientResults = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, String> itemResults = new ConcurrentHashMap<>();

        BiConsumer<String, String> ingredientCheck = (name, quantity) -> {
            Ingredient ingredient = ingredientService.findByName(name);
            if (quantity.isEmpty() || quantity.equals("0")){
                throw new NullOrEmptyParamException("Ingredient should not be empty or has null quantity: " + name + " - " + quantity);
            }
            ingredientResults.put(ingredient.getName(), quantity);
        };

        BiConsumer<String, String> itemCheck = (name, quantity) -> {
            Item item = itemService.findByName(name);
            if (quantity.isEmpty() || quantity.equals("0")){
                throw new NullOrEmptyParamException("Item should not be empty or has null quantity: " + name + " - " + quantity);
            }
            itemResults.put(item.getName(), quantity);
        };

        CompletableFuture<?>[] futures = goods.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(() -> {
                    try {
                        ingredientCheck.accept(entry.getKey(), entry.getValue());
                    } catch (ResourceNotFoundException e) {
                        itemCheck.accept(entry.getKey(), entry.getValue());
                    }
                }))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures).join();
        return new GoodsWrapperForWriteOff(ingredientResults, itemResults);
    }
}
