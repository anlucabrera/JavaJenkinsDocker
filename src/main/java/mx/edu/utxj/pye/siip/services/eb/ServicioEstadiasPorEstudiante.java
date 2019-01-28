/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.eb;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import edu.mx.utxj.pye.seut.util.collection.SerializableArrayList;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EstadiasPorEstudiante;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEstadiasPorEstudiante;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaEstadiasPorEstudiante;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEstadiasPorEstudiante;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbOrganismosVinculados;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioEstadiasPorEstudiante implements EjbEstadiasPorEstudiante {

    @EJB
    Facade facadeVinculacion;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    @EJB
    EjbOrganismosVinculados ejbOrganismosVinculados;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    public ListaEstadiasPorEstudiante getListaEstadiasPorEstudiante(String rutaArchivo) throws Throwable {
        ListaEstadiasPorEstudiante listaEstadiasPorEstudiante = new ListaEstadiasPorEstudiante();

        List<DTOEstadiasPorEstudiante> listaDtoEstadiasPorEstudiante = new ArrayList<>();
        AreasUniversidad areaUniversidad;
        MatriculaPeriodosEscolares matriculaPeriodosEscolar;
        OrganismosVinculados organismoVinculado;
        EstadiasPorEstudiante estadiaPorEstudiante;
        DTOEstadiasPorEstudiante dtoEstadiaPorEstudiante;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;

        Integer matricula = 0;
        String matriculaNueva = "000000";
        if ((primeraHoja.getSheetName().equals("Estadías"))) {
            for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
                fila = (XSSFRow) (Row) primeraHoja.getRow(i);
                if (fila.getCell(1).getNumericCellValue() != 0) {
                    areaUniversidad = new AreasUniversidad();
                    organismoVinculado = new OrganismosVinculados();
                    matriculaPeriodosEscolar = new MatriculaPeriodosEscolares();
                    estadiaPorEstudiante = new EstadiasPorEstudiante();
                    dtoEstadiaPorEstudiante = new DTOEstadiasPorEstudiante();

                    switch (fila.getCell(1).getCellTypeEnum()) {
                        case FORMULA:
                            estadiaPorEstudiante.setCicloEscolar((int) fila.getCell(1).getNumericCellValue());
                            dtoEstadiaPorEstudiante.setCicloEscolar(fila.getCell(2).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(4).getCellTypeEnum()) {
                        case FORMULA:
                            estadiaPorEstudiante.setGeneracion((short) ((int) fila.getCell(4).getNumericCellValue()));
                            dtoEstadiaPorEstudiante.setGeneracion(fila.getCell(5).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(7).getCellTypeEnum()) {
                        case FORMULA:
                            matriculaPeriodosEscolar.setPeriodo((int) fila.getCell(7).getNumericCellValue());
                            dtoEstadiaPorEstudiante.setPeriodoEscolar(fila.getCell(8).getStringCellValue());
                            break;
                        default:
                            break;
                    }
//                    Matricula
                    switch (fila.getCell(9).getCellTypeEnum()) {
                        case NUMERIC:
                            matricula = (int) fila.getCell(9).getNumericCellValue();
                            if (matricula > 20000 && matricula < 99999) {
                                matriculaNueva = "0" + String.valueOf(matricula);
                            } else {
                                matriculaNueva = String.valueOf(matricula);
                            }
                            matriculaPeriodosEscolar.setMatricula(matriculaNueva);
                            break;
                        default:
                            break;
                    }

                    switch (fila.getCell(11).getCellTypeEnum()) {
                        case FORMULA:
                            areaUniversidad.setArea((short) ((int) fila.getCell(11).getNumericCellValue()));
                            areaUniversidad.setNombre(fila.getCell(12).getStringCellValue());
                            estadiaPorEstudiante.setProgramaEducativo(areaUniversidad.getArea());
                            break;
                        default:
                            break;
                    }
                    switch (fila.getCell(14).getCellTypeEnum()) {
                        case FORMULA:
                            organismoVinculado.setEmpresa((int) fila.getCell(14).getNumericCellValue());
                            organismoVinculado.setNombre(fila.getCell(15).getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    estadiaPorEstudiante.setMatriculaPeriodosEscolares(matriculaPeriodosEscolar);
                    estadiaPorEstudiante.setEmpresa(organismoVinculado);
                    dtoEstadiaPorEstudiante.setAreasUniversidad(areaUniversidad);
                    dtoEstadiaPorEstudiante.setEstadiasPorEstudiante(estadiaPorEstudiante);

                    listaDtoEstadiasPorEstudiante.add(dtoEstadiaPorEstudiante);
                }
            }
            listaEstadiasPorEstudiante.setEstadiasPorEstudiante(listaDtoEstadiasPorEstudiante);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaEstadiasPorEstudiante;
    }

    @Override
    public void guardaEstadiasPorEstudiante(ListaEstadiasPorEstudiante listaEstadiasPorEstudiante, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//        listaEstadiasPorEstudiante.getEstadiasPorEstudiante().forEach((t) -> {
//            System.out.println(t.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula() + " " + t.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getPeriodo() + " " + t.getEstadiasPorEstudiante().getEmpresa().getEmpresa());
//            
//        });
        List<String> listaCondicional = new ArrayList<>();
        listaEstadiasPorEstudiante.getEstadiasPorEstudiante().forEach((estadiaPorEstudiante) -> {
            facadeVinculacion.setEntityClass(EstadiasPorEstudiante.class);
            EstadiasPorEstudiante estadiaEncontrada = getEstadiaPorEstudiante(estadiaPorEstudiante.getEstadiasPorEstudiante());
            Boolean registroAlmacenado = false;

            if (estadiaEncontrada != null) {
                listaCondicional.add(estadiaPorEstudiante.getPeriodoEscolar() + " " + estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                estadiaPorEstudiante.getEstadiasPorEstudiante().setRegistro(estadiaEncontrada.getRegistro());
                estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula(), estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().getEmpresa()));
                facadeVinculacion.edit(estadiaPorEstudiante.getEstadiasPorEstudiante());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getMatricula(), estadiaPorEstudiante.getEstadiasPorEstudiante().getMatriculaPeriodosEscolares().getPeriodo()));
                estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().setRegistro(ejbOrganismosVinculados.getRegistroOrganismoEspecifico(estadiaPorEstudiante.getEstadiasPorEstudiante().getEmpresa().getEmpresa()));
                estadiaPorEstudiante.getEstadiasPorEstudiante().setRegistro(registro.getRegistro());
                facadeVinculacion.create(estadiaPorEstudiante.getEstadiasPorEstudiante());
            }
            facadeVinculacion.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }

    @Override
    public EstadiasPorEstudiante getEstadiaPorEstudiante(EstadiasPorEstudiante estadiasPorEstudiante) {
        EstadiasPorEstudiante estadiaEncontrada = new EstadiasPorEstudiante();
        TypedQuery<EstadiasPorEstudiante> query = em.createQuery("SELECT e FROM EstadiasPorEstudiante e JOIN e.matriculaPeriodosEscolares m WHERE e.cicloEscolar = :cicloEscolar AND m.periodo = :periodoEscolar AND m.matricula = :matricula", EstadiasPorEstudiante.class);
        query.setParameter("cicloEscolar", estadiasPorEstudiante.getCicloEscolar());
        query.setParameter("periodoEscolar", estadiasPorEstudiante.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", estadiasPorEstudiante.getMatriculaPeriodosEscolares().getMatricula());
        try {
            estadiaEncontrada = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            estadiaEncontrada = null;
            System.out.println(ex.toString());
        }
        return estadiaEncontrada;
    }

}
