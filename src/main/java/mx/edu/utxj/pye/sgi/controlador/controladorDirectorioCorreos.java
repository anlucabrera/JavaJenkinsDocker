package mx.edu.utxj.pye.sgi.controlador;


import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.dto.dtoCorreosAreas;

import org.omnifaces.cdi.ViewScoped;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorPersonalAltasYBajas;


import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Named;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;



@Named
@ViewScoped
@ManagedBean

public class controladorDirectorioCorreos implements Serializable {
    @Getter @Setter List<Personal> listPersonal;
    ControladorPersonalAltasYBajas ctrl= new ControladorPersonalAltasYBajas();
    @Getter @Setter List<dtoCorreosAreas> listCorrreosAreas;
    @Getter @Setter List<dtoCorreosAreas> list2CorreosAreas;



@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;




    @PostConstruct
    public void init(){
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        listPersonal = ctrl.getListaPersonalTotal();
        listCorrreosAreas= correosAreas();
        list2CorreosAreas = correosAreas();

    }
    public List<dtoCorreosAreas> correosAreas(){
        listCorrreosAreas = new ArrayList<>();
        listCorrreosAreas.add(new dtoCorreosAreas("Rectoría","rectoria@utxicotepec.edu.mx","gerardo.vargas@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Administración y Finanzas","administracion.finanzas@utxicotepec.edu.mx","gema.melo@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Área Jurídica","area.juridica@utxicotepec.edu.mx","zoila.salazar@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Planeación y Evaluación","planeacion.evaluacion@utxicotepec.edu.mx","miguel.sanchez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Recursos Financieros","recursos.financieros@utxicotepec.edu.mx","arantxa.rivera@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Secretaría Académica","secretaria.academica@utxicotepec.edu.mx","altagracia.carrillo@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Extensión  y Vinculación","vinculacion@utxicotepec.edu.mx","alba.maldonado@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Tecnologías de la Infomación","tecnologias.informacion@utxicotepec.edu.mx","raul.chirinos@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Terapia Física y Rehabilitación","terapia.rehabilitacion@utxicotepec.edu.mx","concepcion.garcia@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Mecatrónica","mecatronica@utxicotepec.edu.mx","joseantonio.martinez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Mantenimiento Industrial","mantenimiento.industrial@utxicotepec.edu.mx","javier.ortega@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Agro-Industrial Alimentaria","agroindustrial.alimentaria@utxicotepec.edu.mx","victor.morales@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Económico Administrativa","economica.administrativa@utxicotepec.edu.mx","juancarlos.carmona@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Personal","personal@utxicotepec.edu.mx","claudia.santamaria@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Prensa, Difusión y Actividades Culturales","prensa.difusion@utxicotepec.edu.mx","rodrigo.cruz@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Programación y Contabilidad","programacion.contabilidad@utxicotepec.edu.mx","gisela.velasco@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Servicios Escolares","servicios.escolares@utxicotepec.edu.mx","juan.sanchez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Servicios Estudiantiles","servicios.estudiantiles@utxicotepec.edu.mx","julio.lopez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Servicios Tecnológicos y Convenios","servicios.tecnologicos@utxicotepec.edu.mx","alba.maldonado@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Información y Estadística","informacion.estadistica@utxicotepec.edu.mx"," jonny.valderrabano@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Informática y Educación a Distancia","informatica.distancia@utxicotepec.edu.mx","jorgearroyo@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Recursos Materiales y Servicios Generales","recursos.materiales@utxicotepec.edu.mx","manuel.gayosso@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Fortalecimiento de Desarrollo Académico","fortalecimiento.academico@utxicotepec.edu.mx","julia.castro@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Académia de Idiomas","academia.idiomas@utxicotepec.edu.mx","raul.ojeda@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Adquisiciones","adquisiciones@utxicotepec.edu.mx","fabiola.picazo@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Centro de Emprendimiento y Desarrollo Empresarial","emprendedores@utxicotepec.edu.mx","cesar.gonzalez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Psicopedagogía","psicopedagogia@utxicotepec.edu.mx","clara.grajeda@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Sistema de Gestión de la Calidad","coordinacion.calidad@utxicotepec.edu.mx","miguel.sanchez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Movilidad e Internacionalización","movilidad.internacionalizacion@utxicotepec.edu.mx","cesar.gonzalez@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Actividades Deportivas","actividades.deportivas@utxicotepec.edu.mx","marco.leon@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Coordinación de Titulación","coordinacion.titulacion@utxicotepec.edu.mx","andrea.rivera@utxicotepec.edu.mx"));
        listCorrreosAreas.add(new dtoCorreosAreas("Coordinación de Desarrollo de Software","sistemas@utxicotepec.edu.mx","jonny.valderrabano@utxicotepec.edu.mx"));


        return listCorrreosAreas;

    }









}
