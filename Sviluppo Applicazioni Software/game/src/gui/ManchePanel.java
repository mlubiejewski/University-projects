/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.gui;

import java.awt.Color;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import unoxtutti.UnoXTutti;
import unoxtutti.domain.RemoteManche;
import unoxtutti.game.Card;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import static unoxtutti.UnoXTutti.theUxtController;
import unoxtutti.connection.P2PConnection;

/**
 *
 * @author joana
 */
public class ManchePanel extends MainWindowSubPanel implements Observer {

    /**
     * Creates new form OutsideRoomPanel
     */
    private RemoteManche currentManche;
    private P2PConnection conn;
    private boolean drew;
    private boolean uno;

    public ManchePanel() {
        carteInMano = new LinkedList();
        initComponents();

    }

    @Override
    public void initializeContent() {
        drew = false;
        uno = false;
        this.currentManche = UnoXTutti.theUxtController.getCurrentRoom().getPartita().getManche();
        // la currentManche= la mia remotemanche che viene osservata dal pannello (ManchePanel)
        this.currentManche.addObserver(this);

        this.lCarte.addListSelectionListener(new ListSelectionListener() {//aggiungo il listener che si attiva ogni volta che seleziono una carta

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting() && lCarte.getSelectedValue() != null) {
                    Card selectedCard = lCarte.getSelectedValue();
                    bUno.setEnabled(false);
                    bUno.setBackground(new Color(204, 0, 0));
                    bScarta.setEnabled(false);
                    tUno.setText("");
                    bInterrompi.setEnabled(false);
                    uno = false;
                    for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                        AbstractButton button = buttons.nextElement();

                        button.setEnabled(false);

                    }
                    if ((currentManche.getPlayerTurn().equals(UnoXTutti.theUxtController.getCurrentRoom().getConnectedPlayer().getName())) && !(currentManche.getLastAction().equals("bluff"))&&!currentManche.isEnded()) {//se è il mio turno
                        if (!drew) {
                            bPesca.setEnabled(true);
                        }
                        if (currentManche.getTopCard().getColore() == (selectedCard.getColore()) || currentManche.getTopCard().getNumero() == selectedCard.getNumero() || selectedCard.getTipo() == 'j' || selectedCard.getTipo() == 'a') {

                            bScarta.setEnabled(true);
                            if ((currentManche.getTopCard().getTipo() == selectedCard.getTipo() && selectedCard.getTipo() == 'a') && (currentManche.getTopCard().getColore() != (selectedCard.getColore()))) {
                                if (currentManche.getTopCard().getEffetto() == selectedCard.getEffetto()) {
                                    bScarta.setEnabled(true);
                                } else {
                                    bScarta.setEnabled(false);
                                }

                            } else if (((currentManche.getTopCard().getTipo() != selectedCard.getTipo()) && selectedCard.getTipo() == 'a')) {
                                if (currentManche.getTopCard().getColore() != (selectedCard.getColore())) {
                                    bScarta.setEnabled(false);
                                }

                            }
                            if (selectedCard.getTipo() == 'j' && (currentManche.countCard() != 1)) {

                                for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                                    AbstractButton button = buttons.nextElement();

                                    button.setEnabled(true);

                                }

                            } else if (selectedCard.getTipo() == 'j' && (currentManche.countCard() == 1)) {
                                bScarta.setEnabled(false);
                            }
                        }
                        if (bScarta.isEnabled() && (currentManche.countCard() == 2)) {
                            bUno.setEnabled(true);
                            bUno.setBackground(Color.green);

                        }

                    } else if (!(currentManche.getPlayerTurn().equals(UnoXTutti.theUxtController.getCurrentRoom().getConnectedPlayer().getName())) && !(currentManche.getLastAction().equals("bluff"))&&!currentManche.isEnded()) { //se non è il mio turno       
                        bScarta.setEnabled(false);
                        bPassa.setEnabled(false);
                        if ((selectedCard.getTipo() == currentManche.getTopCard().getTipo() && selectedCard.getColore() == currentManche.getTopCard().getColore()&&selectedCard.getNumero() == currentManche.getTopCard().getNumero()) || selectedCard.getTipo() == 'j') {
                            bInterrompi.setEnabled(true);
                            if (selectedCard.getTipo() == 'j') {
                                for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                                    AbstractButton button = buttons.nextElement();

                                    button.setEnabled(true);

                                }

                            }

                        }
                        /*if ((selectedCard.getTipo() == 'j' || selectedCard.getTipo() == 'a' || currentManche.getTopCard().getColore() == (selectedCard.getColore()) || currentManche.getTopCard().getNumero() == selectedCard.getNumero())) {
                            bInterrompi.setEnabled(true);
                            if ((currentManche.getTopCard().getTipo() == selectedCard.getTipo() && selectedCard.getTipo() == 'a') && (currentManche.getTopCard().getColore() != (selectedCard.getColore()))) {
                                if ((currentManche.getTopCard().getEffetto() == selectedCard.getEffetto())) {
                                    bInterrompi.setEnabled(true);
                                } else {
                                    bInterrompi.setEnabled(false);
                                }
                            } else if ((currentManche.getTopCard().getTipo() != selectedCard.getTipo() && selectedCard.getTipo() == 'a')) {
                                if (currentManche.getTopCard().getColore() != (selectedCard.getColore())) {
                                    bInterrompi.setEnabled(false);
                                }

                            }
                            if (selectedCard.getTipo() == 'j' && (currentManche.countCard() != 1)) {
                                for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                                    AbstractButton button = buttons.nextElement();

                                    button.setEnabled(true);

                                }

                            } else if (selectedCard.getTipo() == 'j' && (currentManche.countCard() == 1)){
                                bInterrompi.setEnabled(false);
                            }
                            if (bInterrompi.isEnabled() && (currentManche.countCard() == 2)) {
                                bUno.setEnabled(true);
                                bUno.setBackground(Color.green);

                            }
                        } else {

                            for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                                AbstractButton button = buttons.nextElement();

                                button.setEnabled(false);

                            }
                            bInterrompi.setEnabled(false);
                        }
                         */
                    }

                }
            }
        });

        /*this.stanzeList.setModel(UnoXTutti.theUxtController.getServerRoomNames());
         this.stanzeList.setSelectedIndex(-1);
         this.chiudiStanzaButton.setEnabled(false);
         this.infoButton.setEnabled(false);
         this.ipStanzaField.setText("");
         this.portaStanzaField.setText("");
         this.nomeStanzaField.setText("");
         */
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup5 = new javax.swing.ButtonGroup();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane1 = new javax.swing.JScrollPane();
    lCarte = new javax.swing.JList<>();
    jPanel1 = new javax.swing.JPanel();
    bPesca = new javax.swing.JButton();
    bScarta = new javax.swing.JButton();
    bUno = new javax.swing.JButton();
    bInterrompi = new javax.swing.JButton();
    bDidNotSayUno = new javax.swing.JButton();
    bPassa = new javax.swing.JButton();
    bBluff = new javax.swing.JButton();
    bNotBluff = new javax.swing.JButton();
    jLabelTurno = new javax.swing.JLabel();
    tUno = new javax.swing.JLabel();
    jLabelCartaTavolo = new javax.swing.JLabel();
    tTurno = new javax.swing.JLabel();
    jLabelTurno1 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    lNumCarte = new javax.swing.JList<>();
    jScrollPane3 = new javax.swing.JScrollPane();
    cartaSulTavolo = new javax.swing.JTextArea();
    jRadioButton1 = new javax.swing.JRadioButton();
    jRadioButton6 = new javax.swing.JRadioButton();
    jRadioButton7 = new javax.swing.JRadioButton();
    jRadioButton8 = new javax.swing.JRadioButton();
    jSeparator1 = new javax.swing.JSeparator();
    jScrollPane4 = new javax.swing.JScrollPane();
    AreaLog = new javax.swing.JTextArea();

    jLabel1.setFont(new java.awt.Font("Comic Sans MS", 0, 24)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("Carte");

    jScrollPane1.setViewportView(lCarte);

    jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

    bPesca.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
    bPesca.setText("Pesca");
    bPesca.setEnabled(false);
    bPesca.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bPescaActionPerformed(evt);
      }
    });

    bScarta.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
    bScarta.setText("Scarta");
    bScarta.setEnabled(false);
    bScarta.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bScartaActionPerformed(evt);
      }
    });

    bUno.setBackground(new java.awt.Color(204, 0, 0));
    bUno.setFont(new java.awt.Font("Comic Sans MS", 1, 14)); // NOI18N
    bUno.setText("UNO!");
    bUno.setEnabled(false);
    bUno.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bUnoActionPerformed(evt);
      }
    });

    bInterrompi.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
    bInterrompi.setText("Interrompi");
    bInterrompi.setEnabled(false);
    bInterrompi.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bInterrompiActionPerformed(evt);
      }
    });

    bDidNotSayUno.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
    bDidNotSayUno.setText("Non ha detto UNO!");
    bDidNotSayUno.setActionCommand("");
    bDidNotSayUno.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bDidNotSayUnoActionPerformed(evt);
      }
    });

    bPassa.setFont(new java.awt.Font("Comic Sans MS", 0, 14)); // NOI18N
    bPassa.setText("Passa");
    bPassa.setActionCommand("bPassa");
    bPassa.setEnabled(false);
    bPassa.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bPassaActionPerformed(evt);
      }
    });

    bBluff.setText("Bluff");
    bBluff.setEnabled(false);
    bBluff.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bBluffActionPerformed(evt);
      }
    });

    bNotBluff.setText("No Bluff");
    bNotBluff.setEnabled(false);
    bNotBluff.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bNotBluffActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(bInterrompi, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(bPesca, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(bPassa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
        .addGap(20, 20, 20)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addComponent(bBluff, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(18, 18, 18)
            .addComponent(bNotBluff, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(bScarta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(bUno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(bDidNotSayUno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(bPassa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(bPesca)
              .addComponent(bDidNotSayUno))
            .addGap(0, 0, Short.MAX_VALUE))
          .addComponent(bBluff, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(bNotBluff, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(18, 18, 18)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(bInterrompi)
              .addComponent(bUno)))
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(5, 5, 5)
            .addComponent(bScarta)))
        .addContainerGap())
    );

    bPassa.getAccessibleContext().setAccessibleName("bPassa");

    jLabelTurno.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
    jLabelTurno.setText("Turno: ");

    tUno.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N

    jLabelCartaTavolo.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
    jLabelCartaTavolo.setText("Carta sul tavolo: ");

    tTurno.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N

    jLabelTurno1.setFont(new java.awt.Font("Comic Sans MS", 0, 18)); // NOI18N
    jLabelTurno1.setText("Carte nelle mani dei giocatori");

    jScrollPane2.setViewportView(lNumCarte);

    cartaSulTavolo.setColumns(20);
    cartaSulTavolo.setRows(1);
    jScrollPane3.setViewportView(cartaSulTavolo);

    buttonGroup5.add(jRadioButton1);
    jRadioButton1.setSelected(true);
    jRadioButton1.setText("Rosso");
    jRadioButton1.setEnabled(false);
    jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jRadioButton1ActionPerformed(evt);
      }
    });

    buttonGroup5.add(jRadioButton6);
    jRadioButton6.setText("Giallo");
    jRadioButton6.setEnabled(false);

    buttonGroup5.add(jRadioButton7);
    jRadioButton7.setText("Verde");
    jRadioButton7.setToolTipText("");
    jRadioButton7.setEnabled(false);

    buttonGroup5.add(jRadioButton8);
    jRadioButton8.setText("Blu");
    jRadioButton8.setEnabled(false);

    AreaLog.setEditable(false);
    AreaLog.setColumns(20);
    AreaLog.setRows(5);
    AreaLog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
    jScrollPane4.setViewportView(AreaLog);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(21, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jLabelTurno)
            .addGap(8, 8, 8)
            .addComponent(tTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jLabelTurno1)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
              .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
              .addComponent(jLabelCartaTavolo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tUno, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jRadioButton7)
                  .addComponent(jRadioButton6)
                  .addComponent(jRadioButton1)
                  .addComponent(jRadioButton8))
                .addGap(131, 131, 131))))
          .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(jLabelTurno)
                  .addComponent(tTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTurno1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createSequentialGroup()
                .addGap(112, 112, 112)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jRadioButton6)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jRadioButton7)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jRadioButton8)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                  .addGroup(layout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
                    .addGap(6, 6, 6)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                  .addComponent(jLabelCartaTavolo, javax.swing.GroupLayout.Alignment.TRAILING)
                  .addComponent(tUno, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGap(18, 18, 18)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(7, 7, 7)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addComponent(jScrollPane1))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

    private void bScartaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bScartaActionPerformed
        Card selectedCard = lCarte.getSelectedValue();
        String color = "";
        if (selectedCard.getTipo() == 'j') {
            for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    color = button.getText();
                }

            }

        }
        if (uno != true) {
            UnoXTutti.theUxtController.discard(selectedCard, color);
        } else {
            UnoXTutti.theUxtController.uno(selectedCard, color);
        }
        lCarte.clearSelection();
        bScarta.setEnabled(false);
        bPassa.setEnabled(false);
        drew = false;
        for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            button.setEnabled(false);
        }

        bUno.setEnabled(false);
        uno = false;

    }//GEN-LAST:event_bScartaActionPerformed

    private void bPescaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPescaActionPerformed
        UnoXTutti.theUxtController.drawCard();
        bPesca.setEnabled(false);
        bPassa.setEnabled(true);
        drew = true;
    }//GEN-LAST:event_bPescaActionPerformed

    private void bInterrompiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bInterrompiActionPerformed
        Card selectedCard = lCarte.getSelectedValue();
        String color = "";
        if (selectedCard.getTipo() == 'j') {
            for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
                AbstractButton button = buttons.nextElement();
                if (button.isSelected()) {
                    color = button.getText();
                }

            }

        }
        if (!uno) {
            UnoXTutti.theUxtController.interrupt(selectedCard, color);

        } else {
            UnoXTutti.theUxtController.uno(selectedCard, color);

        }
        lCarte.clearSelection();
        bInterrompi.setEnabled(false);
        for (Enumeration<AbstractButton> buttons = buttonGroup5.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            button.setEnabled(false);
        }
        bUno.setEnabled(false);
        uno = false;

    }//GEN-LAST:event_bInterrompiActionPerformed

    private void bUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUnoActionPerformed
        tUno.setText("UNO!!!");
        bUno.setBackground(new Color(28, 122, 57));
        uno = true;
    }//GEN-LAST:event_bUnoActionPerformed

    private void bDidNotSayUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bDidNotSayUnoActionPerformed
        UnoXTutti.theUxtController.notSayUno();
    }//GEN-LAST:event_bDidNotSayUnoActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void bPassaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPassaActionPerformed
        UnoXTutti.theUxtController.pass();
        drew = false;
        bPassa.setEnabled(false);
    }//GEN-LAST:event_bPassaActionPerformed

    private void bBluffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBluffActionPerformed
        UnoXTutti.theUxtController.bluff(true);
    }//GEN-LAST:event_bBluffActionPerformed

    private void bNotBluffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNotBluffActionPerformed
        UnoXTutti.theUxtController.bluff(false);
    }//GEN-LAST:event_bNotBluffActionPerformed
    /*
    public void update(final int turno, final List carteInMano, final int[] numCarte, final Card cartaScoperta) {
        setTurno(turno);
        setCarteInMano(carteInMano);
        setLNumCarte(numCarte);
        setCartaScoperta(cartaScoperta);
        //------------------------------------------------------------------------------CHIUDI EVENTUALI DIALOGS!! TODO
    }
     */
    public void setTurno(final int t) {
        tTurno.setText("" + t);
    }

    /*
    public void setCarteInMano(final List l) {
        carteInMano.clear();
        carteInMano.addAll(l);
        updateLCarte();
    }*/
 /*
    private void updateLCarte() {
        String[] temp = new String[carteInMano.size()];
        int i = 0;
        for (Card c : carteInMano) {
            temp[i++] = c.toString();
        }
        lCarte.setListData(temp);
    }*/
 /*
    public void setLNumCarte(final int[] numCarte) {
        String[] temp = new String[numCarte.length];
        int i = 0;
        for (int nCarte : numCarte) {
            temp[i++] = nCarte + "";
        }
        lNumCarte.setListData(temp);
    }
     */
    public void setCartaScoperta(final Card c) {
        cartaScoperta = c;
        updateCartaScoperta();
    }

    private void updateCartaScoperta() {
        tUno.setText(cartaScoperta.toString());
        bUno.setSelected(true);
    }

    //dialog to ask for a bluff
    public void ckeckBluff() {
        opt = new JOptionPane("Vuoi controllare se il giocatore che ha scartato il pesca 4 sta bluffando?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
    }

    //dialog to ask if the player wanna discard the card that he draw
    public void scartaPescata() {
        opt = new JOptionPane("Vuoi scartare la carta appena pescata?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
    }

    //Methods to check wich action the player can undertake
    private void unoButton() {
        if (carteInMano.size() == 1) {
            bUno.setVisible(true);
        } else {
            bUno.setVisible(false);
        }
    }

    private void scartaButton(Card carta) {
        //se è una carta jolly
        if (carta.getTipo() == 'j') {
            bScarta.setVisible(true);
        }
        //stesso colore
        if (carta.getColore() == cartaScoperta.getColore()) {
            bScarta.setVisible(true);
        }
        if (carta.getTipo() == 'b' && carta.getNumero() == cartaScoperta.getNumero()) {
            bScarta.setVisible(true);
        }
        bScarta.setVisible(false);
    }

    private void buttons() {
        if (mioTurno) {
            bPesca.setVisible(true);
            //bScarta.setVisible(true); lo rendiamo visibile con scartaButton selezionando carte nella lista

        } else {
            bPesca.setVisible(false);
            bScarta.setVisible(false);
            bUno.setVisible(false);
        }
    }

    /**
     * @return the mainWindow
     */
    @Override
    public UnoXTuttiGUI getMainWindow() {
        return mainWindow;
    }

    /**
     * @param mainWindow the mainWindow to set
     */
    @Override
    public void setMainWindow(UnoXTuttiGUI mainWindow) {
        this.mainWindow = mainWindow;
    }


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTextArea AreaLog;
  private javax.swing.JButton bBluff;
  private javax.swing.JButton bDidNotSayUno;
  private javax.swing.JButton bInterrompi;
  private javax.swing.JButton bNotBluff;
  private javax.swing.JButton bPassa;
  private javax.swing.JButton bPesca;
  private javax.swing.JButton bScarta;
  private javax.swing.JButton bUno;
  private javax.swing.ButtonGroup buttonGroup5;
  private javax.swing.JTextArea cartaSulTavolo;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabelCartaTavolo;
  private javax.swing.JLabel jLabelTurno;
  private javax.swing.JLabel jLabelTurno1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JRadioButton jRadioButton1;
  private javax.swing.JRadioButton jRadioButton6;
  private javax.swing.JRadioButton jRadioButton7;
  private javax.swing.JRadioButton jRadioButton8;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JSeparator jSeparator1;
  private javax.swing.JList<Card> lCarte;
  private javax.swing.JList<String> lNumCarte;
  private javax.swing.JLabel tTurno;
  private javax.swing.JLabel tUno;
  // End of variables declaration//GEN-END:variables
    private javax.swing.ButtonGroup color;
    private final LinkedList<Card> carteInMano;
    private Card cartaScoperta;
    JOptionPane opt;
    private boolean mioTurno; //if it's my round to play

    @Override
    public void update(Observable o, Object o1) {
        String laction = currentManche.getLastAction();

        if (laction.equals("first")) {
            //cartaSulTavolo.setText(currentManche.getTop());
            this.lCarte.setModel(currentManche.getHand());
            this.lNumCarte.setModel(currentManche.getPlayersHand());
            cartaSulTavolo.setText(((RemoteManche) o).getTop());
            tTurno.setText(currentManche.getPlayerTurn());
            if (currentManche.getPlayerTurn().equals(UnoXTutti.theUxtController.getCurrentRoom().getConnectedPlayer().getName()) && !drew) {
                bPesca.setEnabled(true);
            } else {
                bPesca.setEnabled(false);

            }
        } else if (laction.equals("update")) {
            cartaSulTavolo.setText(currentManche.getTop());
            tTurno.setText(currentManche.getPlayerTurn());
            AreaLog.setText(currentManche.getMex());
            this.lNumCarte.setModel(currentManche.getPlayersHand());
            if (currentManche.getPlayerTurn().equals(UnoXTutti.theUxtController.getCurrentRoom().getConnectedPlayer().getName()) && !drew) {
                bPesca.setEnabled(true);

            } else {

                bPesca.setEnabled(false);
            }

            bUno.setEnabled(false);
            bInterrompi.setEnabled(false);
            bBluff.setEnabled(false);
            bNotBluff.setEnabled(false);
            bScarta.setEnabled(false);

        } else if (laction.equals("bluff")) {
            cartaSulTavolo.setText(currentManche.getTop());
            tTurno.setText(currentManche.getPlayerTurn());
            AreaLog.setText(currentManche.getMex());
            this.lNumCarte.setModel(currentManche.getPlayersHand());
            if (currentManche.getPlayerTurn().equals(UnoXTutti.theUxtController.getCurrentRoom().getConnectedPlayer().getName())) {
                lNumCarte.setEnabled(false);
                bBluff.setEnabled(true);
                bNotBluff.setEnabled(true);

            }
            bPesca.setEnabled(false);
            bUno.setEnabled(false);
            bInterrompi.setEnabled(false);
            bScarta.setEnabled(false);

        } else if (laction.equals("winner")) {
            AreaLog.setText(currentManche.getMex());
            bPesca.setEnabled(false);
            bUno.setEnabled(false);
            bInterrompi.setEnabled(false);
            bScarta.setEnabled(false);
            bDidNotSayUno.setEnabled(false);
            bBluff.setEnabled(false);
            bNotBluff.setEnabled(false);

        }

    }
}
