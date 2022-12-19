package com.mycompany.tictactoereal.gamelogic;

import com.mycompany.tictactoereal.networking.MulticastPublisher;
import com.mycompany.tictactoereal.networking.Pinger;
import com.mycompany.tictactoereal.networking.MessageCreator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogic {

    private int[][] gameBoard;
    //private int playerSymbol = 1;
    private int playerSymbol;
    private int symbolInTurn = 1;
    private int playerAmount = 4;
    private int gameWonBy = 0;
    private int turnNumber = 0;
    private MulticastPublisher publisher;
    private BoardChangedEventHandler boardChangedEventHandler;
    private ArrayList<Move> moves;
    private String[] playerArray;
    private Pinger pinger;
    private ArrayList<Integer> eliminatedTurnNumbers;
    //key is userHash and list contains every userHash voting to kick out
    private HashMap<String, ArrayList<String>> votersForKickingOut;

    //Used for temporarily differenciating users, may not be useful later
    private String userHash;

    // 0 - empty
    // 1 - x
    // 2 - o
    // 3 - triangle
    // 4 - star
    public GameLogic(MulticastPublisher publisher) {
        this.gameBoard = new int[30][30];
        this.publisher = publisher;
        //System.out.println("GameLogic constructor 1, publisher " + publisher);
        this.moves = new ArrayList<>();
        this.eliminatedTurnNumbers = new ArrayList<>();

    }

    public GameLogic(MulticastPublisher publisher, int playerSymbol) {
        this.gameBoard = new int[30][30];
        this.publisher = publisher;
        this.playerSymbol = playerSymbol;
        this.eliminatedTurnNumbers = new ArrayList<>();
        //System.out.println("GameLogic constructor 2, publisher " + publisher);

    }

    public void restoreGameState(ArrayList<Move> moveList) {
        //was done to send game state to someone dropped out
        gameBoard = new int[30][30];
        moves = moveList;
        //todo: sort movelist to match turn order
        for (int i = 0; i < moveList.size(); i++) {
            gameBoard[moveList.get(i).getX()][moveList.get(i).getY()] = moveList.get(i).getSymbol();
        }
        symbolInTurn = moveList.get(moveList.size() - 1).getSymbol();
        symbolInTurn++;
        if (symbolInTurn >= playerAmount + 1) {
            symbolInTurn = 1;
        }
        checkIfGameWon();
    }

    public void searchGame(String adr) throws IOException {
        this.publisher.multicast(userHash, adr);
    }

    public int[][] getGameBoard() {
        return gameBoard;
    }

    public int tileIdAt(int x, int y) {
        if (x >= 0 && x < 30 && y >= 0 && y < 30) {
            return gameBoard[x][y];
        }
        return 0;
    }

    public boolean placeTile(int x, int y, int tileId, boolean isMulticasting) throws IOException {
        //if (isMulticasting && (symbolInTurn != playerSymbol || gameBoard[x][y] != 0) || gameWonBy != 0) {
        if (isMulticasting && (symbolInTurn != playerSymbol || gameBoard[x][y] != 0) || gameWonBy != 0) {
            return false;
        }
        if (x >= 0 && x < 30 && y >= 0 && y < 30) {
            if (isMulticasting) {

                String multicastMessage = MessageCreator.createPlaceTileMessage(x, y, tileId, this);
                publisher.multicast(multicastMessage);
            }

            turnNumber++;
            moves.add(new Move(tileId, x, y, turnNumber));
            gameBoard[x][y] = tileId;
            symbolInTurn++;

            if (symbolInTurn >= playerAmount + 1) {
                symbolInTurn = 1;

            }
            while (eliminatedTurnNumbers.contains(symbolInTurn)) {
                symbolInTurn++;
                if (symbolInTurn >= playerAmount + 1) {
                    symbolInTurn = 1;
                }
            }
            checkIfGameWon();
            boardChanged();
            return true;

        }
        return false;
    }

    public void checkIfGameWon() {

        int neededToWin = 5;

        //rows and columns
        int currentSymbolHor = 0;
        int currentInRowHor = 0;
        int currentSymbolVer = 0;
        int currentInRowVer = 0;

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard.length; j++) {
                int currentHor = gameBoard[j][i];
                if (currentHor != 0) {
                    if (currentSymbolHor != currentHor) {
                        currentSymbolHor = currentHor;
                        currentInRowHor = 1;
                    } else {
                        currentInRowHor++;
                        if (currentInRowHor == neededToWin) {
                            gameWonBy = currentSymbolHor;
                        }
                    }

                } else {
                    currentInRowHor = 0;
                }
                int currentVer = gameBoard[i][j];
                if (currentVer != 0) {
                    if (currentSymbolVer != currentVer) {
                        currentSymbolVer = currentVer;
                        currentInRowVer = 1;
                    } else {
                        currentInRowVer++;
                        if (currentInRowVer == neededToWin) {
                            gameWonBy = currentSymbolVer;
                        }
                    }
                } else {
                    currentInRowVer = 0;
                }
            }
            currentSymbolHor = 0;
            currentInRowHor = 0;
            currentSymbolVer = 0;
            currentInRowVer = 0;
        }

        //diagonal
        int currentSymbolDiag = 0;
        int currentInRowDiag = 0;
        int currentSymbolDiag2 = 0;
        int currentInRowDiag2 = 0;
        int currentSymbolDiag3 = 0;
        int currentInRowDiag3 = 0;
        int currentSymbolDiag4 = 0;
        int currentInRowDiag4 = 0;

        for (int i = 0; i < gameBoard.length; i++) {
            int checkOffset = 0;
            while (i + checkOffset < 30) {
                int currentDiag = gameBoard[i + checkOffset][checkOffset];
                if (currentDiag != 0) {
                    if (currentSymbolDiag != currentDiag) {
                        currentSymbolDiag = currentDiag;
                        currentInRowDiag = 1;
                    } else {
                        currentInRowDiag++;
                        if (currentInRowDiag == neededToWin) {
                            gameWonBy = currentSymbolDiag;
                        }
                    }
                } else {
                    currentInRowDiag = 0;
                }
                int currentDiag2 = gameBoard[checkOffset][i + checkOffset];
                if (currentDiag2 != 0) {
                    if (currentSymbolDiag2 != currentDiag2) {
                        currentSymbolDiag2 = currentDiag2;
                        currentInRowDiag2 = 1;
                    } else {
                        currentInRowDiag2++;
                        if (currentInRowDiag2 == neededToWin) {
                            gameWonBy = currentSymbolDiag2;
                        }
                    }
                } else {
                    currentInRowDiag2 = 0;
                }
                int currentDiag3 = gameBoard[29 - checkOffset][i + checkOffset];
                if (currentDiag3 != 0) {
                    if (currentSymbolDiag3 != currentDiag3) {
                        currentSymbolDiag3 = currentDiag3;
                        currentInRowDiag3 = 1;
                    } else {
                        currentInRowDiag3++;
                        if (currentInRowDiag3 == neededToWin) {
                            gameWonBy = currentSymbolDiag3;
                        }
                    }
                } else {
                    currentInRowDiag3 = 0;
                }
                int currentDiag4 = gameBoard[checkOffset][29 - (i + checkOffset)];
                if (currentDiag4 != 0) {
                    if (currentSymbolDiag4 != currentDiag4) {
                        currentSymbolDiag4 = currentDiag4;
                        currentInRowDiag4 = 1;
                    } else {
                        currentInRowDiag4++;
                        if (currentInRowDiag4 == neededToWin) {
                            gameWonBy = currentSymbolDiag4;
                        }
                    }
                } else {
                    currentInRowDiag4 = 0;
                }
                checkOffset++;
            }
            currentSymbolDiag = 0;
            currentInRowDiag = 0;
            currentSymbolDiag2 = 0;
            currentInRowDiag2 = 0;
            currentSymbolDiag3 = 0;
            currentInRowDiag3 = 0;
            currentSymbolDiag4 = 0;
            currentInRowDiag4 = 0;
        }
    }

    public int getPlayerSymbol() {
        return playerSymbol;
    }

    public void setPlayerSymbol(int playerSymbol) {
        this.playerSymbol = playerSymbol;
        boardChanged();
    }

    public int getSymbolInTurn() {
        return symbolInTurn;
    }

    public void setSymbolInTurn(int symbolInTurn) {
        this.symbolInTurn = symbolInTurn;
        boardChanged();
    }

    public MulticastPublisher getPublisher() {
        return publisher;
    }

    public String getUserHash() {
        return this.userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public String[] getPlayerArray() {
        if (this.playerArray == null) {
            return new String[0];
        }
        return this.playerArray;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
        boardChanged();
    }

    public void voteForKickingOutForUserFromUser(String voterUserHash, String votedUserHash) {
        votersForKickingOut.get(votedUserHash).add(voterUserHash);
        if (votersForKickingOut(votedUserHash) == this.getPlayerAmount() - 1) {
            System.out.println("All voted for kicking out");
            try {
                publisher.multicast(MessageCreator.kickOut(this, votedUserHash));
                System.out.println("multicast(messageCreator.kickout) called");
            } catch (IOException ex) {
                System.out.println("IOException in multicasting kicking out");
            }
            System.out.println("all voted and after multicast");
            int index = -1;
            String[] newPlayers = new String[this.getPlayerArray().length - 1];
            System.out.println("bnewPlayers initiated");
            for (int i = 0, k = 0; i < this.getPlayerArray().length; i++) {
                if (!this.getPlayerArray()[i].equals(votedUserHash)) {
                    newPlayers[k] = this.getPlayerArray()[i];
                    k++;
                } else {
                    System.out.println("index" + i);
                    System.out.println("kciked out player" + this.getPlayerArray()[i]);
                    newPlayers[k] = "XXX";
                    index = i;
                }
            }
            System.out.println("setting PlayerAmount and array and printing array");
            for (int i=0; i < newPlayers.length; i++) {
                System.out.println(newPlayers[i]);
            }
            this.setPlayerAmount(newPlayers.length);
            this.setPlayerArray(newPlayers);
            if (index != -1) {
                skipKickedOutTurnAndSymbol(index + 1);
            }
            pinger.deletePlayer(votedUserHash);
            boardChanged();
        }
    }

    public void skipKickedOutTurnAndSymbol(int turnNumber) {
        this.eliminatedTurnNumbers.add(turnNumber);
    }

    public int votersForKickingOut(String userHash) {
        return this.votersForKickingOut.get(userHash).size();
    }

    public boolean haveAlreadyVotedForKickingOut(String userHash) {
        if (votersForKickingOut.get(userHash).contains(this.userHash)) {
            return true;
        } else {
            return false;
        }
    }

    public void setVotingToKickOut(String[] userHashes) {
        HashMap<String, ArrayList<String>> voting = new HashMap<>();
        for (int i = 0; i < userHashes.length; i++) {
            voting.put(userHashes[i], new ArrayList<>());
        }
        this.votersForKickingOut = voting;
    }

    public int getGameWonBy() {
        return gameWonBy;
    }

    public void setGameWonBy(int gameWonBy) {
        this.gameWonBy = gameWonBy;
        boardChanged();
    }

    public void setBoardChangedEventHandler(BoardChangedEventHandler handler) {
        this.boardChangedEventHandler = handler;
    }

    private void boardChanged() {
        if (boardChangedEventHandler != null) {
            boardChangedEventHandler.handle(new BoardChangedEvent());
        }
    }

    public ArrayList<Move> getMoves() {
        return moves;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setPlayerArray(String[] arr) {
        this.playerArray = arr;
    }

    public void setPinger(Pinger p) {
        pinger = p;
    }

    public Pinger getPinger() {
        return pinger;
    }
}
