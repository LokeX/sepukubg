package bg.IO;

import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

  final static String[] EFFECT_FILES = {
    "wuerfelbecher",
    "Blop-Mark_DiAngelo",
  };

  static private boolean playFX = true;

  AudioInputStream audioIn;
  Clip[] soundEffect = new Clip[EFFECT_FILES.length];

  public Sound () {

    for (int a = 0; a < soundEffect.length; a++) {
      try {
        audioIn = AudioSystem.getAudioInputStream(getClass().getResource("Sounds/" + EFFECT_FILES[a] + ".wav"));
        soundEffect[a] = AudioSystem.getClip();
        soundEffect[a].open(audioIn);

        FloatControl gainControl = (FloatControl) soundEffect[a].getControl(FloatControl.Type.MASTER_GAIN);

        gainControl.setValue(-15.0f);
      } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ue) {
        System.out.println(ue.getMessage());
      }
    }
  }

  public void playSoundEffect (String effectName) {

    if (playFX) {
      for (int a = 0; a < soundEffect.length; a++) {
        if (EFFECT_FILES[a].equals(effectName)) {
          soundEffect[a].stop();
          soundEffect[a].setFramePosition(0);
          soundEffect[a].start();
        }
      }
    }
  }

}
