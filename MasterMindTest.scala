package mastermind

import org.scalatest.FunSuite
import scala.util.Random
import java.awt.{ Color, Graphics2D }

class MasterMindTest extends FunSuite {
  var master = new Mastermind
  
  test("evaluate user guess"){
    var pattern = List(Color.red, Color.blue, Color.yellow, Color.white)
    var code = List(Color.red, Color.blue, Color.yellow, Color.white)
    var expected = List(Color.red,Color.red,Color.red,Color.red)
    var answer = master.evaluateUserGuess(pattern, code)
    assert(answer == expected)
    
    var code2 = List(Color.red, Color.blue, Color.yellow, Color.black)
    var answer2 = master.evaluateUserGuess(pattern, code2)
    var expected2 = List(Color.red,Color.red,Color.red)
    assert(answer2 == expected2)
    
    var pattern2 = List(Color.blue, Color.yellow, Color.white, Color.red)
    var expected3 = List(Color.white,Color.white,Color.white,Color.white)
    var answer3 = master.evaluateUserGuess(pattern2, code)
    assert(answer3 == expected3)
    
    println("last test")
    code = List(Color.red, Color.red, Color.red, Color.green)
    pattern = List(Color.red, Color.red, Color.orange, Color.black)
    expected = List(Color.red, Color.red)
    answer = master.evaluateUserGuess(pattern,code)
    for (a <- answer) println(a)
    assert(answer == expected)
    
  }
  
  test("right color place"){
    var pattern = List(Color.red, Color.blue, Color.yellow, Color.white)
    var code = List(Color.red, Color.blue, Color.yellow, Color.white)
    var guessed = master.rightColorPlace(pattern, code)
    assert(guessed == 4)
  }
  
  test("rightColor"){
    var pattern = List(Color.red, Color.blue, Color.yellow, Color.white)
    var code = List(Color.blue, Color.yellow, Color.white, Color.red)
    var guessed = master.rightColor(pattern,code)
    assert(guessed == 4)
    
    var pattern2 = List(Color.red,Color.red,Color.red,Color.red)
    var guessed2 = master.rightColor(pattern2,code)
    assert(guessed2 == 1)
    
    var pattern3 = List(Color.orange,Color.orange,Color.orange,Color.black)
    var code2 = List(Color.black, Color.red, Color.yellow, Color.magenta)
    var guessed3 = master.rightColor(pattern3,code2)
    assert(guessed3 == 1)
    
    code = List(Color.red, Color.red, Color.red, Color.green)
    pattern = List(Color.red, Color.red, Color.orange, Color.black)
    guessed = master.rightColor(pattern,code)
    assert(guessed == 2)
    
  }
  
  test("playerWon"){
    var pattern = List(Color.red, Color.blue, Color.yellow, Color.white)
    var code = List(Color.red, Color.blue, Color.yellow, Color.white)
    assert(master.playerWon(pattern, code))
  }

}
