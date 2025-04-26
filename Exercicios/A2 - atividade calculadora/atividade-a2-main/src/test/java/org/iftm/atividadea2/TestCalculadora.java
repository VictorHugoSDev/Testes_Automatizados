package org.iftm.atividadea2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TestCalculadora {
    
    @Test
    public void testeConstrutorSemParametro(){
        Calculadora calc = new Calculadora();
        int resultado = calc.getMemoria();
        assertEquals(0, resultado);
    }

    @Test
    public void testeConstrutorParametroPositivo(){
        Calculadora calc = new Calculadora(3);
        int resultado = calc.getMemoria();
        assertEquals(3, resultado);
    }

    @Test
    public void testeConstrutorParametroNegativo(){
        Calculadora calc = new Calculadora(-3);
        int resultado = calc.getMemoria();
        assertEquals(-3, resultado);
    }

    @Test
    public void testeSomarPositivo(){
        Calculadora calc = new Calculadora(3);
        calc.somar(3);
        int resultado = calc.getMemoria();
        assertEquals(6, resultado);
    }

    @Test
    public void testeSomarNegativo(){
        Calculadora calc = new Calculadora(3);
        calc.somar(-3);
        int resultado = calc.getMemoria();
        assertEquals(0, resultado);
    }

    @Test
    public void testeSubtrairPositivo(){
        Calculadora calc = new Calculadora(3);
        calc.subtrair(3);
        int resultado = calc.getMemoria();
        assertEquals(0, resultado);
    }

    @Test
    public void testeSubtrairNegativo(){
        Calculadora calc = new Calculadora(3);
        calc.subtrair(-3);
        int resultado = calc.getMemoria();
        assertEquals(6, resultado);
    }

    @Test
    public void multiplicarPositivo(){
        Calculadora calc = new Calculadora(3);
        calc.multiplicar(3);
        int resultado = calc.getMemoria();
        assertEquals(9, resultado);
    }

    @Test
    public void multiplicarNegativo(){
        Calculadora calc = new Calculadora(3);
        calc.multiplicar(-3);
        int resultado = calc.getMemoria();
        assertEquals(-9, resultado);
    }

    @Test
    public void dividirPorZero() throws Exception{
        Calculadora calc = new Calculadora(3);
        try {
            calc.dividir(0);
        } catch (Exception e) {
            assertEquals("Divisão por zero!!!", e.getMessage());
        }
    }

    @Test
    public void dividirPositivo() throws Exception{
        Calculadora calc = new Calculadora(3);
        calc.dividir(3);
        int resultado = calc.getMemoria();
        assertEquals(1, resultado);
    }

    @Test
    public void dividirNegativo() throws Exception{
        Calculadora calc = new Calculadora(3);
        calc.dividir(-3);
        int resultado = calc.getMemoria();
        assertEquals(-1, resultado);
    }

    @Test
    public void exponenciarPorUm() throws Exception{
        Calculadora calc = new Calculadora(3);
        calc.exponenciar(1);
        int resultado = calc.getMemoria();
        assertEquals(3, resultado);
    }

    @Test
    public void exponenciarPorDez() throws Exception{
        Calculadora calc = new Calculadora(3);
        calc.exponenciar(10);
        int resultado = calc.getMemoria();
        assertEquals(59049, resultado);
    }

    @Test
    public void exponenciarPor20() throws Exception{
        Calculadora calc = new Calculadora(3);
        try {
            calc.exponenciar(20);
        } catch (Exception e) {
            assertEquals("Expoente incorreto, valor máximo é 10.", e.getMessage());
        }
    }

    @Test
    public void zerarMemoria() throws Exception{
        Calculadora calc = new Calculadora(3);
        calc.zerarMemoria();
        int resultado = calc.getMemoria();
        assertEquals(0, resultado);
    }
}
