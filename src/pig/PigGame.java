/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package pig;

/**
 * This class is the main class for the Pig Game programming assignment.
 * 
 * @version Oct 21, 2020
 */
public class PigGame
{
	private int totalScoreP1 = 0, totalScoreP2 = 0, turnScore = 0, turnRoll = 0,
			previousRoll = -1;
	private boolean turn = true; // true is p1, false is p2
	private boolean gameOver = false;
	private int scoreNeeded;
	private Die[] dice;
	private PigGameVersion version;

	/**
	 * Constructor that takes in the version and dice to be used for the game version.
	 * 
	 * @param version
	 *            the game version
	 * @param scoreNeeded
	 *            the score that a player must achieve (equal to this or greater)
	 * @param dice
	 *            an array of dice to be used for the game.
	 * @throws a
	 *             PigGameException if any of the arguments are invalid (e.g. scoreNeeded
	 *             is <= 0)
	 */
	public PigGame(PigGameVersion version, int scoreNeeded, Die... dice)
			throws PigGameException
	{
		this.version = version;
		if (scoreNeeded <= 0) {
			throw new PigGameException("Invalid score needed to win. (<=0)\n");
		}
		this.scoreNeeded = scoreNeeded;
		if (dice == null || dice.length == 0) {
			throw new PigGameException("No dice given");
		}
		if (version.equals(PigGameVersion.TWO_DICE) && dice.length < 2) {
			throw new PigGameException(
					"Dice array needs 2 dice for version 'TWO_DICE'");
		}
		this.dice = dice;
	}

	/**
	 * This method rolls the dice for the play and returns the result.
	 * 
	 * @return the amount rolled. If this returns 0 it means that the player who rolled
	 *         the dice has pigged out and receives a 0 for the turn and the opposing
	 *         player will make the next roll. If the method returns the scoreNeeded, it
	 *         means the player who rolled wins.
	 * @throws a
	 *             PigGameException if the game is over when this method is called.
	 */
	public int roll()
	{
		if (gameOver) {
			throw new PigGameException("Game over");
		}
		turnRoll = dice[0].roll();
		switch (version) {
			case STANDARD:
				if (turnRoll == 1) {// turnover case
					return turnSwap();
				}
				break;
			case TWO_DICE:
				turnRoll += dice[1].roll();// roll the 2nd die for TWO_DICE version
				if (turnRoll == 7) {// turnover case
					return turnSwap();
				}
				break;
			case ONE_DIE_DUPLICATE:
				// previous roll is set to -1, so we know if this roll is first for the
				// turn
				if (turnRoll == previousRoll)
					return turnSwap();// turnover case, this roll matches previous
				previousRoll = turnRoll;// we do not turn over, store this roll
				break;
		}
		turnScore += turnRoll;// add roll to turn score
		if (turnScore + (turn ? totalScoreP1 : totalScoreP2) >= scoreNeeded) {
			// sum of this turn's score and the total score of the current player
			if (turn)
				totalScoreP1 += turnScore;
			else
				totalScoreP2 += turnScore;
			gameOver = true;// game is over, set this flag

			return scoreNeeded;// win case
		}
		return turnRoll;// normal case
	}

	/**
	 * This method adds the turn total to the current total for the active player and
	 * switches players. A player must have rolled at least once during the turn before
	 * holding.
	 * 
	 * @throws PigGameException
	 *             if the active player has not rolled at least one time during the turn.
	 */
	public void hold()
	{
		if (gameOver) {
			throw new PigGameException("Game over");
		}
		if (turnRoll == 0) {
			throw new PigGameException("Player has not rolled at least once");
		}
		if (turn)
			totalScoreP1 += turnScore;
		else
			totalScoreP2 += turnScore;
		turnSwap();
	}

	private int turnSwap()
	{
		// reset value and toggle turns
		turnScore = 0;
		turnRoll = 0;
		previousRoll = -1;// see ONE_DIE_DUPLICATE case in roll()
		turn = !turn;
		return 0;
	}
}
