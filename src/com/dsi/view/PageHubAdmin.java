package com.dsi.view;

import com.dsi.controller.tableModel.TableModelAnnonce;
import com.dsi.model.beans.Annonce;
import com.dsi.model.bll.AnnonceManager;
import com.dsi.model.bll.BLLException;
import org.mariadb.jdbc.internal.com.read.resultset.SelectResultSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * Classe HubAdmin
 *
 * @author Alexis Moquet
 * @since Créé le 04/02/2020
 */
public class PageHubAdmin extends JFrame {
    private JPanel panPrincipal = new JPanel();
    private JPanel panBtn = new JPanel();
    private JPanel panAnomalies = new JPanel();

    private JButton bUtilisateurs = new JButton("Utilisateurs");
    private JButton bCategories = new JButton("Catégories");
    private JButton bSports = new JButton("Sports");
    private JButton bCommentaires = new JButton("Commentaires");
    private JButton bEnregistrer = new JButton("Enregistrer");
    private JButton bAnnonces = new JButton("Annonces");
    private JButton bMateriels = new JButton("Materiels");
    private JButton bVisuels = new JButton("Visuels");
    private JButton bSorties = new JButton("Sorties");


    private JTable tableauAnomalies = new JTable();

    private JLabel anomaliesASurveiller = new JLabel();

    List<Annonce> listAnomalies = new ArrayList<>();
    List<Annonce> annonces;
    Annonce annonceSelect, annonce;
    ImageIcon icone = new ImageIcon("LogoIconeDSI.png");
    String titreAnnonceModifiee;
    String descriptionAnnonceModifiee;


    //************************************************************
    // Constructeur par defaut
    //************************************************************
    public PageHubAdmin() throws BLLException {
        initialiserComposants();
    }

    //************************************************************
    // Methode qui va charger les composants de la fenetre
    //************************************************************
    public void initialiserComposants() throws BLLException {

        setTitle("HANDISPAP");
        setIconImage(Toolkit.getDefaultToolkit().getImage("LogoIconeDSI.png"));
        setSize(1000, 600);
        setLocation(200, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);

        /***** Panel principal  */
        panPrincipal.setBackground(Color.decode("#11417d"));
        panPrincipal.setLayout(new BorderLayout());
        panPrincipal.add(panBtn, BorderLayout.NORTH);
        panPrincipal.add(panAnomalies, BorderLayout.CENTER);

        /***** Panel des boutons*/
        panBtn.setBackground(Color.decode("#11417d")); //bleu
        panBtn.setPreferredSize(new Dimension(900, 70));
        panBtn.setBorder(new EmptyBorder(10, 10, 0, 10));
        panBtn.add(bUtilisateurs);
        panBtn.add(bCategories);
        panBtn.add(bSports);
        panBtn.add(bAnnonces);
        panBtn.add(bMateriels);
        panBtn.add(bVisuels);
        panBtn.add(bSorties);
        panBtn.add(bCommentaires);
        panBtn.add(bEnregistrer);

        /***** Panel des anomalies*/
        anomaliesASurveiller.setSize(200, 100);
        anomaliesASurveiller.setBackground(Color.white);
        anomaliesASurveiller.setText("<html><body><font color='white'>Anomalies de texte  dans les annonces à surveiller :</body></html>");
        anomaliesASurveiller.setToolTipText(anomaliesASurveiller.getText());

        panAnomalies.setBackground(Color.decode("#11417d")); //rouge = #7d1111
        tableauAnomalies.setGridColor(Color.decode("#7d1111"));
        panAnomalies.add(tableauAnomalies);
        panAnomalies.setLayout(new BorderLayout());
        panAnomalies.add(anomaliesASurveiller, BorderLayout.NORTH);
        panAnomalies.add(new JScrollPane(tableauAnomalies), BorderLayout.CENTER);
        panAnomalies.setPreferredSize(new Dimension(900, 700));
        panAnomalies.setBorder(new EmptyBorder(5, 20, 20, 20));
        tableauAnomalies.setRowHeight(20);

        bUtilisateurs.setSize(100, 50);
        bUtilisateurs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageUtilisateurs pu = new PageUtilisateurs();
            }
        });

        bCategories.setSize(100, 50);
        bCategories.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageCategories pu = new PageCategories();
            }
        });

        bSports.setSize(100, 50);
        bSports.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageSports ps = new PageSports();
            }
        });

        bCommentaires.setSize(100, 50);
        bCommentaires.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageCommentaires pc = new PageCommentaires();
            }
        });
        bAnnonces.setSize(100, 50);
        bAnnonces.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageAnnonces pa = new PageAnnonces();
            }
        });

        bMateriels.setSize(100, 50);
        bMateriels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PageMateriels pm = new PageMateriels();
            }
        });

        bVisuels.setSize(100, 50);
        bVisuels.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { PageVisuels pv = new PageVisuels(); }
        });

        bSorties.setSize(100, 50);
        bSorties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { PageSorties ps = new PageSorties(); }
        });

        /**
         * Listener btnEnregistrer - enregistrer les modifs du tableau anomalies (annonces)
         */
        bEnregistrer.setSize(100, 50);
        bEnregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                AnnonceManager am = AnnonceManager.getInstance();

                /** Récupérer les valeurs du tableauAnomalies, on boucle pour chaque ligne */
                for (int i = 0; i < tableauAnomalies.getRowCount(); i++) {

                    try {
                        annonce = AnnonceManager.getInstance().SelectById((Integer) tableauAnomalies.getValueAt(i, 3));
                    } catch (BLLException bllException) {
                        bllException.printStackTrace();
                    }
                    String titreAnnonceModifiee = String.valueOf(tableauAnomalies.getValueAt(i, 0));
                    String descriptionAnnonceModifiee = String.valueOf(tableauAnomalies.getValueAt(i, 1));

                    tableauAnomalies.setValueAt(descriptionAnnonceModifiee, i, 1);
                    tableauAnomalies.setValueAt(titreAnnonceModifiee, i, 0);


                    /*** ENREGISTRER LES VALEURS DS LA BASE ***/
                    if (!annonce.getAnnonce_titre().equals(titreAnnonceModifiee) || !annonce.getAnnonce_description().equals(descriptionAnnonceModifiee)) {
                        try {
                            annonce.setAnnonce_description(descriptionAnnonceModifiee);
                            annonce.setAnnonce_titre(titreAnnonceModifiee);

                            int j = JOptionPane.showConfirmDialog(bEnregistrer, "La modification est irréversible. Êtes-vous sûr de vouloir continuer ?",
                                    "Veuillez confirmer votre choix",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icone);

                            if (j == 0)  /**user a dit oui*/ {
                                am.update(annonce);
                                JOptionPane.showMessageDialog(null, "Annonce " + tableauAnomalies.getValueAt(i, 3) + " modifiée");
                                remplirJTableWithAnomalies();
                            }
                        } catch (BLLException bllException) {
                            bllException.printStackTrace();
                        }
                    }
                }//fin boucle for

                tableauAnomalies.clearSelection();

            }//fin actionPerformed

        });

        remplirJTableWithAnomalies();

        setContentPane(panPrincipal);

    }//fin initialiser composants


    private void remplirJTableWithAnomalies() throws BLLException {
        listAnomalies = new ArrayList<>();
        AnnonceManager am = new AnnonceManager();
        
        annonces = am.SelectAll();

        for (int i = 0; i < annonces.size(); i++) {
            annonceSelect = annonces.get(i);
            String titreAnnonceSelect = annonces.get(i).getAnnonce_titre().toLowerCase();
            String descriptionAnnonceSelect = annonces.get(i).getAnnonce_description().toLowerCase();

            if (titreAnnonceSelect.contains("sex") || descriptionAnnonceSelect.contains("sex")) {
                listAnomalies.add(annonceSelect);
            }
        }
        TableModelAnnonce model = new TableModelAnnonce(listAnomalies);
        tableauAnomalies.setModel(model);

    }//fin JTable

}//fin class