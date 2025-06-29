package com.iftm.client.resources;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iftm.client.dto.ClientDTO;
import com.iftm.client.services.ClientService;
import com.iftm.client.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ClientResourcesTestsITMockBean {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ClientService service;

        @Autowired
        private ObjectMapper objectMapper; 

        @Test
        @Order(1)
        @DisplayName("Verificar se o endpoint get/clients/ retorna todos os clientes existentes")
        public void testarEndPointListarTodosClientesRetornaCorreto() throws Exception {

                List<ClientDTO> listaClientes = new ArrayList<>();

                listaClientes.add(new ClientDTO(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2));
                listaClientes.add(new ClientDTO(2L, "Lázaro Ramos", "10619244881", 2500.0, Instant.parse("1996-12-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(3L, "Clarice Lispector", "10919444522", 3800.0, Instant.parse("1960-04-13T07:50:00Z"), 2));
                listaClientes.add(new ClientDTO(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(5L, "Gilberto Gil", "10419344882", 2500.0, Instant.parse("1949-05-05T07:00:00Z"), 4));
                listaClientes.add(new ClientDTO(6L, "Djamila Ribeiro", "10619244884", 4500.0, Instant.parse("1975-11-10T07:00:00Z"), 1));
                listaClientes.add(new ClientDTO(7L, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(8L, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(9L, "Yuval Noah Harari", "10619244881", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(10L, "Chimamanda Adichie", "10114274861", 1500.0, Instant.parse("1956-09-23T07:00:00Z"),0));
                listaClientes.add(new ClientDTO(11L, "Silvio Almeida", "10164334861", 4500.0, Instant.parse("1970-09-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(12L, "Jorge Amado", "10204374161", 2500.0, Instant.parse("1918-09-23T07:00:00Z"), 0));

                Page<ClientDTO> paginaClientes = new PageImpl<>(listaClientes);
                Mockito.when(service.findAllPaged(Mockito.any()))
                        .thenReturn(paginaClientes);
                
                // Arrange
                int quantidadeTotalClientes = 12;

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalElements").value(quantidadeTotalClientes));
        }

        @Test
        @Order(2)
        @DisplayName("Verificar se o endpoint get/clients/{id} retorna o cliente correto")
        public void testarEndPointListarClienteCorrete() throws Exception {

                ClientDTO clienteExistente = new ClientDTO(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"), 0);

                // Arrange
                long existingId = 4L;

                // Act
                Mockito.when(service.findById(existingId))
                        .thenReturn(clienteExistente);

                ResultActions resultados = mockMvc.perform(get("/clients/id/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.id").value(existingId))
                        .andExpect(jsonPath("$.name").exists())
                        .andExpect(jsonPath("$.name").value("Carolina Maria de Jesus"))
                        .andExpect(jsonPath("$.cpf").exists())
                        .andExpect(jsonPath("$.cpf").value("10419244771"))
                        .andExpect(jsonPath("$.income").exists())
                        .andExpect(jsonPath("$.income").value(7500.0))
                        .andExpect(jsonPath("$.birthDate").exists())
                        .andExpect(jsonPath("$.birthDate").value("1996-12-23T07:00:00Z"))
                        .andExpect(jsonPath("$.children").exists())
                        .andExpect(jsonPath("$.children").value(0));
        }

        @Test
        @Order(3)
        @DisplayName("Verificar se o endpoint get/clients/{id} retorna 404")
        public void testarEndPointListarClienteInexistenteRetorna404() throws Exception {
                // Arrange
                long nonExistingId = 100L;

                Mockito.when(service.findById(nonExistingId))
                        .thenThrow(new ResourceNotFoundException("Entity not Found"));

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/id/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.error").value("Resource not found"));
        }

        @Test
        @Order(4)
        @DisplayName("Verificar se o cliente tem o income correto e o status 200 OK")
        public void testarEndPointListarClienteComIncomeCorreto() throws Exception {
                // Arrange
                Double rendaEsperada = 2500.0;
                int totalDeElementosEsperado = 3;
                int linhasPorPaginaEsperadas = 12;
                
                List<ClientDTO> listaClientes = new ArrayList<>();
        
                listaClientes.add(new ClientDTO(5L, "Gilberto Gil", "10419344882", 2500.0, Instant.parse("1949-05-05T07:00:00Z"), 4));
                listaClientes.add(new ClientDTO(12L, "Jorge Amado", "10204374161", 2500.0,  Instant.parse("1918-09-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(2L, "Lázaro Ramos", "10619244881", 2500.0, Instant.parse("1996-12-23T07:00:00Z"), 2));

                Pageable paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Sort.Direction.ASC, "name"));
                
                Page<ClientDTO> paginaClientes = new PageImpl<>(listaClientes, paginaEsperada, totalDeElementosEsperado);

                Mockito.when(service.findByIncome(Mockito.any(PageRequest.class), Mockito.eq(rendaEsperada)))
                        .thenReturn(paginaClientes);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/income/")
                        .param("income", String.valueOf(rendaEsperada))
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .param("direction", "ASC")
                        .param("orderBy", "name")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.content[0].name").value("Gilberto Gil"))
                        .andExpect(jsonPath("$.content[1].name").value("Jorge Amado"))
                        .andExpect(jsonPath("$.content[2].name").value("Lázaro Ramos"))
                        .andExpect(jsonPath("$.content[?(@.name == '%s')]", "Conceição Evaristo").doesNotExist());
        }

        @Test
        @Order(5)
        @DisplayName("Teste para não retornar clientes")
        public void testarEndPointListarClientesComRendaInexistente() throws Exception {
                // Arrange
                Double rendaInexistente = 9999.0;
                int totalDeElementosEsperado = 0;
                int linhasPorPaginaEsperadas = 12;

                List<ClientDTO> listaClientesVazia = new ArrayList<>();

                Pageable paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Sort.Direction.ASC, "name"));

                Page<ClientDTO> paginaVaziaDeClientes = new PageImpl<>(listaClientesVazia, paginaEsperada, totalDeElementosEsperado);

                Mockito.when(service.findByIncome(Mockito.any(PageRequest.class), Mockito.eq(rendaInexistente)))
                        .thenReturn(paginaVaziaDeClientes);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/income/")
                        .param("income", String.valueOf(rendaInexistente))
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .param("direction", "ASC")
                        .param("orderBy", "name")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true));
        }

        @Test
        @Order(6)
        @DisplayName("Deve retornar clientes com income maior que o especificado e status 200 OK")
        public void testarEndPointListarClientesComRendaMaiorQueCorreta() throws Exception {
                // Arrange

                Double rendaEsperada = 4000.0;
                int totalDeElementosEsperado = 5;
                int linhasPorPaginaEsperadas = 12;

                List<ClientDTO> listaClientes = new ArrayList<>();
        
                listaClientes.add(new ClientDTO(4L, "Carolina Maria de Jesus", "10419244771", 7500.0, Instant.parse("1996-12-23T07:00:00Z"),  0));
                listaClientes.add(new ClientDTO(6L, "Djamila Ribeiro", "10619244884", 4500.0, Instant.parse("1975-11-10T07:00:00Z"), 1));
                listaClientes.add(new ClientDTO(7L, "Jose Saramago", "10239254871", 5000.0, Instant.parse("1996-12-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(11L, "Silvio Almeida", "10164334861", 4500.0, Instant.parse("1970-09-23T07:00:00Z"),  2));
                listaClientes.add(new ClientDTO(8L, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0));
                
                Pageable paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Sort.Direction.ASC, "name"));

                Page<ClientDTO> paginaClientes = new PageImpl<>(listaClientes, paginaEsperada, totalDeElementosEsperado);

                Mockito.when(service.findByIncomeGreaterThan(Mockito.any(PageRequest.class), Mockito.eq(rendaEsperada)))
                        .thenReturn(paginaClientes);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/incomeGreaterThan/")
                        .param("income", String.valueOf(rendaEsperada))
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .param("direction", "ASC")
                        .param("orderBy", "name")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.totalPages").value(1));
        }

        @Test
        @Order(7)
        @DisplayName("Deve retornar lista vazia e status 200 OK quando nenhum cliente tem income maior")
        public void testarEndPointListarClientesComRendaMaiorQueInexistente() throws Exception {

                Double rendaEsperada = 15000.0;
                int totalDeElementosEsperado = 0;
                int linhasPorPaginaEsperadas = 12;

                List<ClientDTO> listaClientesVazia = new ArrayList<>();

                Pageable paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Sort.Direction.ASC, "name"));

                Page<ClientDTO> paginaVaziaDeClientes = new PageImpl<>(listaClientesVazia, paginaEsperada, totalDeElementosEsperado);

                Mockito.when(service.findByIncomeGreaterThan(Mockito.any(PageRequest.class), Mockito.eq(rendaEsperada)))
                        .thenReturn(paginaVaziaDeClientes);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/incomeGreaterThan/")
                        .param("income", String.valueOf(rendaEsperada))
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .param("direction", "ASC")
                        .param("orderBy", "name")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(0))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(0))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.totalPages").value(0));
        }

        @Test
        @Order(8)
        @DisplayName("Deve retornar clientes com CPF que contêm o padrão e status 200 OK")
        public void testarEndPointListarClientesComCPFLikeCorreto() throws Exception {
                // Arrange

                String parametroCPF = "61";
                String cpfComCoringas = "%" + parametroCPF + "%"; 
                int totalDeElementosEsperado = 7;
                int linhasPorPaginaEsperadas = 12;

                List<ClientDTO> listaClientes = new ArrayList<>();

                listaClientes.add(new ClientDTO(10L, "Chimamanda Adichie", "10114274861", 1500.0, Instant.parse("1956-09-23T07:00:00Z"),0));
                listaClientes.add(new ClientDTO(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2));
                listaClientes.add(new ClientDTO(6L, "Djamila Ribeiro", "10619244884", 4500.0, Instant.parse("1975-11-10T07:00:00Z"), 1));
                listaClientes.add(new ClientDTO(2L, "Lázaro Ramos", "10619244881", 2500.0, Instant.parse("1996-12-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(11L, "Silvio Almeida", "10164334861", 4500.0, Instant.parse("1970-09-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(8L, "Toni Morrison", "10219344681", 10000.0, Instant.parse("1940-02-23T07:00:00Z"), 0));
                listaClientes.add(new ClientDTO(9L, "Yuval Noah Harari", "10619244881", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0));

                PageRequest paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Direction.ASC, "name"));

                Page<ClientDTO> paginaClientes = new PageImpl<>(listaClientes, paginaEsperada, totalDeElementosEsperado);

                Mockito.when(service.findByCpfLike(Mockito.any(PageRequest.class), Mockito.eq(cpfComCoringas)))
                        .thenReturn(paginaClientes);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/cpf/")
                        .param("cpf", parametroCPF)
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .param("direction", "ASC")
                        .param("orderBy", "name")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.totalPages").value(1));
        }

       @Test
        @Order(9)
        @DisplayName("GET /clients/cpf/ - Deve retornar lista vazia e status 200 OK quando nenhum CPF contém o padrão")
        public void testarEndPointListarClientesComCPFLikeInexistente() throws Exception {
        // Arrange
        String parametroCPF = "99999"; 
        String cpfComCoringas = "%" + parametroCPF + "%";
        int totalDeElementosEsperado = 0;
        int linhasPorPaginaEsperadas = 12;

        List<ClientDTO> listaClientesVazia = new ArrayList<>();

        PageRequest expectedPageRequest = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Direction.ASC, "name"));
        
        Page<ClientDTO> paginaClientesVaziaMockada = new PageImpl<>(listaClientesVazia, expectedPageRequest, totalDeElementosEsperado);

        Mockito.when(service.findByCpfLike(Mockito.any(PageRequest.class), Mockito.eq(cpfComCoringas)))
                            .thenReturn(paginaClientesVaziaMockada);

        // Act
        ResultActions resultados = mockMvc.perform(get("/clients/cpf/")
                .param("cpf", parametroCPF)
                .param("page", "0")
                .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                .param("direction", "ASC")
                .param("orderBy", "name")
                .accept(MediaType.APPLICATION_JSON));

        // Assert
        resultados
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0)) 
                .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true))
                .andExpect(jsonPath("$.totalPages").value(0));
        }


        @Test
        @Order(10)
        @DisplayName("Deve retornar 201 ao criar um novo cliente")
        public void inserirUmClienteERetornarCorreto() throws Exception {

                // Arrange
                ClientDTO novoCliente = new ClientDTO(
                                null,
                                "Novo Cliente",
                                "11122233344",
                                5500.0,
                                Instant.parse("1908-06-27T10:00:00Z"),
                                3);


                 ClientDTO clienteRetornadoMock = new ClientDTO(
                                1L,
                                "Novo Cliente",
                                "11122233344",
                                5500.0,
                                Instant.parse("1908-06-27T10:00:00Z"),
                                3);

                String jsonRequest = objectMapper.writeValueAsString(novoCliente);

                Mockito.when(service.insert(Mockito.any(ClientDTO.class)))
                        .thenReturn(clienteRetornadoMock);

                // Act
                ResultActions resultados = mockMvc.perform(post("/clients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.id").isNumber())
                        .andExpect(jsonPath("$.id", notNullValue()))
                        .andExpect(jsonPath("$.name").value("Novo Cliente"))
                        .andExpect(jsonPath("$.cpf").value("11122233344"))
                        .andExpect(jsonPath("$.income").value(5500.0))
                        .andExpect(jsonPath("$.birthDate").value("1908-06-27T10:00:00Z"))
                        .andExpect(jsonPath("$.children").value(3));
        }

        @Test
        @Order(14)
        @DisplayName("Deve retornar 204 No Content quando o id existir")
        public void testarEndPointDeletarClienteExistente() throws Exception {
                // Arrange
                long idExistente = 1L;

                Mockito.doNothing().when(service).delete(idExistente);

                // Act
                ResultActions resultados = mockMvc.perform(delete("/clients/{id}", idExistente)
                                .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isNoContent());
        }

        @Test
        @Order(13)
        @DisplayName("Deve retornar 204 Not Found quando o id não existir")
        public void testarEndPointDeletarClienteInexistente() throws Exception {
                // Arrange
                long idNaoExistente = 100L;
                String erroEsperado = "Resource not found";
                String mensagemEsperada = "Id not found " + idNaoExistente;
                String caminhoEsperado = "/clients/" + idNaoExistente;

                Mockito.doThrow(new ResourceNotFoundException(mensagemEsperada)).when(service).delete(idNaoExistente);

                // Act
                ResultActions resultados = mockMvc.perform(delete("/clients/{id}", idNaoExistente)
                                .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.timestamp").exists())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.error").value(erroEsperado))
                        .andExpect(jsonPath("$.message").value(mensagemEsperada))
                        .andExpect(jsonPath("$.path").value(caminhoEsperado));
        }

        @Test
        @Order(12)
        @DisplayName("Retornar 'ok' (código 200) e o JSON do produto atualizado para um id existente")
        public void testarEndPointAtualizarClienteExistente() throws Exception {
                // Arrange
                long idExistente = 1L;
                String nomeAtualizado = "Cliente Atualizado";
                Double rendaAtualizada = 6000.0;
                String cpfOriginal = "12345678901";
                Instant birthDateOriginal = Instant.parse("1990-01-01T10:00:00Z");
                Integer childrenOriginal = 2;


                ClientDTO clienteAtualizado = new ClientDTO(
                        idExistente,
                        nomeAtualizado,
                        cpfOriginal,
                        rendaAtualizada,
                        birthDateOriginal,
                        childrenOriginal);

                ClientDTO clienteRetornadoPeloMock = new ClientDTO(
                        idExistente,
                        nomeAtualizado,
                        cpfOriginal,
                        rendaAtualizada,
                        birthDateOriginal,
                        childrenOriginal);

                String jsonRequest = objectMapper.writeValueAsString(clienteAtualizado);

                  Mockito.when(service.update(Mockito.eq(idExistente), Mockito.any(ClientDTO.class)))
                            .thenReturn(clienteRetornadoPeloMock);

                // Act
                ResultActions resultados = mockMvc.perform(put("/clients/{id}", idExistente)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonRequest)
                                .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados.andDo(print())
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.id").value(idExistente))
                                .andExpect(jsonPath("$.name").value(nomeAtualizado))
                                .andExpect(jsonPath("$.cpf").value(cpfOriginal))
                                .andExpect(jsonPath("$.income").value(rendaAtualizada))
                                .andExpect(jsonPath("$.birthDate").value("1990-01-01T10:00:00Z"))
                                .andExpect(jsonPath("$.children").value(childrenOriginal));
        }

        @Test
        @Order(11)
        @DisplayName("retornar  “not  found”  (código  404)  quando  o  id  não  existir.  Fazer  uma  assertion  para "
                        +
                        " verificar  no  json  de  retorno  se  o  campo  “error”  contém  a  string  “Resource  not  found” ")
        public void testarEndPointAtualizarClienteNaoExistente() throws Exception {
                // Arrange
                long idNaoExistente = 999L;
                String erroEsperado = "Resource not found";
                String mensagemDeErroEsperada = "Id not found " + idNaoExistente;
                String caminhoEsperado = "/clients/" + idNaoExistente;

                ClientDTO clientToUpdate = new ClientDTO(
                                idNaoExistente,
                                "Non Existent Client",
                                "99988877766",
                                1000.0,
                                Instant.parse("2000-01-01T10:00:00Z"),
                                0);

                String jsonRequest = objectMapper.writeValueAsString(clientToUpdate);

                Mockito.when(service.update(Mockito.eq(idNaoExistente), Mockito.any(ClientDTO.class)))
                        .thenThrow(new ResourceNotFoundException(mensagemDeErroEsperada));

                // Act
                ResultActions result = mockMvc.perform(put("/clients/{id}", idNaoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.timestamp").exists())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.error").value(erroEsperado))
                        .andExpect(jsonPath("$.message").value(mensagemDeErroEsperada))
                        .andExpect(jsonPath("$.path").value(caminhoEsperado));
        }

        @Test
        @Order(15)
        @DisplayName("Testar o findClientByChildrenGreaterThanEqualOrderByNameAsc")
        public void testarFindClientByChildrenGreaterThanEqualOrderByNameAsc() throws Exception {
                // Arrange
                int quantidadeFilhos = 2;
                int totalDeElementosEsperado = 5;
                int linhasPorPaginaEsperadas = 12;

                List<ClientDTO> listaClientes = new ArrayList<>();

                listaClientes.add(new ClientDTO(3L, "Clarice Lispector", "10919444522", 3800.0, Instant.parse("1960-04-13T07:50:00Z"), 2));
                listaClientes.add(new ClientDTO(1L, "Conceição Evaristo", "10619244881", 1500.0, Instant.parse("2020-07-13T20:50:00Z"), 2));
                listaClientes.add(new ClientDTO(2L, "Lázaro Ramos", "10619244881", 2500.0, Instant.parse("1996-12-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(11L, "Silvio Almeida", "10164334861", 4500.0, Instant.parse("1970-09-23T07:00:00Z"), 2));
                listaClientes.add(new ClientDTO(9L, "Yuval Noah Harari", "10619244881", 1500.0, Instant.parse("1956-09-23T07:00:00Z"), 0));

                PageRequest paginaEsperada = PageRequest.of(0, linhasPorPaginaEsperadas, Sort.by(Direction.ASC, "name"));

                Page<ClientDTO> paginaClientesMockada = new PageImpl<>(listaClientes, paginaEsperada, totalDeElementosEsperado);


               Mockito.when(service.findClientByChildrenGreaterThanEqualOrderByNameAsc(
                        Mockito.eq(quantidadeFilhos),
                        Mockito.any(PageRequest.class)
                        ))
                        .thenReturn(paginaClientesMockada);

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/byChildren/")
                        .param("children", String.valueOf(quantidadeFilhos))
                        .param("page", "0")
                        .param("linesPerPage", String.valueOf(linhasPorPaginaEsperadas))
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.totalElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.numberOfElements").value(totalDeElementosEsperado))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.content[0].name").value("Clarice Lispector"))
                        .andExpect(jsonPath("$.content[0].children").value(2))
                        .andExpect(jsonPath("$.content[1].name").value("Conceição Evaristo"))
                        .andExpect(jsonPath("$.content[1].children").value(2))
                        .andExpect(jsonPath("$.content[2].name").value("Lázaro Ramos"))
                        .andExpect(jsonPath("$.content[2].children").value(2))
                        .andExpect(jsonPath("$.content[3].name").value("Silvio Almeida"))
                        .andExpect(jsonPath("$.content[3].children").value(2))
                        .andExpect(jsonPath("$.content[4].name").value("Yuval Noah Harari"))
                        .andExpect(jsonPath("$.content[4].children").value(0));
        }
}
