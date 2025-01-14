package CoffeeApp.sellingservice.controllers;

import CoffeeApp.sellingservice.constants.PaymentMethodConstants;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodResponse;
import CoffeeApp.sellingservice.dto.ResponseDto;
import CoffeeApp.sellingservice.services.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/payment")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @GetMapping
    public PaymentMethodResponse findAll() {
        return paymentMethodService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethod(@PathVariable("id") Integer id) {
        PaymentMethodDto paymentMethodDto = paymentMethodService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(paymentMethodDto);
    }

    @GetMapping("/name/{name}")
    public PaymentMethodResponse findPaymentMethodByName(@PathVariable("name") String name) {
        return paymentMethodService.findByName(name);
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDto> addPaymentMethod(@Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        paymentMethodService.addPaymentMethod(paymentMethodDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(PaymentMethodConstants.STATUS_201, PaymentMethodConstants.MESSAGE_201));
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<ResponseDto> updatePaymentMethod(@PathVariable("id") Integer id,
                                                           @Valid @RequestBody PaymentMethodDto paymentMethodDto) {
        paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
        return responseStatusOk();

    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ResponseDto> deletePaymentMethod(@PathVariable("id") Integer id) {
        paymentMethodService.deletePaymentMethod(id);
        return responseStatusOk();
    }

    private ResponseEntity<ResponseDto> responseStatusOk() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(PaymentMethodConstants.STATUS_200, PaymentMethodConstants.MESSAGE_200));
    }
}
