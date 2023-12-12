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
import organization.MockSocialObligation;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestSocialObligationsBehaviour {

	@Test
	public void testFullfillSocialObligation() {
		Person person = new Person(18, Sex.FEMALE);
		MockSocialObligation socialObligation = new MockSocialObligation(true, false);
		person.getSocialObligations().add(socialObligation);
		PersonBehaviour personalRelationsBehaviour = new SocialObligationsBehaviour(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		
		assertEquals(false, socialObligation.isDone());
		
		personalRelationsBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());

		assertEquals(true, socialObligation.isDone());
	}
}
