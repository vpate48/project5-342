package sample;

import java.lang.Math;


public class gamePlay {

    private int guessNumber;// the number a player guessed

    private int p1Guess;// guessed number of player 1
    private int p1Close;// how close is player 1

    private int p2Guess;// guessed number of player 2
    private int p2Close;// how close is player 2

    private int p3Guess;// guessed number of player 2
    private int p3Close;// how close is player 2

    private int p4Guess;// guessed number of player 2
    private int p4Close;// how close is player 2

    private boolean p1win ; // if a player wins round
    private boolean p2win ;
    private boolean p3win;
    private boolean p4win;

    gamePlay(int p1,int p2, int p3, int p4){
        p1Guess = p1;
        p1Close = differenceFinder(guessNumber,p1Guess);

        p2Guess = p2;
        p2Close = differenceFinder(guessNumber,p2Guess);

        p3Guess = p3;
        p3Close = differenceFinder(guessNumber,p3Guess);

        p4Guess = p4;
        p4Close = differenceFinder(guessNumber,p4Guess);

        p1win = false;
        p2win = false;
        p3win = false;
        p4win = false;

    }

    private int differenceFinder(int random, int choice){
        int temp;
        temp = random - choice;
        if(temp < 0){
            temp = temp* (-1);
        }
        return temp;
    }


    private static int generateNumber(){// generation of random number
        double temp = Math.random();//generates number in double form
        temp = temp * 999;// generates number in range
        int number = (int) temp;// makes number an int
        return number;
    }

    public void makeNumber(){// use this function to generate number
        guessNumber = generateNumber();

    }



    public void setPGuess(int player, int guess){// enter player number and thier guess
        switch(player){
            case 1:// set player 1 guess
                p1Guess = guess;
                p1Close = differenceFinder(guessNumber,p1Guess);
                break;

            case 2:// set player 2 guess
                p2Guess = guess;
                p2Close = differenceFinder(guessNumber,p2Guess);
                break;

            case 3:// set player 3 guess
                p3Guess = guess;
                p3Close = differenceFinder(guessNumber,p3Guess);
                break;

            case 4:// set player 4 guess
                p4Guess = guess;
                p4Close = differenceFinder(guessNumber,p4Guess);
                break;

            default:
                System.out.println(" Invalid Player");
                break;
        }

    }




    public int getPGuess(int player){// enter player number to get guess
        switch(player){
            case 1:// return player 1 guess
                return p1Guess;

            case 2:// return player 2 guess
                return p2Guess;

            case 3:// return player 3 guess
                return p3Guess;

            case 4:// return player 4 guess
                return p4Guess;

            default:
                System.out.println(" Invalid Player");
                return (-1);
        }
    }


    public int getPClose(int close){// to get how close a guess is put in the players guess you want
        switch(close){
            case 1:// return player 1 difference
                return p1Close;

            case 2:// return player 2 difference
                return p2Close;

            case 3:// return player 3 difference
                return p3Close;

            case 4:// return player 4 difference
                return p4Close;

            default:
                System.out.println(" Invalid Player");
                return (-1);
        }
    }

    public int getGuessNumber(){
        return guessNumber;
    }

    public void findExactWiner(){
        if(p1Guess ==guessNumber){
            p1win = true;
        }
        if(p2Guess ==guessNumber){
            p2win = true;
        }
        if(p3Guess ==guessNumber){
            p3win = true;
        }
        if(p4Guess ==guessNumber){
            p4win = true;
        }
    }

    public void findWinner(){
        if(p1Close <=p2Close && p1Close <=p3Close && p1Close <=p4Close){
            p1win = true;
        }
        if(p2Close <=p1Close && p2Close <=p3Close && p2Close <=p4Close){
            p2win = true;
        }
        if(p3Close <=p2Close && p3Close <=p1Close && p3Close <=p4Close){
            p3win = true;
        }
        if(p4Close <=p2Close && p4Close <=p3Close && p4Close <=p1Close){
            p4win = true;
        }
    }

    public boolean getWin(int player){// put player number to get thier win statues
        if( player == 1){
            return p1win;
        }
        if( player == 2){
            return p2win;
        }
        if( player == 3){
            return p3win;
        }
        if( player == 4){
            return p4win;
        }
        return false;
    }
}

