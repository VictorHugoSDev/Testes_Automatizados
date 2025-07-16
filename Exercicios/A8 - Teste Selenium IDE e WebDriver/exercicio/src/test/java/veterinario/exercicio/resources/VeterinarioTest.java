package veterinario.exercicio.resources;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

@TestMethodOrder(OrderAnnotation.class) // Define que a ordem será por @Order
public class VeterinarioTest {

        private static WebDriver driver;
        private static WebDriverWait wait;

        @BeforeAll
        public static void setUp() {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                driver.get("http://localhost:8080/home");
        }

        // Teste: 01 - Cadastrar Veterinário
        @Test
        @Order(1) // Ordem: Criar
        public void test01CadastrarVeterinario() {
                // Define o tamanho da janela do navegador
                driver.manage().window().setSize(new Dimension(1075, 808));

                // Clica no botão "Adicionar"
                driver.findElement(By.cssSelector("a:nth-child(3) > .btn")).click();

                // Preenche o campo Nome
                WebElement nomeInput = driver.findElement(By.id("nome"));
                nomeInput.click();
                nomeInput.sendKeys("Victor");

                // Preenche o campo Email
                WebElement emailInput = driver.findElement(By.id("inputEmail"));
                emailInput.click();
                emailInput.sendKeys("victorhhugo@hotmail.com");

                // Preenche o campo Especialidade
                WebElement especialidadeInput = driver.findElement(By.id("inputEspecialidade"));
                especialidadeInput.click();
                especialidadeInput.sendKeys("grande");

                // Preenche o campo Salário
                WebElement salarioInput = driver.findElement(By.id("inputSalario"));
                salarioInput.click();
                salarioInput.sendKeys("10000");

                // Clica no botão "Cadastrar"
                driver.findElement(By.cssSelector(".btn")).click();

                // Asserts para verificar se o veterinário foi cadastrado corretamente
                // Usamos WebDriverWait para garantir que os elementos estejam visíveis após a
                // submissão do formulário
                wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span")));
                assertEquals("Victor",
                                driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span"))
                                                .getText());
                assertEquals("grande",
                                driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(3) > span"))
                                                .getText());
                assertEquals("victorhhugo@hotmail.com",
                                driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(4) > span"))
                                                .getText());
                assertEquals("10000.00",
                                driver.findElement(By.cssSelector("tr:nth-child(4) > td:nth-child(5) > span"))
                                                .getText());
        }

        // Teste: 02 - Alterar Veterinário
        @Test
        @Order(2) // Ordem: Alterar
        public void test02AlterarVeterinario() {
                // Define o tamanho da janela do navegador
                driver.manage().window().setSize(new Dimension(1077, 808));

                // Asserts para verificar os dados atuais antes da alteração
                wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("tr:nth-child(2) > td:nth-child(2) > span")));
                assertEquals("Conceição Evaristo",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(2) > span"))
                                                .getText());
                assertEquals("pequenos",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(3) > span"))
                                                .getText());
                assertEquals("conceicao@gmail.com",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(4) > span"))
                                                .getText());
                assertEquals("3500.00",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > span"))
                                                .getText());

                // Clica no botão de edição (lápis) da segunda linha
                driver.findElement(By.cssSelector("tr:nth-child(2) .btn-warning > .fa")).click();

                // Altera o campo Salário
                WebElement salarioInput = driver.findElement(By.id("inputSalario"));
                salarioInput.click();
                // Limpa o campo antes de digitar o novo valor
                salarioInput.clear();
                salarioInput.sendKeys("6000");

                // Clica no botão "Atualizar"
                driver.findElement(By.cssSelector(".btn")).click();

                // Assert para verificar se o salário foi atualizado
                wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("tr:nth-child(2) > td:nth-child(5) > span")));
                assertEquals("6000.00",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(5) > span"))
                                                .getText());
        }

        // Teste: 03 - Excluir Veterinário
        @Test
        @Order(3) // Ordem: Excluir (logo após alterar)
        public void test03ExcluirVeterinario() {
                // Define o tamanho da janela do navegador
                driver.manage().window().setSize(new Dimension(1079, 808));

                wait.until(ExpectedConditions
                                .visibilityOfElementLocated(
                                                By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span")));
                assertTrue(driver.findElements(By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span")).size() > 0,
                                "Elemento a ser excluído deve estar presente.");

                // Clica no botão de exclusão (lixeira) da quarta linha
                driver.findElement(By.cssSelector("tr:nth-child(4) .btn-danger > .fa")).click();

                // Asserta que o elemento não está mais presente após a exclusão
                // Usamos ExpectedConditions.invisibilityOfElementLocated para aguardar a
                // remoção
                wait.until(ExpectedConditions
                                .invisibilityOfElementLocated(
                                                By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span")));
                assertFalse(driver.findElements(By.cssSelector("tr:nth-child(4) > td:nth-child(2) > span")).size() > 0,
                                "Elemento excluído não deve mais estar presente.");
        }

        // Teste: 05 - Pesquisar Veterinário
        @Test
        @Order(4) // Ordem: Consultar
        public void test05PesquisarVeterinario() {
                // Define o tamanho da janela do navegador
                driver.manage().window().setSize(new Dimension(1080, 808));

                // Clica no botão "Consultar" na página inicial
                driver.findElement(By.cssSelector("a:nth-child(4) > .btn")).click();

                // Preenche o campo de busca por nome
                WebElement nomeInput = driver.findElement(By.id("nome"));
                nomeInput.click();
                nomeInput.sendKeys("Conceição Evaristo");

                // Clica no botão "Consultar" na página de pesquisa
                driver.findElement(By.cssSelector(".btn")).click();

                // Asserta que o nome pesquisado aparece na tabela de resultados
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td:nth-child(2) > span")));
                assertEquals("Conceição Evaristo",
                                driver.findElement(By.cssSelector("td:nth-child(2) > span")).getText());
        }

        // Teste: 04 - Listar Veterinário
        @Test
        @Order(5)
        public void test04ListarVeterinario() {
                driver.manage().window().setSize(new Dimension(1077, 808));

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("th:nth-child(2)")));
                assertTrue(driver.findElements(By.cssSelector("th:nth-child(2)")).size() > 0,
                                "Cabeçalho 'Nome' deve estar presente.");
                assertTrue(driver.findElements(By.cssSelector("tr:nth-child(2) > td:nth-child(2) > span")).size() > 0,
                                "Primeiro veterinário na lista deve estar presente.");
                assertTrue(driver.findElements(By.cssSelector("tr:nth-child(3) > td:nth-child(2) > span")).size() > 0,
                                "Segundo veterinário na lista deve estar presente.");

                assertEquals("Nome", driver.findElement(By.cssSelector("th:nth-child(2)")).getText());
                assertEquals("Conceição Evaristo",
                                driver.findElement(By.cssSelector("tr:nth-child(2) > td:nth-child(2) > span"))
                                                .getText());
                assertEquals("Erica Queiroz Pinto",
                                driver.findElement(By.cssSelector("tr:nth-child(3) > td:nth-child(2) > span"))
                                                .getText());
        }

        @AfterAll
        public static void tearDown() {
                if (driver != null) {
                        driver.quit();
                }
        }

}
