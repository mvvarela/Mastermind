//Team Members: Rumi Mitra and Marco Varela

package mastermind
import scala.util.Random
import java.awt.{ Color, Graphics2D }


class Mastermind {
  
  val colors = List(Color.red, Color.green, Color.blue, Color.yellow, Color.magenta, Color.orange, Color.black, Color.white)
  /**
   * Creates random code pattern
   */
  def choosePattern(): List[Color] = {
    var pattern = List[Color]()
    for (i <- 1 to 4) pattern = pattern :+ colors(Random.nextInt(colors.length))
    pattern
  }
  /**
   * Returns a list of colors depending on user guess. Color.red for right color
   * guesses in right location, white for just right color guess but not location
   */
  def evaluateUserGuess(pattern: List[Color], code: List[Color]) : List[Color] = {
    var red = rightColorPlace(pattern, code)
    var white = rightColor(pattern, code)
    var evaluation = List[java.awt.Color]()
    for(n <- 0 to red - 1 if red > 0) {
      evaluation = evaluation :+ Color.red 
    }
    for(n <- 0 to white - red -1 if white != red) {
      evaluation = evaluation :+ Color.white
    }
    evaluation
  }
  /**
   * Returns how many bubles were of the right color and in the right place
   */
  def rightColorPlace(pattern: List[Color], code:List[Color]): Int = {
    var counter = 0
    for(n <- 0 to pattern.length-1) {
      if(pattern(n) == code(n)) counter += 1
    }
    counter
  }
  /**
   * Returns the number of bubbles that were of the right color
   */
  def rightColor(pattern: List[Color], code:List[Color]):Int = {
    var counter = 0
    //I added the "distinct" after pattern since we we would add one for each right color
    var numColorPattern = 0
    for(color <- pattern.distinct) {
      numColorPattern = pattern.count(_ == color)
      counter += math.min(code.count(_==color), numColorPattern)
    }
    counter
  }
  /**
   * Indicates whether the player has won
   */
  def playerWon(pattern:List[Color], code:List[Color]) : Boolean = rightColorPlace(pattern, code) == 4
}
