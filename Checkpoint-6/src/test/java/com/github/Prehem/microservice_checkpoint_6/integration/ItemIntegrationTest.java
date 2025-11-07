package com.prehem.checkpoint6.integration;

import com.prehem.checkpoint6.model.Item;
import com.prehem.checkpoint6.repository.ItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes de Integração para a Controller de Item,
 * garantindo o CRUD completo (POST, GET, PUT, DELETE).
 * O perfil 'default' usa o banco de dados H2 em memória.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("default") // Usa o H2 em memória para testes rápidos
public class ItemIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemRepository itemRepository;

    private final String BASE_URL = "/api/items";

    // Item de exemplo para ser usado nos testes
    private Item initialItem;

    /**
     * Configuração inicial antes de cada teste: Limpa o banco e insere um item inicial.
     */
    @BeforeEach
    public void setup() {
        itemRepository.deleteAll();
        initialItem = itemRepository.save(new Item("Monitor Ultrawide", 2));
    }

    /**
     * Limpeza após cada teste: garante que o banco de dados está vazio.
     */
    @AfterEach
    public void teardown() {
        itemRepository.deleteAll();
    }

    // --- 1. Teste de Criação (POST) ---
    @Test
    public void testCreateItem() {
        Item newItem = new Item("Teclado Mecânico", 5);

        webTestClient.post().uri(BASE_URL)
                .bodyValue(newItem)
                .exchange()
                .expectStatus().isCreated() // Verifica se retornou 201 Created
                .expectBody(Item.class)
                .value(item -> {
                    assertThat(item.getId()).isNotNull();
                    assertThat(item.getNome()).isEqualTo("Teclado Mecânico");
                    assertThat(item.getQuantidade()).isEqualTo(5);
                });
    }

    // --- 2. Teste de Leitura (GET) por ID ---
    @Test
    public void testGetItemById() {
        webTestClient.get().uri(BASE_URL + "/{id}", initialItem.getId())
                .exchange()
                .expectStatus().isOk() // Verifica se retornou 200 OK
                .expectBody(Item.class)
                .value(item -> {
                    assertThat(item.getId()).isEqualTo(initialItem.getId());
                    assertThat(item.getNome()).isEqualTo("Monitor Ultrawide");
                });
    }

    // --- 3. Teste de Leitura (GET) de todos os itens ---
    @Test
    public void testGetAllItems() {
        webTestClient.get().uri(BASE_URL)
                .exchange()
                .expectStatus().isOk() // Verifica se retornou 200 OK
                .expectBodyList(Item.class)
                .hasSize(1) // Verifica se o item inicial está presente
                .value(list -> assertThat(list.get(0).getNome()).isEqualTo("Monitor Ultrawide"));
    }

    // --- 4. Teste de Atualização (PUT) ---
    @Test
    public void testUpdateItem() {
        Item updatedDetails = new Item("Monitor Ultrawide Pro", 10);

        webTestClient.put().uri(BASE_URL + "/{id}", initialItem.getId())
                .bodyValue(updatedDetails)
                .exchange()
                .expectStatus().isOk() // Verifica se retornou 200 OK
                .expectBody(Item.class)
                .value(item -> {
                    assertThat(item.getNome()).isEqualTo("Monitor Ultrawide Pro");
                    assertThat(item.getQuantidade()).isEqualTo(10);
                });
    }

    // --- 5. Teste de Exclusão (DELETE) ---
    @Test
    public void testDeleteItem() {
        webTestClient.delete().uri(BASE_URL + "/{id}", initialItem.getId())
                .exchange()
                .expectStatus().isNoContent(); // Verifica se retornou 204 No Content

        // Tenta buscar o item deletado para confirmar que ele não existe (404 Not Found)
        webTestClient.get().uri(BASE_URL + "/{id}", initialItem.getId())
                .exchange()
                .expectStatus().isNotFound();
    }
}