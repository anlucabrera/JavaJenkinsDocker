/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.ca;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
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
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasCausa;
import mx.edu.utxj.pye.sgi.entity.prontuario.BajasTipo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.Registros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.dto.escolar.DTODesercion;
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaDesercionPeriodos;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbMatriculaPeriodosEscolares;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbDesercionPeriodos;
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
public class ServicioDesercionPeriodos implements EjbDesercionPeriodos{
    
    @EJB
    Facade facdepye;
    @EJB
    EjbModulos ejbModulos;
    @EJB
    EjbMatriculaPeriodosEscolares ejbMatriculaPeriodosEscolares;
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;
    
    @Override
    public ListaDesercionPeriodos getListaDesercionPeriodos(String rutaArchivo) throws Throwable {
    
        ListaDesercionPeriodos listaDesercionPeriodos = new  ListaDesercionPeriodos();

        List<DTODesercion> listaDtoDesercion = new ArrayList<>();
        BajasCausa bajasCausa;
        BajasTipo bajasTipo;
        Generaciones generaciones;
        MatriculaPeriodosEscolares matriculaPeriodosEscolares;
        DesercionPeriodosEscolares desercionPeriodosEscolares;
        DTODesercion dTODesercion;

        File excel = new File(rutaArchivo);
        XSSFWorkbook libroRegistro = new XSSFWorkbook();
        libroRegistro = (XSSFWorkbook) WorkbookFactory.create(excel);
        XSSFSheet primeraHoja = libroRegistro.getSheetAt(0);
        XSSFRow fila;
        
        String mat ="";
       
        if (primeraHoja.getSheetName().equals("Deserción")) {
        for (int i = 2; i <= primeraHoja.getLastRowNum(); i++) {
            fila = (XSSFRow) (Row) primeraHoja.getRow(i);

            if ("".equals(fila.getCell(0).getStringCellValue())) {
            } else {
                bajasCausa = new BajasCausa();
                bajasTipo = new BajasTipo();
                generaciones = new Generaciones();
                matriculaPeriodosEscolares = new MatriculaPeriodosEscolares();
                desercionPeriodosEscolares = new DesercionPeriodosEscolares();
                dTODesercion = new DTODesercion();
                
                switch (fila.getCell(0).getCellTypeEnum()) {
                    case FORMULA:
                        desercionPeriodosEscolares.setDpe(fila.getCell(0).getStringCellValue());                  
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(2).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setCicloEscolar(fila.getCell(2).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(4).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setPeriodoEscolar(fila.getCell(4).getStringCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(5).getCellTypeEnum()) {
                    case FORMULA:
                        matriculaPeriodosEscolares.setPeriodo((int) fila.getCell(5).getNumericCellValue());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(6).getCellTypeEnum()) {
                    case STRING:
                        dTODesercion.setGeneracion(fila.getCell(6).getStringCellValue());
                        break;
                    default:
                        break;
                }

                switch (fila.getCell(7).getCellTypeEnum()) {
                    case FORMULA:
                        generaciones.setGeneracion((short) fila.getCell(7).getNumericCellValue());
                        desercionPeriodosEscolares.setGeneracion(generaciones.getGeneracion());
                        break;
                    default:
                        break;
                }
                switch (fila.getCell(8).getCellTypeEnum()) {
                    case NUMERIC:
                        int numero = (int)fila.getCell(8).getNumericCellValue();
                        mat= Integer.toString(numero);
                        matriculaPeriodosEscolares.setMatricula(mat);
                        desercionPeriodosEscolares.setMatriculaPeriodosEscolares(matriculaPeriodosEscolares);
                        break;
                    default:
                        break;
                }
                
                 switch (fila.getCell(9).getCellTypeEnum()) {
                    case STRING:
                       bajasCausa.setCausa(fila.getCell(9).getStringCellValue());
                        break;
                    default:
                        break;
                }
                  switch (fila.getCell(10).getCellTypeEnum()) {
                    case FORMULA:
                        bajasCausa.setCveCausa((int)fila.getCell(10).getNumericCellValue());
                        desercionPeriodosEscolares.setCausaBaja(bajasCausa.getCveCausa());
                        break;
                    default:
                        break;
                }
                   switch (fila.getCell(11).getCellTypeEnum()) {
                    case STRING:
                        bajasTipo.setDescripcion(fila.getCell(11).getStringCellValue());
                        break;
                    default:
                        break;
                }
                    switch (fila.getCell(12).getCellTypeEnum()) {
                    case FORMULA:
                        bajasTipo.setTipoBaja((int)fila.getCell(12).getNumericCellValue());
                        desercionPeriodosEscolares.setTipoBaja(bajasTipo.getTipoBaja());
                        break;
                    default:
                        break;
                }
                    dTODesercion.setBajasCausa(bajasCausa);
                    dTODesercion.setBajasTipo(bajasTipo);
                    dTODesercion.setDesercionPeriodosEscolares(desercionPeriodosEscolares);
                    listaDtoDesercion.add(dTODesercion);
                }
            }
            listaDesercionPeriodos.setDesercion(listaDtoDesercion);

            libroRegistro.close();
            addDetailMessage("<b>Archivo Validado favor de verificar sus datos antes de guardar su información</b>");
        } else {
            libroRegistro.close();
            excel.delete();
            ServicioArchivos.eliminarArchivo(rutaArchivo);
            addDetailMessage("<b>El archivo cargado no corresponde al registro</b>");
        }
        return listaDesercionPeriodos;
        
    }
    
    
    @Override
    public void guardaDesercionPeriodos (ListaDesercionPeriodos listaDesercionPeriodos, RegistrosTipo registrosTipo, EjesRegistro ejesRegistro, Short area, EventosRegistros eventosRegistros) {
//            listaDesercionPeriodos.getDesercion().forEach((desercion) -> {
//            Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
//            
//            desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
//            
//            facdepye.setEntityClass(DesercionPeriodosEscolares.class);
//            desercion.getDesercionPeriodosEscolares().setRegistro(registro.getRegistro());
//            facdepye.create(desercion.getDesercionPeriodosEscolares());
//            facdepye.flush();
//        });
            List<String> listaCondicional = new ArrayList<>();
            listaDesercionPeriodos.getDesercion().forEach((desercion) -> {
            facdepye.setEntityClass(DesercionPeriodosEscolares.class);
            DesercionPeriodosEscolares derPerEscEncontrada = getRegistroDesercionPeriodosEscolares(desercion.getDesercionPeriodosEscolares());
            Boolean registroAlmacenado = false;

            if (derPerEscEncontrada != null) {
                listaCondicional.add(desercion.getPeriodoEscolar()+ " " + desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula());
                registroAlmacenado = true;
            }
            if (registroAlmacenado) {
                desercion.getDesercionPeriodosEscolares().setRegistro(derPerEscEncontrada.getRegistro());
                desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                facdepye.edit(desercion.getDesercionPeriodosEscolares());
            } else {
                Registros registro = ejbModulos.getRegistro(registrosTipo, ejesRegistro, area, eventosRegistros);
                desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().setRegistro(ejbMatriculaPeriodosEscolares.getRegistroMatriculaEspecifico(desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getMatricula(), desercion.getDesercionPeriodosEscolares().getMatriculaPeriodosEscolares().getPeriodo()));
                desercion.getDesercionPeriodosEscolares().setRegistro(registro.getRegistro());
                facdepye.create(desercion.getDesercionPeriodosEscolares());
            }
            facdepye.flush();
        });
        addDetailMessage("<b>Se actualizarón los registros con los siguientes datos: </b> " + listaCondicional.toString());
    }
    
    @Override
    public Integer getRegistroDesercionPeriodosEspecifico(String dpe) {
        TypedQuery<DesercionPeriodosEscolares> query = em.createNamedQuery("DesercionPeriodosEscolares.findByDpe", DesercionPeriodosEscolares.class);
        query.setParameter("dpe", dpe);
        Integer registro = query.getSingleResult().getRegistro();
        return registro;
    }

    @Override
    public DesercionPeriodosEscolares getRegistroDesercionPeriodosEscolares(DesercionPeriodosEscolares desercionPeriodosEscolares) {
        TypedQuery<DesercionPeriodosEscolares> query = em.createQuery("SELECT d FROM DesercionPeriodosEscolares d JOIN d.matriculaPeriodosEscolares m WHERE m.periodo = :periodoEscolar AND m.matricula = :matricula", DesercionPeriodosEscolares.class);
        query.setParameter("periodoEscolar", desercionPeriodosEscolares.getMatriculaPeriodosEscolares().getPeriodo());
        query.setParameter("matricula", desercionPeriodosEscolares.getMatriculaPeriodosEscolares().getMatricula());
        try {
            desercionPeriodosEscolares = query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            desercionPeriodosEscolares = null;
            System.out.println(ex.toString());
        }
        return desercionPeriodosEscolares;
    }
}

