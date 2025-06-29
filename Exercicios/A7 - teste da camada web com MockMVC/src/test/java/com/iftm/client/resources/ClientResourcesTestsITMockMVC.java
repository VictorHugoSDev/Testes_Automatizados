package com.iftm.client.resources;

import java.time.Instant;

import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import com.iftm.client.entities.Client;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ClientResourcesTestsITMockMVC {
        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @Order(1)
        @DisplayName("Verificar se o endpoint get/clients/ retorna todos os clientes existentes")
        public void testarEndPointListarTodosClientesRetornaCorreto() throws Exception {
                // Arrange

                int quantidadeTotalClientes = 12;
                int tamanhoPaginaPadrao = 12;

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/")
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.content").exists())
                        .andExpect(jsonPath("$.content").isArray())
                        .andExpect(jsonPath("$.content.length()").value(quantidadeTotalClientes))
                        .andExpect(jsonPath("$.size").value(tamanhoPaginaPadrao))
                        .andExpect(jsonPath("$.totalElements").value(quantidadeTotalClientes))
                        .andExpect(jsonPath("$.totalPages").value(1))
                        .andExpect(jsonPath("$.number").value(0))
                        .andExpect(jsonPath("$.first").value(true))
                        .andExpect(jsonPath("$.last").value(true))
                        .andExpect(jsonPath("$.totalPages").value(1))
                        .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                        .andExpect(jsonPath("$.pageable.pageSize").value(tamanhoPaginaPadrao))
                        .andExpect(jsonPath("$.content[?(@.id == 1)]").exists())
                        .andExpect(jsonPath("$.content[?(@.id == 13)]").doesNotExist())
                        .andExpect(jsonPath("$.content[?(@.name == '%s')]", "Conceição Evaristo").exists());
        }

        @Test
        @Order(2)
        @DisplayName("Verificar se o endpoint get/clients/{id} retorna o cliente correto")
        public void testarEndPointListarClienteCorrete() throws Exception {
                // Arrange
                long existingId = 1L;
                String nomeEsperado = "Conceição Evaristo";
                String cpfEsperado = "10619244881";
                Double rendaEsperada = 1500.0;
                String dataNascimentoEsperada = "2020-07-13T20:50:00Z";
                Integer filhosEsperados = 2;

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/id/{id}", existingId)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                resultados
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(existingId))
                        .andExpect(jsonPath("$.name").value(nomeEsperado))
                        .andExpect(jsonPath("$.cpf").value(cpfEsperado))
                        .andExpect(jsonPath("$.income").value(rendaEsperada))
                        .andExpect(jsonPath("$.birthDate").value(dataNascimentoEsperada))
                        .andExpect(jsonPath("$.children").value(filhosEsperados));
        }

        @Test
        @Order(3)
        @DisplayName("Verificar se o endpoint get/clients/{id} retorna 404")
        public void testarEndPointListarClienteInexistenteRetorna404() throws Exception {
                // Arrange
                long nonExistingId = 100L;
                String erroEsperado = "Resource not found";
                String mensagemEsperada = "Entity not found";
                String caminhoEsperado = "/clients/id/" + nonExistingId;

                // Act
                ResultActions resultados = mockMvc.perform(get("/clients/id/{id}", nonExistingId)
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
        @Order(4)
        @DisplayName("Verificar se o cliente tem o income correto e o status 200 OK")
        public void testarEndPointListarClienteComIncomeCorreto() throws Exception {
                // Arrange
                Double rendaEsperada = 2500.0;
                int totalDeElementosEsperado = 3;
                int linhasPorPaginaEsperadas = 12;

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
                        .andExpect(jsonPath("$.last").value(true));
        }

        @Test
        @Order(5)
        @DisplayName("Teste para não retornar clientes")
        public void testarEndPointListarClientesComRendaInexistente() throws Exception {
                // Arrange
                Double rendaInexistente = 9999.0;
                int totalDeElementosEsperado = 0;
                int linhasPorPaginaEsperadas = 12;

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
        @DisplayName("eve retornar clientes com CPF que contêm o padrão e status 200 OK")
        public void testarEndPointListarClientesComCPFLikeCorreto() throws Exception {
                // Arrange

                String parametroCPF = "61";
                int totalDeElementosEsperado = 7;
                int linhasPorPaginaEsperadas = 12;

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
                int totalDeElementosEsperado = 0;
                int linhasPorPaginaEsperadas = 12;

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

                String jsonRequest = objectMapper.writeValueAsString(novoCliente);

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
                String caminhoEsperado = "/clients/";

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
                        .andExpect(jsonPath("$.path").value(caminhoEsperado + idNaoExistente));
        }

        @Test
        @Order(12)
        @DisplayName("Retornar 'ok' (código 200) e o JSON do produto atualizado para um id existente")
        public void testarEndPointAtualizarClienteExistente() throws Exception {
                // Arrange
                long idExistente = 1L;
                String nomeAtualizado = "Cliente Atualizado";
                Double rendaAtualizada = 6000.0;

                ClientDTO clienteAtualizado = new ClientDTO(new Client(
                                idExistente,
                                nomeAtualizado,
                                "12345678901",
                                rendaAtualizada,
                                Instant.parse("1990-01-01T10:00:00Z"),
                                2));

                String jsonRequest = objectMapper.writeValueAsString(clienteAtualizado);

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
                        .andExpect(jsonPath("$.income").value(rendaAtualizada));
        }

        @Test
        @Order(11)
        @DisplayName("retornar  “not  found”  (código  404)  quando  o  id  não  existir.  Fazer  uma  assertion  para "
                        +
                        " verificar  no  json  de  retorno  se  o  campo  “error”  contém  a  string  “Resource  not  found” ")
        public void testarEndPointAtualizarClienteNaoExistente() throws Exception {
                // Arrange
                long idNaoExistente = 999L;
                String mensagemDeErroEsperada = "Id not found " + idNaoExistente;

                // Criação do DTO com dados fictícios (o conteúdo em si não importa para o 404)
                ClientDTO clientToUpdate = new ClientDTO(
                                idNaoExistente,
                                "Non Existent Client",
                                "99988877766",
                                1000.0,
                                Instant.parse("2000-01-01T10:00:00Z"),
                                0);
                String jsonRequest = objectMapper.writeValueAsString(clientToUpdate);

                // Act
                ResultActions result = mockMvc.perform(put("/clients/{id}", idNaoExistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON));

                // Assert
                result.andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value(mensagemDeErroEsperada));
        }

        @Test
        @Order(15)
        @DisplayName("Testar o findClientByChildrenGreaterThanEqualOrderByNameAsc")
        public void testarFindClientByChildrenGreaterThanEqualOrderByNameAsc() throws Exception {
                // Arrange
                int quantidadeFilhos = 2;
                int totalDeElementosEsperado = 5;
                int linhasPorPaginaEsperadas = 12;

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
                        .andExpect(jsonPath("$.last").value(true));
        }
}
