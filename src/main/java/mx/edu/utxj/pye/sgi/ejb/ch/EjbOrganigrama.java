package mx.edu.utxj.pye.sgi.ejb.ch;


import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ch.PersonalOrganigrama;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateless
@Path("organigrama")
public class EjbOrganigrama {
    @Getter @Setter Integer idOrganigrama=0;
    @Getter @Setter Integer idArApoRec=0;
    @Getter @Setter List<PersonalOrganigrama> organigrama = new ArrayList<>();
    
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager emch;
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pro_ejb_1.0PU")
    private EntityManager empy;
    
    @EJB    Facade facade;    
    
    @PostConstruct
    public void init(){
//        obtenerPersonal();
    }

    @GET
    @Path("/json/personal")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PersonalOrganigrama> obtenerPersonal(){
        organigrama = new ArrayList<>();
        organigrama.clear();
        List<AreasUniversidad> aus = new ArrayList<>();  
        List<ListaPersonal> direcPersonals = new ArrayList<>();     
        direcPersonals.clear();
        idOrganigrama=0;        
        TypedQuery<AreasUniversidad> q = empy.createQuery("SELECT a FROM AreasUniversidad a INNER JOIN a.categoria c WHERE a.vigente=:vigente AND c.categoria<=:categoria ORDER BY a.area", AreasUniversidad.class);
        q.setParameter("vigente", "1");
        q.setParameter("categoria", 8);
        aus=q.getResultList();
        AreasUniversidad au=aus.get(aus.size()-1);
        AreasUniversidad au2=new AreasUniversidad();
        au2.setArea(Short.parseShort(String.valueOf(au.getArea()+1)));
        au2.setAreaSuperior(Short.valueOf("1"));
        au2.setCategoria(new Categorias(Short.valueOf("10")));
        au2.setNombre("Personal de apoyo");
        au2.setSiglas("Ar p");
        au2.setResponsable(0);
        au2.setVigente("1");
        au2.setTienePoa(Boolean.FALSE);
        au2.setNivelEducativo(au.getNivelEducativo());
        au2.setCorreoInstitucional("@utxicotepec.edu.mx");        
        aus.add(au2);
        aus.forEach((ar) -> {
            ListaPersonal l = new ListaPersonal();
            Personal p = new Personal();
            if (ar.getResponsable() != 0) {
                p=emch.find(Personal.class, ar.getResponsable());
                if(!p.getStatus().equals('B')){
                    l = emch.find(ListaPersonal.class, ar.getResponsable());
                }else{
                    l=new ListaPersonal();
                    l.setClave(0);
                    l.setNombre("Pendiente por reasignar");
                    l.setCategoriaOperativa(Short.valueOf("48"));
                    l.setCategoriaOperativaNombre("Encargado/a de Área");
                }
                direcPersonals.add(l);
            }
            String color = "";
            if (ar.getCategoria().getCategoria() == 7) {
                color = "#000000";
            } else {
                color = "#61BF1A";
            }
            if (ar.getArea() == 1) {
                organigrama.add(new PersonalOrganigrama(0, l.getClave(), null, ar.getNombre(), l.getNombre() + " ______________________________ " + l.getCategoriaOperativaNombre(), color, 1));
            } else {
                if (ar.getAreaSuperior() == 1) {
                    if (ar.getCategoria().getCategoria() == 10) {
                        organigrama.add(new PersonalOrganigrama(Integer.parseInt(ar.getArea().toString()), 0, 0, "Personal de apoyo", "Personal de apoyo administrativo de Rectoría", "#BDBFC1", 1));
                    } else {
                        organigrama.add(new PersonalOrganigrama(Integer.parseInt(ar.getArea().toString()), l.getClave(), 0, ar.getNombre(), l.getNombre() + " ______________________________ " + l.getCategoriaOperativaNombre(), color, 1));
                    }
                } else {
                    organigrama.add(new PersonalOrganigrama(Integer.parseInt(ar.getArea().toString()), l.getClave(), Integer.parseInt(ar.getAreaSuperior().toString()), ar.getNombre(), l.getNombre() + " ______________________________ " + l.getCategoriaOperativaNombre(), color, 1));
                }
            }
        });
        PersonalOrganigrama po = organigrama.get(organigrama.size() - 1);
        
        idArApoRec=po.getId();
        idOrganigrama=po.getId()+1;
        
        List<ListaPersonal> adm = new ArrayList<>();
        List<ListaPersonal> doc = new ArrayList<>();
    
        TypedQuery<ListaPersonal> c = emch.createQuery("SELECT l FROM ListaPersonal l WHERE l.actividad=:actividad ORDER BY l.areaOperativa ", ListaPersonal.class);c.setParameter("actividad", 1);
        TypedQuery<ListaPersonal> d = emch.createQuery("SELECT l FROM ListaPersonal l WHERE l.actividad=:actividad ORDER BY l.areaOperativa ", ListaPersonal.class);d.setParameter("actividad", 3);
        
        adm = c.getResultList();
        doc = d.getResultList();
        
        adm.forEach((l) -> {
            if (!direcPersonals.contains(l)) {
                AreasUniversidad ar = empy.find(AreasUniversidad.class, l.getAreaOperativa());
                if (ar.getCategoria().getCategoria() <= 8) {
                    if (ar.getArea() == 1) {
                        organigrama.add(new PersonalOrganigrama(idOrganigrama, l.getClave(), idArApoRec, l.getNombre(), l.getCategoriaOperativaNombre(), "#BDBFC1", 2));

                    } else {
                        organigrama.add(new PersonalOrganigrama(idOrganigrama, l.getClave(), Integer.parseInt(String.valueOf(l.getAreaOperativa())), l.getNombre(), l.getCategoriaOperativaNombre(), "#BDBFC1", 2));
                    }
                } else {
                    organigrama.add(new PersonalOrganigrama(idOrganigrama, l.getClave(), Integer.parseInt(ar.getAreaSuperior().toString()), l.getNombre(), l.getCategoriaOperativaNombre(), "#BDBFC1", 2));
                }
                idOrganigrama++;
            }
        });
        doc.forEach((l) -> {
            if (!direcPersonals.contains(l)) {
                organigrama.add(new PersonalOrganigrama(idOrganigrama, l.getClave(), Integer.parseInt(String.valueOf(l.getAreaSuperior())), l.getNombre(), l.getCategoriaOperativaNombre(), "#BDBFC1", 2));
                idOrganigrama++;
            }
        });
//        
        
        
        
        if(organigrama.isEmpty()){
            return new ArrayList<>();
        }else{
            return organigrama;
        }
    }
}
