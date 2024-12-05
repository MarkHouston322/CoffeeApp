package CoffeeApp.storageservice.projections;

public record IngredientProjectionImpl(String ingredientName,
                                       float ingredientQuantity) implements IngredientProjection {

}
