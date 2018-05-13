package bg.inUrFace.canvas;

import static bg.Main.win;
public class BoardDim {

  public BoardDim () {

  }

  public float factor = 1;
  public int boardOffsetX = 25;
  public int boardOffsetY = 50;
  public int boardWidth = 725;
  public int boardHeight = 460;
  public int frameWidth = 15;
  public int frameHeight = 15;
  public int chequerSize = 35;
  public int frameOffsetX = boardOffsetX + frameWidth;
  public int frameOffsetY = boardOffsetY + frameHeight;
  public int barWidth = 45;
  public int boardInnerWidth = boardWidth - (frameWidth * 2) - (chequerSize * 2) - barWidth;
  public int boardInnerHeight = boardHeight - (frameHeight * 2);
  public int bearOffHeight = (int) ((float) ((boardInnerHeight) / 2) * 0.77);
  public int bearOffWidth = chequerSize;
  public int topLeftBearOffOffsetX = frameOffsetX;
  public int topLeftBearOffOffsetY = frameOffsetY;
  public int bottomLeftBearOffOffsetX = frameOffsetX;
  public int bottomLeftBearOffOffsetY = boardOffsetY + boardHeight - frameHeight - bearOffHeight;
  public int leftPlayAreaOffsetX = topLeftBearOffOffsetX + chequerSize + frameWidth;
  public int leftPlayAreaOffsetY = frameOffsetY;
  public int leftPlayAreaWidth = (boardInnerWidth / 2) - frameWidth;
  public int leftPlayAreaHeight = boardInnerHeight;
  public int rightPlayAreaOffsetX = leftPlayAreaOffsetX + leftPlayAreaWidth + barWidth;
  public int rightPlayAreaOffsetY = frameOffsetY;
  public int rightPlayAreaWidth = (boardInnerWidth / 2) - frameWidth;
  public int rightPlayAreaHeight = boardInnerHeight;
  public int topRightBearOffOffsetX = rightPlayAreaOffsetX + rightPlayAreaWidth + frameWidth;
  public int topRightBearOffOffsetY = frameOffsetY;
  public int bottomRightBearOffOffsetX = topRightBearOffOffsetX;
  public int bottomRightBearOffOffsetY = boardOffsetY + boardHeight - frameHeight - bearOffHeight;
  public int chequerSpace = 10;
  public int chequerTotalSpace = chequerSpace + chequerSize;
  public int leftTrianglesOffsetX = leftPlayAreaOffsetX + (chequerSpace/2);
  public int rightTrianglesOffsetX = rightPlayAreaOffsetX + (chequerSpace/2);
  public int diePocketHeight = 65;
  public int diePocketOffsetY = frameOffsetY+(boardInnerHeight/2)-(diePocketHeight/2);

  private void factorDimensions () {

    float heightFactor = (float)win.canvas.getHeight()/(float)575;
    float widthFactor = (float)win.canvas.getWidth()/(float)775;

    factor = heightFactor < widthFactor ? heightFactor : widthFactor;
    boardOffsetX *= factor;
    boardOffsetY *= factor;
    boardWidth *= factor;
    boardHeight *= factor;
    frameWidth *= factor;
    frameHeight *= factor;
    chequerSize *= factor;
    frameOffsetX *= factor;
    frameOffsetY *= factor;
    barWidth *= factor;
    boardInnerWidth *= factor;
    boardInnerHeight *= factor;
    bearOffHeight *= factor;
    bearOffWidth *= factor;
    topLeftBearOffOffsetX *= factor;
    topLeftBearOffOffsetY *= factor;
    bottomLeftBearOffOffsetX *= factor;
    bottomLeftBearOffOffsetY *= factor;
    leftPlayAreaOffsetX *= factor;
    leftPlayAreaOffsetY *= factor;
    leftPlayAreaWidth *= factor;
    leftPlayAreaHeight *= factor;
    rightPlayAreaOffsetX *= factor;
    rightPlayAreaOffsetY *= factor;
    rightPlayAreaWidth *= factor;
    rightPlayAreaHeight *= factor;
    topRightBearOffOffsetX *= factor;
    topRightBearOffOffsetY *= factor;
    bottomRightBearOffOffsetX *= factor;
    bottomRightBearOffOffsetY *= factor;
    leftTrianglesOffsetX *= factor;
    rightTrianglesOffsetX *= factor;
    chequerSize *= factor == 1 ? factor : (factor/(factor-0.10));
    chequerTotalSpace *= factor;
    diePocketHeight *= factor;
    diePocketOffsetY *= factor;
  }

  public void recalculateDimensions () {

    factor = 1;
    boardOffsetX = 25;
    boardOffsetY = 50;
    boardWidth = 725;
    boardHeight = 460;
    frameWidth = 15;
    frameHeight = 15;
    chequerSize = 35;
    frameOffsetX = boardOffsetX + frameWidth;
    frameOffsetY = boardOffsetY + frameHeight;
    barWidth = 45;
    boardInnerWidth = boardWidth - (frameWidth * 2) - (chequerSize * 2) - barWidth;
    boardInnerHeight = boardHeight - (frameHeight * 2);
    bearOffHeight = (int) ((float) ((boardInnerHeight) / 2) * 0.77);
    bearOffWidth = chequerSize;
    topLeftBearOffOffsetX = frameOffsetX;
    topLeftBearOffOffsetY = frameOffsetY;
    bottomLeftBearOffOffsetX = frameOffsetX;
    bottomLeftBearOffOffsetY = boardOffsetY + boardHeight - frameHeight - bearOffHeight;
    leftPlayAreaOffsetX = topLeftBearOffOffsetX + chequerSize + frameWidth;
    leftPlayAreaOffsetY = frameOffsetY;
    leftPlayAreaWidth = (boardInnerWidth / 2) - frameWidth;
    leftPlayAreaHeight = boardInnerHeight;
    rightPlayAreaOffsetX = leftPlayAreaOffsetX + leftPlayAreaWidth + barWidth;
    rightPlayAreaOffsetY = frameOffsetY;
    rightPlayAreaWidth = (boardInnerWidth / 2) - frameWidth;
    rightPlayAreaHeight = boardInnerHeight;
    topRightBearOffOffsetX = rightPlayAreaOffsetX + rightPlayAreaWidth + frameWidth;
    topRightBearOffOffsetY = frameOffsetY;
    bottomRightBearOffOffsetX = topRightBearOffOffsetX;
    bottomRightBearOffOffsetY = boardOffsetY + boardHeight - frameHeight - bearOffHeight;
    chequerSpace = 10;
    chequerTotalSpace = chequerSpace + chequerSize;
    leftTrianglesOffsetX = leftPlayAreaOffsetX + (chequerSpace/2);
    rightTrianglesOffsetX = rightPlayAreaOffsetX + (chequerSpace/2);
    diePocketHeight = 65;
    diePocketOffsetY = frameOffsetY+(boardInnerHeight/2)-(diePocketHeight/2);
    factorDimensions();
  }

  public BoardDim getDimensions () {

    return this;
  }

}
