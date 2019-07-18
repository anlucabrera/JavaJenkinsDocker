package mx.edu.utxj.pye.sgi.ejb.finanzasRegistrodePagos;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAlumnoFinanzas;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.AlumnoFinanzas;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Vistapagosprimercuatrimestre;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;

import javax.ejb.Local;

@Local
public interface EjbFinanzasRegistroPagos {
    /**
     * Obtiene el registro por CURP
     * @param curp Curp del Aspirante
     * @return Resultado del proceso
     */


    public ResultadoEJB<Vistapagosprimercuatrimestre> getRegistroByCurp(String curp);
    /**
     * Guarda un estudiante de control escolar, en la base de finanzas
     * @param estudiante
     * @return
     */
    //public ResultadoEJB<dtoAlumnoFinanzas> saveAlumnoFinanzas(dtoAlumnoFinanzas estudiante);

    /**
     * Edita el registro del aspirante, agregandole al registro la matricula, carrera y el periodo. Esto para el historial de pagos del estudiante
     *
     * @return Resultado del proceso(Entidad Editada)
     */
    public  ResultadoEJB<Vistapagosprimercuatrimestre> editRegistro( Vistapagosprimercuatrimestre registro);

    public  ResultadoEJB<Vistapagosprimercuatrimestre> saveCambios(dtoAlumnoFinanzas estudiante);








}
