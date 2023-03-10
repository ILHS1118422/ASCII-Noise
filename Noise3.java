public class Noise3 {
  public Vector[][][] vectors;
  public Vector dimensions;

  // Initialize with custom dimensions
  public Noise3(Vector dims) {
    // Set dimensions
    dimensions = dims.clone();
    // Initialize vector array dimensions
    Vector[][][] vecs = new Vector[(int) dims.vals[0]][(int) dims.vals[1]][(int) dims.vals[2]];
    vectors = vecs;
    // Initialize individual vectors (everything starts at 0)
    for (int x = 0; x < dimensions.vals[0]; x++) {
      for (int y = 0; y < dimensions.vals[1]; y++) {
        for (int z = 0; z < dimensions.vals[2]; z++) {
          vectors[x][y][z] = new Vector();
        }
      }
    }
  }

  // Randomizes the internal vectors
  public void randomize() {
    // i wonder if there's a way to get rid of redundant recursive similar for loops
    // without using something crazy like lambdas
    for (int x = 0; x < dimensions.vals[0]; x++) {
      for (int y = 0; y < dimensions.vals[1]; y++) {
        for (int z = 0; z < dimensions.vals[2]; z++) {
          vectors[x][y][z].randomizeUnit();
        }
      }
    }
  }

  // Uses 5th order smoothstep to blend between 2 values at a phase
  public static double smoothStep(double first, double second, double phase) {
    double func = 6 * Math.pow(phase, 5) - 15 * Math.pow(phase, 4) + 10 * Math.pow(phase, 3);
    return first + func * (second - first);
  }

  // Calculates dot product between single internal vector and relative input
  // point vector
  // Vector corner is a position in space that has a vector at its position - it
  // is not the vector itself
  private double getDotSingle(Vector point, Vector corner) {
    // Get vector to multiply by
    Vector base = this.vectors[(int) corner.vals[0]][(int) corner.vals[1]][(int) corner.vals[2]];
    // Point relative to corner
    Vector offset = corner.getRel(point);
    double output = base.dotProduct(offset);
    return output;
  }

  // Calculates value at a point
  public double getPoint(Vector point) {
    Vector base = point.clone(); // Floored point
    base.floor();
    // Eliminate out of bounds error
    for (int i = 0; i < 3; i ++) {
      if (base.vals[i] >= dimensions.vals[i]) {
        base.vals[i] = dimensions.vals[i] - 1;
      }
    }

    // Eliminate redundancy by creating redundancy.
    Vector offset = new Vector();
    Vector phase = base.getRel(point);
    double[] xBases = { 0, 0 }; // For interpolation

    for (int xOff = 0; xOff <= 1; xOff++) {
      double[] yBases = { 0, 0 }; // For interpolation

      for (int yOff = 0; yOff <= 1; yOff++) {
        double[] zBases = { 0, 0 }; // For interpolation

        for (int zOff = 0; zOff <= 1; zOff++) {
          offset.vals[0] = xOff; // yes i know this is redundant
          offset.vals[1] = yOff; // but the alternative is replacing all '*off's
          offset.vals[2] = zOff; // with a list, and that would look so ugly
          offset.add(base);

          // Use vector to lookup other vector in a 3d array
          Vector gradientVec = new Vector();
          double[] values = vectors[(int) offset.vals[0]][(int) offset.vals[1]][(int) offset.vals[2]].vals;
          gradientVec.vals = values;
          // Store the two values to an array
          zBases[zOff] = getDotSingle(point, offset);
        }
        // Interpolate between the two z corners at the z value
        yBases[yOff] = smoothStep(zBases[0], zBases[1], phase.vals[2]);
      }
      // Interpolate between the two y points at the y value
      xBases[xOff] = smoothStep(yBases[0], yBases[1], phase.vals[1]);
    }
    // Interpolate between the two x points at the x value
    return smoothStep(xBases[0], xBases[1], phase.vals[0]);
    // hold up is this code unreadable (oh no)
    // my whitespace looks goofy
    // TODO: I have not even a clue if this actually works
  }
}