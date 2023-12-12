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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import environment.PublicLocationsImpl;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestPersonalRelationsBehaviour {

	@Test
	public void testPartnerUp() {
		Person person = new Person(18, Sex.FEMALE);
		Person possiblePartner = new Person(18, Sex.MALE);
		MockPersonFinder personFinder = new MockPersonFinder(possiblePartner);
		
		PersonBehaviour personalRelationsBehaviour = new PersonalRelationsBehaviour(personFinder, new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		
		assertEquals(null, person.getFamily().getPartner());
		
		personalRelationsBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());

		assertEquals(possiblePartner, person.getFamily().getPartner());
	}
	
	@Test
	public void testSex() {
		Person person = new Person(18, Sex.FEMALE);
		Person partner = new Person(18, Sex.MALE);
		
		person.getFamily().setPartner(person, partner);
		
		PersonBehaviour personalRelationsBehaviour = new PersonalRelationsBehaviour(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		assertEquals(false, person.isPregnant());
		
		personalRelationsBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(true, person.isPregnant());
	}
}
