/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package pig;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static pig.PigGameVersion.*;

/**
 * TestCases for the PigGame
 * 
 * @version Oct 21, 2020
 */
class PigGameTest
{

	// #1
	@Test
	void testBadScoreToWin()
	{
		assertThrows(PigGameException.class,
				() -> new PigGame(STANDARD, -1, new D6()));
	}

	// #2
	@Test
	void noDice()
	{ // See https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions
		assertThrows(PigGameException.class, () -> new PigGame(STANDARD, 10, null));
		Die[] d = {};
		assertThrows(PigGameException.class, () -> new PigGame(TWO_DICE, 10, d));
	}

	// #3
	@Test
	void testNotEnoughDice()
	{
		assertThrows(PigGameException.class,
				() -> new PigGame(TWO_DICE, 10, new D6()));

	}

	// #4.1
	@Test
	void testP1GameOver()
	{
		PigGame pg = new PigGame(STANDARD, 2, new DX(2));
		assertTrue(pg.roll() == 2);// score is 2, game is won
		assertThrows(PigGameException.class, () -> pg.roll());// roll again throws
	}

	// #4.2
	@Test
	void testP2GameOver()
	{
		PigGame pg = new PigGame(STANDARD, 3, new DX(2));
		pg.roll();
		pg.hold();// p1 has 2, p2 is now the player
		assertTrue(pg.roll() == 2);
		assertTrue(pg.roll() == 3);// p2 has won
		assertThrows(PigGameException.class, () -> pg.roll());// roll again throws
	}

	// test 5,6,7 are all tested by other test cases or implicitly by normal operation

	// #8
	@Test
	void testTurnoverStandard()
	{
		PigGame pg = new PigGame(STANDARD, 10, new DX(1));
		assertTrue(pg.roll() == 0);
	}

	// #9
	@Test
	void testTurnoverTwoDice()
	{
		PigGame pg = new PigGame(TWO_DICE, 10, new DX(3), new DX(4));
		assertTrue(pg.roll() == 0);
		assertTrue(pg.roll() == 0);
	}

	// #10
	@Test
	void testTurnoverDieDuplicate()
	{
		PigGame pg = new PigGame(ONE_DIE_DUPLICATE, 10, new DX(2));
		pg.roll();
		assertTrue(pg.roll() == 0);
	}

	// test 11 is not needed, this is covered by other tests

	// #12.1
	@Test
	void testStandardWin()
	{
		// greater
		PigGame pg = new PigGame(STANDARD, 3, new DX(2));
		pg.roll();// p1=2
		assertTrue(pg.roll() == 3);// p1=win

		pg = new PigGame(STANDARD, 3, new DX(2));
		pg.roll();// p1=2
		pg.hold();
		pg.roll();// p2=2
		assertTrue(pg.roll() == 3);// p2=win

		// equal
		pg = new PigGame(STANDARD, 4, new DX(2));
		pg.roll();// p1=2
		assertTrue(pg.roll() == 4);// p1=win

		pg = new PigGame(STANDARD, 4, new DX(2));
		pg.roll();// p1=2
		pg.hold();
		pg.roll();// p2=2
		assertTrue(pg.roll() == 4);// p2=win
	}

	// #12.2
	@Test
	void testTwoDiceWin()
	{
		// greater
		PigGame pg = new PigGame(TWO_DICE, 6, new DX(2), new DX(2));
		pg.roll();
		assertTrue(pg.roll() == 6);// p1 win

		pg = new PigGame(TWO_DICE, 6, new DX(2), new DX(2));
		pg.roll();
		pg.hold();
		pg.roll();
		assertTrue(pg.roll() == 6);// p2 win

		// equal
		pg = new PigGame(TWO_DICE, 8, new DX(2), new DX(2));
		pg.roll();
		assertTrue(pg.roll() == 8);// p1 win

		pg = new PigGame(TWO_DICE, 8, new DX(2), new DX(2));
		pg.roll();
		pg.hold();
		pg.roll();
		assertTrue(pg.roll() == 8);// p2 win
	}

	// #12.3
	@Test
	void testDieDuplicateWin()
	{
		// greater
		PigGame pg = new PigGame(ONE_DIE_DUPLICATE, 4, new DS(2, 1, 5));
		pg.roll();
		pg.roll();
		assertTrue(pg.roll() == 4);// p1 win

		pg = new PigGame(ONE_DIE_DUPLICATE, 4, new DS(1, 5));
		pg.roll();
		pg.hold();
		assertTrue(pg.roll() == 4);// p2 win

		// equal
		pg = new PigGame(ONE_DIE_DUPLICATE, 4, new DS(2, 1, 4));
		pg.roll();
		pg.roll();
		assertTrue(pg.roll() == 4);// p1 win

		pg = new PigGame(ONE_DIE_DUPLICATE, 4, new DS(1, 4));
		pg.roll();
		pg.hold();
		assertTrue(pg.roll() == 4);// p2 win

	}

	// #13
	@Test
	void testRollNormal()
	{
		PigGame pg = new PigGame(STANDARD, 10, new DX(5));
		assertTrue(pg.roll() == 5);
	}

	// #14
	@Test
	void testHoldGameOver()
	{
		PigGame pg = new PigGame(STANDARD, 2, new DX(3));
		pg.roll();
		assertThrows(PigGameException.class, () -> pg.hold());
	}

	// #15
	@Test
	void testHoldNoRoll()
	{
		PigGame pg = new PigGame(STANDARD, 10, new DX(3));
		assertThrows(PigGameException.class, () -> pg.hold());
	}

	// #16
	@Test
	void testHold()
	{
		PigGame pg = new PigGame(STANDARD, 4, new DX(3));
		assertTrue(pg.roll() == 3);
		pg.hold();// p2 turn now
		assertTrue(pg.roll() == 3);// should also get 3
		pg.hold();// back to p1
		assertTrue(pg.roll() == 4);// p1 rolls and wins
	}

}
