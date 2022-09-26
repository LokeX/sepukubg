package bg.engine.core.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovePointLayouts extends MoveLayout {

  private List<MoveLayout> movePointLayouts;
  private final int[] originalMovePoints;
  private int position;

  public MovePointLayouts (MoveLayout moveLayout) {

    super(moveLayout);
    originalMovePoints = movePoints.clone();
    Arrays.fill(movePoints, -1);
  }

  private void paintPosition () {

    if (hitPoints[position] >= 0) {
      if (position%2 == 1) {
        point[hitPoints[position-1]]--;
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

    if (originalMovePoints[position] != -1) {
      movePoints[position] = originalMovePoints[position];
      paintPosition();
      movePointLayouts.add(new MoveLayout(this));
      if (++position < movePoints.length) {
        generateListEntries();
      }
    }
  }

  public List<MoveLayout> getMoveLayoutsList() {

    if (movePointLayouts == null) {
      movePointLayouts = new ArrayList<>();
//      movePointLayouts.add(new MoveLayout(parentMoves.getParentMoveLayout()));
      setPoint(parentMoves.getParentMoveLayout().getPoint());
//      movePointLayouts.add(parentMoves.getParentMoveLayout());
      generateListEntries();
    }
    return movePointLayouts;
  }

}