package com.dsi.controller.tableModels;

import com.dsi.model.beans.Sortie;
import javax.swing.table.AbstractTableModel;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class TableModelSortie extends AbstractTableModel implements Serializable {

    private final String[] titres = {"Date retour","Date sortie", "Etat sortie", "IdMatériel", "IdSortie"};

    private List<Sortie> sorties;

    public TableModelSortie (List<Sortie> sorties) {
        this.sorties = sorties;

    }

    public int getRowCount() {
        return sorties.size();
    }

    public int getColumnCount() {
        return titres.length;
    }

    @Override
    public String getColumnName(int column) {
        return titres[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return sorties.get(rowIndex).getSortie_date_retour();
            case 1:
                return sorties.get(rowIndex).getSortie_date_sortie();
            case 2:
                return sorties.get(rowIndex).getSortie_etat();
            case 3:
                return sorties.get(rowIndex).getSortie_materiel_id();
            case 4:
                return sorties.get(rowIndex).getSortie_id();
            default:
                return "";
        }
    }
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        fireTableCellUpdated(rowIndex, columnIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

        if(columnIndex == 0) {
            try {
                sorties.get(rowIndex).setSortie_date_retour(sdf.parse(value.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (columnIndex == 1){
            try {
                sorties.get(rowIndex).setSortie_date_sortie(sdf.parse(value.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (columnIndex == 2){
            sorties.get(rowIndex).setSortie_etat((String) value);
        } else if (columnIndex == 3){
            sorties.get(rowIndex).setSortie_materiel_id(Integer.parseInt(value.toString()));
        }
    }
}//fin class
