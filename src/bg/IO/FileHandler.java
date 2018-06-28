package bg.IO;

import bg.Main;
import bg.Settings;
import bg.engine.api.Scenarios.NamedLayout;
import bg.engine.trainer.Bot;
import bg.engine.trainer.Trainer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

import static bg.Main.timedTasks;

public class FileHandler extends WindowAdapter {

    private final File folder = new File("/Sepuku-Bg");
    private final String path;
    private long oldTime = System.currentTimeMillis();

    public FileHandler () {

      if (!folder.exists()) {
        folder.mkdir();
      }
      path = folder.exists() ? "\\Sepuku-Bg\\" : "";
      loadFiles();
      timedTasks.addTimedTask(this::timedFileSaving);
    }

    private void saveScenarios () {

      try {

        ObjectOutput out =
          new ObjectOutputStream(
            new BufferedOutputStream(
              new FileOutputStream(
                path+"sepuku.sce"
              )
            )
          );

        out.writeObject(Main.scenarios.getScenarios());
        out.close();
      } catch (IOException e) {
        System.out.println("Error writing file ["+path+"]: "+e.getMessage());
      }
    }

    private void loadScenarios () {

      try {

        ObjectInput in =
          new ObjectInputStream(
            new BufferedInputStream(
              new FileInputStream(
                path+"sepuku.sce"
              )
            )
          );

        Main.scenarios.setScenarios((List<NamedLayout>)in.readObject());
        in.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      } catch (ClassNotFoundException e) {
        System.out.println("Class not found: "+e.getMessage());
      }
    }

    private void saveBots () {

      try {

        ObjectOutput out =
          new ObjectOutputStream(
            new BufferedOutputStream(
              new FileOutputStream(
                path+"sepuku.bot"
              )
            )
          );

        out.writeObject(Trainer.bots);
        out.close();
      } catch (IOException e) {
        System.out.println("Error writing file ["+path+"]: "+e.getMessage());
      }
    }

    private void loadBots () {

      try {

        ObjectInput in =
          new ObjectInputStream(
            new BufferedInputStream(
              new FileInputStream(
                path+"sepuku.bot"
              )
            )
          );

        Trainer.bots = (List<Bot>)in.readObject();
        in.close();
      } catch (IOException e) {
        System.out.println(e.getMessage());
      } catch (ClassNotFoundException e) {
        System.out.println("Class not found: "+e.getMessage());
      }
    }

    private void saveSettings() {

      try {

        ObjectOutput out =
          new ObjectOutputStream(
            new BufferedOutputStream(
              new FileOutputStream(
                path+"sepuku2.dat"
              )
            )
          );

        out.writeObject(Main.settings);
        out.close();
      } catch (IOException e) {
        System.out.println("Error writing file ["+path+"]: "+e.getMessage());
      }
    }

    private void loadSettings() {

      try {

        ObjectInput in =
          new ObjectInputStream(
            new BufferedInputStream(
              new FileInputStream(
                path+"sepuku2.dat"
              )
            )
          );

        Main.settings = (Settings) in.readObject();
        in.close();
      } catch (IOException e) {
        System.out.println("Error reading file: "+e.getMessage());
      } catch (ClassNotFoundException e) {
        System.out.println("Class not found: "+e.getMessage());
      }
    }

    private void timedFileSaving () {

      if ((System.currentTimeMillis() - oldTime) > 300000) {
        oldTime = System.currentTimeMillis();
        Main.files.saveFiles();
      }
    }

    private void loadFiles () {

      synchronized (this) {
        loadSettings();
        loadScenarios();
        loadBots();
      }
    }

    public void saveFiles () {

      synchronized (this) {
        saveSettings();
        saveScenarios();
        saveBots();
      }
    }

    @Override
    public void windowClosing (WindowEvent e) {

      saveFiles();
      System.exit(0);
    }

  }
