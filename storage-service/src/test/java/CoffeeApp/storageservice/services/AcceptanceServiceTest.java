package CoffeeApp.storageservice.services;

import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceFromFileDto;
import CoffeeApp.storageservice.dto.acceptanceDto.AcceptanceResponse;
import CoffeeApp.storageservice.dto.acceptanceDto.AddAcceptanceDto;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.mappers.AcceptanceMapper;
import CoffeeApp.storageservice.models.Acceptance;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.item.ItemInAcceptance;
import CoffeeApp.storageservice.projections.IngredientProjection;
import CoffeeApp.storageservice.projections.ItemProjection;
import CoffeeApp.storageservice.repositories.AcceptanceRepository;
import CoffeeApp.storageservice.services.ingredientService.IngredientInAcceptanceService;
import CoffeeApp.storageservice.services.ingredientService.IngredientService;
import CoffeeApp.storageservice.services.itemService.ItemInAcceptanceService;
import CoffeeApp.storageservice.services.itemService.ItemService;
import CoffeeApp.storageservice.util.GoodsWrapperForWriteOff;
import CoffeeApp.storageservice.util.WordDocumentParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptanceServiceTest {

    @Mock
    private AcceptanceRepository acceptanceRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private ItemService itemService;

    @Mock
    private IngredientInAcceptanceService ingredientInAcceptanceService;

    @Mock
    private ItemInAcceptanceService itemInAcceptanceService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private WordDocumentParser wordDocumentParser;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private AcceptanceMapper acceptanceMapper;

    @InjectMocks
    private AcceptanceService acceptanceService;

    @Test
    void shouldReturnAcceptanceDtoById() {
        // given
        Acceptance acceptance = new Acceptance(LocalDateTime.now(), "Comment", 1000f);
        when(acceptanceRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(acceptance));
        // when
        AcceptanceDto result = acceptanceService.findById(Mockito.anyInt());
        // then
        assertThat(result.getComment()).isEqualTo(acceptance.getComment());
        verify(acceptanceRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnAcceptanceDtoByIdAndThrowException() {
        // given
        int id = 1;
        when(acceptanceRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> acceptanceService.findById(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Acceptance", "id", Integer.toString(id));
        verify(acceptanceRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldReturnAllAcceptancesDto() {
        // given
        Acceptance acceptance1 = new Acceptance(LocalDateTime.now(), "Comment", 1000f);
        Acceptance acceptance2 = new Acceptance(LocalDateTime.now(), "Pause", 2000f);
        List<Acceptance> acceptances = Arrays.asList(acceptance1, acceptance2);
        AcceptanceDto acceptanceDto1 = new AcceptanceDto("acceptance1.getDate()", "Comment", 1000f);
        AcceptanceDto acceptanceDto2 = new AcceptanceDto("acceptance2.getDate()", "Comment", 1000f);
        when(acceptanceRepository.findAll()).thenReturn(acceptances);
        when(modelMapper.map(acceptance1, AcceptanceDto.class)).thenReturn(acceptanceDto1);
        when(modelMapper.map(acceptance2, AcceptanceDto.class)).thenReturn(acceptanceDto2);
        // when
        AcceptanceResponse response = acceptanceService.findAll();
        // then
        assertThat(response.getAcceptances()).containsExactly(acceptanceDto1, acceptanceDto2);
        verify(acceptanceRepository).findAll();
    }

    @Test
    void shouldNotReturnAllAcceptancesDto() {
        // given
        when(acceptanceRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        AcceptanceResponse response = acceptanceService.findAll();
        // then
        assertThat(response.getAcceptances()).isEmpty();
    }

    @Test
    void shouldReturnIngredientsDtoByAcceptanceId() {
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
        when(acceptanceRepository.findIngredientsByAcceptanceId(Mockito.anyInt())).thenReturn(mockProjections);
        // when
        List<IngredientInDto> result = acceptanceService.getIngredientByAcceptanceId(Mockito.anyInt());
        // then
        assertEquals(2, result.size());
        assertEquals("Sugar", result.get(0).getIngredientName());
        assertEquals(5.0f, result.get(0).getQuantity(), 0.01f);
        assertEquals("Flour", result.get(1).getIngredientName());
        assertEquals(10.0f, result.get(1).getQuantity(), 0.01f);
        verify(acceptanceRepository).findIngredientsByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnIngredientsDtoByAcceptanceId() {
        // given
        when(acceptanceRepository.findIngredientsByAcceptanceId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        List<IngredientInDto> result = acceptanceService.getIngredientByAcceptanceId(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(acceptanceRepository).findIngredientsByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldReturnItemsDtoByAcceptanceId() {
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
        when(acceptanceRepository.findItemsByAcceptanceId(Mockito.anyInt())).thenReturn(mockProjections);
        // when
        List<ItemInDto> result = acceptanceService.getItemsByAcceptanceId(Mockito.anyInt());
        // then
        assertEquals(2, result.size());
        assertEquals("Coffee", result.get(0).getItemName());
        assertEquals(150.0f, result.get(0).getQuantity(), 0.01f);
        assertEquals("Milk", result.get(1).getItemName());
        assertEquals(50.0f, result.get(1).getQuantity(), 0.01f);
        verify(acceptanceRepository).findItemsByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnItemsDtoByAcceptanceId(){
        // given
        when(acceptanceRepository.findItemsByAcceptanceId(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // when
        List<ItemInDto> result = acceptanceService.getItemsByAcceptanceId(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(acceptanceRepository).findItemsByAcceptanceId(Mockito.anyInt());
    }

    @Test
    void shouldAddAcceptanceWithGoods() throws ExecutionException, InterruptedException {
        // given
        Map<String, String> goodsToAccept = Map.of(
                "Ingredient1", "2.0",
                "Item1", "5.0"
        );
        AddAcceptanceDto addAcceptanceDto = new AddAcceptanceDto("Comment");
        ConcurrentHashMap<String,String> ingredients = new ConcurrentHashMap<>();
        ingredients.put("Ingredient1", "2.0");
        ConcurrentHashMap<String,String> items = new ConcurrentHashMap<>();
        ingredients.put("Item1", "5.0");
        GoodsWrapperForWriteOff goodsWrapperMock = new GoodsWrapperForWriteOff(ingredients,items);

        when(acceptanceRepository.save(any(Acceptance.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ingredientService.calculateCost(goodsWrapperMock.ingredientResults())).thenReturn(100.0f);
        when(itemService.calculateCost(goodsWrapperMock.itemResults())).thenReturn(250.0f);
        when(modelMapper.map(addAcceptanceDto,Acceptance.class)).thenReturn(new Acceptance());
        when(acceptanceService.checkGoods(goodsToAccept,ingredientService,itemService)).thenReturn(goodsWrapperMock);
        // when
        acceptanceService.addAcceptance(addAcceptanceDto, goodsToAccept);
        // then
        verify(acceptanceRepository).save(any(Acceptance.class));
        verify(ingredientService).calculateCost(goodsWrapperMock.ingredientResults());
        verify(itemService).calculateCost(goodsWrapperMock.itemResults());
    }

    @Test
    void shouldAddAcceptanceFromFile(){
        // given
        MultipartFile mockFile = mock(MultipartFile.class);
        Map<String, String> isIngredient = Map.of("0", "false", "1", "true");
        String surchargeRatio = "1.2";

        List<AcceptanceFromFileDto> parsedGoods = List.of(
                new AcceptanceFromFileDto("Item1", 10f, 100f),
                new AcceptanceFromFileDto("Ingredient1", 20f, 50f)
        );
        AddItemDto addItemDto = new AddItemDto("Item1",2f,100f,10f,new Category());
        when(wordDocumentParser.parseDocument(mockFile)).thenReturn(parsedGoods);
        when(itemService.calculateCostFromFile(Mockito.anyList())).thenReturn(120f);
        when(acceptanceMapper.convertToAddItemInDto(Mockito.any(),Mockito.anyString())).thenReturn(addItemDto);
        // when
        acceptanceService.addAcceptanceFromFileV2(mockFile, isIngredient, surchargeRatio);
        // then
        verify(wordDocumentParser).parseDocument(mockFile);
        verify(itemService).calculateCostFromFile(Mockito.anyList());
        verify(acceptanceRepository).save(any(Acceptance.class));
        verify(itemService,times(2)).findByName("Item1");
        verify(itemInAcceptanceService).addItemInAcceptance(any(ItemInAcceptance.class));
        verify(streamBridge).send(eq("sendGood-out-0"), any(GoodMessage.class));
    }



}