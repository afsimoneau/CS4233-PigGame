/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package pig;

/**
 * Description
 * 
 * @version Nov 3, 2020
 */
public class DS implements Die
{
	//stands for Die Sequence
private int[] x;
private int count=-1;
	
	DS(int... x) {
		this.x = x;
	}
	public int roll() {
		if (count<x.length) {
			count++;
			return x[count];
		} else {
			return 0;
		}
		
	}

}
