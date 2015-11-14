import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;


public class Player extends Object{
	
	int currentPosition, i, numberOfTurns;
	Boolean isInJail, hasJailCard;
	Random comChest, chan;
	int landedOn[];;
	
	//initializes player parameters
	public Player(){
		this.currentPosition=0;
		this.isInJail = false;
		this.hasJailCard = false;
		landedOn = new int[41];
		numberOfTurns=0;
	}

	public void move(int n){
		currentPosition = currentPosition + n;
		if(currentPosition>40)
			currentPosition = currentPosition - 41;
		
		if(currentPosition == 31)
			this.goToJail();
		else if(currentPosition == 7 || currentPosition == 23 ||
				currentPosition == 37){
			chance(currentPosition);
		}
		else if(currentPosition == 2 || currentPosition == 18 ||
				currentPosition == 34){
			communityChest(currentPosition);
		}
		else{
			landed(currentPosition);
		}		
	}
	
	public void landed(int currentPosition){
		this.landedOn[currentPosition]++;
		System.out.println(this.currentPosition+" has been landed on "+landedOn[this.currentPosition]+" times");
	}
	
	public void increaseTurns(){
		this.numberOfTurns++;
	}
	
	public void goToJail(){
		this.currentPosition = 11;
		this.landed(11);
	}
	public void chance(int currentPosition){
	chan = new Random();
	int card = chan.nextInt(16);
	System.out.println("You pulled card number "+card+" from Chance");
	if(card==0){
		this.currentPosition=0;
		this.landed(0);
	}
	else if(card==1){
		this.currentPosition=25;
		this.landed(25);
	}
	else if(card==2){
		this.currentPosition=12;
		this.landed(12);
	}
	else if(card==3){
		//advance to nearest utility
		if(this.currentPosition==7){
			this.currentPosition=13;
			this.landed(13);
		}
		else{
			this.currentPosition=29;
			this.landed(29);
		}
	}
	else if(card==4){
		//advance to nearest RR
		if(this.currentPosition==7){
			this.currentPosition=5;
			this.landed(5);
		}
		else if(this.currentPosition==23){
			this.currentPosition=26;
			this.landed(26);
		}
		else{
			this.currentPosition=36;
			this.landed(36);
		}
	}
	else if(card==6){
		//get out of jail free card
		this.hasJailCard=true;
		landed(this.currentPosition);
	}
	else if(card==7){
		//go back 3 spaces
		this.currentPosition=currentPosition-3;
		if(this.currentPosition==34){
			this.communityChest(34);
		}
		else{
			landed(this.currentPosition);
		}
	}
	else if(card==8){
		this.goToJail();
	}
	else if(card==11){
		//go to reading RR
		this.currentPosition=5;
		this.landed(5);
	}
	else if(card==12){
		this.currentPosition=40;
		landed(40);
	}
	else{
		landed(this.currentPosition);
	}
	}
	
	public void communityChest(int position){
		comChest = new Random();
		int card = comChest.nextInt(16);
		System.out.println("You pulled card number "+card+" from comChest");
		if(card==0){
			this.currentPosition=0;
			landed(0);
		}
		else if(card==4){
			this.hasJailCard=true;
			landed(this.currentPosition);
		}
		else if(card==5){
			this.goToJail();
		}
		else{
			landed(this.currentPosition);
		}
		
	}
	
	@Override
	public String toString(){
		
		String positionFrequencies="";
		int i;
		int sum=0;
		float totProb=0;

		System.out.println("---------------------------------");
		HashMap<String, Float> map = new HashMap<String, Float>();
		
		for(i=0;i<41;i++){
			float freq = (float)this.landedOn[i]/Game.NUMBER_OF_TURNS;
			sum = sum + this.landedOn[i];			
			map.put(Game.positions[i], freq);
			totProb+=freq;
		}
		
		Set<Entry<String, Float>> entries = map.entrySet();
		Comparator<Entry<String, Float>> valueComparator = new Comparator<Entry<String,Float>>() { 
			@Override 
			public int compare(Entry<String, Float> e1, Entry<String, Float> e2) { 
					Float v1 = e2.getValue(); 
					Float v2 = e1.getValue(); 
					return v1.compareTo(v2); 
				} 
			};
			 
		List<Entry<String, Float>> listOfEntries = new ArrayList<Entry<String, Float>>(entries);
		Collections.sort(listOfEntries, valueComparator); 
		LinkedHashMap<String, Float> sortedByValue = new LinkedHashMap<String, Float>(listOfEntries.size());
		for(Entry<String, Float> entry : listOfEntries){ 
			sortedByValue.put(entry.getKey(), entry.getValue()); 
		}
		System.out.println("HashMap after sorting entries by values "); 
		Set<Entry<String, Float>> entrySetSortedByValue = sortedByValue.entrySet(); 
		
		int count = 1;
		for(Entry<String, Float> mapping : entrySetSortedByValue){
			System.out.println(count + ": " + mapping.getKey() + " ==> " + mapping.getValue());
			count++;
		}

		System.out.println("---------------------------------");
		System.out.println("Summation of probabilities: " + totProb);
		System.out.println("Number of spaces per turn: "+(float)(sum/this.numberOfTurns));
		System.out.println("Total number of turns: "+this.numberOfTurns+"\nTotal number of landed ons: "+sum);
		return positionFrequencies;
		
	}
}