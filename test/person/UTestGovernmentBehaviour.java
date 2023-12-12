package person;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import knowledge.Knowledge;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;
import society.PublicKnowledge;
import society.PublicOrganizations;

public class UTestGovernmentBehaviour {

	@Test
	public void testprocessActions() {
		Person person = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.LANGUAGE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.IRRIGATION);
		
		PublicOrganizations publicOrganizations = new MockPublicOrganizations();
		PublicKnowledge publicKnowledge = new MockPublicKnowledge();
		GovernmentBehaviour governmentBehaviour = new GovernmentBehaviour(null, publicOrganizations, null, publicKnowledge, null);
		governmentBehaviour.processActions(person, null, null);
		governmentBehaviour.processActions(person, null, null);
		assertEquals(1, person.getKnowledgePercentageKnown(Knowledge.CENTRALIZED_GOVERNMENT));
	}
}
