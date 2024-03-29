package com.dsi.model.dal;

import com.dsi.model.beans.Sortie;

import java.util.List;

/**
 * Interface DAO_Sortie
 *
 * @author Alexis Moquet
 * @since Créé le 25/02/2020
 */
public  interface DAO_Sortie extends DAO<Sortie> {
    List<Sortie> selectByIdMateriel(int pIdMateriel) throws DALException;
}
