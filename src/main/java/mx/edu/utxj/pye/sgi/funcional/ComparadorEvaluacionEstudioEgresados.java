/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionEstudioEgresados implements Comparador<EvaluacionEstudioEgresadosResultados> {

    @Override
    public boolean isCompleto(EvaluacionEstudioEgresadosResultados resultado) {
        if (resultado.getR60() == null) {
            return false;
        } else if (resultado.getR60() == 7) {
            if (obligatoriasGeneralesNull(resultado) && resultado.getR61() == null) {
                return false;
            } else {
                return (obligatoriasGeneralesNotNull(resultado) && resultado.getR61() != null);
            }
        } else if (resultado.getR60() != 7) {
            if (resultado.getR63() == null) {
                return false;
            } else {
                switch (resultado.getR63()) {
                    case 1:
                        if (obligatoriasGeneralesNull(resultado) && obligatoriasp60not7Null(resultado)) {
                            return false;
                        } else {
                            return (obligatoriasGeneralesNotNull(resultado) && obligatoriasp60not7NotNull(resultado));
                        }
                    case 2:
                        if (obligatoriasGeneralesNull(resultado) && obligatoriasp60not7Null(resultado) && resultado.getR64a() == null && resultado.getR64b() == null && resultado.getR65() == null) {
                            return false;
                        } else {
                            return !(obligatoriasGeneralesNotNull(resultado) && obligatoriasp60not7NotNull(resultado) && resultado.getR64a() != null && resultado.getR64b() != null && resultado.getR65() != null);
                        }
                }
            }
        }
        return true;
    }

    public boolean obligatoriasGeneralesNull(EvaluacionEstudioEgresadosResultados resultado) {
        return (resultado.getR1() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR10() == null
                || resultado.getR11() == null
                || resultado.getR15() == null
                || resultado.getR16() == null
                || resultado.getR17() == null
                || resultado.getR18() == null
                || resultado.getR19() == null
                || resultado.getR34() == null
                || resultado.getR42() == null
                || resultado.getR43() == null
                || resultado.getR60() == null
                || resultado.getR91a() == null
                || resultado.getR91b() == null
                || resultado.getR91c() == null
                || resultado.getR91d() == null
                || resultado.getR91e() == null
                || resultado.getR91f() == null
                || resultado.getR91g() == null
                || resultado.getR91h() == null
                || resultado.getR91i() == null
                || resultado.getR91j() == null
                || resultado.getR91k() == null
                || resultado.getR91l() == null
                || resultado.getR91m() == null
                || resultado.getR92() == null
                || resultado.getR93() == null
                || resultado.getR94() == null);
    }

    public boolean obligatoriasGeneralesNotNull(EvaluacionEstudioEgresadosResultados resultado) {
        return !(resultado.getR1().trim().isEmpty()
                || resultado.getR4().trim().isEmpty()
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR10().trim().isEmpty()
                || resultado.getR11().trim().isEmpty()
                || resultado.getR15().trim().isEmpty()
                || resultado.getR16().trim().isEmpty()
                || resultado.getR17().trim().isEmpty()
                || resultado.getR18().trim().isEmpty()
                || resultado.getR19().trim().isEmpty()
                || resultado.getR34().trim().isEmpty()
                || resultado.getR42().trim().isEmpty()
                || resultado.getR43().trim().isEmpty()
                || resultado.getR60() == null
                || resultado.getR91a() == null
                || resultado.getR91b() == null
                || resultado.getR91c() == null
                || resultado.getR91d() == null
                || resultado.getR91e() == null
                || resultado.getR91f() == null
                || resultado.getR91g() == null
                || resultado.getR91h() == null
                || resultado.getR91i() == null
                || resultado.getR91j() == null
                || resultado.getR91k() == null
                || resultado.getR91l() == null
                || resultado.getR91m() == null
                || resultado.getR92() == null
                || resultado.getR93() == null
                || resultado.getR94().length() < 15);
    }

    public boolean obligatoriasp60not7Null(EvaluacionEstudioEgresadosResultados resultado) {
        return (resultado.getR62() == null
                || resultado.getR63() == null
                || resultado.getR66() == null
                || resultado.getR67a() == null
                || resultado.getR67b() == null
                || resultado.getR67c() == null
                || resultado.getR67d() == null
                || resultado.getR67e() == null
                || resultado.getR67f() == null
                || resultado.getR68() == null
                || resultado.getR71() == null
                || resultado.getR72() == null
                || resultado.getR78() == null
                || resultado.getR79() == null
                || resultado.getR80() == null
                || resultado.getR81() == null
                || resultado.getR82() == null
                || resultado.getR83() == null
                || resultado.getR84() == null
                || resultado.getR85() == null
                || resultado.getR86a() == null
                || resultado.getR86b() == null
                || resultado.getR86c() == null
                || resultado.getR86d() == null
                || resultado.getR86e() == null
                || resultado.getR86f() == null
                || resultado.getR86g() == null
                || resultado.getR86h() == null
                || resultado.getR86i() == null
                || resultado.getR86j() == null
                || resultado.getR86k() == null
                || resultado.getR86l() == null
                || resultado.getR87() == null
                || resultado.getR88() == null
                || resultado.getR89() == null
                || resultado.getR90aa() == null
                || resultado.getR90ab() == null
                || resultado.getR90ba() == null
                || resultado.getR90bb() == null
                || resultado.getR90ca() == null
                || resultado.getR90cb() == null
                || resultado.getR90da() == null
                || resultado.getR90db() == null
                || resultado.getR90ea() == null
                || resultado.getR90eb() == null
                || resultado.getR90fa() == null
                || resultado.getR90fb() == null
                || resultado.getR90ga() == null
                || resultado.getR90gb() == null
                || resultado.getR90ha() == null
                || resultado.getR90hb() == null
                || resultado.getR90ia() == null
                || resultado.getR90ib() == null
                || resultado.getR90ja() == null
                || resultado.getR90jb() == null
                || resultado.getR90ka() == null
                || resultado.getR90kb() == null
                || resultado.getR915a() == null
                || resultado.getR915b() == null
                || resultado.getR915c() == null
                || resultado.getR915d() == null
                || resultado.getR915e() == null
                || resultado.getR915f() == null
                || resultado.getR915g() == null
                || resultado.getR915h() == null
                || resultado.getR915i() == null
                || resultado.getR915j() == null);
    }

    public boolean obligatoriasp60not7NotNull(EvaluacionEstudioEgresadosResultados resultado) {
        return !(resultado.getR62() == null
                || resultado.getR63() == null
                || resultado.getR66() == null
                || resultado.getR67a() == null
                || resultado.getR67b() == null
                || resultado.getR67c() == null
                || resultado.getR67d() == null
                || resultado.getR67e() == null
                || resultado.getR67f() == null
                || resultado.getR68().trim().isEmpty()
                || resultado.getR71().trim().isEmpty()
                || resultado.getR72().trim().isEmpty()
                || resultado.getR78() == null
                || resultado.getR79() == null
                || resultado.getR80() == null
                || resultado.getR81() == null
                || resultado.getR82() == null
                || resultado.getR83() == null
                || resultado.getR84() == null
                || resultado.getR85() == null
                || resultado.getR86a() == null
                || resultado.getR86b() == null
                || resultado.getR86c() == null
                || resultado.getR86d() == null
                || resultado.getR86e() == null
                || resultado.getR86f() == null
                || resultado.getR86g() == null
                || resultado.getR86h() == null
                || resultado.getR86i() == null
                || resultado.getR86j() == null
                || resultado.getR86k() == null
                || resultado.getR86l() == null
                || resultado.getR87() == null
                || resultado.getR88() == null
                || resultado.getR89() == null
                || resultado.getR90aa() == null
                || resultado.getR90ab() == null
                || resultado.getR90ba() == null
                || resultado.getR90bb() == null
                || resultado.getR90ca() == null
                || resultado.getR90cb() == null
                || resultado.getR90da() == null
                || resultado.getR90db() == null
                || resultado.getR90ea() == null
                || resultado.getR90eb() == null
                || resultado.getR90fa() == null
                || resultado.getR90fb() == null
                || resultado.getR90ga() == null
                || resultado.getR90gb() == null
                || resultado.getR90ha() == null
                || resultado.getR90hb() == null
                || resultado.getR90ia() == null
                || resultado.getR90ib() == null
                || resultado.getR90ja() == null
                || resultado.getR90jb() == null
                || resultado.getR90ka() == null
                || resultado.getR90kb() == null
                || resultado.getR915a() == null
                || resultado.getR915b() == null
                || resultado.getR915c() == null
                || resultado.getR915d() == null
                || resultado.getR915e() == null
                || resultado.getR915f() == null
                || resultado.getR915g() == null
                || resultado.getR915h() == null
                || resultado.getR915i() == null
                || resultado.getR915j() == null);
    }
}
