/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author manuel
 */
public class FramePaint extends JFrame {

    private JButton colorPincel;
    private JButton colorPincelDeg;
    final private JSlider grosor = new JSlider(1, 10, 1);
    final private JSlider degradado = new JSlider(1, 1000, 1);
    private JComboBox combo;
    
    private Vector<JButton> shappes = new Vector<JButton>();
    private Vector<String> shappesNames = new Vector<String>();
    private Vector<JButton> transformations = new Vector<JButton>();
    private String shapeActual;
    private Color color;

    private JPanel panelBotones = new JPanel();
    private LienzoPanel lienzo = new LienzoPanel();
    private JPanel panelTransforms = new JPanel();
    private JPanel panelBajo = new JPanel();

    public FramePaint() {
        init();
    }

    public void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("JAVA2D");

        this.grosor.setMajorTickSpacing(1);
        //this.grosor.setPaintTrack(true);
        //this.grosor.setPaintLabels(true);
        this.grosor.setPaintTicks(true);

        this.degradado.setMajorTickSpacing(50);
        //this.grosor.setPaintTrack(true);
        //this.grosor.setPaintLabels(true);
        this.degradado.setPaintTicks(true);

        createBtnMenu();
        createFigurasBottons();
        createLienzo();
        createTranforms();
        createPanelBajo();
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(this);
    }
    
    public void createPanelBajo()
    {
        JLabel trans = new JLabel("Transparencia");
        JSlider tipoTrans = new JSlider(-1, 11, -1);
        tipoTrans.setMajorTickSpacing(1);
        tipoTrans.setPaintTicks(true);
        
        JSlider inteTrans = new JSlider(0, 10, 5);
        inteTrans.setMajorTickSpacing(1);
        inteTrans.setPaintTicks(true);
        
        
        
        tipoTrans.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lienzo.setRuleIndex(tipoTrans.getValue());
            }
        });
        
        inteTrans.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lienzo.setIntenTrans(inteTrans.getValue());
            }
        });
        
        this.panelBajo.add(trans);
        this.panelBajo.add(tipoTrans);
        this.panelBajo.add(inteTrans);
        
        this.getContentPane().add(panelBajo,BorderLayout.SOUTH);
    }

    public void createBtnMenu() {
        panelBotones.setLayout(new FlowLayout());

        ImageIcon iconColors = new ImageIcon("src/icons/colors.png");
        this.colorPincel = new JButton(iconColors);
        this.colorPincel.setPreferredSize(new Dimension(50, 50));

        JLabel colorLabel = new JLabel("Color: ");
        JLabel degLabel = new JLabel("Degradado: ");
        JLabel nivBorLabel = new JLabel("Nv.Borde: ");
        JLabel nivDegLabel = new JLabel("Nv.Degradado: ");
        
        this.colorPincel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Escoge un color", color);
                lienzo.setColorPincel(newColor);
            }
        });

        this.grosor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lienzo.setGrosor(grosor.getValue());
            }
        });
        
        iconColors = new ImageIcon("src/icons/degradado.png");
        this.colorPincelDeg = new JButton(iconColors);
        this.colorPincelDeg.setPreferredSize(new Dimension(50, 50));

        this.colorPincelDeg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColorDeg = JColorChooser.showDialog(null, "Escoge un color", color);
                lienzo.setDegradado(newColorDeg);
            }
        });

        this.degradado.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lienzo.setValDegradado(degradado.getValue());
            }
        });

        this.combo = new JComboBox();
        combo.addItem("Horizontal");
        combo.addItem("Vertical");
        combo.addItem("Diagonal");
        combo.addItem("Redondo");
        combo.addItem("H.Ciclico");
        combo.addItem("V.Ciclico");
        combo.addItem("D.Ciclico");

        // Accion a realizar cuando el JComboBox cambia de item seleccionado.
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lienzo.setOrDegradado(combo.getSelectedItem().toString());
            }
        });
        
        
        panelBotones.add(colorLabel);
        panelBotones.add(this.colorPincel);
        panelBotones.add(nivBorLabel);
        panelBotones.add(this.grosor);
        panelBotones.add(degLabel);
        panelBotones.add(this.colorPincelDeg);
        panelBotones.add(nivDegLabel);
        panelBotones.add(this.degradado);
        panelBotones.add(combo);
        this.getContentPane().add(panelBotones, BorderLayout.NORTH);
    }

    public void createLienzo() {
        this.getContentPane().add(lienzo, BorderLayout.CENTER);
    }

    public void createFigurasBottons() {
        String[] shapes = {"Cuadrado", "Circulo", "Linea", "cubicCurve"};

        String[] iconsV = {"gorda 2.png", "gorda 3.png", "gorda 4.png", "CUBICCURVE2D.png"};

        
        JLabel formsLabel = new JLabel("Formas");
        formsLabel.setHorizontalAlignment(0);
        formsLabel.setPreferredSize(new Dimension(100,35));
        
        
        for (int i = 0; i < shapes.length; i++) {
            ImageIcon iconShape = new ImageIcon("src/icons/" + iconsV[i]);
            JButton btn = new JButton(iconShape);
            btn.setPreferredSize(new Dimension(50, 35));
            String Shape = shapes[i];
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    shapeActual = Shape;
                    lienzo.setShapeType(shapeActual);
                }
            });

            this.shappes.add(btn);
        }

        String[] controls = {"Limpiar", "Goma", "Relleno"};
        String[] iconsC = {"reset.png", "goma.png", "relleno.png"};

        for (int i = 0; i < controls.length; i++) {
            ImageIcon iconShape = new ImageIcon("src/icons/" + iconsC[i]);
            JButton btn = new JButton(iconShape);
            btn.setPreferredSize(new Dimension(50, 35));
            String control = controls[i];
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (control.equals("Limpiar")) {
                        lienzo.resetShapes();
                    } else if (control.equals("Relleno")) {
                        if (lienzo.getRelleno() == true) {
                            lienzo.setRelleno(false);
                        } else {
                            lienzo.setRelleno(true);
                        }
                    } else if (control.equals("Goma")) {
                        lienzo.setShapeType("goma");
                    }
                }
            });

            this.shappes.add(btn);
        }
        JPanel figuras = new JPanel();
        figuras.add(formsLabel);
        figuras.setLayout(new FlowLayout());
        figuras.setPreferredSize(new Dimension(150, 300));
        for (int i = 0; i < this.shappes.size(); i++) {
            figuras.add(shappes.get(i));
        }

        this.getContentPane().add(figuras, BorderLayout.WEST);
    }

    public void createTranforms() {
        String[] transforms = {"Rotacion", "Traslacion", "Escalar", "Cillamineto", "Reflexion"};

        for (int i = 0; i < transforms.length; i++) {
            JButton btn = new JButton(transforms[i]);
            btn.setPreferredSize(new Dimension(150, 35));
            String tr = transforms[i];
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    lienzo.setShapeType(tr);
                }
            });
            this.transformations.add(btn);
        }

        this.panelTransforms = new JPanel();
        this.panelTransforms.setLayout(new FlowLayout());
        this.panelTransforms.setPreferredSize(new Dimension(170, 300));
        for (int i = 0; i < this.transformations.size(); i++) {
            panelTransforms.add(this.transformations.get(i));
        }

        this.getContentPane().add(this.panelTransforms, BorderLayout.EAST);
    }
}
