package sample;
java.lang.Math;


public class gamePlay {

    private int guessNumber;// the number players have to guess

    private int p1Guess;// guess of player 1
    private int p1Close;// how close is player 1

    private int p2Guess;// guess of player 2
    private int p2Close;// how close is player 2

    private int p3Guess;// guess of player 2
    private int p3Close;// how close is player 2

    private int p4Guess;// guess of player 2
    private int p4Close;// how close is player 2


    private int differnceFinder(int random, int choice){
        int temp;
        temp = random - choice;
        if(temp < 0){
            temp = temp* (-1);
        }
        return temp;
    }


    public static void generateNumber(){// generation of random number
        double temp = Math.random();
        temp = temp * 999;
        guessNumber = (int) temp;
    }

    public void setPGuess(int player, int guess){
        switch(player){
            case 1:// set player 1 guess
                p1Guess = guess;
                p1Close = differnceFinder(guessNumber,p1Guess);
                break;

            case 2:// set player 2 guess
                p2Guess = guess;
                p2Close = differnceFinder(guessNumber,p2Guess);
                break;

            case 3:// set player 3 guess
                p3Guess = guess;
                p3Close = differnceFinder(guessNumber,p3Guess);
                break;

            case 4:// set player 4 guess
                p4Guess = guess;
                p4Close = differnceFinder(guessNumber,p4Guess);
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


    public int getPClose(int close){
        switch(player){
            case 1:// return player 1 difference
                return p1Close;
                break;

            case 2:// return player 2 difference
                return p2Close;
                break;

            case 3:// return player 3 difference
                return p3Close;
                break;

            case 4:// return player 4 difference
                return p4Close;
                break;

            default:
                System.out.println(" Invalid Player");
                break;
        }
    }

}
