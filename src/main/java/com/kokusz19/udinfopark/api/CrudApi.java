package com.kokusz19.udinfopark.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public interface CrudApi<T> {

    @GetMapping
    List<T> getAll();

    @GetMapping("{id}")
    T getOne(
            @PathVariable(name = "id") int id);

    @PostMapping
    int create(
            @RequestBody @Valid T subject);

    @PatchMapping("{id}")
    T update(
            @PathVariable(name = "id") int id,
            @RequestBody @Valid T subject);

    @DeleteMapping("{id}")
    boolean delete(
            @PathVariable(name = "id") int id);

}
