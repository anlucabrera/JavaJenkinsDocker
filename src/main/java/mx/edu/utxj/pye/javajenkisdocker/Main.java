/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.javajenkisdocker;

/**
 *
 * @author Desarrollo
 */
public class Main {
    public static void main(String[] args) {
        System.out.println(checkIfInputIsAnEvenNumber(122));//Testeo del metodo main
    }
    
    public static boolean checkIfInputIsAnEvenNumber(int number) {
        return number %2==0;
    }
}
