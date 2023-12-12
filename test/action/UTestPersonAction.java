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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import environment.MockLightSourceEnvironment;
import environment.TimeRemaining;
import knowledge.Knowledge;
import person.Person;
import person.PersonKnowledgeUtils;
import person.Sex;
import society.MockPublicKnowledge;
import society.PublicKnowledge;

public class UTestPersonAction {

	@Test
	public void testGetFirstActions() {
		PersonAction personAction = new MockPersonAction(new ArrayList<>(), new ArrayList<>(), null);
		assertEquals(null, personAction.getFirstTargetPerson(null, null, null));
		assertEquals(null, personAction.getFirstKnowledge(null, null));
		
		Person person = new Person(18, Sex.FEMALE);
		Knowledge knowledge = Knowledge.STONE_TOOLING;
		personAction = new MockPersonAction(Arrays.asList(person), Arrays.asList(knowledge), null);
		assertEquals(person, personAction.getFirstTargetPerson(null, null, null));
		assertEquals(knowledge, personAction.getFirstKnowledge(null, null));
	}
	
	@Test
	public void testGetRequiredKnowledge() {
		PersonAction personAction = new MockPersonAction(null, null, Knowledge.LANGUAGE);
		TimeRemaining timeRemaining = new TimeRemaining(new MockLightSourceEnvironment());
		
		Person person1 = new Person(18, Sex.FEMALE);
		assertEquals(false, personAction.canPerform(person1, timeRemaining, null, null));
		
		Person person2 = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(person2, Knowledge.LANGUAGE);
		assertEquals(true, personAction.canPerform(person2, timeRemaining, null, null));
		
		PublicKnowledge publicKnowledge = new MockPublicKnowledge();
		assertEquals(false, publicKnowledge.canObserveKnowledge(Knowledge.LANGUAGE));
		personAction.perform(person2, timeRemaining, PersonActionArgs.NONE, null, publicKnowledge, null);
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.LANGUAGE));
	}
}
