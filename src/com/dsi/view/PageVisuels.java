package com.dsi.view;

import com.dsi.controller.tableModel.*;
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

import static com.dsi.controller.Visuels.remplirJTableWithAllVisuels;
import static com.dsi.controller.Visuels.remplirJTableWithVisuelsIdMateriel;


/**
 * Classe PageVisuels
 *
 * @author Alexis Moquet
 * @since Créé le 04/02/2020
 */
public class PageVisuels extends JFrame {

    private JPanel panPrincipal = new JPanel();
    private JPanel panHaut = new JPanel();
    private JPanel panCentre = new JPanel();
    private JPanel panBas = new JPanel();

    private JButton btnEnregistrerVisuel = new JButton("Enregistrer");
    private JButton btnSupprimerVisuel = new JButton("Supprimer");
    private JButton btnAnnuler = new JButton("Annuler");
    private JButton btnAjouterVisuel = new JButton("Ajouter");

    //     private JTextField txtRechercher = new JTextField();
    //     private JButton btnRechercher = new JButton("Rechercher");

    private JTable tableauVisuel = new JTable();
    List<Visuel> visuels = new ArrayList<>();
    //  List <Visuel> listRechercheVisuels = new ArrayList<>();
    Visuel visuel, blankVisuel;
    ImageIcon icone = new ImageIcon("LogoIconeDSI.png");
    Materiel materiel;


    /************************************************************/
    /******************** Constructeur par defaut****************/
    /************************************************************/
    public PageVisuels() {
        initialiserComposants();
    }

    public PageVisuels(Materiel pMateriel) {
        this.materiel = pMateriel;
        initialiserComposants();
    }

    public void initialiserComposants() {
        setTitle("Visuels");
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
        //  txtRechercher.setText("     Rechercher par id visuel   ");
        //  panHaut.add(txtRechercher);
        // panHaut.add(btnRechercher);

        //Panel centre
        panCentre.setPreferredSize(new Dimension(900, 250));
        panCentre.setLayout(new BorderLayout());
        panCentre.add(tableauVisuel.getTableHeader(), BorderLayout.NORTH);
        panCentre.add(tableauVisuel, BorderLayout.CENTER);
        panCentre.add(new JScrollPane(tableauVisuel), BorderLayout.CENTER);
        tableauVisuel.setRowHeight(40);

        panBas.setSize(500, 200);

        if (materiel != null) {
            panBas.add(btnAjouterVisuel);
        }
        panBas.add(btnEnregistrerVisuel);
        panBas.add(btnSupprimerVisuel);
        panBas.add(btnAnnuler);

        setContentPane(panPrincipal);

        displayRightTable();

        /**************************************************************************************************************************************/
        /*************************************************************** Les listenners *******************************************************/
        /**************************************************************************************************************************************/

//            txtRechercher.addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    JTextField txtRechercher = ((JTextField) e.getSource());
//                    txtRechercher.setText("");
//                    txtRechercher.removeMouseListener(this);
//                }
//            });

//            btnRechercher.addActionListener(e -> {
//                listRechercheVisuels = new ArrayList<>();
//                VisuelManager um = VisuelManager.getInstance();
//                try {
//                    um.SelectAll();
//                } catch (BLLException ex) {
//                    ex.printStackTrace();
//                }
//                for (Visuel visuel : visuels) {
//                    String sp = String.valueOf(visuel.getVisuel_id());
//                    String recherche = txtRechercher.getText().toLowerCase();
//
//                    if (sp.equals(recherche)) {
//                        listRechercheVisuels.add(visuel);
//                        TableModelVisuel model = new TableModelVisuel(listRechercheVisuels);
//                        tableauVisuel.setModel(model);
//                    }
//                }
//                if (listRechercheVisuels.size() == 0) {
//                    JOptionPane.showMessageDialog(panPrincipal, "Aucun visuel trouvé", "warning", JOptionPane.INFORMATION_MESSAGE);
//                }
//            });

        btnAnnuler.addActionListener(e -> {
            //      txtRechercher.setText("");
            visuel = null;
            if (materiel == null) {
                afficheJTableWithAllVisuels();
            } else {
                afficheJTableVisuelsWithIdMateriel(materiel.getMateriel_id());
            }
        });

        /**
         * Bouton supprimer le visuel
         */
        btnSupprimerVisuel.addActionListener(e -> {
            if (visuel == null) {
                JOptionPane.showMessageDialog(btnSupprimerVisuel, "Merci de sélectionner un visuel");
                return;
            }
            VisuelManager am = VisuelManager.getInstance();

            int i = JOptionPane.showConfirmDialog(btnSupprimerVisuel, "La suppression est irréversible. Êtes-vous sûr de vouloir supprimer le visuel " + visuel.getVisuel_id() + " ?",
                    "Veuillez confirmer votre choix",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);
            if (i == 0) //user a dit oui
            {
                try {
                    am.delete(visuel);
                    JOptionPane.showMessageDialog(btnSupprimerVisuel, "Visuel " + visuel.getVisuel_id() + " supprimé");
                    if (materiel == null) {
                        afficheJTableWithAllVisuels();
                    } else {
                        afficheJTableVisuelsWithIdMateriel(materiel.getMateriel_id());
                    }
                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
                tableauVisuel.clearSelection();
            }

        });

        /**
         * listenner sur le btnajouterVisuel pour ajouter une ligne vierge
         */
        btnAjouterVisuel.setSize(140, 50);
        btnAjouterVisuel.addActionListener(e -> {
            List<Visuel> allVisuels = null;
            VisuelManager sm = new VisuelManager();
            try {
                allVisuels = VisuelManager.getInstance().SelectAll();
            } catch (BLLException bllException) {
                bllException.printStackTrace();
            }

            blankVisuel = new Visuel();
            visuels.add(blankVisuel);

            //////  On récupére la plus haute id du tableu pour assigner blankSortie à 1 au dessus ////////////////
            assert allVisuels != null;
            int idMax = allVisuels.get(0).getVisuel_id();

            for (int i = 0; i < allVisuels.size(); i++) {
                int sortieId = allVisuels.get(i).getVisuel_id();
                if (sortieId > idMax) {
                    idMax = sortieId;
                }
            }
            blankVisuel.setVisuel_id(idMax + 1);
            blankVisuel.setVisuel_nom_fichier("");

            if (materiel == null) {
                blankVisuel.setVisuel_materiel_id(1);
            } else {
                blankVisuel.setVisuel_materiel_id(materiel.getMateriel_id());
            }

            try {
                VisuelManager.getInstance().insert(blankVisuel);
            } catch (BLLException bllException) {
                bllException.printStackTrace();
            }
            //////////////////////////////////////////////////////////////////////////////////////////////////////

            TableModelVisuel model = new TableModelVisuel(visuels);
            model.fireTableDataChanged();
            tableauVisuel.revalidate();
            tableauVisuel.setModel(model);

            blankVisuel = null;

            displayRightTable();
        });

        /**
         * Bouton Modifier le visuel
         */
        btnEnregistrerVisuel.addActionListener(e -> {

            /** Récupérer les valeurs du tableauUtilisateur, on boucle pour chaque ligne */
            for (int i = 0; i < tableauVisuel.getRowCount(); i++) {
                try {
                    visuel = VisuelManager.getInstance().SelectById((Integer) tableauVisuel.getValueAt(i, 2));
                } catch (BLLException bllException) {
                    bllException.printStackTrace();
                }
                String nomFichierVisuelModifie = String.valueOf(tableauVisuel.getValueAt(i, 1));
                int idMaterielVisuelModifie = (int) tableauVisuel.getValueAt(i, 3);

                tableauVisuel.setValueAt(nomFichierVisuelModifie, i, 1);
                tableauVisuel.setValueAt(idMaterielVisuelModifie, i, 3);

                if (visuel == null) {
                    return;
                } else {
                    /*** ENREGISTRER LES VALEURS DS LA BASE SI BESOIN ***/
                    if (!visuel.getVisuel_nom_fichier().equalsIgnoreCase(nomFichierVisuelModifie) || !(visuel.getVisuel_materiel_id() == idMaterielVisuelModifie)) {
                        visuel.setVisuel_nom_fichier(nomFichierVisuelModifie);
                        visuel.setVisuel_materiel_id(idMaterielVisuelModifie);

                        int j = JOptionPane.showConfirmDialog(btnEnregistrerVisuel, "La modification est irréversible. Êtes-vous sûr de vouloir enregistrer le visuel " + visuel.getVisuel_id() + " ?",
                                "Veuillez confirmer votre choix",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);

                        if (j == 0)  /**user a dit oui*/ {
                            try {
                                VisuelManager.getInstance().update(visuel);
                                JOptionPane.showMessageDialog(null, "Visuel " + visuel.getVisuel_id() + " enregistré");
                                break;
                            } catch (BLLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }//fin for
        });

        /**
         * Mouse listenner sur le tableau Visuel
         */
        tableauVisuel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idVisuelSelected = (int) tableauVisuel.getValueAt(tableauVisuel.getSelectedRow(), 2);
                Object imgVisuel = tableauVisuel.getValueAt(tableauVisuel.getSelectedRow(), 0);

                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    ImageCellRenderer icr = new ImageCellRenderer();
                    icr.getTableCellRendererComponent(tableauVisuel,tableauVisuel.getValueAt(tableauVisuel.getSelectedRow(), 1), true, true, tableauVisuel.getSelectedRow(), 0);
                }

//Gêne pour modifier une ligne du tableauVisuel //JOptionPane.showMessageDialog( null, "Le visuel " + idVisuelSelected + " est sélectionné");
                try {
                    visuel = VisuelManager.getInstance().SelectById(idVisuelSelected);

                } catch (BLLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }//fin initialiserComposants


    private void afficheJTableVisuelsWithIdMateriel(int idMateriel) {
        try {
            visuels = remplirJTableWithVisuelsIdMateriel(materiel.getMateriel_id());
            TableModelVisuel model = new TableModelVisuel(visuels);
            tableauVisuel.setModel(model);
        } catch (BLLException ex) {
            ex.printStackTrace();
        }
    } //fin afficheJTable

    private void afficheJTableWithAllVisuels() {
        try {
            visuels = remplirJTableWithAllVisuels();
            TableModelVisuel model = new TableModelVisuel(visuels);
            tableauVisuel.setModel(model);

        } catch (BLLException ex) {
            ex.printStackTrace();
        }
    } //fin afficheJTable


    public void afficheLeVisuel() throws BLLException {
        String adresseVisuel = "C:\\wamp64\\www\\handispap\\img\\mat";

        for (int i = 0; i < visuels.size(); i++) {
            String fichierVisuel = visuels.get(i).getVisuel_nom_fichier() + adresseVisuel;
            ImageIcon icone1 = new ImageIcon("C:\\wamp64\\www\\handispap\\img\\mat\\" + fichierVisuel);
            tableauVisuel.getValueAt(i, 0);
        }
    }

    private void displayRightTable() {
        if (materiel == null) {
            afficheJTableWithAllVisuels();
        } else {
            afficheJTableVisuelsWithIdMateriel(materiel.getMateriel_id());
        }
    }

}//fin class
