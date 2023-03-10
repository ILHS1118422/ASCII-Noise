
// A 3d vector class for assisting perlin noise
import java.util.Random; // random

// add, dotProduct, getRel, randomize, randomizeUnit, 

public class Vector {
	double[] vals; // {x, y, z}
	Random random = new Random(); // For randomization

	// Initialization with value input
	public Vector(double[] values) {
		this.vals = values;
	}

	// Initialization without value input (overload)
	public Vector() {
		double[] values = { 0, 0, 0 };
		this.vals = values;
	}

	// Returns a clone of this vector
	public Vector clone() {
		Vector out = new Vector();
		for (int i = 0; i < 3; i++)
			out.vals[i] = this.vals[i];
		return out;
	}

	// Adds an input vector to this vector and stores the result in this vector
	public void add(Vector vec) {
		for (int i = 0; i < 3; i++)
			this.vals[i] += vec.vals[i];
	}

	// Dot product of this vector and an input vector
	public double dotProduct(Vector vec) {
		double sum = 0;
		for (int i = 0; i < 3; i++)
			sum += this.vals[i] * vec.vals[i];
		return sum;
	}

	// Gets relative pos of input (offset) vector to this (base) vector
	// (vec - this)
	public Vector getRel(Vector vec) {
		Vector outVec = new Vector();
		for (int i = 0; i < 3; i++)
			outVec.vals[i] = vec.vals[i] - this.vals[i];
		return outVec;
	}

	// Randomize self
	public void randomize() {
		for (int i = 0; i < 3; i++)
			this.vals[i] = 2 * this.random.nextDouble() - 1;
	}

	// Randomize to a unit vector
	// No limit to runtime but faster on average than non-probabilistic trig methods
	// monte carlo
	public void randomizeUnit() {
		// Keep randomizing until within a unit sphere
		// Equal distribution of solid angle
		randomize();
		while (absoluteSquare() > 1) {
			randomize();
		}
		// Project inside of sphere to surface - angle preserving
		normalize();
	}

	// Get absolute value of self, squared
	// Returns |self|^2
	// This is for optimization
	// Faster than absolute() and good for comparing dist with a constant
	// I have no idea if the compiler makes this obsolete so I'm leaving it
	public double absoluteSquare() {
		double dist = 0;
		for (int i = 0; i < 3; i++)
			dist += Math.pow(this.vals[i], 2);
		return dist;
	}

	// True absolute value
	// Separate for optimization
	public double absolute() {
		return Math.sqrt(absoluteSquare());
	}

	// Scale self by a factor
	public void scale(double factor) {
		for (int i = 0; i < 3; i++)
			this.vals[i] *= factor;
	}

	// Normalize self
	// Scale so that value = 1
	public void normalize() {
		double factor = 1 / absolute();
		scale(factor);
	}

	// Prints vector contents
	public void print() {
		System.out.print("[");
		for (int i = 0; i < 2; i++)
			System.out.print(this.vals[i] + ", ");
		System.out.print(this.vals[2] + "]");
	}

	// Prints vector contents with a new line
	// Just for convenience :)
	public void println() {
		print();
		System.out.println();
	}

	// Floor the vector's values
	public void floor() {
		for (int i = 0; i < 3; i++)
			this.vals[i] = Math.floor(this.vals[i]);
	}
}
