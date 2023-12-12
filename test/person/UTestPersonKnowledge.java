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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import knowledge.InformationSource;
import knowledge.Knowledge;
import society.MockPublicKnowledge;
import society.Society;

public class UTestPersonKnowledge {

	@Test
	public void testHasKnowledge() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(false, personKnowledge.hasKnowledge(Knowledge.STONE_TOOLING));
		
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.STONE_TOOLING);
		assertEquals(true, personKnowledge.hasKnowledge(Knowledge.STONE_TOOLING));
	}
	
	@Test
	public void testGetLearnableKnowledge() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(Arrays.asList(Knowledge.STONE_TOOLING, Knowledge.CONTROL_OF_FIRE, Knowledge.LANGUAGE, Knowledge.CLOTHING, Knowledge.CELESTIAL_OBSERVATION, Knowledge.DOMESTICATION_OF_DOG), personKnowledge.getLearnableKnowledge());
	
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.STONE_TOOLING);
		assertEquals(Arrays.asList(Knowledge.CONTROL_OF_FIRE, Knowledge.LANGUAGE, Knowledge.CLOTHING, Knowledge.CELESTIAL_OBSERVATION, Knowledge.DOMESTICATION_OF_DOG), personKnowledge.getLearnableKnowledge());
	}
	
	@Test
	public void testGetLearnableKnowledgePrerequisite() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(false, personKnowledge.getLearnableKnowledge().contains(Knowledge.POTTERY));
		
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.CONTROL_OF_FIRE);
		assertEquals(true, personKnowledge.getLearnableKnowledge().contains(Knowledge.POTTERY));
	}
	
	@Test
	public void testGetLearnableKnowledgeMultiplePrerequisite() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(false, personKnowledge.getLearnableKnowledge().contains(Knowledge.ARITHMETIC));
		
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.CALENDAR);
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.PROTO_WRITING);
		assertEquals(true, personKnowledge.getLearnableKnowledge().contains(Knowledge.ARITHMETIC));
	}
	
	@Test
	public void testGetLearnableKnowledgeInProgress() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(null, personKnowledge.getLearnableKnowledgeInProgress());
	
		personKnowledge.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, new MockPublicKnowledge());
		assertEquals(Knowledge.STONE_TOOLING, personKnowledge.getLearnableKnowledgeInProgress());
		
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.STONE_TOOLING);
		assertEquals(null, personKnowledge.getLearnableKnowledgeInProgress());
		
		for(Knowledge knowledge : Knowledge.VALUES) { PersonKnowledgeUtils.learnKnowledge(personKnowledge, knowledge); }
		assertEquals(null, personKnowledge.getLearnableKnowledgeInProgress());
	}
	
	@Test
	public void testPublicKnowledge() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		Society society = new Society();
		society.addKnowledge(Knowledge.STONE_TOOLING);
		
		assertEquals(true, society.canObserveKnowledge(Knowledge.STONE_TOOLING));
		assertEquals(false, personKnowledge.hasKnowledge(Knowledge.STONE_TOOLING));
		
		for(int i=0; i<10; i++) { personKnowledge.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, society); }
		assertEquals(true, personKnowledge.hasKnowledge(Knowledge.STONE_TOOLING));
	}
	
	@Test
	public void testGetKnownKnowledge() {
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(new ArrayList<>(), personKnowledge.getKnownKnowledge());
		
		personKnowledge.increaseKnowledge(Knowledge.LANGUAGE, InformationSource.PRACTICE, new MockPublicKnowledge());
		PersonKnowledgeUtils.learnKnowledge(personKnowledge, Knowledge.STONE_TOOLING);
		assertEquals(Arrays.asList(Knowledge.STONE_TOOLING), personKnowledge.getKnownKnowledge());
	}
	
	@Test
	public void testGetKnowledgePercentageKnown() {
		Society society = new Society();
		PersonKnowledge personKnowledge = new PersonKnowledge();
		assertEquals(0, personKnowledge.getKnowledgePercentageKnown(Knowledge.STONE_TOOLING));
		
		for(int i=0; i<5; i++) { personKnowledge.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, society); }
		assertEquals(31, personKnowledge.getKnowledgePercentageKnown(Knowledge.STONE_TOOLING));
		
		for(int i=0; i<15; i++) { personKnowledge.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, society); }
		assertEquals(100, personKnowledge.getKnowledgePercentageKnown(Knowledge.STONE_TOOLING));
	}
}
