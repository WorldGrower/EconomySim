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
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestCalendarSystemBehaviour {

	@Test
	public void testCreateCalendarSystem() {
		Person person = new Person(18, Sex.FEMALE);
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		MockPublicKnowledge publicKnowledge = new MockPublicKnowledge();
		PersonBehaviour calendarSystemBehaviour = new CalendarSystemBehaviour(new MockPersonFinder(), publicOrganizations, new MockPublicAssets(), publicKnowledge, new PublicLocationsImpl());
		
		assertEquals(false, person.hasKnowledge(Knowledge.CALENDAR));
		
		while (!publicOrganizations.calendarExists()) {
			calendarSystemBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());
			person.getTimeRemaining().reset(publicOrganizations, true);
		}
		assertEquals(true, publicOrganizations.calendarExists());
		assertEquals(true, person.hasKnowledge(Knowledge.CALENDAR));
		
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.CELESTIAL_OBSERVATION));
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.CALENDAR));
	}
}
