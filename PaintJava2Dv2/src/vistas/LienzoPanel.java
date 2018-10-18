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
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
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
import java.util.Vector;
import javax.swing.JPanel;

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

    private int shapeType = CUBICCURVE2D;

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

    public LienzoPanel() {
        super();
        setBackground(Color.white);
        setPreferredSize(new Dimension(640, 480));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //g.setColor(this.color);
        Graphics2D g2 = (Graphics2D) g;
        if (ruleIndex > -1) {
            AlphaComposite ac = AlphaComposite.getInstance(rules[ruleIndex], intensidadTrans);
            g2.setComposite(ac);
        }

        for (int i = 0; i < shapes.size(); i++) {
            Shape s = (Shape) shapes.get(i);
            //g2.setColor((Color) shapesColors.get(i));
            g2.setStroke(new BasicStroke((int) this.grosorShapes.get(i)));

            if ((boolean) this.rellenoShapes.get(i) == true) {
                if ((int) this.tipoDeg.get(i) == 1) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), (int) this.valDeg.get(i), 0, (Color) shapesColorsDeg.get(i)));
                } else if ((int) this.tipoDeg.get(i) == 2) {
                    g2.setPaint(new GradientPaint(0, 0, (Color) shapesColors.get(i), 0, (int) this.valDeg.get(i), (Color) shapesColorsDeg.get(i)));
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

        }

    }

    public void mouseClicked(MouseEvent ev) {
    }

    public void mouseEntered(MouseEvent ev) {
    }

    public void mouseExited(MouseEvent ev) {
    }

    public void mousePressed(MouseEvent ev) {
        if (this.shapeType == GOMA) {
            int i = contieneShapePont(ev.getPoint());
            if (i > -1) {
                this.shapes.remove(i);
                this.grosorShapes.remove(i);
                this.shapesColors.remove(i);
                this.shapesColorsDeg.remove(i);
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

    public void mouseReleased(MouseEvent ev) {
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
                s = new Rectangle(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ROUNDRECTANGLE2D:
                s = new RoundRectangle2D.Float(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 10, 10);
                break;
            case ELLIPSE2D:
                s = new Ellipse2D.Float(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ARC2D:
                s = new Arc2D.Float(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 30, 120, Arc2D.OPEN);
                break;
            case LINE2D:
                s = new Line2D.Float(p1.x, p1.y, p.x, p.y);
                break;
            case QUADCURVE2D:
                if (pointIndex > 1) {
                    Point p2 = (Point) points.get(0);
                    s = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                }
                break;
            case CUBICCURVE2D:
                if (pointIndex > 2) {
                    Point p2 = (Point) points.get(pointIndex - 2);
                    Point p3 = (Point) points.get(pointIndex - 3);
                    s = new CubicCurve2D.Float(p3.x, p3.y, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
                }
                break;
            case POLYGON:
                if (ev.isShiftDown()) {
                    s = new Polygon();
                    for (int i = 0; i < pointIndex; i++) {
                        ((Polygon) s).addPoint(((Point) points.get(i)).x, ((Point) points.get(i)).y);
                    }
                    ((Polygon) s).addPoint(p.x, p.y);
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
                double a = Math.atan2(p1.y, p1.x) - Math.atan2(pT.y, pT.x);

                trr = (Shape) this.shapes.get(indexTransform);
                tr.setToRotation(a, trr.getBounds2D().getCenterX(), trr.getBounds2D().getCenterY());
                drawTransformShape(tr);
                repaint();
                break;

            case SCALING:
                p1 = ev.getPoint();
                double sx = Math.abs((double) (p1.x - 0) / (pT.x - 0));
                double sy = Math.abs((double) (p1.y - 0) / (pT.y - 0));
                tr.setToScale(sx, sy);
                drawTransformShape(tr);

                break;

            case SHEARING:
                p1 = ev.getPoint();
                double shx = ((double) (p1.x - 0) / (pT.x - 0)) - 1;
                double shy = ((double) (p1.y - 0) / (pT.y - 0)) - 1;
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
            tipoDeg.add(this.orDegradado);
            valDeg.add(this.valDegradado);
            grosorShapes.add(this.grosor);
            rellenoShapes.add(this.relleno);
            points.clear();
            pointIndex = 0;
            p = null;

            repaint();
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
                if (p != null) {
                    g.drawRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                }
                p = ev.getPoint();
                g.drawRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ROUNDRECTANGLE2D:
                if (p != null) {
                    g.drawRoundRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 10, 10);
                }
                p = ev.getPoint();
                g.drawRoundRect(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 10, 10);
                break;
            case ELLIPSE2D:
                if (p != null) {
                    g.drawOval(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                }
                p = ev.getPoint();
                g.drawOval(p1.x, p1.y, p.x - p1.x, p.y - p1.y);
                break;
            case ARC2D:
                if (p != null) {
                    g.drawArc(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 30, 120);
                }
                p = ev.getPoint();
                g.drawArc(p1.x, p1.y, p.x - p1.x, p.y - p1.y, 30, 120);
                break;
            case LINE2D:
            case POLYGON:
                if (p != null) {
                    g.drawLine(p1.x, p1.y, p.x, p.y);
                }
                p = ev.getPoint();
                g.drawLine(p1.x, p1.y, p.x, p.y);
                break;
            case QUADCURVE2D:
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
                double a = Math.atan2(p1.y, p1.x) - Math.atan2(pT.y, pT.x);

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
        }
    }

    public void resetShapes() {
        shapes = new Vector();
        shapesColors = new Vector();
        shapesColorsDeg = new Vector();
        tipoDeg = new Vector();
        valDeg = new Vector();
        grosorShapes = new Vector();
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

    private int contieneShapePont(Point p) {
        for (int i = 0; i < this.shapes.size(); i++) {
            Shape sh = (Shape) this.shapes.get(i);
            Point2D p2 = new Point2D.Double(p.x, p.y);
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

    public void setRuleIndex(int index) {
        this.ruleIndex = index;
        repaint();
    }
    
    public void setIntenTrans(int inten)
    {
        this.intensidadTrans = (float)inten/10;
        repaint();
    }
}
