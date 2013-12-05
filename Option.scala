package mastermind

import scala.swing.Panel
import java.awt.{ Graphics2D, Color }

class Option(c: Color) extends Panel {
  // Start by erasing this Canvas
  var color = c
  override def paintComponent(g: Graphics2D) {
    g.clearRect(0, 0, size.width, size.height)
    // Draw background here
    g.setColor(color)
    g.fillOval(0, 0, 50, 50)
    repaint()
  }

}
