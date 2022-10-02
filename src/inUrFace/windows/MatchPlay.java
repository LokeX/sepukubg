package inUrFace.windows;

import sepuku.WinApp;
import engine.core.trainer.Trainer;
import util.NumberUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import static sepuku.WinApp.timedTasks;
import static sepuku.WinApp.trainer;
import static sepuku.WinApp.win;
import static util.Dialogs.confirmed;

public class MatchPlay extends JFrame {

  private JTextPane initialReport = new JTextPane();
  private JProgressBar progressBar = new JProgressBar();
  private JButton button = new JButton("Stop playing");
  private DecimalFormat df = NumberUtil.getDottedDecimalFormat();
  private int endValue;

  public MatchPlay() {

    setIconImage(new ImageIcon(getClass().getResource("Icon/AppIcon.gif")).getImage());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setupInitialReport();
    setupProgressBar();
    setupButton();
    addWindowListener(windowEvent());
    pack();
    timedTasks.addTimedTask(this::updateProgress);
    setVisible(false);
  }

  private void setupInitialReport () {

    initialReport.setEnabled(false);
    initialReport.setFont(new Font("Ariel", Font.BOLD, 12));
    initialReport.setMargin(new Insets(5,10,5,10));
    initialReport.setDisabledTextColor(Color.DARK_GRAY);
    initialReport.setPreferredSize(new Dimension(400,75));
    add(initialReport, BorderLayout.NORTH);
  }

  private void setupProgressBar () {

    progressBar.setPreferredSize(new Dimension(400, 50));
    progressBar.setStringPainted(true);
    progressBar.setBorderPainted(true);
    add(progressBar, BorderLayout.CENTER);
  }

  private void setupButton () {

    button.setPreferredSize(new Dimension(250,30));
    button.setFont(new Font("Ariel", Font.BOLD, 14));
    button.setFocusPainted(false);
    button.setForeground(Color.WHITE);
    button.setBackground(new Color(170,36,36));
    button.setBorderPainted(true);
    button.addActionListener(
      (ActionEvent e) -> onUserEvent()
    );
    add(button, BorderLayout.SOUTH);
  }

  public void startProgressMonitor(int min, int max) {

    endValue = max;
    progressBar.setMinimum(min);
    progressBar.setMaximum(max);
    setInitialReportText();
    setLocationRelativeTo(win);
    setVisible(true);
  }

  private void setInitialReportText() {

    initialReport.setText(
      Trainer.bots.get(Trainer.whiteBot).name()+" vs "+
      Trainer.bots.get(Trainer.blackBot).name()+"\n"+
      WinApp.trainer.getInitialReport()
    );
  }

  private WindowAdapter windowEvent () {

    return new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {

        onUserEvent();
      }
    };
  }

  private boolean killConfirmed () {

    return confirmed("Really stop playing?", win);
  }

  private void onUserEvent() {

    setVisible(false);
    if (killConfirmed()) {
      Trainer.killRun = true;
      endProgressMonitor();
    } else {
      setVisible(true);
    }
  }

  public void updateProgress () {

    if (this.isVisible()) {
      setProgressBarString();
      progressBar.setValue(Trainer.getNrOfMatchesPlayed());
      if (Trainer.getNrOfMatchesPlayed() == endValue || Trainer.killRun) {
        endProgressMonitor();
      }
    }
  }

  private void endProgressMonitor () {

    setVisible(false);
    TextDisplay.displayReport(
      "EngineApi play statistics: ",
      trainer.getFinalReport()
    );
  }

  private void setProgressBarString () {

    String timeRemaining = trainer.getTimeRemaining();

    progressBar.setString(
      "Playing: "+
      df.format(Trainer.getNrOfMatchesPlayed())+"/"+df.format(endValue)+
      " matches - ETA: "+(timeRemaining.length() < 15 ? timeRemaining : "")
    );
  }

}