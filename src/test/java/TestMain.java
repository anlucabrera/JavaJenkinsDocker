
import mx.edu.utxj.pye.javajenkisdocker.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Desarrollo
 */
public class TestMain {
    @Test
    public void testInputIsAnEvenNumber(){
        Assertions.assertTrue(Main.checkIfInputIsAnEvenNumber(22)); //asertion
    }
}
