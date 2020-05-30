package bg.engine.match.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovePointLayouts extends MoveLayout {

  private List<MoveLayout> movePointLayouts;
  private final int[] originalMovePoints;
  private int position = 0;

  public MovePointLayouts (MoveLayout moveLayout) {

    super(moveLayout);
    setPoint(
      parentMoves
        .getParentMoveLayout()
        .getPoint()
    );
    originalMovePoints = movePoints.clone();
    Arrays.fill(movePoints, -1);
  }

  private void paintPosition () {

    if (hitPoints[position] >= 0) {
      if (position%2 == 0) {
        point[hitPoints[position]]--;
      } else {
        point[hitPoints[position]]++;
      }
    }
    if (position%2 == 0) {
      point[movePoints2[position]]--;
    } else {
      point[movePoints2[position]]++;
    }
  }

  private void generateListEntries() {

    movePoints[position] = originalMovePoints[position];
    paintPosition();
    movePointLayouts.add(new MoveLayout(this));
    if (position < movePoints.length-1) {
      if (originalMovePoints[++position] != -1) {
          generateListEntries();
      }
    }
  }

  public List<MoveLayout> getMoveLayoutsList() {

    if (movePointLayouts == null) {
      movePointLayouts = new ArrayList<>();
      generateListEntries();
    }
    return movePointLayouts;
  }

}