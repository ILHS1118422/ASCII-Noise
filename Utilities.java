class Utilities {
  // Clears Replit Console's screen, leaving cursor at top left
  // see https://stackoverflow.com/a/32295974, https://en.wikipedia.org/wiki/ANSI_escape_code
  public static void clearScreen() {
    // outputs ANSI escape codes for Home, clear screen
    System.out.print("\033[H\033[2J");
    // for good measure, also clear contents of "scrollback buffer" -- the text that has scrolled up off the screen
    System.out.print("\033[3J");
  }

  // Pauses execution for the given number of milliseconds
  // Thread.sleep() does the work; everything else is there
  // to play nice with whatever system we're running within
  public static void pause(int milliseconds)
  {
    // try/catch is cool and useful - we'll learn about it later
    try {
      Thread.sleep(milliseconds);
    }
    catch(InterruptedException e) {
      // one acceptable way to play nice, as a single-threaded app
      // see https://stackoverflow.com/a/22621931
      throw new RuntimeException("Unexpected interrupt", e);
    }
  }
}