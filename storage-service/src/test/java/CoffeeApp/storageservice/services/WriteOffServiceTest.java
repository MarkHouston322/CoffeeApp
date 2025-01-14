package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.writeOffDto.AddWriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.Acceptance;
import CoffeeApp.storageservice.models.WriteOff;
import CoffeeApp.storageservice.models.ingredient.IngredientInWriteOff;
import CoffeeApp.storageservice.models.item.ItemInWriteOff;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import CoffeeApp.storageservice.repositories.WriteOffRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInWriteOffService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemInFridgeService;
import CoffeeApp.storageservice.services.itemService.ItemInWriteOffService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WriteOffServiceTest {

    @Mock
    private  WriteOffRepository writeOffRepository;

    @Mock
    private  IngredientService ingredientService;

    @Mock
    private  ItemService itemService;

    @Mock
    private  IngredientInWriteOffService ingredientInWriteOffService;

    @Mock
    private  ItemInWriteOffService itemInWriteOffService;

    @Mock
    private  ModelMapper modelMapper;

    @Mock
    private  ItemInFridgeService itemInFridgeService;

    @Mock
    private GoodsWrapperForWriteOff goodsWrapperForWriteOff;

    @InjectMocks
    private WriteOffService writeOffService;

    @Test
    void shouldReturnWriteOffDtoById(){
        // given
        int id = 1;
        WriteOff writeOff = new WriteOff(LocalDateTime.now(),new Acceptance(),"Reason",1000f);
        writeOff.setId(id);
        when(writeOffRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(writeOff));
        // when
        WriteOffDto result = writeOffService.findById(Mockito.anyInt());
        // then
        assertThat(result.getReason()).isEqualTo(writeOff.getReason());
        verify(writeOffRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnWriteOffDtoById(){
        // given
        int id = 1;
        when(writeOffRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> writeOffService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Write-off", "id", Integer.toString(id));
        verify(writeOffRepository).findById(id);
    }

    @Test
    void shouldReturnAllWriteOffsDto(){
        // given
        WriteOff writeOff1 = new WriteOff(LocalDateTime.now(),new Acceptance(),"Reason",1000f);
        WriteOff writeOff2 = new WriteOff(LocalDateTime.now(),new Acceptance(),"Why",2000f);
        List<WriteOff> writeOffs = Arrays.asList(writeOff1,writeOff2);
        WriteOffDto writeOffDto1 = new WriteOffDto(writeOff1.getId(),"Date",writeOff1.getAcceptance(),"Reason",1000f);
        WriteOffDto writeOffDto2 = new WriteOffDto(writeOff2.getId(),"Date",writeOff2.getAcceptance(),"Why",2000f);
        when(writeOffRepository.findAll()).thenReturn(writeOffs);
        when(modelMapper.map(writeOff1,WriteOffDto.class)).thenReturn(writeOffDto1);
        when(modelMapper.map(writeOff2,WriteOffDto.class)).thenReturn(writeOffDto2);
        // when
        WriteOffResponse response = writeOffService.findAll();
        // then
        assertThat(response.getWriteOffs()).containsExactly(writeOffDto1,writeOffDto2);
        verify(writeOffRepository).findAll();
    }

    @Test
    void shouldNotReturnAllWriteOffsDto(){
        // given
        when(writeOffRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        WriteOffResponse response = writeOffService.findAll();
        // then
        assertThat(response.getWriteOffs()).isEmpty();
        verify(writeOffRepository).findAll();
    }

    @Test
    void shouldReturnItemsDtoByWriteOffId() {
        // given
        List<ItemProjection> mockProjections = List.of(
                new ItemProjection() {
                    @Override
                    public String itemName() {
                        return "Coffee";
                    }

                    @Override
                    public float itemQuantity() {
                        return 2.0f;
                    }

                    @Override
                    public float itemCostPrice() {
                        return 150.0f;
                    }
                },
                new ItemProjection() {
                    @Override
                    public String itemName() {
                        return "Milk";
                    }

                    @Override
                    public float itemQuantity() {
                        return 10.0f;
                    }

                    @Override
                    public float itemCostPrice() {
                        return 50.0f;
                    }
                }
        );
        when(writeOffRepository.findItemsByWriteOffId(Mockito.anyInt())).thenReturn(mockProjections);
        // when
        List<ItemInDto> result = writeOffService.getItemsByWriteOffId(Mockito.anyInt());
        // then
        assertEquals(2, result.size());
        assertEquals("Coffee", result.get(0).getItemName());
        assertEquals(150.0f, result.get(0).getQuantity(), 0.01f);
        assertEquals("Milk", result.get(1).getItemName());
        assertEquals(50.0f, result.get(1).getQuantity(), 0.01f);
        verify(writeOffRepository).findItemsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnItemsDtoByWriteOffId(){
        // given
        when(writeOffRepository.findItemsByWriteOffId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        List<ItemInDto> result = writeOffService.getItemsByWriteOffId(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(writeOffRepository).findItemsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldReturnIngredientsDtoByWriteOffId() {
        // given
        List<IngredientProjection> mockProjections = List.of(
                new IngredientProjection() {
                    @Override
                    public String ingredientName() {
                        return "Sugar";
                    }

                    @Override
                    public float ingredientQuantity() {
                        return 5.0f;
                    }
                },
                new IngredientProjection() {
                    @Override
                    public String ingredientName() {
                        return "Flour";
                    }

                    @Override
                    public float ingredientQuantity() {
                        return 10.0f;
                    }
                }
        );
        when(writeOffRepository.findIngredientsByWriteOffId(Mockito.anyInt())).thenReturn(mockProjections);
        // when
        List<IngredientInDto> result = writeOffService.getIngredientsByWriteOffId(Mockito.anyInt());
        // then
        assertEquals(2, result.size());
        assertEquals("Sugar", result.get(0).getIngredientName());
        assertEquals(5.0f, result.get(0).getQuantity(), 0.01f);
        assertEquals("Flour", result.get(1).getIngredientName());
        assertEquals(10.0f, result.get(1).getQuantity(), 0.01f);
        verify(writeOffRepository).findIngredientsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnIngredientsDtoByWriteOffId() {
        // given
        when(writeOffRepository.findIngredientsByWriteOffId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        List<IngredientInDto> result = writeOffService.getIngredientsByWriteOffId(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(writeOffRepository).findIngredientsByWriteOffId(Mockito.anyInt());
    }

    @Test
    void shouldAddWriteOffGoods() throws ExecutionException, InterruptedException {
        // given
        AddWriteOffDto addWriteOffDto = new AddWriteOffDto();
        Map<String, String> goodsForWriteOff = Map.of(
                "Sugar", "10",
                "Cup", "5"
        );

        WriteOff writeOff = new WriteOff();
        writeOff.setId(1);

        ConcurrentHashMap<String, String> ingredientResults = new ConcurrentHashMap<>(Map.of("Sugar", "10"));
        ConcurrentHashMap<String, String> itemResults = new ConcurrentHashMap<>(Map.of("Cup", "5"));

        when(modelMapper.map(addWriteOffDto,WriteOff.class)).thenReturn(writeOff);
        when(goodsWrapperForWriteOff.ingredientResults()).thenReturn(ingredientResults);
        when(goodsWrapperForWriteOff.itemResults()).thenReturn(itemResults);
        when(ingredientService.calculateCost(ingredientResults)).thenReturn(100.0f);
        when(itemService.calculateCost(itemResults)).thenReturn(50.0f);
        // when
        writeOffService.addWriteOffGoods(addWriteOffDto, goodsForWriteOff);
        // then
        verify(writeOffRepository).save(argThat(savedWriteOff ->
                savedWriteOff.getTotal() == 150.0f && savedWriteOff.getDate() != null
        ));
        verify(itemInWriteOffService).addItemInWriteOff(any(ItemInWriteOff.class));
        verify(ingredientInWriteOffService).addIngredientInWriteOff(any(IngredientInWriteOff.class));
        verify(ingredientService).decreaseIngredient("Sugar", 10.0f);
        verify(itemService).decreaseItem("Cup", 5.0f);
        verify(itemInFridgeService).writeOffItemsFromFridge(itemResults);
    }

}