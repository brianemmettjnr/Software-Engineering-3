package monopoly;

import java.util.ArrayList;

public class Panopoly 
{
	private ArrayList<Player> players = new ArrayList<Player>();
	private Player currentPlayer;
	private Board board;
	private Dice dice = new Dice();
	private boolean clockwiseMovement = true;
	
	Panopoly(int numLocations)
	{
		board = new Board(numLocations);
	}
	
	public void createPlayers()
	{
		players.add(new Player("Player 1"));
	}
	
	private void roll()
	{
		currentPlayer.move(dice.rollDice(1, 6), clockwiseMovement);
		getSquareAction();
	}

	//TO DO: COMPLETE ALL POSSIBILITIES
	private void getSquareAction() 
	{
		Locatable square = board.getLocation(currentPlayer.getPosition());
		
		if(square instanceof Taxable)
			currentPlayer.pay(((Taxable) square).getFlatAmount() + ((Taxable) square).getIncomePercentage() * currentPlayer.getBalance());
	}
}
