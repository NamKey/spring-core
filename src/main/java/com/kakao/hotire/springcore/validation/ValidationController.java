package com.kakao.hotire.springcore.validation;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/validation")
public class ValidationController {

    /**
     * Validated 동작하지 않음
     */
    @GetMapping
    public ResponseEntity<Validation> validation(@Validated Validation validation) {
        return ResponseEntity.ok(validation);
    }

    /**
     * Valid 동작하지 않음
     */
    @GetMapping("/2")
    public ResponseEntity<String> validation2(@Valid @NotEmpty String name) {
        return ResponseEntity.ok(name);
    }

    /**
     * ModelAttributeMethodProcessor의 Validation으로 동작함
     */
    @PostMapping
    public ResponseEntity<ValidationRequest> post(@Validated ValidationRequest request) {
        return ResponseEntity.ok(request);
    }
}