package sample;

public class gamePlay {

    private int guessNumber// the number players have to guess

    private int p1Guess;// guess of player 1
    private int p1Close;// how close is player 1

    private int p2Guess;// guess of player 2
    private int p2Close;// how close is player 2

    private int p3Guess;// guess of player 2
    private int p3Close;// how close is player 2

    private int p4Guess;// guess of player 2
    private int p4Close;// how close is player 2

    public void setPGuess(int player, int guess){
        switch(player){
            case 1:// set player 1 guess
                p1Guess = guess;
                break;

            case 2:// set player 2 guess
                p2Guess = guess;
                break;

            case 3:// set player 3 guess
                p3Guess = guess;
                break;

            case 4:// set player 4 guess
                p4Guess = guess;
                break;

            default:
                System.out.println(" Invalid Player");
                break;
        }

    }




    public int getPGuess(int player){
        switch(player){
            case 1:// return player 1 guess
                return p1Guess;
                break;

            case 2:// return player 2 guess
                return p2Guess;
                break;

            case 3:// return player 3 guess
                return p3Guess;
                break;

            case 4:// return player 4 guess
                return p4Guess;
                break;

            default:
                System.out.println(" Invalid Player");
                break;
        }
    }

}
