/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.datamodel.ch;

import javax.faces.model.DataModel;
import javax.faces.model.FacesDataModel;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@FacesDataModel(forClass = Personal.class)
public class PersonalDataModel extends DataModel<Personal>{
    private PersonalData data;
    @Getter @Setter private int rowIndex = 0;

    public PersonalDataModel() {
        this(null);
    }

    public PersonalDataModel(PersonalData data){
        this.data = data;
    }
    
    @Override
    public boolean isRowAvailable() {
        return this.rowIndex >= 0 && this.rowIndex < this.getRowCount();
    }

    @Override
    public int getRowCount() {
        return this.data != null ? this.data.getPersonal().size() : 0;
    }

    @Override
    public Personal getRowData() {
        System.out.println("mx.edu.utxj.pye.sgi.datamodel.ch.PersonalDataModel.getRowData()");
        if (this.rowIndex >= 0 && this.rowIndex < this.getRowCount()) {
            System.out.println("com.hantsylabs.example.ee8.jsf.UserDataModel.getRowData() user: " + this.data.getPersonal().get(this.rowIndex));
            Personal p = data.getPersonal().get(rowIndex);
            p.setClave(rowIndex);
            data.getPersonal().set(rowIndex, p);            
            System.out.println("mx.edu.utxj.pye.sgi.datamodel.ch.PersonalDataModel.getRowData() p: " + p);
            return this.data.getPersonal().get(this.rowIndex);
        }

        return null;
    }

    @Override
    public Object getWrappedData() {
        return this.data;
    }

    @Override
    public void setWrappedData(Object data) {
        this.data = (PersonalData) data;
    }
    
}
