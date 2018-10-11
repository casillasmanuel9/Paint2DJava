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
import javax.swing.JFrame;
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
    private Vector<JButton> shappes = new Vector<JButton>();
    private Vector<String> shappesNames = new Vector<String>();
    private String shapeActual;
    private Color color;
    private JPanel panelBotones = new JPanel();
    private LienzoPanel lienzo = new LienzoPanel();
    final private JSlider grosor = new JSlider(1, 10, 1);
    private JButton relleno;

    public FramePaint() {
        init();
    }

    public void init() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setTitle("JAVA2D");
        createBtnMenu();
        createFigurasBottons();
        createLienzo();
        this.pack();
    }

    public void createBtnMenu() {
        panelBotones.setLayout(new FlowLayout());

        ImageIcon iconColors = new ImageIcon("src/icons/colores.png");
        this.colorPincel = new JButton(iconColors);
        this.colorPincel.setPreferredSize(new Dimension(50, 30));

        this.colorPincel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color newColor = JColorChooser.showDialog(null, "Choose a color", color);
                lienzo.setColorPincel(newColor);
            }
        });

        this.grosor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                lienzo.setGrosor(grosor.getValue());
            }
        });

        panelBotones.add(this.colorPincel);
        panelBotones.add(this.grosor);
        this.getContentPane().add(panelBotones, BorderLayout.NORTH);
    }

    public void createLienzo() {
        this.getContentPane().add(lienzo, BorderLayout.CENTER);
    }

    public void createFigurasBottons() {
        String[] shapes = {"Cuadrado","Circulo","Linea","cubicCurve"};

        String[] iconsV = {"gorda 2.png","gorda 3.png","gorda 4.png","CUBICCURVE2D.png"};

        for (int i = 0; i < shapes.length; i++) {
            ImageIcon iconShape = new ImageIcon("src/icons/"+iconsV[i]);
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
        
        String[] controls={"Limpiar","Relleno"};
        String[] iconsC = {"reset.png","relleno.png"};
        
        for (int i = 0; i < controls.length; i++) {
            ImageIcon iconShape = new ImageIcon("src/icons/"+iconsC[i]);
            JButton btn = new JButton(iconShape);
            btn.setPreferredSize(new Dimension(50, 35));
            String control = controls[i];
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(control.equals("Limpiar")){
                        lienzo.resetShapes();
                    }else if(control.equals("Relleno")){
                        if(lienzo.getRelleno()==true)
                        {
                            lienzo.setRelleno(false);
                        }else{
                            lienzo.setRelleno(true);
                        }
                    }
                }
            });

            this.shappes.add(btn);
        }
        /*ImageIcon iconShape = new ImageIcon("src/icons/gorda 3.png");
        JButton btn = new JButton(iconShape);
        btn.setPreferredSize(new Dimension(50, 35));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeActual = "Circulo";
                lienzo.setShapeType(shapeActual);
            }
        });

        this.shappes.add(btn);

        iconShape = new ImageIcon("src/icons/gorda 2.png");
        btn = new JButton(iconShape);
        btn.setPreferredSize(new Dimension(50, 35));
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapeActual = "Cuadrado";
                lienzo.setShapeType(shapeActual);
            }
        });

        this.shappes.add(btn);
*/        
        JPanel figuras = new JPanel();
        figuras.setLayout(new FlowLayout());
        figuras.setPreferredSize(new Dimension(150, 300));
        for (int i = 0; i < this.shappes.size(); i++) {
            figuras.add(shappes.get(i));
        }

        this.getContentPane().add(figuras, BorderLayout.WEST);
    }
}
