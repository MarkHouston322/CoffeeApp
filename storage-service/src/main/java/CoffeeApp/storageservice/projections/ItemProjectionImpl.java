package CoffeeApp.storageservice.projections;

public record ItemProjectionImpl(String itemName, float itemQuantity, float itemCostPrice) implements ItemProjection {
}
