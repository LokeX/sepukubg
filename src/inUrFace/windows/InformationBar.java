package inUrFace.windows;

import util.time.Timeable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static sepuku.WinApp.sepukuPlay;
import static sepuku.WinApp.timedTasks;

public class InformationBar extends JLabel implements Timeable {

  public InformationBar() {

    setPreferredSize(new Dimension(900, 35));
    setOpaque(true);
    setBackground(new Color(20,20,20));
    setForeground(new Color(250, 173, 7));
    setBorder(new EmptyBorder(0,10,0,0));
    setFont(new Font("Arial", Font.BOLD, 14));
    timedTasks.addTimedTask(this);
  }

  private boolean gameInfoAvailable () {

    return
      sepukuPlay.getGameInfoHTML() != null
      && !sepukuPlay.scenarios().isEditing();
  }
  
  private String getDataHTML () {
    
    return
      gameInfoAvailable()
      ? sepukuPlay.getGameInfoHTML().getHTMLFormattedDataString()
      : sepukuPlay.getScenarioInfoHTML().getHTMLFormattedDataString();
  }

  @Override
  public void timerUpdate() {

    setText(getDataHTML());
  }

}