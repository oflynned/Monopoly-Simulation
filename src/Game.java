import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.AbstractCollection;


public class Game extends Object{
	
	int numberOfTurns, doublesInARow, turnsInJail;
	int landedOn[] = new int[40];
	Player pawn;
	Random die1, die2, chan, comChest;
	Boolean isInJail;
	
	static String positions[] = {
		"Go", 
		"Mediterranean", 
		"Community Chest",
		"Baltic", 
		"Income Tax", 
		"Reading RR", 
		"Oriental", 
		"Chance",
		"Vermont", 
		"Connecticut", 
		"Just Visiting", 
		"Jail", 
		"St. Charles",
		"Electric Company", 
		"States", 
		"Virginia", 
		"Pennsylvania RR", 
		"St. James",
		"Community Chest", 
		"Tennessee", 
		"New York", 
		"Free Parking", 
		"Kentucky", 
		"Chance", 
		"Indiana", 
		"Illinois", 
		"B.&.O RR", 
		"Atlantic", 
		"Ventor", 
		"Water Works", 
		"Marvin Gardens", 
		"Go to Jail", 
		"Pacific", 
		"North Carolina",
		"Community Chest", 
		"Pennsylvania", 
		"Short Line RR", 
		"Chance", 
		"Park Place", 
		"Luxury Tax", 
		"Boardwalk"
	};
	
	static String chance[] = {
		"Advance to Go", 
		"Advance to Illinois Ave", 
		"Advance to St. Charles",
		"Advance to nearest utility", 
		"Advance to nearest RR", 
		"Bank pays you $50", 
		"Get out of jail free", 
		"Go back 3 spaces", 
		"Go to jail", 
		"Make repairs on your property",
		"Pay 15 dollars", 
		"Go to Reading RR", 
		"Go to Boardwalk", 
		"Pay each player $50", 
		"Collect $150",
		"Collect $100"
	};
	
	static String chest[] = {
		"Advance to Go", 
		"Collect $200", 
		"Pay $50", 
		"Get $50", 
		"Get out of jail free", 
		"Go to jail",
		"Collect $50 from each player", 
		"Get $100", 
		"Collect $20", 
		"Collect $10 from each player",
		"Collect $100 dollars", 
		"Pay $100", 
		"Pay $150", 
		"Get $25", 
		"Get $100", 
		"Get $10"};
	
	public Game(){
		pawn = new Player();
		this.numberOfTurns = 0;
		this.die1 = new Random();
		this.die2 = new Random();
		doublesInARow=0;
		turnsInJail=0;
	}
	
	public int rollDice(){
		int result;
		int roll1, roll2;
		roll1 = die1.nextInt(6)+1;
		roll2 = die2.nextInt(6)+1;
		if(roll1 == roll2){
			doublesInARow++;
			if(doublesInARow==3){
				pawn.goToJail();
				doublesInARow=0;
				return 0;
			}
			System.out.println("You rolled doubles!");
		}
		else{
			System.out.println("You did not roll doubles!");
			doublesInARow=0;
		}
		result = roll1 + roll2;
		return result;
		
	}
	
	public void takeTurn(Player pawn){
		if(doublesInARow==0){
			pawn.increaseTurns();
		}
		if(pawn.isInJail){
			takeJailTurn(pawn);
			return;
		}
		int moveSpaces = rollDice();
		if(moveSpaces==0){
			return;
		}
		pawn.move(moveSpaces);
		if(!pawn.isInJail && doublesInARow>0){
			takeTurn(pawn);
		}
		
	}
	
	public void takeJailTurn(Player pawn){
		int jailRoll;
		turnsInJail++;
		if(turnsInJail>2){
			pawn.currentPosition=10;
			pawn.isInJail=false;
			takeTurn(pawn);
			return;
		} 
		//use get out of jail card
		else if(pawn.hasJailCard == true && pawn.numberOfTurns < 5000){
			pawn.currentPosition=10;
			pawn.hasJailCard=false;
			pawn.isInJail=false;
			takeTurn(pawn);
			return;
		}
		jailRoll = rollDice();
		//Check number of turns, if late game, sit in jail, otherwise get out
		if(doublesInARow==1){
			pawn.currentPosition=11;
			pawn.move(jailRoll);
			pawn.increaseTurns();
			doublesInARow=0;
			return;
		}
		else{
			pawn.landed(10);
			pawn.increaseTurns();
			return;
		}
		
		
	}
	
	
	public static void main(String[] args){
		int i;
		Game mon = new Game();
		while(mon.pawn.numberOfTurns<10000){
			mon.takeTurn(mon.pawn);
		}
		System.out.println(mon.pawn.toString());
	}
}