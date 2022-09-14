package bg.inUrFace.windows;

import bg.inUrFace.canvas.Canvas;
import bg.inUrFace.menu.MenuBar;

import static bg.Main.*;
import static bg.util.ThreadUtil.threadSleep;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Window extends JFrame {


  static boolean windowReady = false;

  public Canvas canvas = new Canvas();
  public MenuBar menu = new MenuBar();
  public MatchPlay progressBar = new MatchPlay();
  public InformationBar informationBar = new InformationBar();

  public Window() {

    super("Sepuku Backgammon - pre-alpha: 0.33/2022.09.14");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
//    System.out.println(getClass().getResource("Icon/AppIcon.gif"));
    setIconImage(new ImageIcon(getClass().getResource("Icon/AppIcon.gif")).getImage());
//    setIconImage(new ImageIcon("/Icon/AppIcon.gif").getImage());
    canvas.getPaintJobs().bonusPainter.setComponent(canvas);
    setJMenuBar(menu);
    setLayout(new BorderLayout());
    add(canvas, BorderLayout.CENTER);
    add(informationBar, BorderLayout.SOUTH);
    addWindowStateListener(new WinMax());
    addWindowListener(files);
    addComponentListener(new WindowControl());
    pack();
    setWindowLocation();
    timedTasks.addTimedTask(this::repaint);
    if (settings.getWinMaximized()) {
      setExtendedState(MAXIMIZED_BOTH);
    }
    setVisible(true);
  }

  static public void runWindow () {

    Font font = new Font("Sansserif",Font.PLAIN,16);

    UIManager.put("Menu.font", font);
    UIManager.put("MenuItem.font", font);
    UIManager.put("RadioButtonMenuItem.font", font);

    SwingUtilities.invokeLater(() -> {
      win = new Window();
      windowReady = true;
    });
    while (!windowReady) {
      threadSleep(100);
    }
  }

  public class WinMax extends WindowAdapter {

    @Override
    public void windowStateChanged (WindowEvent e) {

      settings.setWinMaximized(
        e.getOldState() == JFrame.NORMAL &&
        e.getNewState() == JFrame.MAXIMIZED_BOTH
      );
    }

  }

  public class WindowControl extends ComponentAdapter {

    @Override
    public void componentResized (ComponentEvent e) {

      canvas.getDimensions().recalculateDimensions();
      if (mouse != null && mouse.scenarioEditor != null) {
        mouse.scenarioEditor.generateClickPoints();
      }
//      if (mouse != null && mouse.getMoveInputListener().startHumanMove() != null) {
//        mouse.getMoveInputListener().moveInput.calculatePoints();
//      }
      if (!settings.getWinMaximized()) {
        settings.setCanvasWidth(canvas.getWidth());
        settings.setCanvasHeight(canvas.getHeight());
      }
    }

    @Override
    public void componentMoved (ComponentEvent e) {

      try {

        Point p = getLocationOnScreen();

        settings.setFrameX((int) p.getX());
        settings.setFrameY((int) p.getY());
      } catch (IllegalComponentStateException icse) {
        System.out.println("JFrame: win.getLocation(), error: " + icse.getMessage());
        System.out.println("Severity: none");
      }
    }

  }

  private void setWindowLocation () {

    if (settings.getFrameX() > -1 && settings.getFrameY() > -1) {
      setLocation(settings.getFrameX(), settings.getFrameY());
    } else {
      setLocationRelativeTo(null);
    }
  }

  public Canvas getCanvas () {

    return canvas;
  }

//  public BonusPainter getBonusPainter () {
//
//    return text;
//  }

}
