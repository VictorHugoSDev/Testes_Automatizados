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
}
