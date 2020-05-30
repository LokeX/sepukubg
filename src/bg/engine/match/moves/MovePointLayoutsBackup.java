package bg.engine.match.moves;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovePointLayoutsBackup extends MoveLayout {

  private List<MoveLayout> movePointLayouts;
  private int[] originalMovePoints;
  private int position;

  public MovePointLayoutsBackup(MoveLayout moveLayout) {

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

  private void generateListEntries() {

    movePoints[position] = originalMovePoints[position];
    paintPosition(position);
    movePointLayouts.add(new MoveLayout(this));
    if (position < movePoints.length-1) {
      System.out.println("MovePointLength = "+movePoints.length);
      System.out.println("Position = "+position);
      if (movePoints[++position] != -1) {
          generateListEntries();
      }
    }
  }

//  private List<MoveLayout> generateMoveLayouts (
//
//    List<MoveLayout> moveLayouts,
//    int[] originalMovePoints,
//    int position ) {
//
//    movePoints[position] =
//      originalMovePoints[position];
//    paintPosition(position);
//    moveLayouts.add(new MoveLayout(this));
//    if (position < movePoints.length-1) {
//      System.out.println("MovePointLength = "+movePoints.length);
//      System.out.println("Position = "+position);
//      if (originalMovePoints[position+1] != -1) {
//        new MovePointLayouts(this)
//          .generateMoveLayouts(
//            moveLayouts,
//            originalMovePoints,
//            position+1
//          );
//      }
//    }
//    return moveLayouts;
//  }

  private void resetMovePoints () {

    Arrays.fill(movePoints, -1);
  }

//  private MovePointLayouts firstMovePointLayouts() {
//
//    MovePointLayouts movePointLayouts =
//      new MovePointLayouts(new MoveLayout(this));
//
//    movePointLayouts.setPoint(
//      parentMoves
//        .getParentMoveLayout()
//        .getPoint()
//    );
//    movePointLayouts.resetMovePoints();
//    return movePointLayouts;
//  }

  private void generateList() {

    setPoint(
      parentMoves
        .getParentMoveLayout()
        .getPoint()
    );
    originalMovePoints = movePoints.clone();
    movePointLayouts = new ArrayList<>();
    Arrays.fill(movePoints, -1);
    generateListEntries();
  }

//  private List<MoveLayout> moveLayoutsList () {
//
//    MovePointLayouts firstMovePointLayouts = initLayouts();
//    List<MoveLayout> moveLayouts = new ArrayList<>();
//
//    moveLayouts.add(firstMovePointLayouts.parentMoves.getParentMoveLayout());
//    return
//      firstMovePointLayouts.
//      generateMoveLayouts(
//        moveLayouts,
//        movePoints,
//        0
//      );
//  }

//  private List<MoveLayout> moveLayoutsList () {
//
//    MovePointLayouts firstMovePointLayouts = initLayouts();
//    List<MoveLayout> moveLayouts = new ArrayList<>();
//
//    moveLayouts.add(firstMovePointLayouts.parentMoves.getParentMoveLayout());
//    return
//      firstMovePointLayouts.
//        generateMoveLayoutsList(
//          moveLayouts,
//          movePoints,
//          0
//        );
//  }

  private List<MoveLayout> parentMoveLayoutList () {

    return
      List.of(
        parentMoves
          .getParentMoveLayout()
      );
  }

  public List<MoveLayout> getMoveLayoutsList() {
    
    generateList();
    return movePointLayouts;
  }

}