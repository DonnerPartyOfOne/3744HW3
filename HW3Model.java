package cs3744.hw3;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

/**
 * Homework 2 model class.
 * Stores a collection of circles.
 * 
 * @author Denis Gracanin
 * @version 1
 */
public class HW3Model {
	private ObservableList<HW2Circle> drawData = null; // An observable list of circle objects.
	private ChangeListener<Number> doubleListener = null; // A listener to the changes in the circle's <code>DoubleProperty</code> properties.
	private ChangeListener<Color> colorListener = null; // A listener to the changes in the circle's <code>ObjecProperty<Color></code> properties.

	/**
	 * Creates an instance of <code>HW2Model</code> class with no data.
	 */
	public HW3Model() { 
		drawData = FXCollections.observableArrayList();
		doubleListener = new ChangeListener<Number>() {

			/* (non-Javadoc)
			 * @see javafx.beans.value.ChangeListener#changed(javafx.beans.value.ObservableValue, java.lang.Object, java.lang.Object)
			 */
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) { touch(); }
		};
		colorListener = new ChangeListener<Color>() {

			@Override
			public void changed( ObservableValue<? extends Color> observable, Color oldValue, Color newValue) { touch(); }		
		};
	}
	
	/**
	 * Generates a list event.
	 */
	private void touch() {
		if (drawData.size() > 0) {
			HW2Circle o = drawData.get(0);
			drawData.remove(0);
			drawData.add(0, o);
		}
	}

	/**
	 * Gets the data property. 
	 * 
	 * @return The observable list of circles.
	 */
	public ObservableList<HW2Circle> drawDataProperty() { return drawData; }

	/**
	 * Selects a circle that contains a point (<code>null</code> if no circle).
	 * 
	 * @param x The point's x-coordinate.
	 * @param y The point's y-coordinate.
	 * @return The circle.
	 */
	public HW2Circle select(double x, double y) {
		HW2Circle selection = null;
		for (HW2Circle o : drawData) {
			if (o.contains(x, y)) {
				selection = o;
			}
		}
		return selection;
	}

	/**
	 * Sets the circle at the end of the list (the last circle drawn).
	 * 
	 * @param c The circle
	 */
	public void setTop(HW2Circle c) {
		if (c != null) {
			int i = drawData.lastIndexOf(c);
			if (i != -1 && i != (drawData.size() - 1)) {
				drawData.remove(i);
				add(c);
			}
		}
	}

	/**
	 * Adds a circle to the model.
	 * 
	 * @param s The string (a comma-separated list of six values: <code>x, y, radius, red, green, blue</code>).
	 */
	public void add(String s) { add(new HW2Circle(s.split("[ ]*,[ ]*"))); }

	/**
	 * Adds a circle to the model.
	 * 
	 * @param x The circle's x-coordinate.
	 * @param y The circle's y-coordinate.
	 * @return The circle.
	 */
	public HW2Circle add(double x, double y) {
		HW2Circle c = new HW2Circle(x, y);
		add(c);
		return c;
	}
	
	/**
	 * Adds a circle to the model.
	 * 
	 * @param c The circle
	 */
	public void add(HW2Circle c) {
		c.centerXProperty().addListener(doubleListener);
		c.centerYProperty().addListener(doubleListener);
		c.radiusProperty().addListener(doubleListener);
		c.colorProperty().addListener(colorListener);
		drawData.add(c);
	}
	
	/**
	 * Removes a circle from the model.
	 * 
	 * @param c The circle.
	 */
	public void remove(HW2Circle c) {
		c.centerXProperty().removeListener(doubleListener);
		c.centerYProperty().removeListener(doubleListener);
		c.radiusProperty().removeListener(doubleListener);
		c.colorProperty().removeListener(colorListener);
		drawData.remove(c);
	}
	
	/**
	 * Clears the model (erases all data).
	 */
	public void clear() { drawData.clear(); }

	/**
	 * Homework 2 circle class.
	 * Stores circle data.
	 * 
	 * @author Denis Gracanin
	 * @version 1
	 */
	public class HW2Circle {
		private DoubleProperty centerX = null;
		private DoubleProperty centerY = null;
		private DoubleProperty radius = null;
		private ObjectProperty<Color> color = null;
		/**
		 * Minimum circle's radius value.
		 */
		public static final double RADIUS_MIN = 10;
		/**
		 * Maximum circle's radius value.
		 */
		public static final double RADIUS_MAX = 100;
		/**
		 * Default circle's radius value.
		 */
		public static final double RADIUS_DEFAULT = 50;
		/**
		 * Minimum circle's center x-coordinate value.
		 */
		public static final double X_MIN = 0;
		/**
		 * Maximum circle's center x-coordinate value.
		 */
		public static final double X_MAX = 1000;
		/**
		 * Default circle's center x-coordinate value.
		 */
		public static final double X_DEFAULT = 0;
		/**
		 * Minimum circle's center y-coordinate value.
		 */
		public static final double Y_MIN = 0;
		/**
		 * Maximum circle's center y-coordinate value.
		 */
		public static final double Y_MAX = 1000;
		/**
		 * Default circle's center y-coordinate value.
		 */
		public static final double Y_DEFAULT = 0;

		/**
		 * Creates an instance of <code>HW2Circle</code> class with the default values:
		 * <ul>
		 * <li>The center's x-coordinate: 0.</li>
		 * <li>The center's y-coordinate: 0.</li>
		 * <li>The radius: 50.</li>
		 * <li>The color: black.</li>
		 * </ul>
		 */
		public HW2Circle() { this(X_DEFAULT, Y_DEFAULT); }

		/**
		 * Creates an instance of <code>HW2Circle</code> class with the default values:
		 * <ul>
		 * <li>The radius: 50.</li>
		 * <li>The color: black.</li>
		 * </ul>
		 * 
		 * @param x The center's x-coordinate.
		 * @param y The center's y-coordinate.
		 */
		public HW2Circle(double x, double y) { this(x, y, RADIUS_DEFAULT, Color.BLACK); }

		/**
		 * Creates an instance of <code>HW2Circle</code> class.
		 * 
		 * @param t The string (a comma-separated list of six numeric values: <code>x, y, radius, red, green, blue</code>).
		 * @throws NumberFormatException One (or more) values is not numeric.
		 * @throws ArrayIndexOutOfBoundsException Less than six values in the string.
		 */
		public HW2Circle(String[] t) throws NumberFormatException, ArrayIndexOutOfBoundsException { this(Double.parseDouble(t[0]), Double.parseDouble(t[1]), Double.parseDouble(t[2]), new Color(Double.parseDouble(t[3]), Double.parseDouble(t[4]), Double.parseDouble(t[5]), 1)); }

		/**
		 * Creates an instance of <code>HW2Circle</code> class.
		 * 
		 * @param c The circle.
		 */
		public HW2Circle(HW2Circle c) { this(c.getCenterX(), c.getCenterY(), c.getRadius(), c.getColor()); }

		/**
		 * Creates an instance of <code>HW2Circle</code> class.
		 * 
		 * @param x The center's x-coordinate.
		 * @param y The center's y-coordinate.
		 * @param r The radius.
		 * @param c The color.
		 */
		public HW2Circle(double x, double y, double r, Color c) {
			centerX = new SimpleDoubleProperty(this, "CenterX");
			setCenterX(x);
			centerY = new SimpleDoubleProperty(this, "CenterY");
			setCenterY(y);
			radius = new SimpleDoubleProperty(this, "radius");
			setRadius(r);
			color = new SimpleObjectProperty<Color>(this, "color");
			setColor(c);
		}

		/**
		 * Gets the center's x-coordinate property.
		 * 
		 * @return The x-coordinate property.
		 */
		public DoubleProperty centerXProperty() { 
			return centerX; 	
		}

		/**
		 * Gets the center's x-coordinate.
		 * 
		 * @return The x-coordinate.
		 */
		public double getCenterX() { return centerX.get(); 	}

		/**
		 * Sets the center's x-coordinate.
		 * 
		 * @param x The x-coordinate.
		 */
		public void setCenterX(double x) { centerX.set(clamp(x, X_MIN, X_MAX)); }

		/**
		 * Gets the center's y-coordinate property.
		 * 
		 * @return The y-coordinate property.
		 */
		public DoubleProperty centerYProperty() { return centerY; }

		/**
		 * Gets the center's y-coordinate.
		 * 
		 * @return The y-coordinate.
		 */
		public double getCenterY() { return centerY.get(); }

		/**
		 * Sets the center's y-coordinate.
		 * 
		 * @param y The y-coordinate.
		 */
		public void setCenterY(double y) { centerY.set(clamp(y, Y_MIN, Y_MAX)); }

		/**
		 * Gets the radius property.
		 * 
		 * @return The radius property.
		 */
		public DoubleProperty radiusProperty() { return radius; }

		/**
		 * Gets the cirlce's radius.
		 * 
		 * @return The circle.
		 */
		public double getRadius() { return radius.get(); }

		/**
		 * Sets the circle's radius.
		 * 
		 * @param r The radius.
		 */
		public void setRadius(double r) { radius.set(clamp(r, RADIUS_MIN, RADIUS_MAX)); }

		/**
		 * Gets the color property.
		 * 
		 * @return The color property.
		 */
		public ObjectProperty<Color> colorProperty() { return color; }

		/**
		 * Gets the circle's color.
		 * 
		 * @return The color.
		 */
		public Color getColor() { return color.get(); }

		/**
		 * Sets the circle's color.
		 * 
		 * @param c The color.
		 */
		public void setColor(Color c) { color.set(c); }

		/**
		 * Checks if the circle contains a point.
		 * 
		 * @param x The x-coordinate of the point.
		 * @param y The y-coordinate of the point.
		 * @return <code>true</code> if the point is contained, <code>false</code> otherwise.
		 */
		public boolean contains(double x, double y) { return (Math.pow(x - centerX.get(), 2) + Math.pow(y - centerY.get(), 2)) <= Math.pow(radius.get(), 2); }

		/**
		 * Clamps the value between <code>min</code> and <code>max</code>.
		 * 
		 * @param v The value.
		 * @param min The minimum value.
		 * @param max The maximum value.
		 * @return The clamped value.
		 */
		private double clamp(double v, double min, double max) { return (v < min ? min : (v > max ? max : v)); }

	}

}