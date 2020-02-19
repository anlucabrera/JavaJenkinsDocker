/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

/**
 *
 * @author UTXJ
 */
import java.io.Serializable;
import java.util.Hashtable;
import javax.faces.application.FacesMessage;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
 
@ManagedBean
@SessionScoped
public class ControladorValidador implements Serializable {

    private static final long serialVersionUID = -7654043366763398447L;
    
    public ControladorValidador() {
    }
    String emailAddress;
 
    public void setEmailAddress(String s) {
        emailAddress = s;
    }
 
    public String getEmailAddress() {
        return emailAddress;
    }
 
    public void validateEmail() {
        String email = getEmailAddress();
        if (!isValid(email)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    email + " is not a valid email address"));
 
        }
    }
 
    boolean isValid(String email) {
 
        // Reqular expression pattern to validate the format submitted
        String validator = "^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+"
                + "(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,})$";
 
        if (!email.matches(validator)) {
            return false;
        }
 
        // Split the user and the domain name
        String[] parts = email.split("@");
 
        boolean retval=true;
        // This is similar to nslookup –q=mx domain_name.com to query
        // the mail exchanger of the domain.
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put("java.naming.factory.initial",
                    "com.sun.jndi.dns.DnsContextFactory");
            DirContext context = new InitialDirContext(env);
            Attributes attributes =
                    context.getAttributes(parts[1], new String[]{"MX"});
            Attribute attribute = attributes.get("MX");
            if (attribute.size() == 0) {
               retval=false;
            }
            context.close();
            return retval;
 
        } catch (Exception exception) {
            return false;
        }        
    }
 
    public void process(ActionEvent event) {        
        // Validate again 
        String email = getEmailAddress();
        if (!isValid(email)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",
                    email + " is not a valid email address"));
            return;
 
        }
         
        // The task;
    }
}
