package bg.engine.match.moves;

import java.util.ArrayList;
import java.util.List;

public class MovePointLayouts extends MoveLayout {

  private List<MoveLayout> movePointsLayouts;

  public MovePointLayouts (MoveLayout moveLayout) {

    super(moveLayout);
  }

  private void paintPosition (int position) {

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

  private List<MoveLayout> generateMoveLayouts (

    List<MoveLayout> moveLayouts,
    int[] originalMovePoints,
    int position ) {

    movePoints[position] =
      originalMovePoints[position];
    paintPosition(position);
    moveLayouts.add(this);
    if (position < movePoints.length-1) {
      if (movePoints2[position+1] != -1) {
        new MovePointLayouts(this)
          .generateMoveLayouts(
            moveLayouts,
            originalMovePoints,
            position+1
          );
      }
    }
    return moveLayouts;
  }

  private void resetMovePoints () {

    for (int a = 0; a < movePoints.length; a++) {
      movePoints[a] = -1;
    }
  }

  private MovePointLayouts clonedMovePointLayouts () {

    MovePointLayouts movePointLayouts =
      new MovePointLayouts(this);

    movePointLayouts.point =
      parentMoves
        .getParentMoveLayout()
        .point
        .clone();
    movePointLayouts.resetMovePoints();
    return movePointLayouts;
  }

  private List<MoveLayout> moveLayoutsList () {

    return
      clonedMovePointLayouts().
      generateMoveLayouts(
        new ArrayList<>(),
        movePoints,
        0
      );
  }

  private List<MoveLayout> parentMoveLayoutList () {

    return
      List.of(
        parentMoves
          .getParentMoveLayout()
      );
  }

  public List<MoveLayout> getMoveLayoutsList() {

    if (isIllegal()) {
      return parentMoveLayoutList();
    } else {
      if (movePointsLayouts == null) {
        movePointsLayouts = moveLayoutsList();
      }
      return movePointsLayouts;
    }
  }

}