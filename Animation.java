// 1118422
// Produces an infinite animation by displaying in ascii a 2d slice through a 3d block of perlin noise
// See https://en.wikipedia.org/wiki/Perlin_noise
// Total of 316 lines of code & comments

import java.util.Random; // random

public class Animation {
  // Text gradient
  public static final String GRADIENT = " `.:-^;~!?7=CT254AD$Q#0N@";
  // Main method
  public static void main(String[] args) {
    Vector dimensions = new Vector();
    // Configure Perlin noise scale and length of animation
    double[] dimVals = { 3, 3, 100 };
    // Add the extra ending grid nodes
    for (int i = 0; i < 3; i ++) 
      dimVals[i] ++;
    // this is stupid, why cant i initialize an array inline
    dimensions.vals = dimVals; 
    // Initialize noise object and run animation
    Noise3 noise = new Noise3(dimensions);
    noise.randomize();
    printAnimation(noise, 0.1, 5, 50, 25, 0.2, 1.6);
  }

  // Chooses a random character from a list
  public static char randChar(String characters) {
    int index = (int)(Math.random() * (double)characters.length());
    return characters.charAt(index);
  }

  // Converts a value into a character with similar brightness
	// scalar: Desired brightness value
	// dithering: Scale of random input, breaking up banding
	// random: Random Object for dithering
  public static char asciify(double scalar, double dithering, Random random) {
    // Add randomization for a smoother output
    scalar += random.nextDouble() * dithering;
    // Clamp input (0.9999 so that floor still works)
    if (scalar < -0.9999)
      scalar = -0.9999;
    else if (scalar > 0.9999)
      scalar = 0.9999;
    // Convert from -1 to 1 to 0 to 25, and apply a visual correction function
    scalar = scalar / 2 + 0.5;
    scalar = Math.pow(scalar, 1.75);
    int index = (int) (scalar * GRADIENT.length());
    // Find character
    return GRADIENT.charAt(index);
  }

  // Prints one frame
  // See printAnimation for detailed input explanations
	// random: Random Object for dithering
  public static void printFrame(Noise3 noise, double pos, int width, int height, double dithering, double contrast, Random random) {
    // Find increment
    double xIncrement = (noise.dimensions.vals[0] - 1) / width;
    double yIncrement = (noise.dimensions.vals[1] - 1) / height;
    // Buffer frame to print all at once
    String outputText = "";
    // Print the text
    for (double y = 0; y < noise.dimensions.vals[0] - 1; y += yIncrement) {
      for (double x = 0; x < noise.dimensions.vals[1] - 1; x += xIncrement) {
        double[] vals = { x, y, pos };
        Vector point = new Vector(vals);
        char nextChar = asciify(noise.getPoint(point) * contrast, dithering, random);
        outputText = outputText.concat(Character.toString(nextChar));
      }
      outputText = outputText.concat("\n");
    }
    // Print everything at once
    // Uses home only without clear to eliminate any flickering
    // System.out.print("\033[H");
    Utilities.clearScreen();
    System.out.print(outputText);
  }
  // Prints the entire animation
  // Width and height are measured in characters
  // noise: A Noise3 object to pull a frame from
  // inc: interval between each frame, lower is faster
  // pos: The z position within noise to pull the frame from
  // width: Approximate number of characters wide to output
  // height: Approximate number of characters tall to output
  // dithering: Scale of random input, breaking up banding
  // contrast: Makes lower values even lower and higher values
  //           even higher. May cause clamping artifacts.
  public static void printAnimation(Noise3 noise, double inc, int pauseTime, int width, int height, double dithering, double contrast) {
    Utilities.clearScreen();
    // Initialize a Random object
    Random random = new Random();
    for (double pos = 0;
         pos < noise.dimensions.vals[2] - 1;
         pos += inc) {
      // Print frame
      printFrame(noise, pos, width, height, dithering, contrast, random);
      // Wait
      Utilities.pause(pauseTime);
    }
    System.out.println("\n...Done!");
  }
}