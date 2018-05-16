package bg.api;

import bg.Main;
import bg.engine.*;
import bg.engine.moves.Layout;
import bg.inUrFace.canvas.move.MoveOutput;
import bg.inUrFace.canvas.scenario.ScenarioOutput;
import bg.inUrFace.mouse.MoveInput;
import bg.inUrFace.windows.TextDisplay;

import static bg.Main.*;
import static bg.util.Dialogs.*;
import static bg.util.ThreadUtil.runWhenNotified;

public class MatchApi extends Selection {

  protected ScoreBoard scoreBoard;
  private boolean autoCompleteGame = false;

  public MatchApi(MatchApi matchApi) {

    super(matchApi);
    scoreBoard = matchApi.scoreBoard;
  }

  public MatchApi() {

    scoreBoard = new ScoreBoard(settings.getScoreToWin());
    new ScenarioOutput(scenarios).outputSelectedScenario();
    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setText("Start Match");
    getActionButton().setHideActionButton(false);
    getLayoutEditor().startEditor();
  }

  public Input getInput () {

    return new Input(this);
  }

  public boolean matchOver () {

    return scoreBoard.matchOver();
  }

  public boolean gameIsPlaying () {

    return getGame() != null && getLatestTurn() != null;
  }

  public ScoreBoard getScoreBoard () {

    return scoreBoard;
  }

  public boolean getAutoCompleteGame () {

    return autoCompleteGame;
  }

  public void setAutoCompleteGame (boolean autoComplete) {

    autoCompleteGame = autoComplete;
  }

  public boolean gameOver () {

    return getGame() == null || getGame().gameOver();
  }

  private boolean cubeWasRejected () {

    return getGame().getGameCube().cubeWasRejected();
  }

  private void resolveCubeHandling () {

    if (turnsPlayerIsHuman(getLatestTurn())) {
      game.getGameCube().setCubeWasRejected(
        !confirmed(
          "Opponent doubles - accept?",win
        )
      );
    } else {
      game.getGameCube().setCubeWasRejected(
        !getSelectedMove().shouldTake()
      );
    }
    if (!cubeWasRejected()) {
      game.playerDoubles();
    }
  }

  private void computerHandlesCube () {

    if (nextPlayerIsComputer()) {
      if (!scoreBoard.isCrawfordGame() && !cubeWasRejected()) {
        if (game.playerCanOfferCube() && getSelectedMove().shouldDouble()) {
          resolveCubeHandling();
        }
      }
    }
  }

  public void humanHandlesCube () {

    if (!nextPlayerIsComputer()) {
      if (!getScoreBoard().isCrawfordGame() && getNrOfTurns() > 0) {
        if (game.playerCanOfferCube() || game.getGameCube().getOwner() > 0) {
          resolveCubeHandling();
          if (cubeWasRejected()) {
            showMessage(
              "Opponent rejects the double" +
                      "\nand resigns!",win
            );
          }
        }
      }
      if (getGame().getGameCube().cubeWasRejected()) {
//        getActionButton().setShowPleaseWaitButton(false);
        endTurn();
      }
    }
  }

  public void endTurn () {

    getActionButton().setShowPleaseWaitButton(false);
    getActionButton().setHideActionButton(false);
    if (settings.isAutomatedEndTurn() && !getGame().gameOver()) {
      getMoveInputController().setAcceptMoveInput(false);
      actionButtonClicked();
    } else if (gameOver()) {
      autoCompleteGame = false;
      scoreBoard.writeGameScore(game);
    } else if (autoCompleteGame) {
      actionButtonClicked();
    }
  }

  public void showMove(int showMoveStartPoint) {

    MoveOutput moveOutput = new MoveOutput(getSelectedMove());

    moveOutput.writeMove();
    moveOutput.showMove(
      showMoveStartPoint,
      getSelectedMove().getMovePoints().length-1,
      runWhenNotified(this::endTurn)
//      runWhenNotified(() -> endTurn())
    );
  }

  private boolean noMoveAutoCompletion () {

    return !getSettings().isAutoCompleteMoves() &&
      !getSettings().isAutoCompletePartMoves();
  }

  public void humanMove() {

    getActionButton().setHideActionButton(true);
    if (getSelectedTurn().getNrOfMoves() > 1 || noMoveAutoCompletion()) {
      getMouse().setMoveInput(new MoveInput());
      getMouse().setAcceptMoveInput(true);
      if (getSettings().isAutoCompletePartMoves()) {
        getMouse().getMoveInput().initialAutoMove(runWhenNotified(() -> {
          if (getMouse().getMoveInput().endOfInputReached()) {
            endTurn();
          }
        }));
      }
    } else {
      showMove(0);
    }
  }

  private void computerMove () {

    showMove(0);
  }

  private void noMove () {

    new MoveOutput(getSelectedMove()).outputMove();
    endTurn();
  }

  void Move () {

    if (!getSelectedMove().isIllegal()) {
      if (!autoCompleteGame && turnsPlayerIsHuman(getLatestTurn())) {
        humanMove();
      } else {
        computerMove();
      }
    } else {
      noMove();
    }
  }

  private void rollAndMove () {

    if (getNrOfTurns() == 0) {
      game.nextTurn(0, 0);
    } else {
      game.nextTurn(selectedTurn, selectedMove);
    }
    selectedTurn = game.getLatestTurnNr();
    selectedMove = 0;
    Main.sound.playSoundEffect("wuerfelbecher");
    searchRolledMoves();
    getActionButton().setHideActionButton(true);
    getActionButton().setShowPleaseWaitButton(false);
    Move();
  }

  private void displaySearchReport () {

    TextDisplay.displayReport(
      "Search report",
      "Turn nr: "+(getLatestTurnNr()+1)+"\n"+
        getLatestTurn().getSearchEvaluation().getReport()
    );
  }

  private void searchTurn (Turn turn) {

    turn.generateSearchEvaluations(
      settings.getNrOfMovesToSearch(),
      settings.getSearchToPly()
    ).sortMovesBySearchEvaluation();
  }

  private boolean okToSearch () {

    return !settings.searchIsOff() && (
      settings.getLookaheadForAllPlayers() ||
      !turnsPlayerIsHuman(getLatestTurn())
    );
  }

  void searchRolledMoves () {

    if (okToSearch()) {
      searchTurn(getLatestTurn());
      if (settings.isSearchReportOn()) {
        displaySearchReport();
      }
    }
  }

  private boolean nextPlayerIsComputer () {

    return settings.
      playerStatus[getLatestTurn().getPlayerOnRollsID() == 0 ? 1 : 0] == settings.COMPUTER;
  }

  private void initMatch () {

    Main.getLayoutEditor().endEdit();
    matchLayout = new Layout(win.canvas.getDisplayedLayout());
    matchLayout.setPlayerID(settings.getGameStartMode());
    matchLayout.setUseWhiteBot(settings.getWhiteBotOpponent());
    matchLayout.setUseBlackBot(settings.getBlackBotOpponent());
  }

  private void initGame () {

    scoreBoard.writeMatchScore();
    win.canvas.setDisplayedLayout(new Layout(matchLayout));
  }

  private void startNewGame() {

    game = new Game(matchLayout);
    rollAndMove();
  }

  private void newGame () {

    if (!gameIsPlaying()) {
      System.out.println("Init Match");
      initMatch();
    } else {
      System.out.println("Init Game");
      initGame();
    }
    startNewGame ();
  }

  public void actionButtonClicked () {

    if (gameIsPlaying()) {
      computerHandlesCube();
      if (cubeWasRejected()) {
        endTurn();
        return;
      }
    }
    if (matchOver()) {
      matchApi = new MatchApi();
    } else if (gameOver()) {
      newGame();
    } else {
      rollAndMove();
    }
  }

}
