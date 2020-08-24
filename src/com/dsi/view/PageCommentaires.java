package com.dsi.view;

import com.dsi.controller.tableModel.TableModelCommentaire;
import com.dsi.controller.tableModel.TableModelSortie;
import com.dsi.model.beans.*;
import com.dsi.model.bll.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableRowSorter;
import javax.xml.stream.events.Comment;
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

    private JPanel panPrincipal = new JPanel();
    private JPanel panHaut = new JPanel();
    private JPanel panCentre = new JPanel();
    private JPanel panBas = new JPanel();

    private JButton btnModifierCommentaire = new JButton("Enregistrer");
    private JButton btnSupprimerCommentaire = new JButton("Supprimer");
    private JButton btnAnnuler = new JButton("Annuler");
    private JButton btnAjouterCommentaire = new JButton("Ajouter commentaire");

    private JTextField txtRechercher = new JTextField();
    private JButton btnRechercher = new JButton("Rechercher");

    private JTable tableauCommentaire = new JTable();
    List<Commentaire> commentaires = new ArrayList<>();
    List <Commentaire> listRechercheCommentaires = new ArrayList<>();

    Annonce annonce;
    Utilisateur utilisateur;
    Commentaire commentaire, blankCommentaire;
    ImageIcon icone = new ImageIcon("LogoIconeDSI.png");


    /************************************************************/
    /******************** Constructeur par defaut****************/
    /************************************************************/
    public PageCommentaires() {
        initialiserComposants();
    }

    public PageCommentaires(Annonce annonce) {
        this.annonce = annonce;
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

        /*********************Panel Principal******************************************/
        panPrincipal.setLayout(new BorderLayout());
        panPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panPrincipal.setBackground(Color.decode("#11417d"));
        panPrincipal.add(panHaut, BorderLayout.NORTH);
        panPrincipal.add(panCentre, BorderLayout.CENTER);
        panPrincipal.add(panBas, BorderLayout.SOUTH);

        /*********************Panel haut******************************************/
        panHaut.setPreferredSize(new Dimension(900, 100));
        txtRechercher.setText(" Rechercher par mot(s) clé(s) ");
        panHaut.add(txtRechercher);
        panHaut.add(btnRechercher);

        /*********************Panel centre******************************************/
        panCentre.setPreferredSize(new Dimension(900, 250));
        panCentre.setLayout(new BorderLayout());
        panCentre.add(tableauCommentaire.getTableHeader(), BorderLayout.NORTH);
        panCentre.add(tableauCommentaire, BorderLayout.CENTER);
        panCentre.add(new JScrollPane(tableauCommentaire), BorderLayout.CENTER);
        tableauCommentaire.setRowHeight(30);

        /*********************Panel Bas******************************************/
        panBas.setSize(500, 200);
        panBas.add(btnAjouterCommentaire);
        panBas.add(btnModifierCommentaire);
        panBas.add(btnSupprimerCommentaire);
        panBas.add(btnAnnuler);

        setContentPane(panPrincipal);

        displayRightTable();


        /**************************************************************************************************************************************/
        /*************************************************************** Les listenners *******************************************************/
        /**************************************************************************************************************************************/

        txtRechercher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtRechercher.setText("");
            }
        });

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

        btnAnnuler.addActionListener(e -> {
            txtRechercher.setText(" Rechercher par mot(s) clé(s) ");
            blankCommentaire = null;
            commentaire = null;
            displayRightTable();
        });

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
                    afficheJTableCommentaires();
                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
                tableauCommentaire.clearSelection();
            }
            displayRightTable();
        });

        /**
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

            for (int i = 0; i < allCommentaires.size(); i++) {
                int commentaireId = allCommentaires.get(i).getCommentaire_id();
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
                blankCommentaire.setCommentaire_utilisateur_id(2);
            }
            if (annonce != null){
                blankCommentaire.setCommentaire_annonce_id(annonce.getAnnonce_id());
            }else{
                blankCommentaire.setCommentaire_annonce_id(2);
            }

            try {
                CommentaireManager.getInstance().insert(blankCommentaire);
                JOptionPane.showMessageDialog(btnAjouterCommentaire, "Commentaire " + blankCommentaire.getCommentaire_id()+ " ajouté");
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

        /**
         * listenner sur le bouton Enregistrer les modifications dans la base
         */
        btnModifierCommentaire.setSize(140, 50);
        btnModifierCommentaire.addActionListener(e -> {

            /** Récupérer les valeurs du tableauSortie, on boucle pour chaque ligne */
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

                if (commentaireIdAnnonceModifie ==0 || commentaireIdUtilisateurModifie ==0){
                    JOptionPane.showMessageDialog(null, "Merci de corriger les champs IidUtilisateur et/ou IdAnnonce");
                    return;
                }

                tableauCommentaire.setValueAt(commentaireMessageModifie, i,0);
                tableauCommentaire.setValueAt(commentaireNoteModifie, i,2);
                tableauCommentaire.setValueAt(idCommentaireModifie, i,3);
                tableauCommentaire.setValueAt(commentaireIdUtilisateurModifie, i,4);
                tableauCommentaire.setValueAt(commentaireIdAnnonceModifie, i,5);


                if (commentaire == null) {
                    //JOptionPane.showMessageDialog(btnModifierCommentaire, "Merci d'ajouter le message");
                    return;
                } else {
                    /*** ENREGISTRER LES VALEURS DS LA BASE ***/
                    if (commentaire.getCommentaire_note() != (commentaireNoteModifie) || !commentaire.getCommentaire_message().equalsIgnoreCase(commentaireMessageModifie) || !(commentaire.getCommentaire_id() == idCommentaireModifie)
                            || !(commentaire.getCommentaire_annonce_id() == commentaireIdAnnonceModifie) || !(commentaire.getCommentaire_utilisateur_id() == commentaireIdUtilisateurModifie)) {
                        commentaire.setCommentaire_message(commentaireMessageModifie);
                        commentaire.setCommentaire_id(idCommentaireModifie);
                        commentaire.setCommentaire_note(commentaireNoteModifie);
                        commentaire.setCommentaire_annonce_id(commentaireIdAnnonceModifie);
                        commentaire.setCommentaire_utilisateur_id(commentaireIdUtilisateurModifie);

                        int j = JOptionPane.showConfirmDialog(btnModifierCommentaire, "La modification est irréversible. Êtes-vous sûr de vouloir continuer ?",
                                "Veuillez confirmer votre choix",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);

                        if (j == 0)  /**user a dit oui*/ {
                            try {
                                    CommentaireManager.getInstance().update(commentaire);
                                    JOptionPane.showMessageDialog(btnModifierCommentaire, "Commentaire " + commentaire.getCommentaire_id() + " enregistré");
                                    displayRightTable();
                                    break;

                            } catch (BLLException ex) {
                                ex.printStackTrace();
                            }
                        } else {break;}
                    }
                }
            }//fin for
        });


        /**
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

    private void displayRightTable(){
        if (annonce ==  null && utilisateur == null){
            afficheJTableCommentaires();
        } else if (utilisateur == null){
            afficheJTableCommentairesWithIdAnnonce();
        } else if (annonce == null){
            afficheJTableCommentairesWithIdUtilisateur();
        }
    }

    private void afficheJTableCommentaires() {
        try {
            commentaires = remplirJTableWithCommentaires();
            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            tableauCommentaire.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }

    } //fin afficheJTable

    private void afficheJTableCommentairesWithIdAnnonce() {
        try {
            commentaires = remplirJTableWithCommentairesIdAnnonce(annonce.getAnnonce_id());
            TableModelCommentaire model = new TableModelCommentaire(commentaires);
            tableauCommentaire.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }

    } //fin afficheJTable


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

