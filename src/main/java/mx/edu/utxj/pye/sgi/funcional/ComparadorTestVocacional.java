/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestVocacional;

/**
 *
 * @author UTXJ
 */
public class ComparadorTestVocacional implements Comparador<TestVocacional>{

    @Override
    public boolean isCompleto(TestVocacional resultado) {
        if(resultado.getCarreraInteresOu() == null
                || "".equals(resultado.getCarreraInteresOu())
                || resultado.getR1() == null
                || resultado.getR2() == null    
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9() == null
                || resultado.getR10() == null
                || resultado.getR11() == null
                || resultado.getR12() == null
                || resultado.getR13() == null
                || resultado.getR14() == null
                || resultado.getR15() == null
                || resultado.getR16() == null
                || resultado.getR17() == null
                || resultado.getR18() == null
                || resultado.getR19() == null
                || resultado.getR20() == null
                || resultado.getR21() == null
                || resultado.getR22() == null
                || resultado.getR23() == null
                || resultado.getR24() == null
                || resultado.getR25() == null
                || resultado.getR26() == null
                || resultado.getR27() == null
                || resultado.getR28() == null
                || resultado.getR29() == null
                || resultado.getR30() == null
                || resultado.getR31() == null
                || resultado.getR32() == null
                || resultado.getR33() == null
                || resultado.getR34() == null
                || resultado.getR35() == null
                || resultado.getR36() == null
                || resultado.getR37() == null
                || resultado.getR38() == null
                || resultado.getR39() == null
                || resultado.getR40() == null
                || resultado.getR41() == null
                || resultado.getR42() == null
                || resultado.getR43() == null
                || resultado.getR44() == null
                || resultado.getR45() == null
                || resultado.getR46() == null
                || resultado.getR47() == null
                || resultado.getR48() == null
                || resultado.getR49() == null
                || resultado.getR50() == null
                || resultado.getR51() == null
                || resultado.getR52() == null
                || resultado.getR53() == null
                || resultado.getR54() == null
                || resultado.getR55() == null
                || resultado.getR56() == null
                || resultado.getR57() == null
                || resultado.getR58() == null
                || resultado.getR59() == null
                || resultado.getR60() == null
                || resultado.getR61() == null
                || resultado.getR62() == null
                || resultado.getR63() == null
                || resultado.getR64() == null
                || resultado.getR65() == null
                || resultado.getR66() == null
                || resultado.getR67() == null
                || resultado.getR68() == null
                || resultado.getR69() == null
                || resultado.getR70() == null
                || resultado.getR71() == null
                || resultado.getR72() == null
                || resultado.getR73() == null
                || resultado.getR74() == null
                || resultado.getR75() == null
                || resultado.getR76() == null
                || resultado.getR77() == null
                || resultado.getR78() == null
                || resultado.getR79() == null
                || resultado.getR80() == null
                || resultado.getR81() == null
                || resultado.getR82() == null
                || resultado.getR83() == null
                || resultado.getR84() == null
                || resultado.getR85() == null
                || resultado.getR86() == null
                || resultado.getR87() == null
                || resultado.getR88() == null)
        {
            return false;
        }
        
        return !(resultado.getCarreraInteresOu() == null
                || "".equals(resultado.getCarreraInteresOu())
                || resultado.getR1() == null
                || resultado.getR2() == null    
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9() == null
                || resultado.getR10() == null
                || resultado.getR11() == null
                || resultado.getR12() == null
                || resultado.getR13() == null
                || resultado.getR14() == null
                || resultado.getR15() == null
                || resultado.getR16() == null
                || resultado.getR17() == null
                || resultado.getR18() == null
                || resultado.getR19() == null
                || resultado.getR20() == null
                || resultado.getR21() == null
                || resultado.getR22() == null
                || resultado.getR23() == null
                || resultado.getR24() == null
                || resultado.getR25() == null
                || resultado.getR26() == null
                || resultado.getR27() == null
                || resultado.getR28() == null
                || resultado.getR29() == null
                || resultado.getR30() == null
                || resultado.getR31() == null
                || resultado.getR32() == null
                || resultado.getR33() == null
                || resultado.getR34() == null
                || resultado.getR35() == null
                || resultado.getR36() == null
                || resultado.getR37() == null
                || resultado.getR38() == null
                || resultado.getR39() == null
                || resultado.getR40() == null
                || resultado.getR41() == null
                || resultado.getR42() == null
                || resultado.getR43() == null
                || resultado.getR44() == null
                || resultado.getR45() == null
                || resultado.getR46() == null
                || resultado.getR47() == null
                || resultado.getR48() == null
                || resultado.getR49() == null
                || resultado.getR50() == null
                || resultado.getR51() == null
                || resultado.getR52() == null
                || resultado.getR53() == null
                || resultado.getR54() == null
                || resultado.getR55() == null
                || resultado.getR56() == null
                || resultado.getR57() == null
                || resultado.getR58() == null
                || resultado.getR59() == null
                || resultado.getR60() == null
                || resultado.getR61() == null
                || resultado.getR62() == null
                || resultado.getR63() == null
                || resultado.getR64() == null
                || resultado.getR65() == null
                || resultado.getR66() == null
                || resultado.getR67() == null
                || resultado.getR68() == null
                || resultado.getR69() == null
                || resultado.getR70() == null
                || resultado.getR71() == null
                || resultado.getR72() == null
                || resultado.getR73() == null
                || resultado.getR74() == null
                || resultado.getR75() == null
                || resultado.getR76() == null
                || resultado.getR77() == null
                || resultado.getR78() == null
                || resultado.getR79() == null
                || resultado.getR80() == null
                || resultado.getR81() == null
                || resultado.getR82() == null
                || resultado.getR83() == null
                || resultado.getR84() == null
                || resultado.getR85() == null
                || resultado.getR86() == null
                || resultado.getR87() == null
                || resultado.getR88() == null);
    }
}
