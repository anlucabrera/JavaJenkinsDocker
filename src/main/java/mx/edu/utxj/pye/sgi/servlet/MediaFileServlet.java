/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.servlet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import org.omnifaces.servlet.FileServlet;

/**
 *
 * @author UTXJ
 */
@WebServlet(name = "MediaFileServlet", urlPatterns = {"/media/*"})
public class MediaFileServlet extends FileServlet {

    private static final long serialVersionUID = -3030047644663156293L;
    private File folder;

    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;

    @Override
    public void init() throws ServletException {
        folder = new File(ServicioArchivos.carpetaRaiz);
    }

    @Override
    protected File getFile(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        //System.out.println("jvv.aldesa.sgot.servlet.MediaFileServlet.getFile():" + pathInfo);
        if (pathInfo == null || pathInfo.isEmpty() || "/".equals(pathInfo)) {
            throw new IllegalArgumentException();
        }

        File f = new File(folder, pathInfo);
//        try {
//            BufferedImage image = ImageIO.read(f);
//            int type = image.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : image.getType();
//
//            BufferedImage resizeImageJpg = resizeImage(image, type);
//            
//            File f2 = new File("resized");
//            ImageIO.write(resizeImageJpg, "jpg", f);
//            
//            return f2;
//        } catch (IOException ex) {
//            Logger.getLogger(MediaFileServlet.class.getName()).log(Level.SEVERE, null, ex);
//            return f;
//        }
        return f;
    }

    private static BufferedImage resizeImage(BufferedImage image, int type) {
        int cociente = image.getWidth() % 300;
        BufferedImage resizedImage = new BufferedImage(image.getWidth() / cociente, image.getHeight() / cociente, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(image, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
        g.dispose();

        return resizedImage;
    }
}
