package CoffeeApp.storageservice.services.itemService;

import CoffeeApp.storageservice.dto.itemDto.AddItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInFridgeResponse;
import CoffeeApp.storageservice.exceptions.ResourceNotFoundException;
import CoffeeApp.storageservice.models.item.Item;
import CoffeeApp.storageservice.models.item.ItemInFridge;
import CoffeeApp.storageservice.repositories.itemRepository.ItemInFridgeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemInFridgeServiceTest {

    @Mock
    private ItemInFridgeRepository itemInFridgeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemInFridgeService itemInFridgeService;

    @Test
    void shouldReturnItemInFridgeDtoById(){
        // given
        Item item = new Item();
        ItemInFridge itemInFridge = new ItemInFridge(item,1000L);
        ItemInFridgeDto itemInFridgeDto = new ItemInFridgeDto(item, new Date(),1000L,false, LocalDateTime.now(),false);
        when(itemInFridgeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(itemInFridge));
        when(modelMapper.map(itemInFridge,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto);
        // when
        ItemInFridgeDto result = itemInFridgeService.findById(Mockito.anyInt());
        // then
        assertThat(result.getItem()).isEqualTo(itemInFridge.getItem());
        assertThat(result.getStorageTime()).isEqualTo(itemInFridge.getStorageTime());
        verify(itemInFridgeRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnItemInFridgeDtoById(){
        // given
        int id = 1;
        when(itemInFridgeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> itemInFridgeService.findById(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item in fridge", "id", Integer.toString(id));
        verify(itemInFridgeRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldReturnAllItemsInFridgeDto(){
        // given
        Item item = new Item();
        ItemInFridge itemInFridge1 = new ItemInFridge(item,1000L);
        ItemInFridge itemInFridge2 = new ItemInFridge(item,2000L);
        List<ItemInFridge> items = Arrays.asList(itemInFridge1,itemInFridge2);

        ItemInFridgeDto itemInFridgeDto1 = new ItemInFridgeDto(item, new Date(),1000L,false, LocalDateTime.now(),false);
        ItemInFridgeDto itemInFridgeDto2 = new ItemInFridgeDto(item, new Date(),2000L,false, LocalDateTime.now(),false);
        when(itemInFridgeRepository.findAll()).thenReturn(items);
        when(modelMapper.map(itemInFridge1,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto1);
        when(modelMapper.map(itemInFridge2,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto2);
        // when
        ItemInFridgeResponse response = itemInFridgeService.findAll();
        // then
        assertThat(response.getItems()).containsExactly(itemInFridgeDto1,itemInFridgeDto2);
        verify(itemInFridgeRepository).findAll();
    }

    @Test
    void shouldNotReturnAllItemsInFridgeDto(){
        // given
        when(itemInFridgeRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        ItemInFridgeResponse response  = itemInFridgeService.findAll();
        // then
        assertThat(response.getItems()).isEmpty();
        verify(itemInFridgeRepository).findAll();
    }

    @Test
    void shouldReturnItemsInFridgeDtoByItem(){
        // given
        Item item = new Item();
        ItemInFridge itemInFridge1 = new ItemInFridge(item,1000L);
        ItemInFridge itemInFridge2 = new ItemInFridge(item,2000L);
        List<ItemInFridge> items = Arrays.asList(itemInFridge1,itemInFridge2);

        ItemInFridgeDto itemInFridgeDto1 = new ItemInFridgeDto(item, new Date(),1000L,false, LocalDateTime.now(),false);
        ItemInFridgeDto itemInFridgeDto2 = new ItemInFridgeDto(item, new Date(),2000L,false, LocalDateTime.now(),false);
        when(itemInFridgeRepository.findByItemName(Mockito.anyString())).thenReturn(items);
        when(modelMapper.map(itemInFridge1,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto1);
        when(modelMapper.map(itemInFridge2,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto2);
        // when
        ItemInFridgeResponse response = itemInFridgeService.findByItemName(Mockito.anyString());
        // then
        assertThat(response.getItems()).containsExactly(itemInFridgeDto1,itemInFridgeDto2);
        verify(itemInFridgeRepository).findByItemName(Mockito.anyString());
    }

    @Test
    void shouldNotReturnItemsInFridgeDtoByItem(){
        // given
        when(itemInFridgeRepository.findByItemName(Mockito.anyString())).thenReturn(Collections.emptyList());
        // when
        ItemInFridgeResponse response  = itemInFridgeService.findByItemName(Mockito.anyString());
        // then
        assertThat(response.getItems()).isEmpty();
        verify(itemInFridgeRepository).findByItemName(Mockito.anyString());
    }

    @Test
    void shouldReturnAllItemsInFridgeByNow(){
        // given
        Item item = new Item();
        ItemInFridge itemInFridge1 = new ItemInFridge(item,1000L);
        ItemInFridge itemInFridge2 = new ItemInFridge(item,2000L);
        List<ItemInFridge> items = Arrays.asList(itemInFridge1,itemInFridge2);

        ItemInFridgeDto itemInFridgeDto1 = new ItemInFridgeDto(item, new Date(),1000L,false, LocalDateTime.now(),false);
        ItemInFridgeDto itemInFridgeDto2 = new ItemInFridgeDto(item, new Date(),2000L,false, LocalDateTime.now(),false);
        when(itemInFridgeRepository.getItemsInFridge()).thenReturn(items);
        when(modelMapper.map(itemInFridge1,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto1);
        when(modelMapper.map(itemInFridge2,ItemInFridgeDto.class)).thenReturn(itemInFridgeDto2);
        // when
        ItemInFridgeResponse response = itemInFridgeService.getItemsFromFridge();
        // then
        assertThat(response.getItems()).containsExactly(itemInFridgeDto1,itemInFridgeDto2);
        verify(itemInFridgeRepository).getItemsInFridge();
    }

    @Test
    void shouldNotReturnAllItemsInFridgeByNow(){
        // given
        when(itemInFridgeRepository.getItemsInFridge()).thenReturn(Collections.emptyList());
        // when
        ItemInFridgeResponse response  = itemInFridgeService.getItemsFromFridge();
        // then
        assertThat(response.getItems()).isEmpty();
        verify(itemInFridgeRepository).getItemsInFridge();
    }


    @Test
    void shouldAddItemToFridge(){
        // given
        Item item = new Item();
        AddItemInFridgeDto addItemInFridgeDto = new AddItemInFridgeDto(item,1000L);
        when(modelMapper.map(addItemInFridgeDto,ItemInFridge.class)).thenReturn(new ItemInFridge(item,1000L));
        // when
        itemInFridgeService.addItemToFridge(addItemInFridgeDto);
        // then
        ArgumentCaptor<ItemInFridge> itemCaptor = ArgumentCaptor.forClass(ItemInFridge.class);
        verify(itemInFridgeRepository).save(itemCaptor.capture());
        ItemInFridge itemInFridge = itemCaptor.getValue();
        assertThat(itemInFridge.getStorageTime()).isEqualTo(addItemInFridgeDto.getStorageTime()* 3_600_000);
        assertThat(itemInFridge.getExpired()).isEqualTo(false);
    }

    @Test
    void shouldRemoveItemFromFridgeById(){
        // given
        ItemInFridge itemInFridge = new ItemInFridge(new Item(),1000L);
        when(itemInFridgeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(itemInFridge));
        when(itemInFridgeRepository.isItemInFridge(Mockito.any())).thenReturn(Optional.of(itemInFridge));
        // when
        itemInFridgeService.removeItemFromFridge(Mockito.any());
        // then
        assertThat(itemInFridge.getIsSold()).isEqualTo(true);
        verify(itemInFridgeRepository).findById(Mockito.anyInt());
        verify(itemInFridgeRepository).isItemInFridge(Mockito.any());
    }

    @Test
    void shouldNotRemoveItemFromFridgeByIdAndThrowExceptionIdNotFound(){
        // given
        int id = 1;
        when(itemInFridgeRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> itemInFridgeService.removeItemFromFridge(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item in fridge", "id", Integer.toString(id));
        verify(itemInFridgeRepository).findById(Mockito.anyInt());
    }

    @Test
    void shouldNotRemoveItemFromFridgeByIdAndThrowExceptionItemNotInFridge(){
        // given
        String itemName = "Water";
        ItemInFridge itemInFridge = new ItemInFridge(new Item(),1000L);
        when(itemInFridgeRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(itemInFridge));
        when(itemInFridgeRepository.isItemInFridge(Mockito.any())).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> itemInFridgeService.removeItemFromFridge(Mockito.anyInt()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Item in fridge", "name", itemName);
        verify(itemInFridgeRepository).findById(Mockito.anyInt());
        verify(itemInFridgeRepository).isItemInFridge(Mockito.any());
    }

    @Test
    void shouldMarkItemsAsExpiredWhenStorageTimeExceeded() {
        // given
        Item item = new Item();
        item.setName("Water");
        ItemInFridge itemInFridge1 = new ItemInFridge();
        itemInFridge1.setPlaceDate(new Date(System.currentTimeMillis() - 10_000L)); // 10 секунд назад
        itemInFridge1.setStorageTime(5_000L); // Время хранения - 5 секунд
        itemInFridge1.setExpired(false);
        itemInFridge1.setItem(item);

        ItemInFridge itemInFridge2 = new ItemInFridge();
        itemInFridge2.setPlaceDate(new Date(System.currentTimeMillis() - 3_000L)); // 3 секунды назад
        itemInFridge2.setStorageTime(5_000L); // Время хранения - 5 секунд
        itemInFridge2.setExpired(false);
        itemInFridge2.setItem(item);

        List<ItemInFridge> items = List.of(itemInFridge1, itemInFridge2);
        when(itemInFridgeRepository.getItemsInFridge()).thenReturn(items);
        // when
        itemInFridgeService.checkForExpiration();
        // then
        assertTrue(itemInFridge1.getExpired(), "Item 1 should be marked as expired");
        assertFalse(itemInFridge2.getExpired(), "Item 2 should not be marked as expired");
        verify(itemInFridgeRepository).getItemsInFridge();
    }
}