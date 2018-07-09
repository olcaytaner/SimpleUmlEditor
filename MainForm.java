import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import Diagram.ClassDiagram;
import Panel.DiagramPanel;
import Panel.SequencePanel;
import Panel.UseCasePanel;
import Panel.ClassPanel;
import Panel.EnumCommand;
import Uml.UmlObject;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class MainForm extends JApplet implements ActionListener{

    private JMenuItem itemClose;
    private JMenuItem itemUndo;
    private JMenuItem itemCloseAll;
    private JMenuItem itemSave;
    private JMenuItem itemSaveAs;
    private JMenuItem itemExport;
    private JMenuItem itemPaste;
    private JMenuItem itemGenerate;
    private JTabbedPane diagramPane;
    private ArrayList<EnumDiagram> diagrams;
    private ArrayList<UmlObject> copyList = null;
    private int useCaseCount = 0, classCount = 0, sequenceCount = 0;
    static final private String EMPTY = "empty";
    static final private String ACTOR = "actor";
    static final private String USECASE = "usecase";
    static final private String AGGREGATION = "aggregation";
    static final private String CLASS = "class";
    static final private String CONNECTION = "connection";
    static final private String INHERITANCE = "inheritance";
    static final private String MESSAGE = "message";
    static final private String ATTRIBUTE = "attribute";
    static final private String METHOD = "method";
    private EnumDiagram copyType = null;


    private JMenuItem addMenuItem(JMenu menu, String name, KeyStroke stroke){
        JMenuItem newItem;
        newItem = new JMenuItem(name);
        newItem.setAccelerator(stroke);
        menu.add(newItem);
        return newItem;
    }

    protected JButton makeDrawingButton(String imageName, String actionCommand, String toolTipText) {
        String imgLocation = "icons/"+ imageName + ".gif";
        URL imageURL = MainForm.class.getResource(imgLocation);
        JButton button = new JButton();
        button.setActionCommand(actionCommand);
        button.setToolTipText(toolTipText);
        button.addActionListener(this);
        if (imageURL != null) {
            button.setIcon(new ImageIcon(imageURL));
        } else {
            button.setText(toolTipText);
            System.err.println("Resource not found: " + imgLocation);
        }
        return button;
    }

    private void addButtons(JToolBar toolBar){
        JButton button;
        button = makeDrawingButton("empty", EMPTY, "");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("actor", ACTOR, "Add Actor");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("usecase", USECASE, "Add Use Case");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("connection", CONNECTION, "Add Connection");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("aggregation", AGGREGATION, "Add Aggregation");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("class", CLASS, "Add Class");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("inheritance", INHERITANCE, "Add Inheritance");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("message", MESSAGE, "Add Message");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("attribute", ATTRIBUTE, "Add Attribute");
        button.setVisible(false);
        toolBar.add(button);
        button = makeDrawingButton("method", METHOD, "Add Method");
        button.setVisible(false);
        toolBar.add(button);
    }

    private void enableMenu(){
        itemClose.setEnabled(true);
        itemCloseAll.setEnabled(true);
        itemSave.setEnabled(true);
        itemSaveAs.setEnabled(true);
        itemExport.setEnabled(true);
    }

    private void disableMenu(){
        itemClose.setEnabled(false);
        itemCloseAll.setEnabled(false);
        itemSave.setEnabled(false);
        itemSaveAs.setEnabled(false);
        itemExport.setEnabled(false);
        itemPaste.setEnabled(false);
    }

    public MainForm(){
        JMenuBar menu;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu helpMenu;
        JMenu newMenu;
        menu = new JMenuBar();
        setJMenuBar(menu);
        fileMenu = new JMenu("File");
        menu.add(fileMenu);
        newMenu = new JMenu("New");
        fileMenu.add(newMenu);
        JMenuItem itemUseCaseDiagram = addMenuItem(newMenu, "Use Case Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
        JMenuItem itemClassDiagram = addMenuItem(newMenu, "Class Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        JMenuItem itemSequenceDiagram = addMenuItem(newMenu, "Sequence Diagram", KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        JMenuItem itemOpen = addMenuItem(fileMenu, "Open", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        itemSave = addMenuItem(fileMenu, "Save", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        itemSaveAs = addMenuItem(fileMenu, "Save As", null);
        itemExport = addMenuItem(fileMenu, "Export", KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        itemGenerate = addMenuItem(fileMenu, "Generate Code", KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        itemClose = addMenuItem(fileMenu, "Close", null);
        itemCloseAll = addMenuItem(fileMenu, "Close All", null);
        editMenu = new JMenu("Edit");
        menu.add(editMenu);
        itemUndo = addMenuItem(editMenu, "Undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        JMenuItem itemCut = addMenuItem(editMenu, "Cut", KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        JMenuItem itemCopy = addMenuItem(editMenu, "Copy", KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        itemPaste = addMenuItem(editMenu, "Paste", KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        JMenuItem itemDelete = addMenuItem(editMenu, "Delete", KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        JMenuItem itemSelectAll = addMenuItem(editMenu, "Select All", KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        helpMenu = new JMenu("Help");
        menu.add(helpMenu);
        JMenuItem itemAbout = addMenuItem(helpMenu, "About", null);
        diagramPane = new JTabbedPane();
        disableMenu();
        add(diagramPane, BorderLayout.CENTER);
        final JToolBar ToolBar = new JToolBar("ToolBox");
        addButtons(ToolBar);
        add(ToolBar, BorderLayout.PAGE_START);
        ToolBar.setVisible(true);
        diagrams = new ArrayList<EnumDiagram>();
        diagramPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent c){
                int i;
                if (diagramPane.getSelectedIndex() != -1){
                    if (diagrams.get(diagramPane.getSelectedIndex()) == EnumDiagram.CLASS){
                        itemGenerate.setEnabled(true);
                    } else {
                        itemGenerate.setEnabled(false);
                    }
                    if (((DiagramPanel) diagramPane.getSelectedComponent()).canUndo()){
                        itemUndo.setEnabled(true);
                    } else {
                        itemUndo.setEnabled(false);                        
                    }
                    switch (diagrams.get(diagramPane.getSelectedIndex())){
                        case USE_CASE:for (i = 0; i < 10; i++){
                            ToolBar.getComponent(i).setVisible(false);
                        }
                            ToolBar.getComponent(0).setVisible(true);
                            ToolBar.getComponent(1).setVisible(true);
                            ToolBar.getComponent(2).setVisible(true);
                            ToolBar.getComponent(3).setVisible(true);                            
                            if (copyType == null){
                                itemPaste.setEnabled(false);
                            } else {
                                if (copyType != EnumDiagram.USE_CASE){
                                    itemPaste.setEnabled(false);
                                } else {
                                    itemPaste.setEnabled(true);
                                }
                            }
                                              break;
                        case    CLASS:for (i = 0; i < 10; i++){
                            ToolBar.getComponent(i).setVisible(false);
                        }
                            ToolBar.getComponent(0).setVisible(true);
                            ToolBar.getComponent(3).setVisible(true);
                            ToolBar.getComponent(4).setVisible(true);
                            ToolBar.getComponent(5).setVisible(true);
                            ToolBar.getComponent(6).setVisible(true);
                            ToolBar.getComponent(8).setVisible(true);
                            ToolBar.getComponent(9).setVisible(true);
                            if (copyType == null){
                                itemPaste.setEnabled(false);
                            } else {
                                if (copyType != EnumDiagram.CLASS){
                                    itemPaste.setEnabled(false);
                                } else {
                                    itemPaste.setEnabled(true);
                                }
                            }
                                              break;
                        case SEQUENCE:for (i = 0; i < 10; i++){
                            ToolBar.getComponent(i).setVisible(false);
                        }
                            ToolBar.getComponent(0).setVisible(true);
                            ToolBar.getComponent(1).setVisible(true);
                            ToolBar.getComponent(5).setVisible(true);
                            ToolBar.getComponent(7).setVisible(true);
                            if (copyType == null){
                                itemPaste.setEnabled(false);
                            } else {
                                if (copyType != EnumDiagram.SEQUENCE){
                                    itemPaste.setEnabled(false);
                                } else {
                                    itemPaste.setEnabled(true);
                                }
                            }
                                              break;
                    }
                    diagramPane.getSelectedComponent().repaint();
                } else {
                    for (i = 0; i < 10; i++){
                        ToolBar.getComponent(i).setVisible(false);
                    }                    
                }
            }
        });
        itemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JOptionPane.showMessageDialog(null, "Uml Editor Version 1.0.24");
            }
        });
        itemUseCaseDiagram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                UseCasePanel newPanel;
                newPanel = new UseCasePanel();
                useCaseCount++;
                diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.USE_CASE);
                diagramPane.add(newPanel, "Use Case Diagram" + useCaseCount, diagramPane.getSelectedIndex() + 1);
                enableMenu();
            }
        });
        itemClassDiagram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                ClassPanel newPanel;
                newPanel = new ClassPanel();
                classCount++;
                diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.CLASS);
                diagramPane.add(newPanel, "Class Diagram" + classCount, diagramPane.getSelectedIndex() + 1);
                enableMenu();
            }
        });
        itemSequenceDiagram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                SequencePanel newPanel;
                newPanel = new SequencePanel();
                diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.SEQUENCE);
                sequenceCount++;
                diagramPane.add(newPanel, "Sequence Diagram" + sequenceCount, diagramPane.getSelectedIndex() + 1);
                enableMenu();
            }
        });
        itemClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                diagrams.remove(diagramPane.getSelectedIndex());
                diagramPane.remove(diagramPane.getSelectedIndex());
                if (diagramPane.getTabCount() == 0){
                    disableMenu();
                }
            }
        });
        itemCloseAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                while (diagramPane.getSelectedIndex() != -1){
                    diagrams.remove(diagramPane.getSelectedIndex());
                    diagramPane.remove(diagramPane.getSelectedIndex());
                }
                disableMenu();
            }
        });
        itemUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    current.undo();
                    current.repaint();
                }
            }
        });
        itemSelectAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    current.getDiagram().selectAll();
                    current.repaint();
                }
            }
        });
        itemCopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    copyType = diagrams.get(diagramPane.getSelectedIndex());
                    copyList = current.getDiagram().copyAll();
                }
            }
        });
        itemCut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    copyType = diagrams.get(diagramPane.getSelectedIndex());
                    copyList = current.getDiagram().copyAll();
                    current.save();
                    current.getDiagram().deleteSelected();
                    current.repaint();
                }
            }
        });
        itemPaste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    current.save();
                    current.getDiagram().pasteObjects(copyList);
                    current.repaint();
                }
            }
        });
        itemDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current != null){
                    current.save();
                    current.getDiagram().deleteSelected();
                    current.repaint();
                }
            }
        });
        itemGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String directory;
                final JFileChooser fcdirectory = new JFileChooser();
                fcdirectory.setDialogTitle("Select output directory");
                fcdirectory.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fcdirectory.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    DiagramPanel current;
                    current = (ClassPanel) diagramPane.getSelectedComponent();
                    directory = fcdirectory.getSelectedFile().getAbsolutePath();
                    ((ClassDiagram)current.getDiagram()).generateCode(directory);
                }
            }
        });
        itemSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String filename;
                final JFileChooser fcoutput = new JFileChooser();
                fcoutput.setDialogTitle("Select output file");
                fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
                int returnVal = fcoutput.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    DiagramPanel current;
                    current = (DiagramPanel) diagramPane.getSelectedComponent();
                    filename = fcoutput.getSelectedFile().getAbsolutePath();
                    diagramPane.setToolTipTextAt(diagramPane.getSelectedIndex(), fcoutput.getSelectedFile().getName());
                    current.setFileName(filename);
                    current.getDiagram().save(filename);
                }
            }
        });
        itemExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String filename;
                final JFileChooser fcoutput = new JFileChooser();
                fcoutput.setDialogTitle("Select output jpg file");
                fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
                int returnVal = fcoutput.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    DiagramPanel current;
                    current = (DiagramPanel) diagramPane.getSelectedComponent();
                    filename = fcoutput.getSelectedFile().getAbsolutePath();
                    BufferedImage image = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = image.createGraphics();
                    g2.fillRect(0, 0, current.getWidth(), current.getHeight());
                    current.paint(g2);
                    try {
                        ImageIO.write(image, "jpeg", new File(filename));
                    }
                    catch (IOException ioException){
                        System.out.println("Output file can not be opened");
                    }                    
                    image.flush();
                }
            }
        });
        itemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                DiagramPanel current;
                current = (DiagramPanel) diagramPane.getSelectedComponent();
                if (current.getFileName() == null){
                    final JFileChooser fcoutput = new JFileChooser();
                    fcoutput.setDialogTitle("Select output file");
                    fcoutput.setDialogType(JFileChooser.SAVE_DIALOG);
                    int returnVal = fcoutput.showSaveDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION){
                        String filename;
                        filename = fcoutput.getSelectedFile().getAbsolutePath();
                        diagramPane.setToolTipTextAt(diagramPane.getSelectedIndex(), fcoutput.getSelectedFile().getName());
                        current.setFileName(filename);
                        current.getDiagram().save(filename);
                    }
                } else {
                    current.getDiagram().save(current.getFileName());
                }
            }
        });
        itemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String filename;
                final JFileChooser fcinput = new JFileChooser();
                fcinput.setDialogTitle("Select diagram file");
                fcinput.setDialogType(JFileChooser.OPEN_DIALOG);
                int returnVal = fcinput.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    Node rootNode;
                    NamedNodeMap attributes;
                    String diagramType;
                    filename = fcinput.getSelectedFile().getAbsolutePath();
                    DOMParser parser = new DOMParser();
                    Document doc;
                    try {
                        parser.parse(filename);
                    } catch (SAXException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    doc = parser.getDocument();
                    rootNode = doc.getFirstChild();
                    if (rootNode.hasAttributes()){
                        attributes = rootNode.getAttributes();
                        diagramType = attributes.getNamedItem("type").getNodeValue();
                        if (diagramType.equalsIgnoreCase("UseCase")){
                            UseCasePanel newPanel;
                            newPanel = new UseCasePanel();
                            diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.USE_CASE);
                            diagramPane.add(newPanel, fcinput.getSelectedFile().getName(), diagramPane.getSelectedIndex() + 1);
                            newPanel.getDiagram().loadFromXml(rootNode);
                            enableMenu();
                        } else {
                            if (diagramType.equalsIgnoreCase("Class")){
                                ClassPanel newPanel;
                                newPanel = new ClassPanel();
                                diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.CLASS);
                                diagramPane.add(newPanel, fcinput.getSelectedFile().getName(), diagramPane.getSelectedIndex() + 1);
                                newPanel.getDiagram().loadFromXml(rootNode);
                                enableMenu();
                            } else {
                                if (diagramType.equalsIgnoreCase("Sequence")){
                                    SequencePanel newPanel;
                                    newPanel = new SequencePanel();
                                    diagrams.add(diagramPane.getSelectedIndex() + 1, EnumDiagram.SEQUENCE);
                                    diagramPane.add(newPanel, fcinput.getSelectedFile().getName(), diagramPane.getSelectedIndex() + 1);
                                    newPanel.getDiagram().loadFromXml(rootNode);
                                    enableMenu();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        DiagramPanel current;
        String cmd = e.getActionCommand();
        this.showStatus("");
        EnumCommand lastCommand;
        if (EMPTY.equals(cmd)) {
            lastCommand = EnumCommand.EMPTY;
        } else {
            if (ACTOR.equals(cmd)){
                lastCommand = EnumCommand.ACTOR;
                this.showStatus("Actor");
            } else {
                if (USECASE.equals(cmd)){
                    lastCommand = EnumCommand.USE_CASE;
                    this.showStatus("Use Case");
                } else {
                    if (AGGREGATION.equals(cmd)){
                        lastCommand = EnumCommand.AGGREGATION;
                        this.showStatus("Aggregation");
                    } else {
                        if (CLASS.equals(cmd)){
                            lastCommand = EnumCommand.CLASS;
                            this.showStatus("Class");
                        } else {
                            if (CONNECTION.equals(cmd)){
                                lastCommand = EnumCommand.CONNECTION;
                                this.showStatus("Connection");
                            } else {
                                if (INHERITANCE.equals(cmd)){
                                    lastCommand = EnumCommand.INHERITANCE;
                                    this.showStatus("Inheritance");
                                } else {
                                    if (MESSAGE.equals(cmd)){
                                        lastCommand = EnumCommand.MESSAGE;
                                        this.showStatus("Message");
                                    } else {
                                        if (ATTRIBUTE.equals(cmd)){
                                            lastCommand = EnumCommand.ATTRIBUTE;
                                            this.showStatus("Attribute");
                                        } else {
                                            lastCommand = EnumCommand.METHOD;
                                            this.showStatus("Method");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        current = (DiagramPanel) diagramPane.getSelectedComponent();
        current.setCommand(lastCommand);
    }

}
