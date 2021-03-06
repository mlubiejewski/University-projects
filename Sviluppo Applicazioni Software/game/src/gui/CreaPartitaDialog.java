/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unoxtutti.gui;

import javax.swing.JOptionPane;

/**
 *
 * @author picardi
 */
public class CreaPartitaDialog extends javax.swing.JDialog {

	private int result;
	/**
	 * Creates new form NuovaStanzaDialog
	 */
	public CreaPartitaDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		result = JOptionPane.CANCEL_OPTION;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        nomePartita = new javax.swing.JTextField();
        javax.swing.Box.Filler filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        numeroGiocatori = new javax.swing.JTextField();
        javax.swing.Box.Filler filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 10), new java.awt.Dimension(0, 10), new java.awt.Dimension(32767, 10));
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        modalita = new javax.swing.JTextField();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel4.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 5), javax.swing.BorderFactory.createEtchedBorder()));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel2.setPreferredSize(new java.awt.Dimension(400, 250));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Nome Partita:");
        jPanel1.add(jLabel1);
        jPanel1.add(jPanel5);

        nomePartita.setColumns(20);
        nomePartita.setAlignmentX(0.0F);
        nomePartita.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomePartitaActionPerformed(evt);
            }
        });
        jPanel1.add(nomePartita);
        jPanel1.add(filler2);

        jLabel3.setText("Numero Giocatori");
        jPanel1.add(jLabel3);

        numeroGiocatori.setColumns(5);
        numeroGiocatori.setAlignmentX(0.0F);
        numeroGiocatori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numeroGiocatoriActionPerformed(evt);
            }
        });
        jPanel1.add(numeroGiocatori);
        jPanel1.add(filler3);

        jLabel4.setText("Modalità");
        jPanel1.add(jLabel4);

        modalita.setColumns(5);
        modalita.setAlignmentX(0.0F);
        jPanel1.add(modalita);

        jPanel2.add(jPanel1);

        jPanel4.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        okButton.setText("Ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        jPanel3.add(okButton);

        cancelButton.setText("Annulla");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(cancelButton);

        jPanel4.add(jPanel3, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel4, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        result = JOptionPane.OK_OPTION;
		boolean ok = true;
		int num = 0;
		try {
			num = Integer.parseInt(this.numeroGiocatori.getText().trim());
		} catch (NumberFormatException ex) {
			ok = false;
			JOptionPane.showMessageDialog(this, "La porta deve essere un numero\ncompreso fra 1024 e 65535");
		}
		
		if (ok) setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        result = JOptionPane.CANCEL_OPTION;
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void nomePartitaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomePartitaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomePartitaActionPerformed

    private void numeroGiocatoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeroGiocatoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numeroGiocatoriActionPerformed

	
	public String getPartitaName() {
		return this.nomePartita.getText().trim();
	}
	
	public int getPartitaGioc() {
		return Integer.parseInt(this.numeroGiocatori.getText().trim());
	}
	public String getPartitaMod() {
		return this.modalita.getText().trim();
	}
	
	public int getResult() {
		return result;
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField modalita;
    private javax.swing.JTextField nomePartita;
    private javax.swing.JTextField numeroGiocatori;
    private javax.swing.JButton okButton;
    // End of variables declaration//GEN-END:variables
}
