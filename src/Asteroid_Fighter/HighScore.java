package Asteroid_Fighter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A class for storing top scores (and corresponding names) to a file then allowing
 * later access to those scores and names.
 * 
 * @author Seth Tinglof
 * @version 1.0
 */
public class HighScore {
	private Scanner scan;
	private PrintWriter writer;
	
	private int newScore;
	private String newName;
	private int[] scores = new int[10];
	private String[] names = new String[10];
	
	private int indexScoreAdded;             //the index where a new score was added
	private boolean scoreAdded = false;

	/**
	 * Create a high score instance adding a new score.
	 * @param newScore
	 * @param newName
	 */
	public HighScore(int newScore, String newName){
		this.newScore = newScore;
		this.newName = newName;
		initializeDefaultScores();
		initializeDefaultNames();
		readScores();
		readNames();
		addNewScore();
		addNewName();
		writeScores();
		writeNames();
	}

	/**
	 * Create a high score instance to view current scores and names.
	 */
	public HighScore(){
		initializeDefaultScores();
		initializeDefaultNames();
		readScores();
		readNames();
	}
	
	/**
	 * @return a ten index int array containing the current high scores.
	 */
	public int[] getScores(){
		return scores;
	}
	
	/**
	 * @return The names of the people with the top ten scores.
	 */
	public String[] getNames(){
		return names;
	}
	
	/**
	 * Set all scores to an initial value of zero.
	 */
	private void initializeDefaultScores(){
		for(int i = 0; i < scores.length; i++){
			scores[i] = 0;
		}
	}
	
	/**
	 * Set all names to an initial value of unplayed.
	 */
	private void initializeDefaultNames(){
		for(int i = 0; i < names.length; i++){
			names[i] = "Unplayed";
		}
	}
	
	/**
	 * Reads high scores from the high score file.
	 */
	private void readScores(){
		try{
			scan = new Scanner(new File("HighScores.txt"));
			for(int i = 0; i < scores.length; i++){
				if(!scan.hasNext())
					break;
				scores[i] = scan.nextInt();
			}
			scan.close();
		}catch(IOException e) {
			initializeDefaultScores();}
		 catch(InputMismatchException e) {scan.close(); initializeDefaultScores();}
	}
	
	/**
	 * Reads names from file of names
	 */
	private void readNames(){
		try{
			scan = new Scanner(new File("Names.txt"));
			for(int i = 0; i < names.length; i++){
				if(!scan.hasNext())
					break;
				names[i] = scan.nextLine();
			}
			scan.close();
		}catch(IOException e) {
			initializeDefaultNames();}
		 catch(InputMismatchException e) {scan.close(); initializeDefaultNames();}
	}
	
	/**
	 * Adds new Score to array of scores.
	 * newScore is added at the first index
	 * where newScore is larger than the value
	 * in scores at that index.  All other values
	 * in scores are displaced by one.
	 */
	private void addNewScore(){
		for(int i = 0; i < scores.length; i++){
			if(newScore > scores[i]){
				int temp1 = scores[i];
				int temp2;
				scores[i] = newScore;
				indexScoreAdded = i;
				scoreAdded = true;
				for(int j = i + 1; j < scores.length; j++){
					temp2 = scores[j];
					scores[j] = temp1;
					temp1 = temp2;
				}
				break;
			}
		}
	}
	
	/**
	 * Adds new name to array of names.
	 * Location corresponds to a score
	 * that was added to the score array.
	 */
	private void addNewName(){
		if(scoreAdded){
			String temp1 = names[indexScoreAdded];
			String temp2;
			names[indexScoreAdded] = newName;
			for(int i = indexScoreAdded + 1; i < scores.length; i++){
				temp2 = names[i];
				names[i] = temp1;
				temp1 = temp2;
			}
		}
	}
		
	
	/**
	 * Prints scores to Text File.
	 * @throws NullPointerException if any index in scores is null.
	 */
	private void writeScores(){
		try{
			writer = new PrintWriter("HighScores.txt");
			for(int i = 0; i < scores.length; i++){
				writer.println(scores[i]);
			}
			writer.close();
		} catch (FileNotFoundException e) {}
	}
	
	/**
	 * Prints names to Text File
	 * @throws NullPointerException if any index in names is null.
	 */
	private void writeNames(){
		try{
			writer = new PrintWriter("Names.txt");
			for(int i = 0; i < names.length; i++){
				writer.println(names[i]);
			}
			writer.close();
		} catch (FileNotFoundException e) {}
	}
}
