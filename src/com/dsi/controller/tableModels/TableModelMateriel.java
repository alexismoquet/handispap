package com.dsi.controller.tableModels;

import com.dsi.model.beans.Materiel;
import javax.swing.table.AbstractTableModel;
import java.util.List;


public class TableModelMateriel extends AbstractTableModel {

    private final String[] titres = {"Nom","Description","IdAdresse","IdCategorie", "IdSport", "IdMateriel", "Prix", "Prix caution"};

    private List<Materiel> materiels;

    public TableModelMateriel (List<Materiel> materiels) {
        this.materiels = materiels;
    }


    public int getRowCount() {
        return materiels.size();
    }

    public int getColumnCount() {
        return titres.length;
    }

    public String getColumnName(int column) {
        return titres[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return materiels.get(rowIndex).getMateriel_nom();
            case 1:
                return materiels.get(rowIndex).getMateriel_description();
            case 2:
                return materiels.get(rowIndex).getMateriel_adresse_id();
            case 3:
                return materiels.get(rowIndex).getMateriel_categorie_id();
            case 4:
                return materiels.get(rowIndex).getMateriel_sport_id();
            case 5 :
                return materiels.get(rowIndex).getMateriel_id();
            case 6:
                return materiels.get(rowIndex).getMateriel_prix();
            case 7:
                return materiels.get(rowIndex).getMateriel_caution_prix();
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        fireTableCellUpdated(rowIndex, columnIndex);
        if(columnIndex == 0) {
            materiels.get(rowIndex).setMateriel_nom(value.toString());
        } else if (columnIndex == 1){
            materiels.get(rowIndex).setMateriel_description(value.toString());
        } else if (columnIndex == 2){
            materiels.get(rowIndex).setMateriel_adresse_id(Integer.parseInt(value.toString()));
        } else if (columnIndex == 3){
            materiels.get(rowIndex).setMateriel_categorie_id(Integer.parseInt(value.toString()));
        } else if (columnIndex == 4){
            materiels.get(rowIndex).setMateriel_sport_id(Integer.parseInt(value.toString()));
        } else if (columnIndex == 5){
            materiels.get(rowIndex).setMateriel_id(Integer.parseInt(value.toString()));
        } else {
            double round = Math.round(Double.parseDouble((value.toString())));
            if (columnIndex == 6){
                materiels.get(rowIndex).setMateriel_prix(round);
            } else if (columnIndex == 7){
                materiels.get(rowIndex).setMateriel_caution_prix(round);
            }
        }
    }

}//fin class
