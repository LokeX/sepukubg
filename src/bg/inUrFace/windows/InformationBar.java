package bg.inUrFace.windows;

import bg.util.time.Timeable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static bg.Main.sepuku;
import static bg.Main.timedTasks;

public class InformationBar extends JLabel implements Timeable {

  public InformationBar() {

    setPreferredSize(new Dimension(900, 35));
    setOpaque(true);
    setBackground(new Color(20,20,20));
    setForeground(new Color(250, 173, 7));
    setBorder(new EmptyBorder(0,10,0,0));
    setFont(new Font("Arial", Font.BOLD, 18));
    timedTasks.addTimedTask(this);
  }

  private boolean gameInfoAvailable () {

    return
      sepuku.getGameInfoHTML() != null
      && !sepuku.getScenarios().isEditing();
  }
  
  private String getDataHTML () {
    
    return
      gameInfoAvailable()
      ? sepuku.getGameInfoHTML().getHTMLFormattedDataString()
      : sepuku.getScenarioInfoHTML().getHTMLFormattedDataString();
  }

  @Override
  public void timerUpdate() {

    setText(getDataHTML());
  }

}
