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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import person.Person;
import util.MutableInt;

public class PopulationStatistics {

	private Map<Integer, MutableInt> stats = new TreeMap<>();
	
	public PopulationStatistics(List<Person> persons) {
		for(int age=0; age<76; age++) {
			stats.put(age, new MutableInt(0));
		}
		
		int size = persons.size();
		for(int i=0; i<size; i++) {
			Person person = persons.get(i);
			int age = person.getAge();
			stats.get(age).increment();
		}
	}

	public Map<Integer, MutableInt> getStats() {
		return Collections.unmodifiableMap(stats);
	}
}
