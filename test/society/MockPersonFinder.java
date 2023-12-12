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
import java.util.Arrays;
import java.util.List;

import person.Person;

public class MockPersonFinder implements PersonFinder {

	private Person foundPerson;
	
	public MockPersonFinder() {
		this(null);
	}
	
	public MockPersonFinder(Person foundPerson) {
		super();
		this.foundPerson = foundPerson;
	}

	public void setFoundPerson(Person foundPerson) {
		this.foundPerson = foundPerson;
	}

	@Override
	public Person findPerson(PersonAcceptanceFunction personAcceptanceFunction) {
		return foundPerson;
	}

	@Override
	public List<Person> findPersons(PersonAcceptanceFunction personAcceptanceFunction, int limit) {
		if (foundPerson != null) {
			return Arrays.asList(foundPerson);
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public int size() {
		return foundPerson != null ? 1 : 0;
	}
}
