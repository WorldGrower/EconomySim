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
package action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import knowledge.Knowledge;
import person.Person;

public class PersonActions implements Iterable<PersonAction> {

	public static final PersonActions NONE = new PersonActions(Collections.emptyList());
	private final List<PersonAction> personActions;
	
	public PersonActions(List<PersonAction> personActions) {
		this.personActions = personActions;
	}
	
	public PersonActions(PersonAction... personActions) {
		this.personActions = Arrays.asList(personActions);
	}

	public PersonActions() {
		this.personActions = new ArrayList<>();
	}

	public PersonActions filterOutUnavailablePersonActions(Person person) {
		List<PersonAction> filteredPersonActions = new ArrayList<>();
		for(PersonAction personAction : personActions) {
			Knowledge requiredKnowledge = personAction.getRequiredKnowledge();
			if (requiredKnowledge != null) {
				if (person.hasKnowledge(requiredKnowledge)) {
					filteredPersonActions.add(personAction);
				}
			} else {
				filteredPersonActions.add(personAction);
			}
		}
		return new PersonActions(filteredPersonActions);
	}
	
	public PersonActions sortPersonActions() {
		List<PersonAction> copyPersonActions = new ArrayList<>(personActions);
		Collections.sort(copyPersonActions, (p1, p2) -> p1.getName().compareTo(p2.getName()));
		return new PersonActions(copyPersonActions);
	}

	@Override
	public Iterator<PersonAction> iterator() {
		return personActions.iterator();
	}

	public void add(PersonAction personAction) {
		this.personActions.add(personAction);
	}

	public void addAll(PersonActions personActions) {
		this.personActions.addAll(personActions.personActions);
	}

	public int size() {
		return this.personActions.size();
	}
	
	public List<String> getNames() {
		return personActions.stream().map(p -> p.getName()).collect(Collectors.toList());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((personActions == null) ? 0 : personActions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonActions other = (PersonActions) obj;
		if (personActions == null) {
			if (other.personActions != null)
				return false;
		} else if (!personActions.equals(other.personActions))
			return false;
		return true;
	}

	public PersonAction get(int index) {
		return personActions.get(index);
	}

	@Override
	public String toString() {
		return "PersonActions [" + personActions + "]";
	}

	public PersonActions unmodifiablePersonActions() {
		return new PersonActions(Collections.unmodifiableList(personActions));
	}
}
