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

import java.util.List;

import knowledge.Knowledge;
import person.Person;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

public class MockPersonAction extends PersonAction {

	private final List<Person> possibleTargetPersons;
	private final List<Knowledge> possibleKnowledge;
	private final Knowledge requiredKnowledge;
	
	public MockPersonAction(List<Person> possibleTargetPersons, List<Knowledge> possibleKnowledge, Knowledge requiredKnowledge) {
		super();
		this.possibleTargetPersons = possibleTargetPersons;
		this.possibleKnowledge = possibleKnowledge;
		this.requiredKnowledge = requiredKnowledge;
	}

	@Override
	public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
	}

	@Override
	protected boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		return true;
	}

	@Override
	public int getTimeRequired() {
		return 0;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
		return possibleTargetPersons;
	}

	@Override
	public List<Knowledge> getPossibleKnowledge(Person person, Person targetPerson) {
		return possibleKnowledge;
	}

	@Override
	public Knowledge getRequiredKnowledge() {
		return requiredKnowledge;
	}	
	
	
}
