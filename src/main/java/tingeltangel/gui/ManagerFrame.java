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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import tingeltangel.Tingeltangel;
import tingeltangel.core.Repository;
import tingeltangel.core.Stick;
import tingeltangel.core.constants.TxtFile;
import tingeltangel.tools.Callback;
import tingeltangel.tools.Progress;
import tingeltangel.tools.ProgressDialog;

public class ManagerFrame extends JFrame {
    
    private final static Logger log = LogManager.getLogger(ManagerFrame.class);
    
    private final JPanel centerPanel = new JPanel();
    private final JLabel statusLabel = new JLabel("kein Stift gefunden");
    private boolean online = false;
    
    public ManagerFrame() {
        super(Tingeltangel.MAIN_FRAME_TITLE + Tingeltangel.MAIN_FRAME_VERSION);
        
        
        JFrame.setDefaultLookAndFeelDecorated(true);

        setBounds(
                    Tingeltangel.MAIN_FRAME_POS_X,
                    Tingeltangel.MAIN_FRAME_POS_Y,
                    Tingeltangel.MAIN_FRAME_WIDTH + getInsets().left + getInsets().right,
                    Tingeltangel.MAIN_FRAME_HEIGHT + getInsets().top + getInsets().bottom
        );
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        setContentPane(getPanel());
        
        centerPanel.setLayout(new PushBorderLayout());
        
        Runnable task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Stick stick = Stick.getStick();
                    if(online && (stick == null)) {
                        // go offline
                        online = false;
                        statusLabel.setText("keinen Stift gefunden");
                        centerPanel.removeAll();
                    } else if((!online) && (stick != null)) {
                        // go online
                        online = true;
                        goOnline();
                    }             
                } catch(IOException ioe) {
                    ioe.printStackTrace();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(task, 3, 3, TimeUnit.SECONDS);
        
        setVisible(true);
    }

    private void updateList() throws IOException {
        Stick stick = Stick.getStick();
        Iterator<Integer> ids = stick.getBooks().iterator();

        centerPanel.removeAll();
        boolean first = true;
        while(ids.hasNext()) {
            int id = ids.next();
            if(first) {
                first = false;
            } else {
                centerPanel.add(PushBorderLayout.pad(10), PushBorderLayout.PAGE_START);
            }
            centerPanel.add(getBookPanel(id), PushBorderLayout.PAGE_START);
        }
        validate();
        repaint();
    }
    
    private void goOnline() {
        statusLabel.setText("Stift gefunden");
        try {
            updateList();
            
        } catch(IOException ioe) {
            log.warn("Stick konnte nicht geöffnet werden", ioe);
            JOptionPane.showMessageDialog(this, "Stick konnte nicht geöffnet werden");
        }
    }
    
    private JPanel getBookPanel(final int mid) throws IOException {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        
        HashMap<String, String> bookTxt = Repository.getBookTxt(mid);
        
        
        File coverImage = Repository.getBookPng(mid);
        try {
            if((coverImage != null) && coverImage.exists()) {
                panel.add(new JLabel(new ImageIcon(ImageIO.read(coverImage))), BorderLayout.WEST);
            } else {
                panel.add(new JLabel(new ImageIcon(ImageIO.read(getClass().getResource("/noCover.png")))), BorderLayout.WEST);
            }
        } catch(IOException ioe) {
            log.warn("unable to load cover (mid=" + mid + ")", ioe);
        }
        
        
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new PushBorderLayout());
        actionPanel.add(PushBorderLayout.pad(10), PushBorderLayout.PAGE_START);
        addButton(actionPanel, "aktualisieren", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                if(!Repository.txtExists(mid)) {
                    Repository.search(mid);
                }
                if(!Repository.txtExists(mid)) {
                    log.warn("book " + mid + " not found in repository");
                    JOptionPane.showMessageDialog(ManagerFrame.this, "Das Buch wurde im Repository nicht gefunden");
                } else {
                    Progress pr = new Progress(ManagerFrame.this, "Buch aktualisieren") {
                        @Override
                        public void action(ProgressDialog progressDialog) {
                            try {
                                Repository.update(mid, progressDialog);
                                Stick.getStick().copyFromRepositoryToStick(mid);
                                updateList();
                            } catch(IOException ioe) {
                                log.warn("book " + mid + " update failed", ioe);
                                JOptionPane.showMessageDialog(ManagerFrame.this, "Das Buch konnte nicht aktualisiert werden");
                            }
                        }
                    };
                }
            }
        });
        actionPanel.add(PushBorderLayout.pad(10), PushBorderLayout.PAGE_START);
        addButton(actionPanel, "löschen", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                try {
                    Stick.getStick().delete(mid);
                    updateList();
                } catch(IOException ioe) {
                    log.warn("book " + mid + " delete failed", ioe);
                    JOptionPane.showMessageDialog(ManagerFrame.this, "Das Buch konnte nicht gelöscht werden werden");
                }
            }
        });
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new PushBorderLayout());
        
        infoPanel.add(PushBorderLayout.pad(10), PushBorderLayout.LINE_START);
        infoPanel.add(actionPanel, PushBorderLayout.LINE_START);
        infoPanel.add(PushBorderLayout.pad(10), PushBorderLayout.LINE_START);
        
        
        if(bookTxt == null) {
            infoPanel.add(new JLabel("keine Informationen vorhanden"), PushBorderLayout.PAGE_START);
        } else {
            JLabel title = new JLabel(bookTxt.get(TxtFile.KEY_NAME));
            Font f = title.getFont();
            title.setFont(f.deriveFont(f.getSize2D() + 5f));


            infoPanel.add(title, PushBorderLayout.PAGE_START);
            infoPanel.add(new JLabel("Autor: " + bookTxt.get(TxtFile.KEY_AUTHOR)), PushBorderLayout.PAGE_START);
            infoPanel.add(new JLabel("Verlag: " + bookTxt.get(TxtFile.KEY_PUBLISHER)), PushBorderLayout.PAGE_START);
            infoPanel.add(new JLabel("URL: " + bookTxt.get(TxtFile.KEY_URL)), PushBorderLayout.PAGE_START);
            infoPanel.add(new JLabel("Version: " + bookTxt.get(TxtFile.KEY_VERSION)), PushBorderLayout.PAGE_START);
        }
        
        
        
        panel.add(infoPanel, BorderLayout.CENTER);
        
        
        return(panel);
    }
    
    private JPanel getPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(centerPanel), BorderLayout.CENTER);
        panel.add(getRightPanel(), BorderLayout.EAST);
        return(panel);
    }
    
    private JPanel getRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new PushBorderLayout());
        
        addButton(panel, "Stift aktualisieren", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                
                
                // update all books on stick
                Stick stick;
                try {
                    stick = Stick.getStick();
                    if(stick == null) {
                        log.warn("no stick found");
                        JOptionPane.showMessageDialog(ManagerFrame.this, "kein Stift gefunden");
                        return;
                    }
                } catch(IOException ioe) {
                    log.warn("failed to access stick", ioe);
                    JOptionPane.showMessageDialog(ManagerFrame.this, "Auf den Stift kann nicht zugegriffen werden");
                    return;
                }
                
                final Stick _stick = stick;
                Progress pr = new Progress(ManagerFrame.this, "Stift aktualisieren") {
                    @Override
                    public void action(ProgressDialog progressDialog) {
                        if(_stick.update(ManagerFrame.this, progressDialog)) {
                            try {
                                updateList();
                            } catch(IOException ioe) {
                                log.warn("failed to update book list", ioe);
                            }
                            JOptionPane.showMessageDialog(ManagerFrame.this, "Aktualisierung erfolgreich");
                        } else {
                            JOptionPane.showMessageDialog(ManagerFrame.this, "Aktualisierung fehlgeschlagen");
                        }
                    }
                };
            }
        });
        addButton(panel, "Neue Bücher suchen", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                Progress pr = new Progress(ManagerFrame.this, "neue Bücher suchen") {
                    @Override
                    public void action(ProgressDialog progressDialog) {
                        Repository.search(progressDialog);
                        JOptionPane.showMessageDialog(ManagerFrame.this, "Update erfolgreich");
                    }
                };
            }
        });
        addButton(panel, "Repository aktualisieren", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                Progress pr = new Progress(ManagerFrame.this, "Repository aktualisieren") {
                    @Override
                    public void action(ProgressDialog progressDialog) {
                        try {
                            Repository.update(progressDialog);
                            JOptionPane.showMessageDialog(ManagerFrame.this, "Aktualisierung erfolgreich");
                        } catch (IOException ex) {
                            log.warn("Repository.update error", ex);
                            JOptionPane.showMessageDialog(ManagerFrame.this, "Es ist ein Fehler aufgetreten");
                        }
                    }
                };
            }
        });
        /*
        addButton(panel, "Stift reparieren", new Callback<Object>() {
            @Override
            public void callback(Object t) {
                
            }
        });
        */
        
        return(panel);
    }
    
    private void addButton(JPanel panel, String label, final Callback<Object> callback) {
        JButton button = new JButton(label);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                callback.callback(null);
            }
        });
        panel.add(button, PushBorderLayout.PAGE_START);
    }
}
