// Define the package name
package com.mathapplication;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.script.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.List;
import java.util.ArrayList;

// For symbolic math using Matheclipse
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.interfaces.IExpr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("all")
public class MathApplication {

    private static final Logger logger = LogManager.getLogger(MathApplication.class);

    // ----------------- Windowed Calculator -----------------

        public static void windowed_calculator() {
            SwingUtilities.invokeLater(() -> {
                JFrame root = new JFrame("Windowed Calculator");
                root.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                new TI84Calculator(root);
                root.setSize(400, 600);
                root.setVisible(true);
            });
        }
    
        public static class TI84Calculator {
            private JTextField entry;
            private final List<ButtonInfo> buttons = new ArrayList<>();
    
            private static class ButtonInfo {
                String text;
                int row;
                int column;
    
                ButtonInfo(String text, int row, int column) {
                    this.text = text;
                    this.row = row;
                    this.column = column;
                }
            }
    
            public TI84Calculator(JFrame root) {
                entry = new JTextField();
                entry.setFont(new Font("Arial", Font.PLAIN, 18));
                entry.setBorder(new LineBorder(Color.BLACK, 2));
                entry.setEditable(false);
    
                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(3, 3, 3, 3);
    
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridwidth = 4;
                panel.add(entry, gbc);
    
                initButtons();
                addButtons(panel, gbc);
    
                root.setContentPane(panel);
            }
    
            private void initButtons() {
                String[][] layout = {
                    {"7", "8", "9", "/"},
                    {"4", "5", "6", "*"},
                    {"1", "2", "3", "-"},
                    {"0", ".", "+", "="},
                    {"sqrt", "^", "cos", "sin"},
                    {"tan", "log", "exp", "("},
                    {")", "pi", "e", "C"}
                };
    
                for (int r = 0; r < layout.length; r++) {
                    for (int c = 0; c < layout[r].length; c++) {
                        buttons.add(new ButtonInfo(layout[r][c], r + 1, c));
                    }
                }
            }
    
            private void addButtons(JPanel panel, GridBagConstraints gbc) {
                for (ButtonInfo bi : buttons) {
                    JButton btn = new JButton(bi.text);
                    btn.setFont(new Font("Arial", Font.BOLD, 16));
                    btn.setPreferredSize(new Dimension(80, 50));
    
                    if (bi.text.equals("=")) {
                        btn.addActionListener(e -> calculate());
                    } else if (bi.text.equals("C")) {
                        btn.addActionListener(e -> entry.setText(""));
                    } else {
                        btn.addActionListener(e -> button_click(bi.text));
                    }
    
                    gbc.gridx = bi.column;
                    gbc.gridy = bi.row;
                    gbc.gridwidth = 1;
                    panel.add(btn, gbc);
                }
            }
    
            private void button_click(String text) {
                String current = entry.getText();
                if (text.equals("pi")) {
                    entry.setText(current + Math.PI);
                } else if (text.equals("e")) {
                    entry.setText(current + Math.E);
                } else {
                    entry.setText(current + text);
                }
            }
    
            private void calculate() {
                try {
                    String expression = entry.getText();
                    // Replace math functions
                    expression = expression.replaceAll("sqrt", "Math.sqrt");
                    expression = expression.replaceAll("cos", "Math.cos");
                    expression = expression.replaceAll("sin", "Math.sin");
                    expression = expression.replaceAll("tan", "Math.tan");
                    expression = expression.replaceAll("log", "Math.log");
                    expression = expression.replaceAll("exp", "Math.exp");
                    expression = expression.replaceAll("(\\d+)\\s*\\^\\s*(\\d+)", "Math.pow($1,$2)");
    
                    ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
                    Object result = engine.eval(expression);
                    entry.setText(result.toString());
                } catch (Exception e) {
                    entry.setText("Error");
                }
            }
        }

    // ----------------- Round Result -----------------
    public static double round_result(double result, int decimal_places) {
        double factor = Math.pow(10, decimal_places);
        return Math.round(result * factor) / factor;
    }

    // ----------------- Simple Math -----------------
    public static void simplemath() {
        System.out.println("Evaluated Math Calculator (type 'exit' to return to menu)");
        Scanner scanner = new Scanner(System.in);
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    
        while (true) {
            System.out.print(">: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            String parsedInput = input.replaceAll("(\\d+(?:\\.\\d+)?)\\s*\\^\\s*(\\d+(?:\\.\\d+)?)", "Math.pow($1, $2)");
    
            try {
                Object res = engine.eval(parsedInput);
                System.out.println(res);
            } catch (Exception e) {
                System.out.println("Err: " + e.getMessage());
            }
        }
    }
    
    

    // ----------------- Area of Shapes -----------------
    public static void area_of_shapes() {
        System.out.println("Area of Shapes");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a shape\n-----------------\nrectangle\ntriangle\ncircle\ntrapezoid\nrhombus\nhexagon\noctagon\n-----------------\nto calculate area (or 'exit' to quit): ");
            String shape = scanner.nextLine().toLowerCase();
            if (shape.equals("exit")) {
                break;
            }
            System.out.print("Enter the unit (cm, ft, yd): ");
            String unit = scanner.nextLine();
            try {
                if (shape.equals("rectangle")) {
                    System.out.print("Enter length: ");
                    double l = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter width: ");
                    double w = Double.parseDouble(scanner.nextLine());
                    double area = l * w;
                    System.out.println("Area of rectangle: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("triangle")) {
                    System.out.print("Enter base: ");
                    double b = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter height: ");
                    double h = Double.parseDouble(scanner.nextLine());
                    double area = 0.5 * b * h;
                    System.out.println("Area of triangle: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("circle")) {
                    System.out.print("Enter radius: ");
                    double r = Double.parseDouble(scanner.nextLine());
                    double area = Math.PI * Math.pow(r, 2);
                    System.out.println("Area of circle: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("trapezoid")) {
                    System.out.print("Enter base 1: ");
                    double b1 = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter base 2: ");
                    double b2 = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter height: ");
                    double h = Double.parseDouble(scanner.nextLine());
                    double area = 0.5 * (b1 + b2) * h;
                    System.out.println("Area of trapezoid: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("rhombus")) {
                    System.out.print("Enter diagonal 1: ");
                    double p = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter diagonal 2: ");
                    double q = Double.parseDouble(scanner.nextLine());
                    double area = 0.5 * (p * q);
                    System.out.println("Area of rhombus: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("hexagon")) {
                    System.out.print("Enter side length: ");
                    double a = Double.parseDouble(scanner.nextLine());
                    double area = 0.5 * (3 * Math.sqrt(3)) * Math.pow(a, 2);
                    System.out.println("Area of hexagon: " + area + " " + unit + "\u00B2");
                } else if (shape.equals("octagon")) {
                    System.out.print("Enter side length: ");
                    double a = Double.parseDouble(scanner.nextLine());
                    double area = 2 * (1+Math.sqrt(2)) * Math.pow(a, 2);
                    System.out.println("Side of octagon: " + area + " " + unit + "\u00B2");
                } else {
                    System.out.println("Invalid shape, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Perimeter and Circumference -----------------
    public static void perimeter_and_circumference() {
        System.out.println("Perimeter and Circumference");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a shape (rectangle, triangle, circle) to calculate perimeter/circumference (or 'exit' to quit): ");
            String shape = scanner.nextLine().toLowerCase();
            if (shape.equals("exit")) {
                break;
            }
            System.out.print("Enter the unit (cm, ft, yd): ");
            String unit = scanner.nextLine();
            try {
                if (shape.equals("rectangle")) {
                    System.out.print("Enter length: ");
                    double l = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter width: ");
                    double w = Double.parseDouble(scanner.nextLine());
                    double perimeter = 2 * (l + w);
                    System.out.println("Perimeter of rectangle: " + perimeter + " " + unit);
                } else if (shape.equals("triangle")) {
                    System.out.print("Enter side a: ");
                    double a = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter side b: ");
                    double b = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter side c: ");
                    double c = Double.parseDouble(scanner.nextLine());
                    double perimeter = a + b + c;
                    System.out.println("Perimeter of triangle: " + perimeter + " " + unit);
                } else if (shape.equals("circle")) {
                    System.out.print("Enter radius: ");
                    double r = Double.parseDouble(scanner.nextLine());
                    double circumference = 2 * Math.PI * r;
                    System.out.println("Circumference of circle: " + circumference + " " + unit);
                } else {
                    System.out.println("Invalid shape, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Volume of Solids -----------------
    public static void volume_of_solids() {
        System.out.println("Volume of Solids");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a solid (cube, rectangular prism, cylinder) to calculate volume (or 'exit' to quit): ");
            String solid = scanner.nextLine().toLowerCase();
            if (solid.equals("exit")) {
                break;
            }
            System.out.print("Enter the unit (cm³, ft³, yd³): ");
            String unit = scanner.nextLine();
            try {
                if (solid.equals("cube")) {
                    System.out.print("Enter side length: ");
                    double s = Double.parseDouble(scanner.nextLine());
                    double volume = Math.pow(s, 3);
                    System.out.println("Volume of cube: " + volume + " " + unit);
                } else if (solid.equals("rectangular prism")) {
                    System.out.print("Enter length: ");
                    double l = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter width: ");
                    double w = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter height: ");
                    double h = Double.parseDouble(scanner.nextLine());
                    double volume = l * w * h;
                    System.out.println("Volume of rectangular prism: " + volume + " " + unit);
                } else if (solid.equals("cylinder")) {
                    System.out.print("Enter radius: ");
                    double r = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter height: ");
                    double h = Double.parseDouble(scanner.nextLine());
                    double volume = Math.PI * Math.pow(r, 2) * h;
                    System.out.println("Volume of cylinder: " + volume + " " + unit);
                } else {
                    System.out.println("Invalid solid, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Surface Area of Solids -----------------
    public static void surface_area_of_solids() {
        System.out.println("Surface Area of Solids");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a solid (cube, rectangular prism, sphere) to calculate surface area (or 'exit' to quit): ");
            String solid = scanner.nextLine().toLowerCase();
            if (solid.equals("exit")) {
                break;
            }
            System.out.print("Enter the unit (cm², ft², yd²): ");
            String unit = scanner.nextLine();
            try {
                if (solid.equals("cube")) {
                    System.out.print("Enter side length: ");
                    double s = Double.parseDouble(scanner.nextLine());
                    double surface_area = 6 * Math.pow(s, 2);
                    System.out.println("Surface area of cube: " + surface_area + " " + unit);
                } else if (solid.equals("rectangular prism")) {
                    System.out.print("Enter length: ");
                    double l = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter width: ");
                    double w = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter height: ");
                    double h = Double.parseDouble(scanner.nextLine());
                    double surface_area = 2 * (l * w + l * h + w * h);
                    System.out.println("Surface area of rectangular prism: " + surface_area + " " + unit);
                } else if (solid.equals("sphere")) {
                    System.out.print("Enter radius: ");
                    double r = Double.parseDouble(scanner.nextLine());
                    double surface_area = 4 * Math.PI * Math.pow(r, 2);
                    System.out.println("Surface area of sphere: " + surface_area + " " + unit);
                } else {
                    System.out.println("Invalid solid, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Pythagorean Theorem -----------------
    public static void pythagorean_theorem() {
        System.out.println("Pythagorean Theorem");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two sides of a right triangle (a, b) separated by space (or 'exit' to quit): ");
            String sides = scanner.nextLine();
            if (sides.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = sides.split("\\s+");
                double a = Double.parseDouble(parts[0]);
                double b = Double.parseDouble(parts[1]);
                double c = Math.sqrt(a*a + b*b);
                System.out.println("Hypotenuse (c) = " + c);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // Helper method to test closeness (similar to numpy.isclose)
    public static boolean isClose(double a, double b) {
        return Math.abs(a - b) < 1e-9;
    }

    // ----------------- Similar Triangles -----------------
    public static void similar_triangles() {
        System.out.println("Similar Triangles");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter corresponding sides of two triangles (a1, a2, b1, b2, c1, c2) separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double a1 = Double.parseDouble(parts[0]);
                double a2 = Double.parseDouble(parts[1]);
                double b1 = Double.parseDouble(parts[2]);
                double b2 = Double.parseDouble(parts[3]);
                double c1 = Double.parseDouble(parts[4]);
                double c2 = Double.parseDouble(parts[5]);
                double ratio1 = a1 / a2;
                double ratio2 = b1 / b2;
                double ratio3 = c1 / c2;
                if (isClose(ratio1, ratio2) && isClose(ratio2, ratio3)) {
                    System.out.println("The triangles are similar.");
                } else {
                    System.out.println("The triangles are not similar.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Complementary Angles -----------------
    public static void complementary_angles() {
        System.out.println("Complementary Angles");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two angles A and B separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double A = Double.parseDouble(parts[0]);
                double B = Double.parseDouble(parts[1]);
                if (isClose(A + B, 90)) {
                    System.out.println("The angles are complementary.");
                } else {
                    System.out.println("The angles are not complementary.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Supplementary Angles -----------------
    public static void supplementary_angles() {
        System.out.println("Supplementary Angles");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two angles A and B separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double A = Double.parseDouble(parts[0]);
                double B = Double.parseDouble(parts[1]);
                if (isClose(A + B, 180)) {
                    System.out.println("The angles are supplementary.");
                } else {
                    System.out.println("The angles are not supplementary.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Distance Formula -----------------
    public static void distance_formula() {
        System.out.println("Distance Formula");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two points (x1, y1) and (x2, y2) separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
                System.out.println("Distance between points: " + distance);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Midpoint Formula -----------------
    public static void midpoint_formula() {
        System.out.println("Midpoint Formula");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two points (x1, y1) and (x2, y2) separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                double midX = (x1 + x2) / 2;
                double midY = (y1 + y2) / 2;
                System.out.println("Midpoint: (" + midX + ", " + midY + ")");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Slope Formula -----------------
    public static void slope_formula() {
        System.out.println("Slope Formula");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two points (x1, y1) and (x2, y2) separated by space (or 'exit' to quit): ");
            String input = scanner.nextLine();
            if (input.toLowerCase().equals("exit")) {
                break;
            }
            try {
                String[] parts = input.split("\\s+");
                double x1 = Double.parseDouble(parts[0]);
                double y1 = Double.parseDouble(parts[1]);
                double x2 = Double.parseDouble(parts[2]);
                double y2 = Double.parseDouble(parts[3]);
                double slope = (y2 - y1) / (x2 - x1);
                System.out.println("Slope: " + slope);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                            }
        }
    }

    // ----------------- Plot Equation -----------------
    public static void plot_equation(String eqStr, String variable, String title) {
        // Create a new JFrame to hold the plot
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        // Create PlotPanel with the equation string and variable name
        PlotPanel plotPanel = new PlotPanel(eqStr, variable);
        frame.add(plotPanel);
        frame.setVisible(true);
    }

    // Helper method to create linspace similar to numpy.linspace
    public static double[] linspace(double start, double end, int num) {
        double[] arr = new double[num];
        double step = (end - start) / (num - 1);
        for (int i = 0; i < num; i++) {
            arr[i] = start + step * i;
        }
        return arr;
    }

    // PlotPanel for plotting the equation
    public static class PlotPanel extends JPanel {
        private String eqStr;
        private String variable;
        private double[] xVals;
        private double[] yVals;
        private boolean computed = false;

        public PlotPanel(String eqStr, String variable) {
            this.eqStr = eqStr;
            this.variable = variable;
            this.xVals = linspace(-10, 10, 400);
            this.yVals = new double[xVals.length];
            computeYValues();
        }

        // Compute y values using JavaScript evaluation engine.
        private void computeYValues() {
            ScriptEngineManager mgr = new ScriptEngineManager();
            ScriptEngine engine = mgr.getEngineByName("JavaScript");
            // Replace math functions in the equation string
            String expression = eqStr;
            expression = expression.replace("sqrt", "Math.sqrt");
            expression = expression.replace("cos", "Math.cos");
            expression = expression.replace("sin", "Math.sin");
            expression = expression.replace("tan", "Math.tan");
            expression = expression.replace("log", "Math.log");
            expression = expression.replace("exp", "Math.exp");
            // Assume variable is a single letter, like x
            for (int i = 0; i < xVals.length; i++) {
                try {
                    // Set the variable in the engine context
                    engine.put(variable, xVals[i]);
                    Object result = engine.eval(expression);
                    yVals[i] = Double.parseDouble(result.toString());
                } catch (Exception e) {
                    yVals[i] = 0;
                }
            }
            computed = true;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!computed) return;
            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();

            // Draw axes
            g2.setColor(Color.BLACK);
            g2.drawLine(0, h/2, w, h/2);
            g2.drawLine(w/2, 0, w/2, h);

            // Find min and max y to scale the plot automatically
            double yMin = Double.POSITIVE_INFINITY, yMax = Double.NEGATIVE_INFINITY;
            for (double y : yVals) {
                if (y < yMin) yMin = y;
                if (y > yMax) yMax = y;
            }
            if (yMin == yMax) { yMin -= 1; yMax += 1; }
            // Plot the function
            g2.setColor(Color.BLUE);
            for (int i = 0; i < xVals.length - 1; i++) {
                int x1 = (int) (( (xVals[i] + 10) / 20 ) * w);
                int x2 = (int) (( (xVals[i+1] + 10) / 20 ) * w);
                int y1 = (int) ((1 - (yVals[i] - yMin) / (yMax - yMin)) * h);
                int y2 = (int) ((1 - (yVals[i+1] - yMin) / (yMax - yMin)) * h);
                g2.drawLine(x1, y1, x2, y2);
            }
        }
    }

    // ----------------- Algebra Solver -----------------
    public static void algebra_solver() {
        System.out.println("Algebra Solver");
        Scanner scanner = new Scanner(System.in);
        List<String[]> history = new ArrayList<>();
        // Create a symbolic evaluator using Matheclipse
        ExprEvaluator evaluator;
        try {
            evaluator = new ExprEvaluator(false, (short) 100);
        } catch (Exception e) {
            System.err.println("Error initializing Matheclipse: " + e.getMessage());
            e.printStackTrace();
            return;
        }


        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Solve an equation");
            System.out.println("2. Solve an inequality");
            System.out.println("3. Solve a system of equations");
            System.out.println("4. Factor a polynomial");
            System.out.println("5. Plot an equation");
            System.out.println("6. Simplify rational expressions");
            System.out.println("7. Calculate exponent and logarithm");
            System.out.println("8. Solve trigonometric equations");
            System.out.println("9. Calculate derivative");
            System.out.println("10. Calculate integral");
            System.out.println("11. Perform polynomial long division");
            System.out.println("12. Unit conversion");
            System.out.println("13. View history");
            System.out.println("14. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            try {
                if (choice.equals("1")) {
                    System.out.print("What variable do you want to solve for? (e.g., x, y): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter an equation (e.g., 2*x + 3*y = 7): ");
                    String equation = scanner.nextLine();
                    // Convert '=' to '==' for the solver
                    String eqForSolve = equation.replace("=", "==");
                    IExpr solutionExpr = evaluator.evaluate("Solve(" + eqForSolve + ", " + var + ")");
                    String solution = solutionExpr.toString().replace("->", "=");
                    history.add(new String[]{equation, solution.toString()});
                    System.out.println("Solution for " + var + ": " + solution);
                    // Note: Numeric substitution is not handled in this basic translation.
                } else if (choice.equals("2")) {
                    System.out.print("What variable do you want to solve for? (e.g., x, y): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter an inequality (e.g., x + 2 > 5): ");
                    String inequality = scanner.nextLine();
                    IExpr solution = evaluator.evaluate("Reduce(" + inequality + ", " + var + ")");
                    System.out.println("Solution for " + var + ": " + solution);
                } else if (choice.equals("3")) {
                    List<String> equations = new ArrayList<>();
                    while (true) {
                        System.out.print("Enter an equation (or type 'done' to finish): ");
                        String eq = scanner.nextLine();
                        if (eq.toLowerCase().equals("done")) {
                            break;
                        }
                        equations.add(eq.replace("=", "=="));
                    }
                    StringBuilder sysEqs = new StringBuilder("{");
                    for (int i = 0; i < equations.size(); i++) {
                        sysEqs.append(equations.get(i));
                        if (i < equations.size() - 1) sysEqs.append(", ");
                    }
                    sysEqs.append("}");
                    IExpr solution = evaluator.evaluate("Solve(" + sysEqs.toString() + ")");
                    System.out.println("Solution for the system of equations: " + solution);
                } else if (choice.equals("4")) {
                    System.out.print("Enter a polynomial (e.g., x^2 + 3*x + 2): ");
                    String polynomial = scanner.nextLine();
                    IExpr factored = evaluator.evaluate("Factor(" + polynomial + ")");
                    System.out.println("Factored form: " + factored);
                } else if (choice.equals("5")) {
                    System.out.print("Enter the variable for plotting (e.g., x): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter the equation to plot (e.g., x^2 - 4): ");
                    String eqToPlot = scanner.nextLine();
                    // Use our plot_equation function. Replace '^' with '**' is not needed here.
                    plot_equation(eqToPlot, var, "Plot of " + eqToPlot);
                } else if (choice.equals("6")) {
                    System.out.print("Enter a rational expression (e.g., (x^2 - 1)/(x - 1)): ");
                    String exprStr = scanner.nextLine();
                    IExpr simplified = evaluator.evaluate("Simplify(" + exprStr + ")");
                    System.out.println("Simplified form: " + simplified);
                } else if (choice.equals("7")) {
                    System.out.print("Enter the base (e.g., 2): ");
                    String base = scanner.nextLine();
                    System.out.print("Enter the exponent (e.g., 3): ");
                    String exponent = scanner.nextLine();
                    double result = Math.pow(Double.parseDouble(base), Double.parseDouble(exponent));
                    System.out.println(base + " raised to the power of " + exponent + " is: " + result);
                } else if (choice.equals("8")) {
                    System.out.print("What variable do you want to solve for? (e.g., x): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter a trigonometric equation (e.g., sin(x) = 0.5): ");
                    String trigEq = scanner.nextLine();
                    String eqForSolve = trigEq.replace("=", "==");
                    IExpr solution = evaluator.evaluate("Solve(" + eqForSolve + ", " + var + ")");
                    System.out.println("Solution for " + var + ": " + solution);
                } else if (choice.equals("9")) {
                    System.out.print("Enter the variable for differentiation (e.g., x): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter the function (e.g., x^2 + 3*x): ");
                    String func = scanner.nextLine();
                    IExpr derivative = evaluator.evaluate("D(" + func + ", " + var + ")");
                    System.out.println("The derivative of " + func + " with respect to " + var + " is: " + derivative);
                } else if (choice.equals("10")) {
                    System.out.print("Enter the variable for integration (e.g., x): ");
                    String var = scanner.nextLine().trim();
                    System.out.print("Enter the function (e.g., x^2 + 3*x): ");
                    String func = scanner.nextLine();
                    IExpr integral = evaluator.evaluate("Integrate(" + func + ", " + var + ")");
                    System.out.println("The indefinite integral of " + func + " with respect to " + var + " is: " + integral);
                } else if (choice.equals("11")) {
                    System.out.print("Enter the dividend polynomial (e.g., x^3 + 2*x^2 + 3): ");
                    String dividend = scanner.nextLine();
                    System.out.print("Enter the divisor polynomial (e.g., x + 1): ");
                    String divisor = scanner.nextLine();
                    IExpr quotient = evaluator.evaluate("PolynomialQuotient(" + dividend + ", " + divisor + ", x)");
                    IExpr remainder = evaluator.evaluate("PolynomialRemainder(" + dividend + ", " + divisor + ", x)");
                    System.out.println("Quotient: " + quotient + ", Remainder: " + remainder);
                } else if (choice.equals("12")) {
                    System.out.println("Unit Conversion Options: ");
                    System.out.println("1. Length (cm to m)");
                    System.out.println("2. Area (m² to km²)");
                    System.out.print("Select an option: ");
                    String opt = scanner.nextLine();
                    if (opt.equals("1")) {
                        System.out.print("Enter length in cm: ");
                        double cm = Double.parseDouble(scanner.nextLine());
                        double m = cm / 100;
                        System.out.println(cm + " cm is " + m + " m");
                    } else if (opt.equals("2")) {
                        System.out.print("Enter area in m²: ");
                        double m2 = Double.parseDouble(scanner.nextLine());
                        double km2 = m2 / 1_000_000;
                        System.out.println(m2 + " m² is " + km2 + " km²");
                    }
                } else if (choice.equals("13")) {
                    System.out.println("Calculation History:");
                    for (String[] entry : history) {
                        System.out.println(entry[0] + " = " + entry[1]);
                    }
                } else if (choice.equals("14")) {
                    System.out.println("Exiting the calculator.");
                    break;
                } else {
                    System.out.println("Invalid choice, please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    // ----------------- Main Menu -----------------
    public static void main_menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Math Application");
            System.out.println("1. Simple Math");
            System.out.println("2. Windowed Calculator");
            System.out.println("3. Area of Shapes");
            System.out.println("4. Perimeter and Circumference");
            System.out.println("5. Volume of Solids");
            System.out.println("6. Surface Area of Solids");
            System.out.println("7. Pythagorean Theorem");
            System.out.println("8. Similar Triangles");
            System.out.println("9. Complementary Angles");
            System.out.println("10. Supplementary Angles");
            System.out.println("11. Distance Formula");
            System.out.println("12. Midpoint Formula");
            System.out.println("13. Slope Formula");
            System.out.println("14. Algebra Solver");
            System.out.println("15. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                simplemath();
            } else if (choice.equals("2")) {
                windowed_calculator();
            } else if (choice.equals("3")) {
                area_of_shapes();
            } else if (choice.equals("4")) {
                perimeter_and_circumference();
            } else if (choice.equals("5")) {
                volume_of_solids();
            } else if (choice.equals("6")) {
                surface_area_of_solids();
            } else if (choice.equals("7")) {
                pythagorean_theorem();
            } else if (choice.equals("8")) {
                similar_triangles();
            } else if (choice.equals("9")) {
                complementary_angles();
            } else if (choice.equals("10")) {
                supplementary_angles();
            } else if (choice.equals("11")) {
                distance_formula();
            } else if (choice.equals("12")) {
                midpoint_formula();
            } else if (choice.equals("13")) {
                slope_formula();
            } else if (choice.equals("14")) {
                algebra_solver();
            } else if (choice.equals("15")) {
                System.out.println("Exiting the Math Application.");
                break;
            } else {
                System.out.println("Invalid choice, please try again.");
                            }
        }
    }

    // ----------------- Main Method -----------------
    public static void main(String[] args) {
        MathApplication instance = new MathApplication();
        main_menu();
    }
}
