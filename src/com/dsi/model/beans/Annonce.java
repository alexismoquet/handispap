package com.dsi.model.beans;

import java.io.Serializable;
import java.util.Date;
/**
 * Classe Annonce
 *
 * @author Alexis Moquet
 * @since Créé le 24/02/2020
 */
public class Annonce implements Serializable {

 //#################
 //### Attributs ###
 //#################

    private int annonce_id;
    private int annonce_utilisateur_id;
    private int annonce_materiel_id;
    private String annonce_titre;
    private String annonce_description;
    private Date annonce_date_parution;

    //#################
    //### Constructeurs
    //#################

    /**
     * Constructeur par defaut
     */
    public Annonce() {
    }

    /**
     * Constructeur
     * @param annonce_id
     * @param annonce_utilisateur_id
     * @param annonce_materiel_id
     * @param annonce_titre
     * @param annonce_description
     * @param annonce_date_parution
     */
    public Annonce(int annonce_id, int annonce_utilisateur_id, int annonce_materiel_id, String annonce_titre, String annonce_description, Date annonce_date_parution) {
        this.annonce_id = annonce_id;
        this.annonce_utilisateur_id = annonce_utilisateur_id;
        this.annonce_materiel_id = annonce_materiel_id;
        this.annonce_titre = annonce_titre;
        this.annonce_description = annonce_description;
        this.annonce_date_parution = annonce_date_parution;
    }

    //#######################
    //### Getters and setters
    //#######################

    /**
     * Retourne l'identifiant de l'annonce
     * @return int: Identifiant annonce
     */
    public int getAnnonce_id() {
        return annonce_id;
    }
    /**
     * Défini l'identifiant de l'annonce
     * @param annonce_id
     */
    public void setAnnonce_id(int annonce_id) {
        this.annonce_id = annonce_id;
    }

    /**
     * Retourne l'identifiant de l'utilisateur de l'annonce
     * @return int: identifiant de utilisateur de l'annonce
     */
    public int getAnnonce_utilisateur_id() {
        return annonce_utilisateur_id;
    }
    /**
     * Défini l'identifiant de l'utilisateur de l'annonce
     * @param annonce_utilisateur_id
     */
    public void setAnnonce_utilisateur_id(int annonce_utilisateur_id) {
        this.annonce_utilisateur_id = annonce_utilisateur_id;
    }

    /**
     * Retourne l'identifiant du materiel de l'annonce
     * @return int: annonce_materiel_id
     */
    public int getAnnonce_materiel_id() {
        return annonce_materiel_id;
    }
    /**
     * Défini l'identifiant du materiel de l'annonce
     * @param annonce_materiel_id
     */
    public void setAnnonce_materiel_id(int annonce_materiel_id) {
        this.annonce_materiel_id = annonce_materiel_id;
    }

    /**
     * Retourne le titre de l'annonce
     * @return String: annonce_titre
     */
    public String getAnnonce_titre() {
        return annonce_titre;
    }
    /**
     * Défini le titre de l'annonce
     * @param annonce_titre
     */
    public void setAnnonce_titre(String annonce_titre) {
        this.annonce_titre = annonce_titre;
    }

    /**
     * Retourne la description de l'annonce
     * @return String: annonce_description
     */
    public String getAnnonce_description() {
        return annonce_description;
    }
    /**
     * Défini la description de l'annonce
     * @param annonce_description
     */
    public void setAnnonce_description(String annonce_description) {
        this.annonce_description = annonce_description;
    }

    /**
     * Retourne la date de parution de l'annonce
     * @return String: annonce_date_parution
     */
    public Date getAnnonce_date_parution() {
        return annonce_date_parution;
    }
    /**
     * Défini la date de parution de l'annonce
     * @param annonce_date_parution
     */
    public void setAnnonce_date_parution(Date annonce_date_parution) {
        this.annonce_date_parution = annonce_date_parution;
    }

}//fin class
