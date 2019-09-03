package mx.edu.utxj.pye.sgi.ejb.finanzasRegistrodePagos;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoAlumnoFinanzas;
import mx.edu.utxj.pye.sgi.ejb.EJBAdimEstudianteBase;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.AlumnoFinanzas;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.AlumnoFinanzasPK;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Personafinanzas;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Vistapagosprimercuatrimestre;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

@Stateful
public class registroDePagosSE  implements EjbFinanzasRegistroPagos{
    @EJB Facade facade;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EJBAdimEstudianteBase ejbAdimEstudianteBase;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = facade.getEntityManager();
    }
    @Override
    public ResultadoEJB<Vistapagosprimercuatrimestre> getRegistroByCurp(String curp) {
        try{
            Vistapagosprimercuatrimestre registro= new Vistapagosprimercuatrimestre();
            if(curp==null){ ResultadoEJB.crearErroneo(2,registro,"El CURP no debe ser nulo");}
            //TODO: Se busca el registro del pago, por curp
            Vistapagosprimercuatrimestre c= em.createNamedQuery("Vistapagosprimercuatrimestre.findByCurp", Vistapagosprimercuatrimestre.class)
            .setParameter("curp",curp)
            .getResultStream()
            .findFirst()
            .orElse(null);
            if (c == null) {
                return ResultadoEJB.crearErroneo(2, registro, "No existe registro de pago del primer cuatrimestre del aspirante.");
            } else  {
                registro = c;
                return ResultadoEJB.crearCorrecto(registro, "Se econtro registro del pago");
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se puedo realizar la consulta del pago del aspirante (EjbFinanzasRegistroPagos-getResgitrobyCurp)", e, Vistapagosprimercuatrimestre.class);

        }
    }

    @Override
    public ResultadoEJB<Vistapagosprimercuatrimestre> editRegistro(Vistapagosprimercuatrimestre registro) {
        try{
            em.merge(registro);
            return ResultadoEJB.crearCorrecto(registro,"Se ha modiificado el registro con exito");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la comprobación del pago del Aspirante(EjbFinanzasRegistroPago(comprobarPago))", e, Vistapagosprimercuatrimestre.class);
        }
    }

    @Override
    public ResultadoEJB<Vistapagosprimercuatrimestre> saveCambios(dtoAlumnoFinanzas estudiante) {
        try{
            System.out.println("Estudiante que recibe en finanzas!" + estudiante.getMatricula());
            Vistapagosprimercuatrimestre registro = new Vistapagosprimercuatrimestre();
            AlumnoFinanzas alumnoFinanzas = new AlumnoFinanzas();
            AlumnoFinanzasPK alumnoFinanzasPK = new AlumnoFinanzasPK();
                //TODO: Se obtiene el registro de pago por CURP
                ResultadoEJB<Vistapagosprimercuatrimestre> ResRegistro = getRegistroByCurp(estudiante.getCurp());
                if(ResRegistro.getCorrecto()==true){
                    //TODO:Obtenemos a la persona por curp en la base de Finanzas
                    Personafinanzas personafinanzas = em.createNamedQuery("Personafinanzas.findByCurp",Personafinanzas.class)
                            .setParameter("curp",estudiante.getCurp())
                            .getResultStream()
                            .findFirst()
                            .orElse(null);
                    System.out.println("Obtuvo persona finanzas" + personafinanzas);
                    if(personafinanzas!=null){
                        //TODO: Se buscan las siglas del area, segun las que tiene asignadas el estudiante
                        AreasUniversidad areas = ejbProcesoInscripcion.buscaAreaByClave(estudiante.getEstudianteCE().getCarrera());
                        System.out.println("Siglas" + areas.getSiglas());
                        //TODO: Se mandan los valores al dto
                        estudiante.setSiglas(areas.getSiglas());
                        estudiante.setPeriodo(estudiante.getEstudianteCE().getPeriodo());
                        //TODO:Se llena la entidad del estudiante de finanzas para crearlo
                        alumnoFinanzas.setSiglas(areas.getSiglas());
                        alumnoFinanzas.setCurp(personafinanzas);
                        alumnoFinanzasPK.setMatricula(estudiante.getEstudianteCE().getMatricula());
                        alumnoFinanzasPK.setPeriodo(estudiante.getEstudianteCE().getPeriodo());
                        alumnoFinanzas.setAlumnoFinanzasPK(alumnoFinanzasPK);
                        System.out.println("Alumno Finanzas" + alumnoFinanzas);
                        em.persist(alumnoFinanzas);
                        //TODO: PROCEDE A EDITAR EL REGISTRO DE PAGO
                        registro = ResRegistro.getValor();
                        registro.setMatricula(estudiante.getMatricula());
                        registro.setSiglas(estudiante.getSiglas());
                        registro.setPeriodo(estudiante.getPeriodo());
                        ResultadoEJB<Vistapagosprimercuatrimestre> editRegistro= editRegistro(registro);
                        if(editRegistro.getCorrecto()!=true){return ResultadoEJB.crearErroneo(3,registro,"No se pudo actualizar el pago");}
                        else {return ResultadoEJB.crearCorrecto(registro,"Se ha actualizado el registro");}

                    }else {return ResultadoEJB.crearErroneo(2,registro,"No se encontro a la persona en la base de finanzas");}

                }else {return ResultadoEJB.crearErroneo(2,registro,"No existe registro");}

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar el proceso de actulizacion del registro de pago(EjbFinanzasRegistroPago(saveCambios))", e, Vistapagosprimercuatrimestre.class);

        }
    }


    /*
    @Override
    public ResultadoEJB<dtoAlumnoFinanzas> saveAlumnoFinanzas(dtoAlumnoFinanzas estudiante) {
        try{
            System.out.println("Estudiante que recibe en guarda Estudiante" + estudiante.getMatricula() + " "+estudiante.getPeriodo());
            dtoAlumnoFinanzas dtoAlumnoFinanzas = new dtoAlumnoFinanzas();
            AlumnoFinanzas alumnoFinanzas = new AlumnoFinanzas();
            AlumnoFinanzasPK alumnoFinanzasPK  = new AlumnoFinanzasPK();
            if(estudiante==null){return ResultadoEJB.crearErroneo(2,dtoAlumnoFinanzas,"El estudiante no debe ser nulo");}
            else {
                //TODO: Obentemos a la persona de la base de finanzas por la curp del estudiante de CE
                Personafinanzas personafinanzas = facade.getEntityManager().createNamedQuery("Personafinanzas.findByCurp",Personafinanzas.class)
                        .setParameter("curp",estudiante.getCurp())
                        .getResultStream()
                        .findFirst()
                        .orElse(null);
                System.out.println("Obtuvo persona finanzas" + personafinanzas);
                if(personafinanzas!=null){
                    //TODO: Se buscan las siglas del area, segun las que tiene asignadas el estudiante
                    AreasUniversidad areas = ejbProcesoInscripcion.buscaAreaByClave(estudiante.getCarrera());
                    System.out.println("Siglas" + areas.getSiglas());
                    //TODO:Se llena la entidad del estudiante de finanzas para crearlo
                    alumnoFinanzas.setSiglas(areas.getSiglas());
                    alumnoFinanzas.setCurp(personafinanzas);
                    alumnoFinanzasPK.setMatricula(estudiante.getMatricula());
                    alumnoFinanzasPK.setPeriodo(estudiante.getPeriodo());
                    alumnoFinanzas.setAlumnoFinanzasPK(alumnoFinanzasPK);
                    System.out.println("Alumno Finanzas" + alumnoFinanzas);
                    facade.create(alumnoFinanzas);
                    return ResultadoEJB.crearCorrecto(dtoAlumnoFinanzas,"Se ha creado al alumno en la base de finanzas");
                }
                else {return ResultadoEJB.crearErroneo(3,dtoAlumnoFinanzas,"No existe persona en la base de finanzas");}

            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo crear al Estudiante en la base de Finanzas(EjbFinanzasRegistroPago(saveAlumno))", e, dtoAlumnoFinanzas.class);
        }
    }
    */


}
