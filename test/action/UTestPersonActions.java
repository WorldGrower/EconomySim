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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import knowledge.Knowledge;
import person.Person;
import person.PersonKnowledgeUtils;
import person.Sex;

public class UTestPersonActions {

	@Test
	public void testFilterOutUnavailablePersonActions() {
		Person person = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.CENTRALIZED_GOVERNMENT);
		
		PersonActions personActions = new PersonActions(
				PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION, 
				PersonActionFactory.PARTNER_UP_PERSON_ACTION,
				PersonActionFactory.TRADE_PERSON_ACTION
		);
		
		personActions = personActions.filterOutUnavailablePersonActions(person);
		
		PersonActions expectedPersonActions = new PersonActions(
				PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION, 
				PersonActionFactory.PARTNER_UP_PERSON_ACTION);
		
		assertEquals(expectedPersonActions, personActions);
	}
	
	@Test
	public void testSortPersonActions() {
		PersonActions personActions = new PersonActions(
				PersonActionFactory.PARTNER_UP_PERSON_ACTION, 
				PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION, 
				PersonActionFactory.ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION
		);
		
		personActions = personActions.sortPersonActions();
		
		assertEquals(3, personActions.size());
		PersonActions expectedPersonActions = new PersonActions(
				PersonActionFactory.ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION, 
				PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION, 
				PersonActionFactory.PARTNER_UP_PERSON_ACTION);
		assertEquals(expectedPersonActions, personActions);
	}
}
