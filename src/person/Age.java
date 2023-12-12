/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package person;

import static person.DurationConstants.AGE_INCREASE;
import static person.DurationConstants.YEAR_DURATION;

import java.io.Serializable;

class Age implements Serializable {
	
	private int age;
	
	public Age(int age) {
		this.age = age * YEAR_DURATION;
	}
	
	public int getAge() {
		return age / YEAR_DURATION;
	}
	
	public CauseOfDeath processTurn() {
		age += AGE_INCREASE;
		if (age >= 75 * YEAR_DURATION) {
			return CauseOfDeath.OLD_AGE;
		}
		return null;
	}
	
	public boolean isAdult() {
		return age >= 18 * YEAR_DURATION;
	}
}
