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

    super("Sepuku Backgammon - Alpha-Ver.2022.09.25");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
    setIconImage(new ImageIcon(getClass().getResource("Icon/AppIcon.gif")).getImage());
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
    if (sepuku.getSettings().getWinMaximized()) {
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

      sepuku.getSettings().setWinMaximized(
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
      if (!sepuku.getSettings().getWinMaximized()) {
        sepuku.getSettings().setCanvasWidth(canvas.getWidth());
        sepuku.getSettings().setCanvasHeight(canvas.getHeight());
      }
    }

    @Override
    public void componentMoved (ComponentEvent e) {

      try {

        Point p = getLocationOnScreen();

        sepuku.getSettings().setFrameX((int) p.getX());
        sepuku.getSettings().setFrameY((int) p.getY());
      } catch (IllegalComponentStateException icse) {
        System.out.println("JFrame: win.getLocation(), error: " + icse.getMessage());
        System.out.println("Severity: none");
      }
    }

  }

  private void setWindowLocation () {

    if (sepuku.getSettings().getFrameX() > -1 && sepuku.getSettings().getFrameY() > -1) {
      setLocation(sepuku.getSettings().getFrameX(), sepuku.getSettings().getFrameY());
    } else {
      setLocationRelativeTo(null);
    }
  }

  public Canvas getCanvas () {

    return canvas;
  }

}
