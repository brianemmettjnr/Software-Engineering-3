package monopoly;

import java.util.ArrayList;

import interfaces.*;

public class Panopoly 
{
	private ArrayList<Player> players;
	private Player currentPlayer;
	private Board board;
	private GUI gui;
	private Dice dice = new Dice();
	private boolean clockwiseMovement = true;
	
	Panopoly(int numLocations)
	{
		board = new Board(numLocations);
		GUI.PlayerCountGui(this);
	}
	
	public Board getBoard()
	{
		return this.board;
	}
	
	public String roll()
	{
		currentPlayer.canRoll = false;
		currentPlayer.rollComplete = true;
		int movePositions = dice.rollNormalDice();
		
		String msg = currentPlayer.getIdentifier() + " has rolled " + movePositions + ". ";
		
		currentPlayer.move(movePositions, clockwiseMovement);
		
		if(dice.getDoubles())
		{
			currentPlayer.canRoll = true;
			msg += currentPlayer.getIdentifier() + " has rolled doubles and can roll again.";
		}
		
		getSquareAction();
		
		gui.resetCommands();
		gui.updatePlayers();
		return (msg);
	}
	
	public String buyProperty(Rentable property)
	{
		currentPlayer.buyProperty(property, property.getPrice());
		gui.buyCommand = false;
		gui.updatePlayers();
		return currentPlayer.getIdentifier() + " has bought " + property.getIdentifier() + " for " + property.getPrice() + ".";
	}

	//TO DO: DRAW CARD
	private void getSquareAction() 
	{
		Locatable square = board.getLocation(currentPlayer.getPosition());
		
		if(square instanceof TaxableProperty)
		{
			currentPlayer.pay(((Taxable) square).getFlatAmount());
			gui.updateAction(currentPlayer.getIdentifier() + " has paid " + ((Taxable) square).getFlatAmount() + " in tax.");
		}
		//rental property owned by another player
		else if((square instanceof RentalProperty) && (((Rentable) square).getOwner()!=null) && (((Rentable) square).getOwner()!=currentPlayer))
		{
			int rent = ((Rentable) square).getRentalAmount();
			currentPlayer.pay(rent);
			((Player) ((Rentable) square).getOwner()).earn(rent);
			gui.updateAction(currentPlayer.getIdentifier() + " has paid " + rent + " to " + ((Rentable) square).getOwner().getIdentifier());
		}
		
		
	}
	
	//buy, roll, drawCard, endTurn
	public void setPossibleCommands()
	{
		Locatable square = board.getLocation(currentPlayer.getPosition());
		
		gui.rollCommand = currentPlayer.canRoll;
		//unowned property and player has rolled at least once
		if(square instanceof Rentable && ((Rentable) square).getOwner() == null && currentPlayer.rollComplete)
			gui.buyCommand = true;
				
		gui.endCommand = !gui.rollCommand;
	}
	
	//redeem, mortgage, build, demolish
	public void setPossiblePropertyCommands()
	{
		
	}

	public void createGUI() 
	{
		players = GUI.getPlayersArray();
		currentPlayer = players.get(0);
		gui = new GUI(board.getNumLocations());
		gui.updatePlayers();
	}
	
	public String nextPlayer()
	{
		currentPlayer.canRoll = true;
		currentPlayer.rollComplete = false;
		currentPlayer = players.get((players.indexOf(currentPlayer)+1)%players.size());
		gui.resetCommands();
		gui.updatePlayers();
		return currentPlayer.getIdentifier() + "'s turn";
	}

	public Player getCurrentPlayer() {
		return currentPlayer;
	}
}
