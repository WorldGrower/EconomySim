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

import java.util.ArrayList;
import java.util.List;

import person.Person;

public class PersonFinderImpl {

	public static Person findPerson(List<Person> persons, PersonAcceptanceFunction personAcceptanceFunction) {
		for(Person person : persons) {
			if (personAcceptanceFunction.accept(person)) {
				return person;
			}
		}
		return null;
	}
	
	public static List<Person> findPersons(List<Person> persons, PersonAcceptanceFunction personAcceptanceFunction, int limit) {
		List<Person> foundPersons = new ArrayList<>();
		for(Person person : persons) {
			if (personAcceptanceFunction.accept(person)) {
				foundPersons.add(person);
				if (foundPersons.size() >= limit) {
					return foundPersons;
				}
			}
		}
		return foundPersons;
	}
}
