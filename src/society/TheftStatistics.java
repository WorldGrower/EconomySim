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
package society;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import person.Person;

class TheftStatistics implements Serializable {

	private final Set<Person> knownThieves = new HashSet<>();
	private int latestNumberOfThefts = 0;
	
	public void registerTheft(Person person, Person targetPerson, boolean success, boolean detected) {
		if (detected) {
			knownThieves.add(person);
		}
		latestNumberOfThefts++;
	}
	
	public void onDeath(Person person) {
		knownThieves.remove(person);
	}

	public boolean isKnownThief(Person person) {
		return knownThieves.contains(person);
	}

	public int getLatestNumberOfThefts() {
		return latestNumberOfThefts;
	}
	
	public void startTurn() {
		latestNumberOfThefts = 0;
	}
}
