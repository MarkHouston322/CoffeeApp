package CoffeeApp.storageservice.controllers;

import CoffeeApp.storageservice.constants.WriteOffConstants;
import CoffeeApp.storageservice.dto.ingredientDto.IngredientInDto;
import CoffeeApp.storageservice.dto.ResponseDto;
import CoffeeApp.storageservice.dto.itemDto.ItemInDto;
import CoffeeApp.storageservice.dto.writeOffDto.AddWriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffDto;
import CoffeeApp.storageservice.dto.writeOffDto.WriteOffResponse;
import CoffeeApp.storageservice.services.WriteOffService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/write-offs")
@AllArgsConstructor
public class WriteOffController {

    private final WriteOffService writeOffService;

    @GetMapping
    public WriteOffResponse findAll(){
        return writeOffService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WriteOffDto> findById(@PathVariable("id") Integer id){
        WriteOffDto writeOffDto = writeOffService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(writeOffDto);
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<List<IngredientInDto>> getIngredientsInWriteOff(@PathVariable("id") Integer writeOffId){
        List<IngredientInDto> ingredients = writeOffService.getIngredientsByWriteOffId(writeOffId);
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<ItemInDto>> getItemsInWriteOff(@PathVariable("id") Integer writeOffId){
        List<ItemInDto> items = writeOffService.getItemsByWriteOffId(writeOffId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> writeOff(@Valid @RequestBody AddWriteOffDto addWriteOffDto,
                                                @RequestParam Map<String, String> goods) throws ExecutionException, InterruptedException {
        writeOffService.addWriteOffGoods(addWriteOffDto, goods);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(WriteOffConstants.STATUS_201, WriteOffConstants.MESSAGE_201));
    }
}
