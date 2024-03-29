package com.dsi.controller;

import com.dsi.librairies.UMdp;
import com.dsi.model.beans.UtilisateurBo;
import com.dsi.model.bll.BLLException;
import com.dsi.model.bll.UtilisateurBoManager;

import java.io.Serializable;

public class BOConnexion implements Serializable {

    public boolean actionIdentification(String texteLogin, String texteMotDePasse) throws BLLException {
        UtilisateurBoManager ubom = UtilisateurBoManager.getInstance();
        UtilisateurBo ubo = ubom.selectByLogin(texteLogin);
        return UMdp.mdpCompare(texteMotDePasse, ubo.getMdp());
    }
}//fin class
