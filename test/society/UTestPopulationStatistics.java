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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import person.Person;
import person.Sex;
import util.MutableInt;

public class UTestPopulationStatistics {

	@Test
	public void testBuildStatistics() {
		List<Person> persons = Arrays.asList(
				new Person(5, Sex.MALE), 
				new Person(15, Sex.FEMALE),
				new Person(15, Sex.MALE)
			);
		PopulationStatistics populationStatistics = new PopulationStatistics(persons);
		Map<Integer, MutableInt> stats = populationStatistics.getStats();
		
		assertEquals(76, stats.size());
		assertEquals(0, stats.get(2).getValue());
		assertEquals(1, stats.get(5).getValue());
		assertEquals(2, stats.get(15).getValue());
	}
}
