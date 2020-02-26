package com.dsi.model.bll;

import com.dsi.model.beans.Commentaire;
import com.dsi.model.dal.DAO_Commentaire;
import com.dsi.model.dal.DAO_Factory;
import java.util.List;

/**
 * Classe CommentaireManager
 *
 * @author Alexis Moquet
 * @since Créé le 25/02/2020
 */
public class CommentaireManager implements Manager<Commentaire> {

    private static CommentaireManager instance = null;

    private DAO_Commentaire dao = null;
    private Commentaire commentaire;
    private List<Commentaire> commentaires;

    /**
     * Constructeur
     */
    private CommentaireManager() { dao = DAO_Factory.getDAO_Commentaire(); }

    /**
     * Singleton
     * @return
     */
    public static CommentaireManager getInstance(){
        if (instance == null){
            instance = new CommentaireManager();
        }

        return instance;
    }

    @Override
    public void insert(Commentaire pObj) throws BLLException {

    }

    @Override
    public void update(Commentaire pObj) throws BLLException {

    }

    @Override
    public void delete(Commentaire pObj) throws BLLException {

    }

    @Override
    public List<Commentaire> SelectAll() throws BLLException {
        return null;
    }

    @Override
    public Commentaire SelectById(int pId) throws BLLException {
        return null;
    }
}
