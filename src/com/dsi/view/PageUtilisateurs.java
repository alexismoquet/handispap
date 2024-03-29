package com.dsi.view;

import com.dsi.controller.tableModels.TableModelUtilisateur;
import com.dsi.model.beans.Utilisateur;
import com.dsi.model.bll.BLLException;
import com.dsi.model.bll.UtilisateurManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.dsi.controller.Utilisateurs.remplirJTableWithAllUtilisateurs;

/**
 * Classe Page Utilisateurs
 *
 * @author Alexis Moquet
 * @since Créé le 04/02/2020
 */
public class PageUtilisateurs extends JFrame {

    private final JPanel panPrincipal = new JPanel();
    private final JPanel panHaut = new JPanel();
    private final JPanel panCentre = new JPanel();
    private final JPanel panBas = new JPanel();
    private final JButton btnAjouterUtilisateur = new JButton("Ajouter");
    private final JButton btnEnrModifUtil = new JButton("Enregistrer");
    private final JButton btnSupprimerUtil = new JButton("Supprimer");
    private final JButton btnAnnuler = new JButton("Annuler");
    private final JButton btnAnnonce = new JButton("Annonces");
    private final JButton btnAdresses = new JButton("Adresses");
    private final JButton btnMateriels = new JButton("Materiels");
    private final JButton btnCommentaires = new JButton("Commentaires");
    private final JButton btnRechercher = new JButton("Rechercher");
    private final JTextField txtRechercher = new JTextField(" Rechercher un utilisateur par Nom ");
    private final JTable tableauUtilisateurs = new JTable();

    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private List<Utilisateur> listRechercheUtilisateurs = new ArrayList<>();
    private Utilisateur utilisateur;
    private Utilisateur blankUtilisateur;
    boolean verifSiAjoutLigne = false;

    ImageIcon icone = new ImageIcon("LogoIconeDSI.png");
    
    private static final String  selectUser = "Veuillez sélectionner un utilisateur";

    /**
     * Constructeur par defaut
     */
    public PageUtilisateurs() {
        initialiserComposants();
    }

    /**
     * Méthode qui affiche le graphisme de la page
     */
    public void initialiserComposants() {
        setTitle("Utilisateurs");
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

        panHaut.setPreferredSize(new Dimension(900, 75));
        panHaut.setLayout(new BorderLayout());
        panHaut.add(txtRechercher, BorderLayout.CENTER);
        panHaut.add(btnRechercher, BorderLayout.NORTH);

        panHaut.add(new JLabel("   Attention, si changement du mot de passe," +
         " veuillez créer un nouvel utilisateur  "), BorderLayout.SOUTH);

        //Panel centre
        panCentre.setPreferredSize(new Dimension(900, 250));
        panCentre.setLayout(new BorderLayout());
        panCentre.add(tableauUtilisateurs.getTableHeader(), BorderLayout.NORTH);
        panCentre.add(tableauUtilisateurs, BorderLayout.CENTER);
        panCentre.add(new JScrollPane(tableauUtilisateurs), BorderLayout.CENTER);
        tableauUtilisateurs.setRowHeight(30);

        panBas.setSize(900, 100);
        panBas.add(btnEnrModifUtil);
        panBas.add(btnAjouterUtilisateur);
        panBas.add(btnSupprimerUtil);
        panBas.add(btnAnnuler);
        panBas.add(btnAnnonce);
        panBas.add(btnMateriels);
        panBas.add(btnCommentaires);
        panBas.add(btnAdresses);
        tableauUtilisateurs.setAutoCreateRowSorter(true);

        setContentPane(panPrincipal);

        afficheJTableUtilisateurs();

        /*
         * MouseListenner sur JTextField rechercher
         **/
        txtRechercher.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtRechercher.setText("");
            }
        });

        /*
         * Listenner sur le bouton btnRechercher
         **/
        btnRechercher.addActionListener(e -> {
            listRechercheUtilisateurs = new ArrayList<>();
            try {
                UtilisateurManager.getInstance().SelectAll();  //retourne une list d'utilisateurs = utilisateurs
            } catch (BLLException ex) {
                ex.printStackTrace();
            }
            for (Utilisateur utilisateurRech : utilisateurs) {
                String nom = utilisateurRech.getNom().toLowerCase();
                String prenom = utilisateurRech.getPrenom().toLowerCase();
                String recherche = txtRechercher.getText().toLowerCase();

                if (nom.contains(recherche) || prenom.contains(recherche)) {
                    listRechercheUtilisateurs.add(utilisateurRech);
                    TableModelUtilisateur model;
                    model = new TableModelUtilisateur(listRechercheUtilisateurs);
                    tableauUtilisateurs.setModel(model);
                }
            }
            if (listRechercheUtilisateurs.isEmpty()) {
                JOptionPane.showMessageDialog(panPrincipal, "Aucun utilisateur trouvé", "warning", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        /*
         * Listener bouton annuler
         */
        btnAnnuler.addActionListener(e -> {
            txtRechercher.setText(" Rechercher un utilisateur par Nom ");
            utilisateur = null;
            blankUtilisateur = null;
            afficheJTableUtilisateurs();
        });


        /**
         * listenner sur le btnAjouterUtilisateur pour ajouter une ligne vierge
         * @param: blankUtilisateur
         */
        btnAjouterUtilisateur.addActionListener(e -> {
            verifSiAjoutLigne = true;
            blankUtilisateur = new Utilisateur();

            //////  On récupére la plus haute id du tableu pour assigner blankSport à 1 au dessus ////////////////
            if (!(utilisateurs.isEmpty())) {
                List<Utilisateur> allUtilisateurs = null;
                try {
                    allUtilisateurs = UtilisateurManager.getInstance().SelectAll();
                } catch (BLLException bllException) {
                    bllException.printStackTrace();
                }
                assert allUtilisateurs != null;
                int idMax = allUtilisateurs.get(0).getIdUtilisateur();

                for (Utilisateur allUtilisateur : allUtilisateurs) {
                    int utilisateurId = allUtilisateur.getIdUtilisateur();
                    if (utilisateurId > idMax) {
                        idMax = utilisateurId;
                    }
                }
                blankUtilisateur.setIdUtilisateur(idMax+1);
            } else{
                blankUtilisateur.setIdUtilisateur(1);
            }
            blankUtilisateur.setNom("");
            blankUtilisateur.setPrenom("");
            blankUtilisateur.setEmail("");
            blankUtilisateur.setTelMob("");
            blankUtilisateur.setTelFix("");
            blankUtilisateur.setMotDePasse("");
            blankUtilisateur.setDateInscription(new Date());

            try {
                String input = JOptionPane.showInputDialog(null, "Merci de saisir le  MOT DE PASSE qui sera crypté");
                blankUtilisateur.setMotDePasse(input);

                UtilisateurManager.getInstance().insert(blankUtilisateur);
            } catch (BLLException bllException) {
                bllException.printStackTrace();
            }

            utilisateurs.add(blankUtilisateur);

            TableModelUtilisateur model = new TableModelUtilisateur(utilisateurs);
            tableauUtilisateurs.setModel(model);

            JOptionPane.showMessageDialog(null, "Veuillez créer une ADRESSE afin de créer un MATERIEL puis une ANNONCE");

            blankUtilisateur = null;

            afficheJTableUtilisateurs();
        });

        /*
         * Listener bouton Modifier
         */
        btnEnrModifUtil.addActionListener(e -> {
            /* Récupérer les valeurs du tableauUtilisateurs, on boucle pour chaque ligne */
            for (int i = 0; i < tableauUtilisateurs.getRowCount(); i++) {
                try {
                    utilisateur = UtilisateurManager.getInstance().SelectById((Integer) tableauUtilisateurs.getValueAt(i, 7));
                } catch (BLLException bllException) {
                    bllException.printStackTrace();
                }
                String nomUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 0));
                String prenomUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 1));
                String emailUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 2));
                String telMobUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 3));
                String telFixUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 4));
                String mdpUtilisateurModifie = String.valueOf(tableauUtilisateurs.getValueAt(i, 5));
                Date dateInscUtilisateurModifie = (Date) tableauUtilisateurs.getValueAt(i, 6);

                if (utilisateur == null) {
                    JOptionPane.showMessageDialog(null, selectUser);
                } else {
                    /* VERIFIE ET ENREGISTRE LES VALEURS DS LA BASE SI DIFFERENTES */
                    if (!utilisateur.getNom().equalsIgnoreCase(nomUtilisateurModifie) ||
                            !utilisateur.getPrenom().equalsIgnoreCase(prenomUtilisateurModifie) ||
                            !utilisateur.getEmail().equalsIgnoreCase(emailUtilisateurModifie) ||
                            !utilisateur.getTelMob().equalsIgnoreCase(telMobUtilisateurModifie) ||
                            !utilisateur.getTelFix().equalsIgnoreCase(telFixUtilisateurModifie) ||
                             !utilisateur.getMotDePasse().equalsIgnoreCase(mdpUtilisateurModifie)||
                           !(utilisateur.getDateInscription().equals(dateInscUtilisateurModifie))
                    ) {
                        utilisateur.setNom(nomUtilisateurModifie);
                        utilisateur.setPrenom(prenomUtilisateurModifie);
                        utilisateur.setEmail(emailUtilisateurModifie);
                        utilisateur.setTelMob(telMobUtilisateurModifie);
                        utilisateur.setTelFix(telFixUtilisateurModifie);
                        utilisateur.setMotDePasse(mdpUtilisateurModifie);
                        utilisateur.setDateInscription(dateInscUtilisateurModifie);

                        int j;
                        String confirmChoix = "Veuillez confirmer votre choix";
                        if (verifSiAjoutLigne) {
                            j = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de vouloir enregistrer l'utilisateur " + utilisateur.getIdUtilisateur() + " ?",
                                    confirmChoix,
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);
                            verifSiAjoutLigne = false;
                        } else {
                            j = JOptionPane.showConfirmDialog(null, "La modification est irréversible. Êtes-vous sûr de vouloir enregistrer l'utilisateur " + utilisateur.getIdUtilisateur() + " ?",
                                    confirmChoix,
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);
                        }
                        if (j == 0)  /*user a dit oui*/ {
                            try {
                                UtilisateurManager.getInstance().update(utilisateur);
                                JOptionPane.showMessageDialog(null, "Utilisateur " + utilisateur.getIdUtilisateur() + " enregistré");
                            } catch (BLLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }//fin for
            afficheJTableUtilisateurs();
        });

        /*
         *Listener bouton Supprimer
         */
        btnSupprimerUtil.addActionListener(e -> {
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(null, selectUser);
            return;
            }

            ///On supprime tous les utilisateurs sélectionnés
            int[] selection = tableauUtilisateurs.getSelectedRows();
            for (int j : selection) {
                utilisateur = utilisateurs.get(j);
                try {
                    utilisateur = UtilisateurManager.getInstance().SelectById(utilisateur.getIdUtilisateur());
                } catch (BLLException bllException) {
                    bllException.printStackTrace();
                }

                int i = JOptionPane.showConfirmDialog(null, "La suppression est irréversible. Êtes-vous sûr de vouloir supprimer l'utilisateur " + utilisateur.getIdUtilisateur() + " ?",
                        "Veuillez confirmer votre choix",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);

                ///////Joue un son qd suppression/////////////
//                InputStream in = null;
//                try {
//                    in = new FileInputStream("0200.mp3");
//                } catch (FileNotFoundException fileNotFoundException) {
//                    fileNotFoundException.printStackTrace();
//                }
////                AudioStream as = null;
////                try {
////                    as = new AudioStream(in);
////                } catch (IOException ioException) {
////                    ioException.printStackTrace();
////                }
//                AudioPlayer.player.start(in);
//                AudioPlayer.player.stop(in);
                ////////////////////////////////

                if (i == 0)  /*user a dit oui*/ {
                    try {
                        UtilisateurManager.getInstance().delete(utilisateur);
                        JOptionPane.showMessageDialog(null, "Utilisateur " + utilisateur.getIdUtilisateur() + " supprimé");

                    } catch (BLLException ex) {
                        ex.printStackTrace();
                    }
                    tableauUtilisateurs.clearSelection();
                }
            }//fin for
            afficheJTableUtilisateurs();
     });

        /*
         * Mouse listenner sur le tableau utilisateur
         */
        tableauUtilisateurs.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idUserSelected = (int) tableauUtilisateurs.getValueAt(tableauUtilisateurs.getSelectedRow(), 7);
                try {
                    utilisateur = UtilisateurManager.getInstance().SelectById(idUserSelected);
                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * listenner sur le bouton annonce
         * @param utilisateur
         */
        btnAnnonce.addActionListener(e -> {
            afficheJTableUtilisateurs();
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(null, selectUser);
            } else {
                new PageAnnonces(utilisateur);
            }
        });

        /*
         * listenner sur le bouton adresse
         */
        btnAdresses.addActionListener(e -> {
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(null, selectUser);
            } else {
                new PageAdresses(utilisateur);
            }
        });

        /*
         * listenner sur le bouton materiel
         */
        btnMateriels.addActionListener(e -> {
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(null, selectUser);
            } else {
                new PageMateriels(utilisateur);
            }
        });

        /*
         * listenner sur le bouton commentaire
         */
        btnCommentaires.addActionListener(e -> {
            if (utilisateur == null) {
                JOptionPane.showMessageDialog(null, selectUser);
            } else {
                new PageCommentaires(utilisateur);
            }
        });

    } //fin initialiserComposants


    /**
     * Méthode qui affiche tous les utilisateurs
     */
    private void afficheJTableUtilisateurs() {
        try {
            utilisateurs = remplirJTableWithAllUtilisateurs();
            TableModelUtilisateur model = new TableModelUtilisateur(utilisateurs);
            tableauUtilisateurs.setModel(model);

                /////Vérifie si il y a au moins une adresse pour un utilisateur
                for (Utilisateur utilisateurrr : utilisateurs) {
                    if (utilisateurrr.getAdresses().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Aucune ADRESSE n'est renseignée pour l'utilisateur " + utilisateurrr.getIdUtilisateur(), "ATTENTION", 2);
                    }
            }
        } catch (BLLException ex) {
            ex.printStackTrace();
        }
    } //fin afficheJTable

}// fin class


