/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vistas;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author manuel
 */
public class LienzoPanel extends JPanel implements MouseListener, MouseMotionListener {

    private Vector shapes = new Vector();
    private Vector shapesColors = new Vector();
    private Vector shapesColorsDeg = new Vector();
    private Vector valDeg = new Vector();
    private Vector grosorShapes = new Vector();
    private Vector rellenoShapes = new Vector();
    private Vector tipoDeg = new Vector();
    private Vector tipoTrans = new Vector();
    private Vector intentTrans = new Vector();
    private Vector<String> image = new Vector<String>();
    private Vector<Point> puntos = new Vector<Point>();

    private Vector<Integer> iST = new Vector<Integer>();
    private Vector<Double> iSx = new Vector<Double>();
    private Vector<Double> iSy = new Vector<Double>();
    
    private boolean relleno = false;

    private Color color = Color.BLACK;
    private Color degradado = Color.BLACK;
    private int valDegradado = 1;
    private int orDegradado = 1;

    static final int RECTANGLE = 0;
    static final int ROUNDRECTANGLE2D = 1;
    static final int ELLIPSE2D = 2;
    static final int ARC2D = 3;
    static final int LINE2D = 4;
    static final int QUADCURVE2D = 5;
    static final int CUBICCURVE2D = 6;
    static final int POLYGON = 7;
    static final int GENERAL = 8;
    static final int AREA = 9;
    static final int GOMA = 10;

    static final int TRANSLATION = 11;
    static final int ROTATION = 12;
    static final int SCALING = 13;
    static final int SHEARING = 14;
    static final int REFLECTION = 15;
    static final int PUNTERO = 16;

    private int shapeType = PUNTERO;
    private int rotateGlobal = 0;
    
    private int indexTransform = -1;
    private int grosor = 1;
    // vector of input points
    Vector points = new Vector();
    int pointIndex = 0;
    int ruleIndex = -1;
    int[] rules = {AlphaComposite.CLEAR, AlphaComposite.SRC_OVER,
        AlphaComposite.DST_OVER, AlphaComposite.SRC_IN,
        AlphaComposite.DST_IN, AlphaComposite.SRC_OUT,
        AlphaComposite.DST_OUT, AlphaComposite.SRC,
        AlphaComposite.DST, AlphaComposite.SRC_ATOP,
        AlphaComposite.DST_ATOP, AlphaComposite.XOR};
    float intensidadTrans = 0.5f;

    Shape partialShape = null;
    Point p = null;
    Point pT = null; //Transforms

    Shape tempShape = null;

    private int x0 = 320;
    private int y0 = 240;

    JPopupMenu miMenuPopup = new JPopupMenu("cambiar p.shappes");

    //cambiar propiedades
    int indexPropiedad = 0;

    public LienzoPanel() {
        super();
        createJpopuMenu();
        setBackground(Color.white);
        setPreferredSize(new Dimension(640, 480));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void createJpopuMenu() {
        String[] opcionesJpopuMenu = {"Color Primario", "Color Secundario", "Grosor", "Degradado", "Transparencia", "Rellenar con imagen"};
        for (int i = 0; i < opcionesJpopuMenu.length; i++) {
            if (!opcionesJpopuMenu[i].equals("Degradado") && !opcionesJpopuMenu[i].equals("Transparencia")) {
                JMenuItem opcion = new JMenuItem(opcionesJpopuMenu[i]);
                opcion.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cambiarPropiedades(opcion.getText());
                    }
                });
                miMenuPopup.add(opcion);
            } else if (opcionesJpopuMenu[i].equals("Degradado")) {
                JSeparator separador = new JSeparator();
                miMenuPopup.add(separador);
                JMenu deg = new JMenu("Degradado");
                String[] opcionesDegradado = {"D.Nivel", "D.Tipo"};
                for (int j = 0; j < opcionesDegradado.length; j++) {
                    JMenuItem opcion = new JMenuItem(opcionesDegradado[j]);
                    opcion.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cambiarPropiedades(opcion.getText());
                        }
                    });
                    deg.add(opcion);
                }
                miMenuPopup.add(deg);
            } else if (opcionesJpopuMenu[i].equals("Transparencia")) {
                JSeparator separador = new JSeparator();
                miMenuPopup.add(separador);
                JMenu trans = new JMenu("Transparencia");
                String[] opcionesTrans = {"T.Nivel", "T.Tipo"};
                for (int j = 0; j < opcionesTrans.length; j++) {
                    JMenuItem opcion = new JMenuItem(opcionesTrans[j]);
                    opcion.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            cambiarPropiedades(opcion.getText());
                        }
                    });
                    trans.add(opcion);
                }
                miMenuPopup.add(trans);
            }
        }

    }

    private void cambiarPropiedades(String propiedad) {
        if (propiedad.equals("Color Primario")) {
            Color newColor = JColorChooser.showDialog(null, "Escoge un color", color);
            shapesColors.set(this.indexPropiedad, newColor);
            repaint();
        } else if (propiedad.equals("Color Secundario")) {
            Color newColor = JColorChooser.showDialog(null, "Escoge un color", color);
            shapesColorsDeg.set(this.indexPropiedad, newColor);
            repaint();
        } else if (propiedad.equals("Grosor")) {
            JOptionPane paneProp = new JOptionPane();
            JSlider slider = new JSlider(1, 10, (int) this.grosorShapes.get(indexPropiedad));
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);
            //slider.setPaintLabels(true);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider theSlider = (JSlider) e.getSource();
                    if (!theSlider.getValueIsAdjusting()) {
                        grosorShapes.set(indexPropiedad, theSlider.getValue());
                        repaint();
                    }
                }
            });

            paneProp.setMessage(new Object[]{"Selecciona el nuevo Grosor: ", slider});
            paneProp.setMessageType(JOptionPane.QUESTION_MESSAGE);
            JDialog dialog = paneProp.createDialog(this, "Grosor");
            dialog.setVisible(true);
        } else if (propiedad.equals("D.Nivel")) {
            JOptionPane paneProp = new JOptionPane();
            JSlider slider = new JSlider(1, 1000, (int) this.valDeg.get(indexPropiedad));
            slider.setMajorTickSpacing(50);
            slider.setPaintTicks(true);
            //slider.setPaintLabels(true);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider theSlider = (JSlider) e.getSource();
                    if (!theSlider.getValueIsAdjusting()) {
                        paneProp.setInputValue(theSlider.getValue());
                        valDeg.set(indexPropiedad, paneProp.getInputValue());
                        repaint();
                    }
                }
            });

            paneProp.setMessage(new Object[]{"Selecciona el nivel de degradado: ", slider});
            paneProp.setMessageType(JOptionPane.QUESTION_MESSAGE);
            JDialog dialog = paneProp.createDialog(this, "Degradado");
            dialog.setVisible(true);
        } else if (propiedad.equals("D.Tipo")) {
            JOptionPane paneProp = new JOptionPane();
            JComboBox opciones = new JComboBox();
            opciones.addItem("Horizontal");
            opciones.addItem("Vertical");
            opciones.addItem("Diagonal");
            opciones.addItem("Redondo");
            opciones.addItem("H.Ciclico");
            opciones.addItem("V.Ciclico");
            opciones.addItem("D.Ciclico");
            opciones.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    tipoDeg.set(indexPropiedad, (int) whathIsOrDegradado(opciones.getSelectedItem().toString()));
                    repaint();

                }
            });

            paneProp.setMessage(new Object[]{"Selecciona el tipo de degradado: ", opciones});
            paneProp.setMessageType(JOptionPane.QUESTION_MESSAGE);
            JDialog dialog = paneProp.createDialog(this, "Transparencia");
            dialog.setVisible(true);

        } else if (propiedad.equals("T.Nivel")) {
            JOptionPane paneProp = new JOptionPane();
            JSlider slider = new JSlider(1, 10, Math.round((float) this.intentTrans.get(indexPropiedad) * 10));
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);
            //slider.setPaintLabels(true);
            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider theSlider = (JSlider) e.getSource();
                    if (!theSlider.getValueIsAdjusting()) {
                        intentTrans.set(indexPropiedad, (float) theSlider.getValue() / 10.0f);
                        repaint();
                    }
                }
            });

            paneProp.setMessage(new Object[]{"Selecciona el nivel de transparencia: ", slider});
            paneProp.setMessageType(JOptionPane.QUESTION_MESSAGE);
            JDialog dialog = paneProp.createDialog(this, "Transparencia");
            dialog.setVisible(true);
        } else if (propiedad.equals("T.Tipo")) {
            JOptionPane paneProp = new JOptionPane();
            JSlider slider = new JSlider(-1, 11, (int) this.tipoTrans.get(indexPropiedad));
            slider.setMajorTickSpacing(1);
            slider.setPaintTicks(true);

            slider.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider theSlider = (JSlider) e.getSource();
                    if (!theSlider.getValueIsAdjusting()) {
                        tipoTrans.set(indexPropiedad, (int) theSlider.getValue());
                        repaint();
                    }
                }
            });
            paneProp.setMessage(new Object[]{"Selecciona el tipo de transparencia: ", slider});
            paneProp.setMessageType(JOptionPane.QUESTION_MESSAGE);
            JDialog dialog = paneProp.createDialog(this, "Transparencia");
            dialog.setVisible(true);
            System.out.println("tipo transparencia");
        } else if (propiedad.equals("Desrrellenar")) {
            rellenoShapes.set(indexPropiedad, false);
        } else if (propiedad.equals("Rellenar")) {
            rellenoShapes.set(indexPropiedad, true);
        } else if (propiedad.equals("Rellenar con imagen")) {
            String imagePath = openImage();
            if(imagePath!="")
            {
                this.image.set(indexPropiedad, imagePath);
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(this.color);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(x0, y0);
        g2.rotate(this.rotateGlobalToPi());
        g2.drawLine(-200, 0, 200, 0);
        g2.drawLine(0, -200, 0, 200);
        /*ImageIcon image = new ImageIcon("src/icons/mel.jpg");
        g2.drawImage(image.getImage(),-320,-240,640,480,null);
         */
        for (int i = 0; i < shapes.size(); i++) {
            Shape s = (Shape) shapes.get(i);
            g2.setStroke(new BasicStroke((int) this.grosorShapes.get(i)));
            //g2.rotate(this.rotateGlobalToPi(),pT.x, pT.y);
            
            if ((int) this.tipoTrans.get(i) > -1) {
                AlphaComposite ac = AlphaComposite.getInstance(rules[(int) this.tipoTrans.get(i)], (float) intentTrans.get(i));
                g2.setComposite(ac);
            } else {
                AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
                g2.setComposite(ac);
            }

            if ((boolean) this.rellenoShapes.get(i) == true) {
                if ((int) this.tipoDeg.get(i) == 1) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), 0, (int) this.valDeg.get(i), (Color) shapesColorsDeg.get(i)));
                } else if ((int) this.tipoDeg.get(i) == 2) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), (int) this.valDeg.get(i), 0, (Color) shapesColorsDeg.get(i)));
                } else if ((int) this.tipoDeg.get(i) == 3) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), (int) this.valDeg.get(i) / 2, (int) this.valDeg.get(i) / 2, (Color) shapesColorsDeg.get(i)));
                } else if ((int) this.tipoDeg.get(i) == 4) {
                    g2.setPaint(new GradientPaint((int) this.valDeg.get(i) / 2, (int) this.valDeg.get(i) / 2, (Color) shapesColors.get(i), 0, 100, (Color) shapesColorsDeg.get(i)));
                } else if ((int) this.tipoDeg.get(i) == 5) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), 0, (int) this.valDeg.get(i), (Color) shapesColorsDeg.get(i), true));
                } else if ((int) this.tipoDeg.get(i) == 6) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), (int) this.valDeg.get(i), 0, (Color) shapesColorsDeg.get(i), true));
                } else if ((int) this.tipoDeg.get(i) == 7) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), (int) this.valDeg.get(i) / 2, (int) this.valDeg.get(i) / 2, (Color) shapesColorsDeg.get(i), true));
                }

                g2.fill(s);
            } else {

                g2.setColor((Color) shapesColors.get(i));
                g2.draw(s);
            }
            
            if(this.image.get(i)!="")
            {
                try {
                    BufferedImage slate = ImageIO.read(new File(this.image.get(i)));
                    TexturePaint slatetp = new TexturePaint(slate,  new Rectangle(0, 0, 240, 120));
                    g2.setPaint(slatetp);
                    g2.fill(s);
                } catch (IOException ex) {
                    Logger.getLogger(LienzoPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /*try {
                BufferedImage slate = ImageIO.read(new File("src/icons/mel.jpg"));
                TexturePaint slatetp = new TexturePaint(slate,  new Rectangle(0, 0, 240, 120));
                g2.setPaint(slatetp);
                g2.fill(s);
            } catch (IOException ex) {
                Logger.getLogger(LienzoPanel.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }

    }

    public void mouseClicked(MouseEvent ev) {
        if (ev.getClickCount() == 1) {
            if (ev.getButton() == MouseEvent.BUTTON1) {
                System.out.println("click izquierdo");
            } else {
                int index = this.contieneShapePont(ev.getPoint());
                if (index > -1) {
                    indexPropiedad = index;

                    if (miMenuPopup.countComponents() == 9) {
                        miMenuPopup.remove(8);
                    }
                    if ((boolean) this.rellenoShapes.get(indexPropiedad) == true) {
                        JMenuItem relleno = new JMenuItem("Desrrellenar");
                        relleno.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cambiarPropiedades(relleno.getText());
                                repaint();
                            }
                        });
                        miMenuPopup.add(relleno);
                    } else {
                        JMenuItem relleno = new JMenuItem("Rellenar");
                        relleno.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                cambiarPropiedades(relleno.getText());
                                repaint();
                            }
                        });
                        miMenuPopup.add(relleno);
                    }

                    this.miMenuPopup.show(this, ev.getX(), ev.getY());
                }

            }
        }
    }

    public void mouseEntered(MouseEvent ev) {
    }

    public void mouseExited(MouseEvent ev) {
    }

    public void mousePressed(MouseEvent ev) {
        if (ev.getButton() == MouseEvent.BUTTON1) {
            if (this.shapeType == GOMA) {
                int i = contieneShapePont(ev.getPoint());
                if (i > -1) {
                    this.shapes.remove(i);
                    this.grosorShapes.remove(i);
                    this.tipoTrans.remove(i);
                    this.intentTrans.remove(i);
                    this.shapesColors.remove(i);
                    this.shapesColorsDeg.remove(i);
                    this.image.remove(i);
                    this.puntos.remove(i);
                    this.tipoDeg.remove(i);
                    this.valDeg.remove(i);
                    this.rellenoShapes.remove(i);
                    this.tempShape = null;
                    repaint();
                }
                //points.clear();
            } else if (this.shapeType == TRANSLATION || this.shapeType == ROTATION || this.shapeType == SCALING || this.shapeType == SHEARING || this.shapeType == REFLECTION) {
                int i = contieneShapePont(ev.getPoint());
                if (i > -1) {
                    this.indexTransform = i;
                    this.tempShape = null;
                    this.pT = ev.getPoint();

                    if (this.shapeType == SCALING) {
                        Shape pAux = (Shape) this.shapes.get(i);

                        this.iST.add(i);
                        this.iSx.add(pAux.getBounds2D().getCenterX());
                        this.iSy.add(pAux.getBounds2D().getCenterY());

                    }
                }

            }
            points.add(ev.getPoint());
            pointIndex++;
            //System.err.println(points);
            //System.err.println(pointIndex);
            p = null;
            System.out.print("\n" + points);
        }
    }

    public void mouseReleased(MouseEvent ev) {
        if (ev.getButton() == MouseEvent.BUTTON1) {
            Graphics g = getGraphics();
            //g.setColor(this.color);
            Point p1 = (Point) (points.get(pointIndex - 1));
            p = ev.getPoint();
            //System.err.println(p);
            //System.err.println(p1);
            Shape s = null;
            Shape trr = null;
            AffineTransform tr = new AffineTransform();
            switch (shapeType) {
                case RECTANGLE:
                    s = new Rectangle(p1.x - x0, p1.y - y0, p.x - p1.x, p.y - p1.y);
                    break;
                case ROUNDRECTANGLE2D:
                    s = new RoundRectangle2D.Float(p1.x - x0, p1.y - y0, p.x - p1.x, p.y - p1.y, 10, 10);
                    break;
                case ELLIPSE2D:
                    s = new Ellipse2D.Float(p1.x - x0, p1.y - y0, p.x - p1.x, p.y - p1.y);
                    break;
                case ARC2D:
                    s = new Arc2D.Float(p1.x - x0, p1.y - y0, p.x - p1.x, p.y - p1.y, 30, 120, Arc2D.OPEN);
                    break;
                case LINE2D:
                    s = new Line2D.Float(p1.x - x0, p1.y - y0, p.x - x0, p.y - y0);
                    break;
                case QUADCURVE2D:
                    if (pointIndex > 1) {
                        Point p2 = (Point) points.get(0);
                        s = new QuadCurve2D.Float(p2.x - x0, p2.y - y0, p1.x - x0, p1.y - y0, p.x - x0, p.y - y0);
                    }
                    break;
                case CUBICCURVE2D:
                    if (pointIndex > 2) {
                        Point p2 = (Point) points.get(pointIndex - 2);
                        Point p3 = (Point) points.get(pointIndex - 3);
                        s = new CubicCurve2D.Float(p3.x - x0, p3.y - y0, p2.x - x0, p2.y - y0, p1.x - x0, p1.y - y0, p.x - x0, p.y - y0);
                    }
                    break;
                case POLYGON:
                    if (ev.isShiftDown()) {
                        s = new Polygon();
                        for (int i = 0; i < pointIndex; i++) {
                            ((Polygon) s).addPoint(((Point) points.get(i)).x, ((Point) points.get(i)).y);
                        }
                        ((Polygon) s).addPoint(p.x - x0, p.y - y0);
                    }
                    break;

                case TRANSLATION:
                    p1 = ev.getPoint();
                    tr = new AffineTransform();
                    tr.setToTranslation(p1.x - this.pT.x, p1.y - this.pT.y);
                    drawTransformShape(tr);
                    break;

                case ROTATION:
                    p1 = ev.getPoint();
                    tr = new AffineTransform();
                    double a = Math.atan2(p1.y - y0, p1.x - x0) - Math.atan2(pT.y - y0, pT.x - x0);

                    trr = (Shape) this.shapes.get(indexTransform);
                    tr.setToRotation(a, trr.getBounds2D().getCenterX(), trr.getBounds2D().getCenterY());
                    drawTransformShape(tr);
                    repaint();
                    break;

                case SCALING:
                    p1 = ev.getPoint();
                    double sx = Math.abs((double) (p1.x) / (pT.x));
                    double sy = Math.abs((double) (p1.y) / (pT.y));
                    tr.setToScale(sx, sy);
                    drawTransformShape(tr);

                    break;

                case SHEARING:
                    p1 = ev.getPoint();
                    double shx = ((double) (p1.x) / (pT.x)) - 1;
                    double shy = ((double) (p1.y) / (pT.y)) - 1;
                    tr.setToShear(shx, shy);
                    drawTransformShape(tr);
                    break;

                case REFLECTION:
                    tr.setTransform(-1, 0, 0, 1, 0, 0);
                    drawTransformShape(tr);
                    break;

            }
            if (s != null) {
                shapes.add(s);
                shapesColors.add(this.color);
                shapesColorsDeg.add(this.degradado);
                image.add("");
                this.puntos.add(new Point(p1.x,p1.y));
                tipoDeg.add(this.orDegradado);
                valDeg.add(this.valDegradado);
                grosorShapes.add(this.grosor);
                this.tipoTrans.add(this.ruleIndex);
                this.intentTrans.add(this.intensidadTrans);
                rellenoShapes.add(this.relleno);
                points.clear();
                pointIndex = 0;
                p = null;

                repaint();
            }
        }
    }

    public void mouseMoved(MouseEvent ev) {
    }

    public void mouseDragged(MouseEvent ev) {

        Graphics2D g = (Graphics2D) getGraphics();
        g.setXORMode(Color.white);
        g.setColor(this.color);
        g.setStroke(new BasicStroke(this.grosor));

        Point p1 = (Point) points.get(pointIndex - 1);

        AffineTransform tr = new AffineTransform();
        switch (shapeType) {
            case RECTANGLE:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (p != null) {
                    g.drawRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                }
                p = ev.getPoint();
                //;
                g.drawRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ROUNDRECTANGLE2D:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (p != null) {
                    g.drawRoundRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 10, 10);
                }
                p = ev.getPoint();
                g.drawRoundRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 10, 10);
                break;
            case ELLIPSE2D:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (p != null) {
                    g.drawOval(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                }
                p = ev.getPoint();
                g.drawOval(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ARC2D:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (p != null) {
                    g.drawArc(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 30, 120);
                }
                p = ev.getPoint();
                g.drawArc(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 30, 120);
                break;
            case LINE2D:
            case POLYGON:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (p != null) {
                    g.drawLine(p1.x, p1.y, p.x, p.y);
                }
                p = ev.getPoint();
                g.drawLine(p1.x, p1.y, p.x, p.y);
                break;
            case QUADCURVE2D:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (pointIndex == 1) {
                    if (p != null) {
                        g.drawLine(p1.x, p1.y, p.x, p.y);
                    }
                    p = ev.getPoint();
                    g.drawLine(p1.x, p1.y, p.x, p.y);
                } else {
                    Point p2 = (Point) points.get(pointIndex - 2);
                    if (p != null) {
                        g.draw(partialShape);
                    }
                    p = ev.getPoint();
                    partialShape = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                    g.draw(partialShape);
                }
                break;
            case CUBICCURVE2D:
                g.rotate(this.rotateGlobalToPi(),p1.x, p1.y);
                if (pointIndex == 1) {
                    if (p != null) {
                        g.drawLine(p1.x, p1.y, p.x, p.y);
                    }
                    p = ev.getPoint();
                    g.drawLine(p1.x, p1.y, p.x, p.y);
                } else if (pointIndex == 2) {
                    Point p2 = (Point) points.get(pointIndex - 2);
                    if (p != null) {
                        g.draw(partialShape);
                    }
                    p = ev.getPoint();
                    partialShape = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                    g.draw(partialShape);
                } else {
                    Point p2 = (Point) points.get(pointIndex - 2);
                    Point p3 = (Point) points.get(pointIndex - 3);
                    if (p != null) {
                        g.draw(partialShape);
                    }
                    p = ev.getPoint();
                    partialShape = new CubicCurve2D.Float(p3.x, p3.y, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                    g.draw(partialShape);
                }
                break;

            case TRANSLATION:
                p1 = ev.getPoint();
                tr = new AffineTransform();
                tr.setToTranslation(p1.x - pT.x, p1.y - pT.y);

                drawTransformShapeTemp(tr);
                break;

            case ROTATION:
                p1 = ev.getPoint();
                double a = Math.atan2(p1.y - y0, p1.x - x0) - Math.atan2(pT.y - y0, pT.x - x0);

                Shape trr = (Shape) this.shapes.get(indexTransform);
                tr.setToRotation(a, trr.getBounds2D().getCenterX(), trr.getBounds2D().getCenterY());

                drawTransformShapeTemp(tr);
                break;

            case SCALING:
                p1 = ev.getPoint();
                tr = new AffineTransform();
                double sx = Math.abs((double) (p1.x - 0) / (pT.x - 0));
                double sy = Math.abs((double) (p1.y - 0) / (pT.y - 0));
                tr.setToScale(sx, sy);
                drawTransformShapeTempT(tr);
                break;

            case SHEARING:
                p1 = ev.getPoint();
                double shx = ((double) (p1.x - 0) / (pT.x - 0)) - 1;
                double shy = ((double) (p1.y - 0) / (pT.y - 0)) - 1;
                tr.setToShear(shx, shy);
                drawTransformShapeTempT(tr);
                break;

            case REFLECTION:
                tr.setTransform(-1, 0, 0, 1, 0, 0);
                drawTransformShapeTempT(tr);
                break;
        }
        //System.err.println("Dragged");

    }

    public void setColorPincel(Color color) {
        this.color = color;
    }

    public void drawTransformShapeTemp(AffineTransform tr) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setXORMode(Color.white);

        g.translate(x0, y0);
        if (tempShape != null) {
            g.draw(tempShape);
        }
        tempShape = tr.createTransformedShape((Shape) this.shapes.get(indexTransform));
        g.draw(tempShape);
    }

    public void drawTransformShape(AffineTransform tr) {
        this.shapes.set(this.indexTransform, tr.createTransformedShape((Shape) this.shapes.get(this.indexTransform)));
        repaint();
    }

    public void drawTransformShapeTempT(AffineTransform tr) {
        Graphics2D g = (Graphics2D) getGraphics();
        g.setXORMode(Color.white);
        g.translate(x0, y0);
        if (tempShape != null) {
            g.draw(tempShape);
        }
        tempShape = tr.createTransformedShape((Shape) this.shapes.get(indexTransform));

        g.draw(tempShape);
    }

    public void setShapeType(String nameShape) {
        if (nameShape.equalsIgnoreCase("Circulo")) {
            this.shapeType = ELLIPSE2D;
        } else if (nameShape.equalsIgnoreCase("Cuadrado")) {
            this.shapeType = RECTANGLE;
        } else if (nameShape.equalsIgnoreCase("Linea")) {
            this.shapeType = LINE2D;
        } else if (nameShape.equalsIgnoreCase("cubicCurve")) {
            this.shapeType = CUBICCURVE2D;
        } else if (nameShape.equalsIgnoreCase("goma")) {
            this.shapeType = GOMA;
        } else if (nameShape.equalsIgnoreCase("Traslacion")) {
            this.shapeType = TRANSLATION;
        } else if (nameShape.equals("Rotacion")) {
            this.shapeType = ROTATION;
        } else if (nameShape.equals("Escalar")) {
            this.shapeType = SCALING;
        } else if (nameShape.equals("Cillamineto")) {
            this.shapeType = SHEARING;
        } else if (nameShape.equals("Reflexion")) {
            this.shapeType = REFLECTION;
        } else if (nameShape.equals("Puntero")) {
            this.shapeType = PUNTERO;
        }
    }

    public void resetShapes() {
        shapes = new Vector();
        shapesColors = new Vector();
        shapesColorsDeg = new Vector();
        image = new Vector();
        puntos = new Vector<Point>();
        tipoDeg = new Vector();
        valDeg = new Vector();
        grosorShapes = new Vector();
        this.tipoTrans = new Vector();
        this.intentTrans = new Vector();
        rellenoShapes = new Vector();
        tempShape = null;
        repaint();
    }

    public void setGrosor(int gr) {
        this.grosor = gr;
    }

    public void setRelleno(boolean re) {
        this.relleno = re;
    }

    public boolean getRelleno() {
        return this.relleno;
    }

    public int contieneShapePont(Point p) {
        for (int i = 0; i < this.shapes.size(); i++) {
            Shape sh = (Shape) this.shapes.get(i);
            Point2D p2 = new Point2D.Double(p.x - x0, p.y - y0);
            if (sh.contains(p2)) {
                return i;
            }
        }
        return -1;
    }

    public void setValDegradado(int val) {
        this.valDegradado = val;
    }

    public void setDegradado(Color col) {
        this.degradado = col;
    }

    public void setOrDegradado(String ori) {
        if (ori.equalsIgnoreCase("Horizontal")) {
            this.orDegradado = 1;
        } else if (ori.equalsIgnoreCase("Vertical")) {
            this.orDegradado = 2;
        } else if (ori.equalsIgnoreCase("Diagonal")) {
            this.orDegradado = 3;
        } else if (ori.equalsIgnoreCase("Redondo")) {
            this.orDegradado = 4;
        } else if (ori.equalsIgnoreCase("H.Ciclico")) {
            this.orDegradado = 5;
        } else if (ori.equalsIgnoreCase("V.Ciclico")) {
            this.orDegradado = 6;
        } else if (ori.equalsIgnoreCase("D.Ciclico")) {
            this.orDegradado = 7;
        }
    }

    public int whathIsOrDegradado(String ori) {
        if (ori.equalsIgnoreCase("Horizontal")) {
            return 1;
        } else if (ori.equalsIgnoreCase("Vertical")) {
            return 2;
        } else if (ori.equalsIgnoreCase("Diagonal")) {
            return 3;
        } else if (ori.equalsIgnoreCase("Redondo")) {
            return 4;
        } else if (ori.equalsIgnoreCase("H.Ciclico")) {
            return 5;
        } else if (ori.equalsIgnoreCase("V.Ciclico")) {
            return 6;
        } else if (ori.equalsIgnoreCase("D.Ciclico")) {
            return 7;
        }
        return -1;
    }

    public void setRuleIndex(int index) {
        this.ruleIndex = index;
        repaint();
    }

    public void setIntenTrans(int inten) {
        this.intensidadTrans = (float) inten / 10;
        repaint();
    }

    public void setBackGroundLienzo(Color color) {
        this.setBackground(color);
        repaint();
    }

    public void saveImage(String name, String type) {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        JFileChooser fc = new JFileChooser();
        int retval = fc.showSaveDialog(this);
        try {
            //ImageIO.write(image, type, new File(name + "." + type));    
            if (retval == JFileChooser.APPROVE_OPTION) {
                try {
                    ImageIO.write(image, "jpg", fc.getSelectedFile());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String openImage() {

        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg", "jpeg","png");
        fc.setFileFilter(filter);
        fc.addChoosableFileFilter(filter);
        
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String sname = file.getAbsolutePath(); //Path de la imagen
            
            return sname;
        }
        return "";
    }
    
    private double rotateGlobalToPi()
    {
        if(this.rotateGlobal==0)
        {
            return 0;
        }else if(this.rotateGlobal==1)
        {
            return Math.PI/6;
        }else if(this.rotateGlobal==2)
        {
            return Math.PI/4;
        }else if(this.rotateGlobal==3)
        {
            return Math.PI/3;
        }else if(this.rotateGlobal==4)
        {
            return Math.PI/2;
        }else if(this.rotateGlobal==5)
        {
            return (2.0/3.0)*Math.PI;
        }else if(this.rotateGlobal==6)
        {
            return (3.0/4.0)*Math.PI;
        }else if(this.rotateGlobal==7)
        {
            return (5.0/6.0)*Math.PI;
        }else if(this.rotateGlobal==8)
        {
            return Math.PI;
        }else if(this.rotateGlobal==9)
        {
            return (7.0/6.0)*Math.PI;
        }else if(this.rotateGlobal==10)
        {
            return (5.0/4.0)*Math.PI;
        }else if(this.rotateGlobal==11)
        {
            return (4.0/3.0)*Math.PI;
        }else if(this.rotateGlobal==12)
        {
            return (3.0/2.0)*Math.PI;
        }else if(this.rotateGlobal==13)
        {
            return (5.0/3.0)*Math.PI;
        }else if(this.rotateGlobal==14)
        {
            return (7.0/4.0)*Math.PI;
        }else if(this.rotateGlobal==15)
        {
            return (11.0/6.0)*Math.PI;
        }
        return 0;
    }
    
    public void setRotateGlobal(int rotate)
    {
        this.rotateGlobal = rotate;
        repaint();
    }
    
    public int getRotateGlobal()
    {
        return this.rotateGlobal;
    }
    
    public void setX0(int x)
    {
        this.x0 = x;
        repaint();
    }
    
    public int getX0()
    {
        return this.x0;
    }
    
    public void setY0(int y)
    {
        this.y0 = y;
        repaint();
    }
    
    public int getY0()
    {
        return this.y0;
    }
    
    
    
}
