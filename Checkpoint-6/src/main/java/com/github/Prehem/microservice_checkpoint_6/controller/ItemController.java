package com.prehem.checkpoint6.controller;

import com.prehem.checkpoint6.model.Item;
import com.prehem.checkpoint6.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    // GET - Obter todos os itens
    @GetMapping
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    // GET - Obter item por ID
    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST - Criar novo item
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    // PUT - Atualizar item
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        Optional<Item> item = itemRepository.findById(id);
        if (item.isPresent()) {
            Item existingItem = item.get();
            existingItem.setNome(itemDetails.getNome());
            existingItem.setQuantidade(itemDetails.getQuantidade());
            return ResponseEntity.ok(itemRepository.save(existingItem));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE - Deletar item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}