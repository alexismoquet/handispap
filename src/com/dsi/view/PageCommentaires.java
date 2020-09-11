package com.dsi.view;

import com.dsi.controller.tableModel.TableModelCommentaire;
import com.dsi.model.beans.*;
import com.dsi.model.bll.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dsi.controller.Commentaires.*;

/**
 * Classe PageCommentaires
 *
 * @author Alexis Moquet
 * @since Créé le 04/02/2020
 */
public class PageCommentaires extends JFrame {

    private final JPanel panPrincipal = new JPanel();
    private final JPanel panHaut = new JPanel();
    private final JPanel panCentre = new JPanel();
    private final JPanel panBas = new JPanel();

    private final JButton btnModifierCommentaire = new JButton("Enregistrer");
    private final JButton btnSupprimerCommentaire = new JButton("Supprimer");
    private final JButton btnAnnuler = new JButton("Annuler");
    private final JButton btnAjouterCommentaire = new JButton("Ajouter");

    private final JTextField txtRechercher = new JTextField();
    private final JButton btnRechercher = new JButton("Rechercher");

    private final JTable tableauCommentaire = new JTable();
    List<Commentaire> commentaires = new ArrayList<>();
    List <Commentaire> listRechercheCommentaires = new ArrayList<>();

    Annonce annonce;
    Utilisateur utilisateur;
    Commentaire commentaire, blankCommentaire;
    ImageIcon icone = new ImageIcon("LogoIconeDSI.png");


    /**
     * Constructeur par defaut
     */
    public PageCommentaires() {
        initialiserComposants();
    }

    /**
     * Constructeur
     * @param: pAnnonce
     */
    public PageCommentaires(Annonce pAnnonce) {
        this.annonce = pAnnonce;
        initialiserComposants();
    }

    public PageCommentaires(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        initialiserComposants();
    }

    public void initialiserComposants() {
        setTitle("Commentaires");
        setIconImage(Toolkit.getDefaultToolkit().getImage("LogoIconeDSI.png"));
        setSize(1100, 700);
        setVisible(true);
        setResizable(true);

        panPrincipal.setLayout(new BorderLayout());
        panPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panPrincipal.setBackground(Color.decode("#11417d"));
        panPrincipal.add(panHaut, BorderLayout.NORTH);
        panPrincipal.add(panCentre, BorderLayout.CENTER);
        panPrincipal.add(panBas, BorderLayout.SOUTH);

        panHaut.setPreferredSize(new Dimension(900, 100));
        txtRechercher.setText(" Rechercher par mot(s) clé(s) ");
        panHaut.add(txtRechercher);
        panHaut.add(btnRechercher);

        panCentre.setPreferredSize(new Dimension(900, 250));
        panCentre.setLayout(new BorderLayout());
        panCentre.add(tableauCommentaire.getTableHeader(), BorderLayout.NORTH);
        panCentre.add(tableauCommentaire, BorderLayout.CENTER);
        panCentre.add(new JScrollPane(tableauCommentaire), BorderLayout.CENTER);
        tableauCommentaire.setRowHeight(30);

        panBas.setSize(500, 200);
        panBas.add(btnAjouterCommentaire);
        panBas.add(btnModifierCommentaire);
        panBas.add(btnSupprimerCommentaire);
        panBas.add(btnAnnuler);

        setContentPane(panPrincipal);

        displayRightTable();


        /*
         * Mouse listenner du champ de recherche qui efface le contenu du champ
         **/
        txtRechercher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtRechercher.setText("");
            }
        });

        /*
         * Mouse listenner du bouton de recherche
         **/
        btnRechercher.addActionListener(e -> {
            listRechercheCommentaires = new ArrayList<>();
            CommentaireManager um = CommentaireManager.getInstance();
            try {
                um.SelectAll();
            } catch (BLLException ex) {
                ex.printStackTrace();
            }
            for (Commentaire commentaire : commentaires) {
                String cm = commentaire.getCommentaire_message().toLowerCase();
                String recherche = txtRechercher.getText().toLowerCase();

                if (cm.contains(recherche)) {
                    listRechercheCommentaires.add(commentaire);
                    TableModelCommentaire model = new TableModelCommentaire(listRechercheCommentaires);
                    tableauCommentaire.setModel(model);
                }
            }
            if (listRechercheCommentaires.size() == 0) {
                JOptionPane.showMessageDialog(panPrincipal, "Aucun commentaire trouvé contenant ce(s) mot(s)", "warning", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        /*
         * Mouse listenner sur le bouton annuler
         */
        btnAnnuler.addActionListener(e -> {
            txtRechercher.setText(" Rechercher par mot(s) clé(s) ");
            blankCommentaire = null;
            commentaire = null;
            displayRightTable();
        });

        /*
         * Mouse listenner sur le bouton supprimer
         */
        btnSupprimerCommentaire.addActionListener(e -> {
            if (commentaire == null) {
                JOptionPane.showMessageDialog(btnSupprimerCommentaire, "Veuillez sélectionner un commentaire");
                return;
            }
            CommentaireManager cm = CommentaireManager.getInstance();

            int i= JOptionPane.showConfirmDialog(btnSupprimerCommentaire, "La suppression est irréversible. Êtes-vous sûr de vouloir supprimer le commentaire "+commentaire.getCommentaire_id()+" ?",
                    "Veuillez confirmer votre choix",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,icone);
            if (i==0) //user a dit oui
            {
                try {
                    cm.delete(commentaire);
                    JOptionPane.showMessageDialog(btnSupprimerCommentaire, "Commentaire " + commentaire.getCommentaire_id() + " supprimé");
                    afficheJTableCommentaires();
                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
                tableauCommentaire.clearSelection();
            }
            displayRightTable();
        });

        /*
         * listenner sur le btnajouterSortie pour ajouter une ligne vierge
         */
        btnAjouterCommentaire.setSize(140, 50);
        btnAjouterCommentaire.addActionListener(e -> {

            List<Commentaire>allCommentaires = null;
            CommentaireManager mm = new CommentaireManager();
            try {
                allCommentaires = mm.SelectAll();
            } catch (BLLException bllException) {
                bllException.printStackTrace();
            }

            blankCommentaire = new Commentaire();
            commentaires.add(blankCommentaire);

            //////  On récupére la plus haute id du tableau pour assigner blankCommentaire à 1 au dessus ////////////////
            assert allCommentaires != null;
            int idMax = allCommentaires.get(0).getCommentaire_id();

            for (Commentaire allCommentaire : allCommentaires) {
                int commentaireId = allCommentaire.getCommentaire_id();
                if (commentaireId > idMax) {
                    idMax = commentaireId;
                }
            }
            blankCommentaire.setCommentaire_id(idMax + 1);
            blankCommentaire.setCommentaire_note(0);
            blankCommentaire.setCommentaire_message("");
            blankCommentaire.setCommentaire_date_parution(new Date());

            if (utilisateur != null){
                blankCommentaire.setCommentaire_utilisateur_id(utilisateur.getIdUtilisateur());
            } else {
                blankCommentaire.setCommentaire_utilisateur_id(1);
            }
            if (annonce != null){
                blankCommentaire.setCommentaire_annonce_id(annonce.getAnnonce_id());
            }else{
                blankCommentaire.setCommentaire_annonce_id(1);
            }

            try {
                CommentaireManager.getInstance().insert(blankCommentaire);
              //  JOptionPane.showMessageDialog(btnAjouterCommentaire, "Commentaire ajouté");
            } catch (BLLException bllException) {
                bllException.printStackTrace();
            }

            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            model.fireTableDataChanged();
            tableauCommentaire.revalidate();
            tableauCommentaire.setModel(model);

            blankCommentaire = null;
            displayRightTable();
        });

        /*
         * listenner sur le bouton btnModifierCommentaire qui enregistre les modifications dans la base
         */
        btnModifierCommentaire.setSize(140, 50);
        btnModifierCommentaire.addActionListener(e -> {

            /* Récupérer les valeurs du tableauSortie, on boucle pour chaque ligne */
            for (int i = 0; i < tableauCommentaire.getRowCount(); i++) {
                try {
                    commentaire = CommentaireManager.getInstance().SelectById((Integer) tableauCommentaire.getValueAt(i, 3));
                } catch (BLLException bllException) {
                    bllException.printStackTrace();
                }

                String commentaireMessageModifie = String.valueOf(tableauCommentaire.getValueAt(i, 0));
                int commentaireNoteModifie = (int) tableauCommentaire.getValueAt(i, 2);
                int idCommentaireModifie = (int) tableauCommentaire.getValueAt(i, 3);
                int commentaireIdUtilisateurModifie = (int) tableauCommentaire.getValueAt(i, 4);
                int commentaireIdAnnonceModifie = (int) tableauCommentaire.getValueAt(i, 5);
                Date dateParutionAnnonceModifie = (Date) tableauCommentaire.getValueAt(i, 1);

                if (commentaireIdAnnonceModifie ==0 || commentaireIdUtilisateurModifie ==0){
                    JOptionPane.showMessageDialog(null, "Merci de corriger les champs IidUtilisateur et/ou IdAnnonce");
                    return;
                }
//                tableauCommentaire.setValueAt(commentaireMessageModifie, i,0);
//                tableauCommentaire.setValueAt(commentaireNoteModifie, i,2);
//                tableauCommentaire.setValueAt(idCommentaireModifie, i,3);
//                tableauCommentaire.setValueAt(commentaireIdUtilisateurModifie, i,4);
//                tableauCommentaire.setValueAt(commentaireIdAnnonceModifie, i,5);

                if (commentaire == null) {
                    //JOptionPane.showMessageDialog(btnModifierCommentaire, "Merci d'ajouter le message");
                    return;
                } else {
                    /* ENREGISTRER LES VALEURS DS LA BASE ***/
                    if (commentaire.getCommentaire_note() != (commentaireNoteModifie) || !commentaire.getCommentaire_message().equalsIgnoreCase(commentaireMessageModifie) || !(commentaire.getCommentaire_id() == idCommentaireModifie)
                            || !(commentaire.getCommentaire_annonce_id() == commentaireIdAnnonceModifie) || !(commentaire.getCommentaire_utilisateur_id() == commentaireIdUtilisateurModifie)) {
                        commentaire.setCommentaire_message(commentaireMessageModifie);
                        commentaire.setCommentaire_id(idCommentaireModifie);
                        commentaire.setCommentaire_note(commentaireNoteModifie);
                        commentaire.setCommentaire_annonce_id(commentaireIdAnnonceModifie);
                        commentaire.setCommentaire_utilisateur_id(commentaireIdUtilisateurModifie);
                        commentaire.setCommentaire_date_parution(dateParutionAnnonceModifie);

                        int j = JOptionPane.showConfirmDialog(btnModifierCommentaire, "La modification est irréversible. Êtes-vous sûr de vouloir enregistrer le commentaire "+commentaire.getCommentaire_id()+" ?",
                                "Veuillez confirmer votre choix",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);

                        if (j == 0)  /*user a dit oui*/ {
                            try {
                                    CommentaireManager.getInstance().update(commentaire);JOptionPane.showMessageDialog(btnModifierCommentaire, "Commentaire " + commentaire.getCommentaire_id() + " enregistré");
                            } catch (BLLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }//fin for
            displayRightTable();
        });


        /*
         * Mouse listenner sur le tableau commentaire
         */
        tableauCommentaire.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int id = (int) tableauCommentaire.getValueAt(tableauCommentaire.getSelectedRow(), 3);
            //    JOptionPane.showMessageDialog(null, "Le commentaire " + id + " est sélectionné");

                try {
                    commentaire = CommentaireManager.getInstance().SelectById(id);
                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }//fin initialiserComposants

    /**
     * Méthode qui affiche le bon tableau selon la page de provenance
     */
    private void displayRightTable(){
        if (annonce ==  null && utilisateur == null){
            afficheJTableCommentaires();
        } else if (utilisateur == null){
            afficheJTableCommentairesWithIdAnnonce();
        } else if (annonce == null){
            afficheJTableCommentairesWithIdUtilisateur();
        }
    }

    /**
     * Méthode qui affiche le tableau de tous les commentaires
     */
    private void afficheJTableCommentaires() {
        try {
            commentaires = remplirJTableWithCommentaires();
            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            tableauCommentaire.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }
    } //fin afficheJTable

    /**
     * Méthode qui affiche les commentaires selon l'annonce sélectionnée
     */
    private void afficheJTableCommentairesWithIdAnnonce() {
        try {
            commentaires = remplirJTableWithCommentairesIdAnnonce(annonce.getAnnonce_id());
            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            tableauCommentaire.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }

    } //fin afficheJTable

    /**
     * Méthode qui affiche les commentaires selon l'utilisateur sélectionné
     */
    private void afficheJTableCommentairesWithIdUtilisateur() {
        try {
            commentaires = remplirJTableWithCommentairesIdUtilisateur(utilisateur.getIdUtilisateur());
            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            tableauCommentaire.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }

    } //fin afficheJTable

}//fin class

