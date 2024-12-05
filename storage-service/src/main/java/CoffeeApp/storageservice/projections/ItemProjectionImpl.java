package CoffeeApp.storageservice.projections;

public record ItemProjectionImpl(String itemName, Float itemQuantity, Float itemCostPrice) implements ItemProjection {
}
