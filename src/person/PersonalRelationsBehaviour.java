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

import java.io.Serializable;

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class PersonalRelationsBehaviour implements PersonBehaviour, Serializable {

	private static final PersonAction[] PERSON_ACTIONS = {
			PersonActionFactory.PARTNER_UP_PERSON_ACTION,
			PersonActionFactory.HAVE_SEX_PERSON_ACTION,
	};
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	public PersonalRelationsBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		for(PersonAction personAction : PERSON_ACTIONS) {
			if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
				Person targetPerson = personAction.getFirstTargetPerson(person, personFinder, publicOrganizations);
				Knowledge knowledge = personAction.getFirstKnowledge(person, targetPerson);
				
				PersonActionArgs args = new PersonActionArgs(targetPerson, knowledge, null, OrganizationArgs.NONE, personDecisions);
				personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
			}
		}
	}
}
