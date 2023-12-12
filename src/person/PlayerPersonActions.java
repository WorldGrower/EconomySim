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

import java.util.ArrayList;
import java.util.List;

import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActions;

public class PlayerPersonActions {
	private final Person person;
	private final PersonActions personActions = new PersonActions();
	private final List<PersonActionArgs> args = new ArrayList<>();
	
	public PlayerPersonActions(Person person) {
		super();
		this.person = person;
	}

	public Person getPerson() {
		return person;
	}
	
	public void addPersonAction(PersonAction personAction, PersonActionArgs personActionArgs) {
		personActions.add(personAction);
		args.add(personActionArgs);
	}

	public PersonActions getPersonActions() {
		return personActions.unmodifiablePersonActions();
	}

	public PersonActionArgs getArgs(int index) {
		return args.get(index);
	}
}
