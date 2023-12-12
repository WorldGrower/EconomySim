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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProfessionStatistics {
	private final int[] statistics = new int[Profession.VALUES.length];
	private int size;
	
	public ProfessionStatistics() {
		updateInfo(Collections.emptyList());
	}
	
	public ProfessionStatistics(List<Person> persons) {
		updateInfo(persons);
	}
	
	public int size() {
		return size;
	}
	
	public int getProfessionCount(Profession profession) {
		return statistics[profession.ordinal()];
	}
	
	public boolean needExistsForProfession(Profession profession) {
		return statistics[Profession.FARMER.ordinal()] > 10 && statistics[profession.ordinal()] == 0;
	}
	
	public void updateInfo(List<Person> persons) {
		Arrays.fill(statistics, 0);
		for(Person person : persons) {
			statistics[person.getProfession().ordinal()]++;
		}
		size = persons.size();
	}
}