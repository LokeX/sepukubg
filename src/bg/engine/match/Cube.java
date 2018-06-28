package bg.engine.match;

public class Cube {

  private int value = 2;
  private int owner = -1;
  private boolean cubeWasRejected = false;


  public Cube (Cube cube) {

    value = cube.getValue();
    owner = cube.getOwner();
  }

  public Cube () {

  }

  public void setCube (Cube cube) {

    value = cube.getValue();
    owner = cube.getOwner();
  }

  public int getValue () {

    return value;
  }

  public void setValue (int value) {

    this.value = value;
  }

  public void setOwner (int owner) {

    this.owner = owner;
  }

  public int getOwner () {

    return owner;
  }

  public boolean cubeWasRejected () {

    return cubeWasRejected;
  }

  public void setCubeWasRejected (boolean rejected) {

    cubeWasRejected = rejected;
  }

  public boolean playerCanOfferCube (int giver) {

    return (giver == owner || owner == -1);
  }

  public void playerDoubles(int playerID) {

    if (owner >= 0) {
      value *= 2;
      owner = owner == 1 ? 0 : 1;
    } else {
      owner = playerID;
    }
  }

}