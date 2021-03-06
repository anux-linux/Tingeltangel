/*
    Copyright (C) 2015   Martin Dames <martin@bastionbytes.de>
  
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
  
*/
package tingeltangel.gui;

import java.io.IOException;
import java.util.Iterator;
import java.util.SortedSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import tingeltangel.core.TTSEntry;
import tingeltangel.tools.TTS;

public class TTSDialog extends javax.swing.JDialog {

    private final TTSEntry entry;
    private final Runnable callback;
    
    private String[] voiceIDTable;
    private String[] variantIDTable;
    
    /**
     * Creates new form TTSDialog
     */
    public TTSDialog(java.awt.Frame parent, boolean modal, TTSEntry entry, Runnable callback) {
        super(parent, modal);
        initComponents();
        this.entry = entry;
        this.callback = callback;
        
        amplitudeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                amplitudeLabel.setText(Integer.toString(amplitudeSlider.getValue()));
            }
        });
        pitchSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                pitchLabel.setText(Integer.toString(pitchSlider.getValue()));
            }
        });
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speedLabel.setText(Integer.toString(speedSlider.getValue()));
            }
        });
        
        DefaultComboBoxModel voiceModel = (DefaultComboBoxModel)voiceComboBox.getModel();
        SortedSet<String> voiceIDs = TTS.getVoiceIDs();
        voiceIDTable = new String[voiceIDs.size()];
        int c = 0;
        String defVoiceID = TTSPreferences.getDefaultVoice();
        int currentVoice = -1;
        int currentVoiceDef = 0;
        Iterator<String> i = voiceIDs.iterator();
        while(i.hasNext()) {
            voiceIDTable[c] = i.next();
            if(voiceIDTable[c].equals(entry.voice)) {
                currentVoice = c;
            }
            if(voiceIDTable[c].equals(defVoiceID)) {
                currentVoiceDef = c;
            }
            String name = TTS.getVoiceName(voiceIDTable[c]);
            voiceModel.addElement(name);
            c++;
        }
        if(currentVoice < 0) {
            currentVoice = currentVoiceDef;
        }
        
        DefaultComboBoxModel variantModel = (DefaultComboBoxModel)variantComboBox.getModel();
        SortedSet<String> variantIDs = TTS.getVariantIDs();
        variantIDTable = new String[variantIDs.size() + 1];
        variantIDTable[0] = "";
        variantModel.addElement("keine Variante");
        c = 1;
        String defVariantID = TTSPreferences.getDefaultVariant();
        int currentVariant = -1;
        int currentVariantDef = 0;
        i = variantIDs.iterator();
        while(i.hasNext()) {
            variantIDTable[c] = i.next();
            if(variantIDTable[c].equals(entry.variant)) {
                currentVariant = c;
            }
            if(variantIDTable[c].equals(defVariantID)) {
                currentVariantDef = c;
            }
            String name = TTS.getVariantName(variantIDTable[c]);
            /*
            switch(TTS.getVariantGender(variantIDTable[c])) {
                case TTS.FEMALE:
                    name += " (Frau)";
                    break;
                case TTS.MALE:
                    name += " (Mann)";
            }
            */
            variantModel.addElement(name);
            c++;
        }
        if(currentVariant < 0) {
            currentVariant = currentVariantDef;
        }
        
        // preselection
        amplitudeSlider.setValue(entry.amplitude);
        speedSlider.setValue(entry.speed);
        pitchSlider.setValue(entry.pitch);
        textArea.setText(entry.text);
        voiceComboBox.setSelectedIndex(currentVoice);
        variantComboBox.setSelectedIndex(currentVariant);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        voiceComboBox = new javax.swing.JComboBox();
        variantComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        speedSlider = new javax.swing.JSlider();
        pitchSlider = new javax.swing.JSlider();
        amplitudeSlider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        okButton = new javax.swing.JButton();
        speedLabel = new javax.swing.JLabel();
        pitchLabel = new javax.swing.JLabel();
        amplitudeLabel = new javax.swing.JLabel();
        playButton = new javax.swing.JButton();

        setTitle("Text To Speech (TTS)");

        voiceComboBox.setModel(new DefaultComboBoxModel());

        variantComboBox.setModel(new DefaultComboBoxModel());

        jLabel1.setText("Stimme:");

        jLabel2.setText("Variante:");

        jLabel3.setText("Wörter pro Minute:");

        jLabel4.setText("Tonhöhe:");

        jLabel5.setText("Lautstärke:");

        speedSlider.setMaximum(450);
        speedSlider.setMinimum(80);
        speedSlider.setValue(160);

        pitchSlider.setMaximum(99);

        amplitudeSlider.setMaximum(200);
        amplitudeSlider.setToolTipText("");
        amplitudeSlider.setValue(200);

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane1.setViewportView(textArea);

        okButton.setText("ok");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        speedLabel.setText("160");

        pitchLabel.setText("50");

        amplitudeLabel.setText("200");

        playButton.setText("play");
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                playButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(amplitudeLabel))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel4)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pitchLabel))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(speedLabel)))
                                        .addGap(0, 17, Short.MAX_VALUE)))
                                .addGap(83, 83, 83))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(amplitudeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(pitchSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(speedSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(variantComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(voiceComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(playButton)
                                .addGap(18, 18, 18)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(voiceComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel2)
                        .addGap(12, 12, 12)
                        .addComponent(variantComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(speedLabel))
                        .addGap(4, 4, 4)
                        .addComponent(speedSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(pitchLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pitchSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(amplitudeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(amplitudeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(playButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(okButton)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        entry.amplitude = amplitudeSlider.getValue();
        entry.pitch = pitchSlider.getValue();
        entry.speed = speedSlider.getValue();
        entry.text = textArea.getText();
        entry.voice = voiceIDTable[voiceComboBox.getSelectedIndex()];
        entry.variant = variantIDTable[variantComboBox.getSelectedIndex()];
        
        callback.run();
        setVisible(false);
    }//GEN-LAST:event_okButtonActionPerformed

    private void playButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playButtonActionPerformed
        try {
            TTS.play(
                            textArea.getText(),
                            amplitudeSlider.getValue(),
                            pitchSlider.getValue(),
                            speedSlider.getValue(),
                            voiceIDTable[voiceComboBox.getSelectedIndex()],
                            variantIDTable[variantComboBox.getSelectedIndex()]
            );
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_playButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel amplitudeLabel;
    private javax.swing.JSlider amplitudeSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel pitchLabel;
    private javax.swing.JSlider pitchSlider;
    private javax.swing.JButton playButton;
    private javax.swing.JLabel speedLabel;
    private javax.swing.JSlider speedSlider;
    private javax.swing.JTextArea textArea;
    private javax.swing.JComboBox variantComboBox;
    private javax.swing.JComboBox voiceComboBox;
    // End of variables declaration//GEN-END:variables
}
