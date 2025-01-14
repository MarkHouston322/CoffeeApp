package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.dto.itemDto.AddItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemDto;
import CoffeeApp.storageservice.dto.itemDto.ItemResponse;
import CoffeeApp.storageservice.dto.messages.GoodMessage;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.exceptions.alreadyExistsExceptions.ItemAlreadyExistsException;
import CoffeeApp.storageservice.models.Category;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.repositories.itemRepository.ItemRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private ItemService itemService;

    @Test
    void shouldReturnItemDtoById(){
        // given
        Item item = new Item("Water",100,10f);
        ItemDto itemDto = new ItemDto("Water",100,10f);
        when(modelMapper.map(item,ItemDto.class)).thenReturn(itemDto);
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        // when
        ItemDto result = itemService.findById(Mockito.anyInt());
        // then
        assertThat(result.getName()).isEqualTo(item.getName());
        verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnItemDtoById(){
        //given
        int id = 1;
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> itemService.findById(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item", "id", Integer.toString(id));
        verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldReturnItemByName(){
        // given
        Item item = new Item("Water",100,10f);
        when(itemRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(item));
        // when
        Item result = itemService.findByName(Mockito.anyString());
        // then
        assertThat(result.getName()).isEqualTo(item.getName());
        verify(itemRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldNotReturnItemByName(){
        // given
        String name = "Water";
        when(itemRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> itemService.findByName(Mockito.anyString()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item", "name", name);
        verify(itemRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldReturnItemsDtoByNameStartingWith(){
        // given
        Item item1 = new Item("Water",100,10f);
        Item item2 = new Item("Whiskey",100,10f);
        List<Item> items = Arrays.asList(item1,item2);
        ItemDto itemDto1 = new ItemDto("Water",100,10f);
        ItemDto itemDto2 = new ItemDto("Whiskey",100,10f);
        when(itemRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(items);
        when(modelMapper.map(item1, ItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(item2, ItemDto.class)).thenReturn(itemDto2);
        // when
        ItemResponse response = itemService.findItemsByName(Mockito.anyString());
        // then
        assertThat(response.getItems()).containsExactly(itemDto1,itemDto2);
        verify(itemRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldNotReturnItemsDtoByNameStartingWith(){
        // given
        when(itemRepository.findByNameStartingWith(Mockito.anyString())).thenReturn(Collections.emptyList());
        // when
        ItemResponse response = itemService.findItemsByName(Mockito.anyString());
        // then
        assertThat(response.getItems()).isEmpty();
        verify(itemRepository).findByNameStartingWith(Mockito.anyString());
    }

    @Test
    void shouldReturnALLItemsDto(){
        // given
        Item item1 = new Item("Water",100,10f);
        Item item2 = new Item("Whiskey",100,10f);
        List<Item> items = Arrays.asList(item1,item2);
        ItemDto itemDto1 = new ItemDto("Water",100,10f);
        ItemDto itemDto2 = new ItemDto("Whiskey",100,10f);
        when(itemRepository.findAll()).thenReturn(items);
        when(modelMapper.map(item1, ItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(item2, ItemDto.class)).thenReturn(itemDto2);
        // when
        ItemResponse response = itemService.findAll();
        // then
        assertThat(response.getItems()).containsExactly(itemDto1,itemDto2);
        verify(itemRepository).findAll();
    }

    @Test
    void shouldNotReturnALLItemsDto(){
        // given
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        ItemResponse response = itemService.findAll();
        // then
        assertThat(response.getItems()).isEmpty();
        verify(itemRepository).findAll();
    }

    @Test
    void shouldAddItem(){
        // given
        AddItemDto addItemDto = new AddItemDto("Water",2f,10f,50f,new Category());
        when(itemRepository.findByName(Mockito.anyString())).thenReturn(Optional.empty());
        setModelMapper(addItemDto);
        // when
        itemService.addItem(addItemDto);
        // then
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());
        Item result = itemCaptor.getValue();
        assertThat(result.getPrice()).isEqualTo((int) Math.ceil((result.getCostPrice() * result.getSurchargeRatio()) / 10) * 10);
        assertThat(result.getName()).isEqualTo(addItemDto.getName());
        verify(itemRepository).findByName(Mockito.anyString());
        testSendItem(result);
    }

    @Test
    void shouldNotAddItem() {
        // given
        Item existingItem = new Item();
        AddItemDto addItemDto = new AddItemDto("Water",2f,10f,50f,new Category());
        when(itemRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(existingItem));
        setModelMapper(addItemDto);
        // when & then
        assertThatThrownBy(() -> itemService.addItem(Mockito.any()))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining("Item has already been added with this name");
        verify(itemRepository).findByName(Mockito.anyString());
    }

    @Test
    void shouldDeleteItemById(){
        // given
        int id = 1;
        Item item = new Item();
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(Mockito.anyInt());
        // when
        itemService.deleteItem(id);
        // then
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(itemRepository).deleteById(idCaptor.capture());
        assertThat(idCaptor.getValue()).isEqualTo(id);
        verify(itemRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldUpdateItemById(){
        // given
        Item item = new Item("Water",2f,50f,10f,new Category());
        AddItemDto addItemDto = new AddItemDto("Water",3f,50f,10f,item.getCategory());
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(item));
        setModelMapper(addItemDto);
        // when
        itemService.updateItem(Mockito.anyInt(),Mockito.any());
        // then
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(itemRepository).save(itemCaptor.capture());
        Item updatedItem = itemCaptor.getValue();
        assertThat(updatedItem.getName()).isEqualTo(item.getName());
        assertThat(updatedItem.getSurchargeRatio()).isEqualTo(addItemDto.getSurchargeRatio());
        verify(itemRepository).findById(Mockito.anyInt());
        testSendItem(updatedItem);
    }

    @Test
    void shouldCalculateTotalCostCorrectly() {
        // given
        Map<String, String> items = Map.of(
                "Apple", "2",
                "Banana", "3"
        );
        Item apple = new Item("Apple",2f,10f, 10.0f,new Category());
        Item banana = new Item("Banana",2f,5f,10f, new Category());
        when(itemRepository.findByName("Apple")).thenReturn(Optional.of(apple));
        when(itemRepository.findByName("Banana")).thenReturn(Optional.of(banana));
        // when
        float totalCost = itemService.calculateCost(items);
        // then
        assertEquals(35.0f, totalCost, 0.01f);
        verify(itemRepository).findByName("Apple");
        verify(itemRepository).findByName("Banana");
        verifyNoMoreInteractions(itemRepository);
    }

    @Test
    void shouldCalculateTotalCostFromFileCorrectly() {
        // given
        List<AddItemDto> items = List.of(
                new AddItemDto("Apple",2f, 2.0f, 10.0f,new Category()),
                new AddItemDto("Banana",2f, 3.0f, 5.0f,new Category())
        );
        // when
        float totalCost = itemService.calculateCostFromFile(items);
        // then
        assertEquals(35.0f, totalCost, 0.01f);
    }




    private void setModelMapper(AddItemDto addItemDto){
        when(modelMapper.map(addItemDto,Item.class)).thenReturn(new Item(addItemDto.getName(),addItemDto.getSurchargeRatio(),
                addItemDto.getQuantityInStock(), addItemDto.getCostPrice(), addItemDto.getCategory()));
    }

    private void testSendItem(Item item){
        ArgumentCaptor<GoodMessage> messageCaptor = ArgumentCaptor.forClass(GoodMessage.class);
        verify(streamBridge).send(eq("sendGood-out-0"), messageCaptor.capture());
        GoodMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage.getName()).isEqualTo(item.getName());
    }

}