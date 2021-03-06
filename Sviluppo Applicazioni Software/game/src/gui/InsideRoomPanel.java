/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.gui;

import java.awt.Rectangle;
import javax.swing.JOptionPane;
import unoxtutti.UnoXTutti;
import unoxtutti.domain.ServerGame;
import unoxtutti.domain.RemoteRoom;
import unoxtutti.domain.Player;

/**
 * Questo pannello fornisce informazioni su una stanza e d&agrave; accesso alle
 * operazioni che l'utente pu&ograve; eseguire appena entrato in una stanza
 *
 * @author picardi
 */
public class InsideRoomPanel extends MainWindowSubPanel {

    private RemoteRoom currentRoom;

    /**
     * Creates new form InsideRoomPanel
     */
    public InsideRoomPanel() {
        initComponents();
    }

    @Override
    public void initializeContent() {
        currentRoom = UnoXTutti.theUxtController.getCurrentRoom();
        this.playersList.setModel(currentRoom.getPlayersAsList());
        this.partiteList.setModel(currentRoom.getPartiteAsList());
        this.roomNameLabel.setText("Stanza: " + currentRoom.getName());
        this.Entra.setEnabled(false);
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        javax.swing.JSplitPane jSplitPane1 = new javax.swing.JSplitPane();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        playersList = new javax.swing.JList<>();
        roomPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(150, 0), new java.awt.Dimension(32767, 0));
        Entra = new javax.swing.JButton();
        javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
        partiteList = new javax.swing.JList<>();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        roomNameLabel = new javax.swing.JLabel();
        roomExitButton = new javax.swing.JButton();

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setLayout(new java.awt.BorderLayout());

        jSplitPane1.setDividerLocation(200);

        jScrollPane1.setMinimumSize(new java.awt.Dimension(150, 23));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 140));

        playersList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        playersList.setMinimumSize(new java.awt.Dimension(150, 85));
        playersList.setPreferredSize(new java.awt.Dimension(150, 85));
        jScrollPane1.setViewportView(playersList);

        jSplitPane1.setLeftComponent(jScrollPane1);

        roomPanel.setPreferredSize(new java.awt.Dimension(100, 399));
        roomPanel.setLayout(new java.awt.BorderLayout());

        jButton2.setText("Crea Partita");
        jButton2.setActionCommand("CreaPartita");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreaPartiaActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jPanel1.add(filler1);

        Entra.setText("Entra");
        Entra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EntraActionPerformed(evt);
            }
        });
        jPanel1.add(Entra);

        roomPanel.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jScrollPane2.setMinimumSize(new java.awt.Dimension(150, 23));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(200, 140));

        partiteList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        partiteList.setMinimumSize(new java.awt.Dimension(150, 85));
        partiteList.setPreferredSize(new java.awt.Dimension(150, 85));
        partiteList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                partiteListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(partiteList);

        roomPanel.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jSplitPane1.setRightComponent(roomPanel);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel2.setLayout(new java.awt.BorderLayout(5, 0));

        roomNameLabel.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        roomNameLabel.setText("Stanza:");
        jPanel2.add(roomNameLabel, java.awt.BorderLayout.CENTER);

        roomExitButton.setText("Esci");
        roomExitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomExitButtonActionPerformed(evt);
            }
        });
        jPanel2.add(roomExitButton, java.awt.BorderLayout.LINE_END);

        add(jPanel2, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void roomExitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomExitButtonActionPerformed
        UnoXTutti.theUxtController.esciDaStanza();
        mainWindow.setGuiState(UnoXTuttiGUI.GUIState.OUTSIDE_ROOM);
    }//GEN-LAST:event_roomExitButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void CreaPartiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreaPartiaActionPerformed
        System.out.println("lallaalalalal");
        CreaPartitaDialog dia = new CreaPartitaDialog(mainWindow, true);
        Rectangle mainrect = this.mainWindow.getBounds();
        Rectangle diarect = dia.getBounds();
        dia.setBounds((int) (mainrect.getCenterX() - diarect.getWidth() / 1.5),
                (int) (mainrect.getCenterY() - diarect.getHeight() / 1.5), (int) diarect.getWidth(), (int) diarect.getHeight());
        dia.setVisible(true);
    
        if (dia.getResult() == JOptionPane.OK_OPTION) {
            System.out.println(dia.getPartitaName() + dia.getPartitaGioc() + dia.getPartitaMod());
       
             UnoXTutti.theUxtController.setPanel(this);
            UnoXTutti.theUxtController.apriPartita(dia.getPartitaName(), dia.getPartitaGioc(), dia.getPartitaMod());//richiamo apri partita del controller 
        }
    }//GEN-LAST:event_CreaPartiaActionPerformed

    private void EntraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EntraActionPerformed
        String sel = partiteList.getSelectedValue();
        String result = sel.substring(sel.indexOf("codice"), sel.length());
        String[] parts = result.split(" ");

        this.mainWindow.setWaiting(true);
        UnoXTutti.theUxtController.entraInPartita(Integer.parseInt(parts[1]));
        this.mainWindow.setWaiting(false);

        if (UnoXTutti.theUxtController.inPartita()) {
            
         
            this.mainWindow.setGuiState(UnoXTuttiGUI.GUIState.INSIDE_PARTITA);
               UnoXTutti.theUxtController.setPanel(this);
        }

    }//GEN-LAST:event_EntraActionPerformed

    private void partiteListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_partiteListValueChanged
        if (!evt.getValueIsAdjusting()) {
            int sel = partiteList.getSelectedIndex();
            this.Entra.setEnabled(true);
        }
    }//GEN-LAST:event_partiteListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Entra;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JList<String> partiteList;
    private javax.swing.JList<Player> playersList;
    private javax.swing.JButton roomExitButton;
    private javax.swing.JLabel roomNameLabel;
    private javax.swing.JPanel roomPanel;
    // End of variables declaration//GEN-END:variables
}
