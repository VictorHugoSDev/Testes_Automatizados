package com.iftm.client.repositories;

import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.iftm.client.dto.ClientDTO;
import com.iftm.client.entities.Client;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ClientServiceIntegrationTest {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientRepository repository;

    private Long idExistente;
    private Long idNaoExistente;
    private PageRequest pageRequest;

    private Client client;
    private Client clientWithTargetIncome;
    private Double targetIncome;

    private ClientDTO clientDTOtoUpdate;

    @BeforeEach
    void setUp() throws Exception {
        repository.deleteAll(); // limpa o banco para garantir isolamento dos testes

        // Cliente padrão para vários testes
        client = new Client(null, "Cliente Test", "12345678901", 2000.0, Instant.now(), 0);
        clientWithTargetIncome = new Client(null, "Cliente Alvo", "09876543210", 2000.0, Instant.now(), 1);

        client = repository.save(client);
        clientWithTargetIncome = repository.save(clientWithTargetIncome);

        idExistente = client.getId();
        idNaoExistente = 1000L;

        pageRequest = PageRequest.of(0, 10);
        targetIncome = 2000.0;

        clientDTOtoUpdate = new ClientDTO(null, "Nome Atualizado", "99988877700", 5000.0, Instant.parse("2001-10-20T00:00:00Z"), 2);
    }

    /* delete deveria
    ◦ retornar vazio quando o id existir
    ◦ lançar uma EmptyResultDataAccessException quando o id não existir */
    @Test
    public void apagarNaoDeveFazerNadaQuandoIdExistente() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(idExistente);
        });

        Optional<Client> result = repository.findById(idExistente);
        Assertions.assertFalse(result.isPresent(), "O cliente deveria ter sido deletado");
    }

    @Test
    void apagarDeveLancarUmaExcecaoQuandoIdNaoExistente() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(idNaoExistente);
        });
    }

    // findAllPaged deveria retornar uma página com todos os clientes (e chamar o método findAll do repository)
    @Test
    void findAllPageDeveRetornarPaginaComTodosOsClientes() {
        Page<ClientDTO> result = service.findAllPaged(pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(2, result.getTotalElements(), "Deveria haver 2 clientes na página");
        Assertions.assertFalse(result.getContent().isEmpty(), "A lista de clientes não deve estar vazia");

        boolean hasClientName = result.getContent().stream().anyMatch(dto -> dto.getName().equals(client.getName()));
        Assertions.assertTrue(hasClientName, "Deveria conter o cliente inserido");
    }

    // findByIncome deveria retornar uma página com os clientes que tenham o Income informado (e chamar o método findByIncome do repository)
    @Test
    public void findByIncomeDeveRetornarPaginaComClientesComIncomeEspecifico() {
        Page<ClientDTO> result = service.findByIncome(targetIncome, pageRequest);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(2, result.getTotalElements(), "Deveria haver 2 clientes com o income especificado");
        Assertions.assertFalse(result.getContent().isEmpty(), "A lista de clientes não deve estar vazia");

        boolean allHaveTargetIncome = result.getContent().stream()
                .allMatch(dto -> dto.getIncome().equals(targetIncome));
        Assertions.assertTrue(allHaveTargetIncome, "Todos os clientes retornados devem ter o income especificado");
    }

    /* findById deveria
    ◦ retornar um ClientDTO quando o id existir
    ◦ lançar ResourceNotFoundException quando o id não existir */
    @Test
    public void findByIdDeveRetornarClientDTOQuandoIdExistir() {
        ClientDTO result = service.findById(idExistente);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(client.getId(), result.getId(), "O ID do cliente deve ser igual");
        Assertions.assertEquals(client.getName(), result.getName(), "O nome do cliente deve ser igual");
        Assertions.assertEquals(client.getCpf(), result.getCpf(), "O CPF do cliente deve ser igual");
    }

    @Test
    public void findByIdDeveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(idNaoExistente);
        });
    }

    /*  update deveria
    ◦ retornar um ClientDTO quando o id existir
    ◦ lançar uma ResourceNotFoundException quando o id não existir */
    @Test
    public void updateDeveRetornarClientDTOQuandoIdExistir() {
        ClientDTO result = service.update(idExistente, clientDTOtoUpdate);

        Assertions.assertNotNull(result, "O resultado não deve ser nulo");
        Assertions.assertEquals(clientDTOtoUpdate.getName(), result.getName(), "O nome do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getCpf(), result.getCpf(), "O CPF do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getIncome(), result.getIncome(), "A renda do cliente atualizado deve ser igual");
        Assertions.assertEquals(clientDTOtoUpdate.getChildren(), result.getChildren(), "O número de filhos do cliente atualizado deve ser igual");
    }

    @Test
    public void updateDeveLancarResourceNotFoundExceptionQuandoIdNaoExistir() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(idNaoExistente, clientDTOtoUpdate);
        });
    }

    // insert deveria retornar um ClientDTO ao inserir um novo cliente
    @Test
    public void insertDeveRetornarClientDTOAoInserirNovoCliente() {
        ClientDTO newClientInputDTO = new ClientDTO(
            null,
            "Novo Cliente Inserido",
            "12345678900",
            3500.0,
            Instant.now(),
            1
        );

        ClientDTO resultDTO = service.insert(newClientInputDTO);

        Assertions.assertNotNull(resultDTO, "O resultado não deve ser nulo");
        Assertions.assertNotNull(resultDTO.getId(), "O ID gerado não deve ser nulo");

        Assertions.assertEquals(newClientInputDTO.getName(), resultDTO.getName(), "O nome do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getCpf(), resultDTO.getCpf(), "O CPF do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getIncome(), resultDTO.getIncome(), "A renda do cliente inserida deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getChildren(), resultDTO.getChildren(), "O número de filhos do cliente inserido deve ser igual");
        Assertions.assertEquals(newClientInputDTO.getBirthDate(), resultDTO.getBirthDate(), "A data de nascimento do cliente inserido deve ser igual");

        Client entidade = repository.findById(resultDTO.getId()).orElse(null);
        Assertions.assertNotNull(entidade, "O cliente deve existir no banco de dados");
        Assertions.assertEquals(newClientInputDTO.getName(), entidade.getName(), "O nome persistido deve ser igual");
    }
}
