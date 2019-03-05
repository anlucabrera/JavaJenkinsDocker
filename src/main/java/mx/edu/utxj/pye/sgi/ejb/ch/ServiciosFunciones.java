package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Categoriasespecificasfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Comentariosfunciones;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosFunciones implements EjbFunciones {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

    @Getter    @Setter    Integer i = 0;
    @Getter    @Setter    List<Short> pDirA = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Funciones
    @Override
    public List<Funciones> mostrarListaFuncionesParaARAE(Personal p) throws Throwable {
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(1)");
        pDirA.clear();
        pDirA.add(Short.parseShort("30"));
        pDirA.add(Short.parseShort("32"));
        pDirA.add(Short.parseShort("41"));
        List<Funciones> listaFunciones = new ArrayList<>();
        TypedQuery<Personal> per = em.createQuery("SELECT p FROM Personal p", Personal.class);
        if (p.getAreaOperativa() >= 23 && p.getAreaOperativa() <= 29) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(2A)");
            per = em.createQuery("SELECT p FROM Personal p INNER JOIN p.categoriaOperativa co INNER JOIN p.categoriaEspecifica ce WHERE p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior GROUP BY co.categoria,ce.categoriaEspecifica", Personal.class);
        } else {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(2B)");
            per = em.createQuery("SELECT p FROM Personal p INNER JOIN p.categoriaOperativa co INNER JOIN p.categoriaEspecifica ce WHERE p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior GROUP BY p.areaOperativa,co.categoria,ce.categoriaEspecifica", Personal.class);
        }
        per.setParameter("areaOperativa", p.getAreaOperativa());
        per.setParameter("areaSuperior", p.getAreaOperativa());
        List<Personal> personas = per.getResultList();
        if (!personas.isEmpty()) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(3)");
            personas.forEach((t) -> {
                if (t.getAreaOperativa() != p.getAreaOperativa() && !Objects.equals(t.getCategoriaOperativa().getCategoria(), p.getCategoriaOperativa().getCategoria()) && !Objects.equals(t.getCategoriaEspecifica().getCategoriaEspecifica(), p.getCategoriaEspecifica().getCategoriaEspecifica())) {
                    System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(4)");
                    List<Funciones> f = new ArrayList<>();
                    try {
                        if (t.getAreaOperativa() >= 23 && t.getAreaOperativa() <= 29) {
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(5A)");
                            if (i != 1) {
                                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(6A)");
                                f = mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), t.getCategoriaOperativa().getCategoria(), t.getCategoriaEspecifica().getCategoriaEspecifica());
                                i++;
                            }
                        } else {
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(5B)");
                            f = mostrarListaFuncionesPersonalLogeado(t.getAreaOperativa(), t.getCategoriaOperativa().getCategoria(), t.getCategoriaEspecifica().getCategoriaEspecifica());
                        }
                        
                        if (t.getAreaSuperior()>= 23 && t.getAreaSuperior() <= 29) {
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(5C)");
                            f = mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), t.getCategoriaOperativa().getCategoria(), t.getCategoriaEspecifica().getCategoriaEspecifica());
                        }else {
                            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(6B)");
                            f = mostrarListaFuncionesPersonalLogeado(t.getAreaOperativa(), t.getCategoriaOperativa().getCategoria(), t.getCategoriaEspecifica().getCategoriaEspecifica());
                        }
                        
                    } catch (Throwable ex) {
                        Logger.getLogger(ServiciosFunciones.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    listaFunciones.addAll(f);
                }
            });
        }
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(7)"+listaFunciones.size());
        if (p.getAreaOperativa() == 2) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(8B)");
            pDirA.forEach((t) -> {
                System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(8.1)");
                List<Funciones> f = new ArrayList<>();
                try {
                    f = mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), t, Short.parseShort("1"));
                } catch (Throwable ex) {
                    Logger.getLogger(ServiciosFunciones.class.getName()).log(Level.SEVERE, null, ex);
                }
                listaFunciones.addAll(f);
            });
        }
        System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(8)"+listaFunciones.size());
        if (listaFunciones.isEmpty()) {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(9A)");
            return new ArrayList<>();
        } else {
            System.out.println("mx.edu.utxj.pye.sgi.ejb.ch.ServiciosFunciones.mostrarListaFuncionesParaARAE(9B)");
            return listaFunciones;
        }
    }

    @Override
    public List<Funciones> mostrarFuncionesPorAreayPuesto(Short area, Short categoria, Integer tipo) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f", Funciones.class);
        switch (tipo) {
            case 1:
                q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co WHERE f.areaOperativa = :areaOperativa AND co.categoria  <> :categoriaOperativa", Funciones.class);
                q.setParameter("categoriaOperativa", categoria);
                break;
            case 2:
                q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaEspesifica co WHERE f.areaOperativa = :areaOperativa AND co.categoriaEspecifica = :categoriaEspecifica", Funciones.class);
                q.setParameter("categoriaEspecifica", categoria);
                break;
        }
        q.setParameter("areaOperativa", area);
        List<Funciones> pr = q.getResultList();
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
        }
        return pr;
    }

    @Override
    public List<Funciones> mostrarListaFuncionesPersonalLogeado(Short area, Short categoriaOperativa, Short categoriaEspecifica) throws Throwable {
        TypedQuery<Funciones> q = em.createQuery("SELECT f FROM Funciones f JOIN f.categoriaOperativa co JOIN f.categoriaEspesifica ce WHERE f.areaOperativa = :areaOperativa AND co.categoria = :categoria AND ce.categoriaEspecifica=:categoriaEspecifica ORDER BY f.tipo DESC", Funciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("categoria", categoriaOperativa);
        q.setParameter("categoriaEspecifica", categoriaEspecifica);
        List<Funciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Funciones agregarFuncion(Funciones nuevaFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.create(nuevaFunciones);
        facade.flush();
        return nuevaFunciones;
    }

    @Override
    public Funciones actualizarFunciones(Funciones nuevaActualizacionFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.edit(nuevaActualizacionFunciones);
        return nuevaActualizacionFunciones;
    }

    @Override
    public Funciones eliminaFunciones(Funciones nuevoFunciones) throws Throwable {
        facade.setEntityClass(Funciones.class);
        facade.remove(nuevoFunciones);
        facade.flush();
        return nuevoFunciones;
    }

////////////////////////////////////////////////////////////////////////////////Comentarios funciones
    @Override
    public Comentariosfunciones agregarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.create(nuevoComentariosfunciones);
        facade.flush();
        return nuevoComentariosfunciones;
    }

    @Override
    public Comentariosfunciones actualizarComentariosfunciones(Comentariosfunciones nuevoComentariosfunciones) throws Throwable {
        facade.setEntityClass(Comentariosfunciones.class);
        facade.edit(nuevoComentariosfunciones);
        return nuevoComentariosfunciones;
    }

    @Override
    public List<Comentariosfunciones> mostrarComentariosfunciones() throws Throwable {
        TypedQuery<Comentariosfunciones> q = em.createQuery("SELECT c FROM Comentariosfunciones c", Comentariosfunciones.class);
        List<Comentariosfunciones> pr = q.getResultList();
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
        }
        return pr;
    }

////////////////////////////////////////////////////////////////////////////////Categorias especificas funciones
    @Override
    public Categoriasespecificasfunciones agregarCategoriasespecificasfunciones(Categoriasespecificasfunciones nuevaCategoriasespecificasfunciones) throws Throwable {
        facade.setEntityClass(Categoriasespecificasfunciones.class);
        facade.create(nuevaCategoriasespecificasfunciones);
        facade.flush();
        return nuevaCategoriasespecificasfunciones;
    }

    @Override
    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfunciones(String nombre, Short area) throws Throwable {
        TypedQuery<Categoriasespecificasfunciones> q = em.createQuery("SELECT c FROM Categoriasespecificasfunciones c WHERE c.nombreCategoria=:nombreCategoria AND c.area=:area", Categoriasespecificasfunciones.class);
        q.setParameter("nombreCategoria", nombre);
        q.setParameter("area", area);
        List<Categoriasespecificasfunciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Categoriasespecificasfunciones> mostrarCategoriasespecificasfuncionesArea(Short area) throws Throwable {
        TypedQuery<Categoriasespecificasfunciones> q = em.createQuery("SELECT c FROM Personal p INNER JOIN p.categoriaEspecifica c WHERE (p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior) GROUP BY c.categoriaEspecifica", Categoriasespecificasfunciones.class);
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Categoriasespecificasfunciones> pr = q.getResultList();
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
        }
        return pr;
    }

}
