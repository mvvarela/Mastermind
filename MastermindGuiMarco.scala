//Team Members: Rumi Mitra and Marco Varela
//[GUI portion of Marco Varela]

package mastermind

import scala.swing._
import scala.swing.BorderPanel.Position._
import event._
import java.awt.{ Color, Graphics2D }
import scala.util.Random
/*
 * Check:
 * -AFTER RESTARTING BUTTONS SHOULD WORK AGAIN... BUT THEY DONT!!
 * -method to check if we can proceed
 * -create method to fill clues given the answers
 * -if All the colors are filled on the row, 
 * 		update the ones that are right
 *   	update round count  
 * -why can't I create a function to listen to stuff?
 * -if answer is discovered reveal code
 * -if answer isn't discovered reveal code
 * -why didn't my things go deaf
 * 
 * TA QUESTIONS
 * -Why won't my buttons go deaf
 */

object MastermindGuiMarco extends SimpleSwingApplication {
  //state of the game
  var answers = List[Answer]()
  var options = List[Option]()
  var clues = List[Clue]()
  var cluesGrids = List[GridPanel]()
  var bubbleRows = List[GridPanel]()
  var colorSelected = Color.gray
  var round = 0
  var roundAnswers = List[Component]()
  var roundClues = List[Component]()
  var rows = 10
  var cols = 4
  var logic = new Mastermind
  var codeColors = logic.choosePattern
  var codeBubbles = List[Code]()
  var defaultMessage ="<html>Enjoy the game! Please click the 'check' button to have " +
  		"answers evaluated. If you wish to to start again from scratch with a new code, press 'Restart Game'. </html>" 

  def top = new MainFrame { // top is a required method
    //update round

    title = "Mastermind"
    for (color <- codeColors) printColor(color)
    //declare the bubbles that will hold answers
    for (i <- 0 to rows - 1; j <- 0 to cols - 1) answers = answers :+ new Answer(Color.gray)
    //declare the bubbles that will show clues
    for (i <- 0 to rows - 1; j <- 0 to cols - 1) clues = clues :+ new Clue(Color.gray)
    //declare the bubbles for the code
    for (i <- 0 to cols - 1) codeBubbles = codeBubbles :+ new Code(Color.darkGray)
    //for (color <- codeColors) codeBubbles = codeBubbles :+ new Code(color)

    //create the grid that will hold the clues
    for (row <- 0 to rows - 1) {
      cluesGrids = cluesGrids :+ new GridPanel(cols / 2, cols / 2) {
        for (j <- 0 to 3) contents += clues(row * 4 + j)
        preferredSize = new Dimension(25, 60)
      }
    }
    //construct the rows with answers and clues
    for (row <- 0 to rows - 1) {
      bubbleRows = bubbleRows :+ new GridPanel(1, cols + 1) {
        for (j <- 0 to 3) contents += answers(row * 4 + j)
        contents += cluesGrids(row)
      }
    }
    //declare options (bubbles on bottom)
    options = options :+ new Option(Color.red)
    options = options :+ new Option(Color.green)
    options = options :+ new Option(Color.blue)
    options = options :+ new Option(Color.yellow)
    options = options :+ new Option(Color.magenta)
    options = options :+ new Option(Color.orange)
    options = options :+ new Option(Color.black)
    options = options :+ new Option(Color.white)

    background = Color.lightGray
    size = new Dimension(1000, 2200)

    val answersPanel = new GridPanel(rows, cols + 1) {
      for (answer <- answers) contents += answer
      preferredSize = new Dimension(250, 600)
    }
    val optionsPanel = new GridPanel(2, 6) {
      for (option <- options) contents += option
      preferredSize = new Dimension(250, 120)
    }
    val cluesPanel = new GridPanel(rows * 2, cols - 2) {
      for (clue <- clues) contents += clue
      preferredSize = new Dimension(25, 600)
    }

    val codePanel = new GridPanel(1, cols) {
      for (bubble <- codeBubbles) contents += bubble
      preferredSize = new Dimension(250, 60)
    }

    val answerCheckButton = new Button {
      text = "Check"
      foreground = Color.blue
      background = Color.red
      borderPainted = true
      enabled = true
      tooltip = "Click once you placed all your guesses"
      preferredSize = new Dimension(100, 100)
    }

    val restartButton = new Button {
      text = "Restart Game"
    }

    val message = new Label {
      text = defaultMessage
      font = new Font("Ariel", java.awt.Font.ITALIC, 12)
    }

    val buttonPanel = new GridPanel(3, 1) {
      preferredSize = new Dimension(100, 250)
      contents += answerCheckButton
      contents += restartButton
      contents += message
    }

    contents = new BorderPanel {
      minimumSize = new Dimension(100, 100)
      layout(answersPanel) = Center
      layout(optionsPanel) = South
      layout(cluesPanel) = East
      layout(buttonPanel) = West
      layout(codePanel) = North
    }

    listenTo(answerCheckButton)
    listenTo(restartButton)

    options foreach (option => listenTo(option.mouse.clicks))
    for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
      listenTo(answers(i).mouse.clicks)
    }

    repaint()

    ///////////////////////////////REACTIONS TO INTERACTIONS////////////////////////////////////////
    reactions += {
    //When player asks to check answer  
    case ButtonClicked(component) if component == answerCheckButton && round <10=>
        //println(s"ready to check: ${readyToCheck}")
        if (readyToCheck) {
          message.text = defaultMessage
          message.repaint()
          updateClues
          if (playerWon) {
            revealCode
            for (answer <- answers) deafTo(answer.mouse.clicks)
            message.text = "You won!!!"
            message.repaint()
          } else {
            for (answer <- answers) deafTo(answer.mouse.clicks)
            round += 1
            if (round != 10) {
              for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
                listenTo(answers(i).mouse.clicks)
              }
            } else {
              message.text = "YOU HAVE LOST"
              message.repaint()
            }
          }
        } else {
          message.text = "4 colors please."
          message.repaint()
        }
        //println(round)
        repaint()
     //when player asks to restart game 
     case ButtonClicked(component) if component == restartButton =>
        message.text = defaultMessage
        message.repaint()
        restart
        answers foreach (answer => deafTo(answer.mouse.clicks))
        for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
          listenTo(answers(i).mouse.clicks)
        }
      //When player clicks on color options to guess, or on the guess bubble
      case MouseClicked(component, _, _, _, _) =>
        //update color with option's color
        component match {
          case c: Option => {
            colorSelected = c.color
          }
          case c: Answer => {
            c.color = colorSelected
            c.repaint()
          }
          case _ => println("do nothing")
        }
    }
  }

  ////////////////////EXTRA METHODS///////////////////////////////////
  /**
   * Returns true if player revealed all 4 colors
   */
  def playerWon = logic.playerWon(currColors, codeColors)

  /**
   * Returns the row of answers of current round
   */
  def currAnswers: List[Answer] = {
    var list = List[Answer]()
    for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
      list = list :+ answers(i)
    }
    list
  }

  /**
   * Returns the 4 bubbles that hold the clues for the current round
   */
  def currClues: List[Clue] = {
    var list = List[Clue]()
    for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
      list = list :+ clues(i)
    }
    list
  }
  /**
   * Updates the colors of the 4 clues for the current round
   *
   */
  def updateClues = {
    var hintColors = logic.evaluateUserGuess(currColors, codeColors)
    //println("hint colors length " + hintColors.length)
    var clues = currClues
    //println("about to repaint clues")
    for (i <- 0 to hintColors.length - 1 if hintColors.length > 0) {
      clues(i).color = hintColors(i)
      clues(i).repaint()
    }
  }
  /**
   * Returns the colors of the answers for the current round
   */
  def currColors: List[Color] = currAnswers map (ans => ans.color)
  /**
   * Returns true if 4 guess have been made for the round
   */
  def readyToCheck: Boolean = {
    var check = true
    for (answer <- currAnswers if check) {
      if (answer.color == Color.gray) check = false
    }
    check
  }
  
  /**
   * Reveals the colors in the code
   */
  def revealCode {
    for (i <- 0 to codeBubbles.length - 1) {
      codeBubbles(i).color = codeColors(i)
      codeBubbles(i).repaint()
    }
  }
  
  /**
   * Prints out color in a more readable format
   */
  def printColor(c: Color) = {
    c match {
      case Color.red => println("red")
      case Color.green => println("green")
      case Color.blue => println("blue")
      case Color.yellow => println("yellow")
      case Color.magenta => println("magenta")
      case Color.orange => println("orange")
      case Color.black => println("black")
      case Color.white => println("white")
      case _ => println("color not found")
    }
  }
  
  /**
   * Resets initial values to start game from scratch
   */
  def restart = {
    codeColors = logic.choosePattern
    for (color <- codeColors) printColor(color)
    //declare the bubbles that will hold answers
    colorSelected = Color.gray
    round = 0
    for (answer <- answers) {
      answer.color = Color.gray
      answer.repaint()
    }
    //declare the bubbles that will show clues
    for (clue <- clues) {
      clue.color = Color.gray
      clue.repaint()
    }
    //declare the bubbles for the code
    //for (i <- 0 to cols -1) codeBubbles = codeBubbles :+ new Code(Color.darkGray)
    answers foreach (answer => deafTo(answer.mouse.clicks))
    for (i <- (rows * cols) - ((round + 1) * cols) to (rows * cols) - (round * cols + 1)) {
      listenTo(answers(i).mouse.clicks)
    }
    for (bubble <- codeBubbles) {
      bubble.color = Color.darkGray
      bubble.repaint()
    }
  }

}
