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

import asset.AssetType;
import asset.SimpleAsset;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import organization.DecisionCriteria;
import organization.DecisionCriteriaFactory;
import organization.Organization;
import organization.OrganizationPolicy;
import person.MockPersonDecisions;
import person.Person;
import person.PersonDecisions;
import person.PersonKnowledgeUtils;
import person.Sex;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;
import society.PublicOrganizations;
import society.Society;
import society.TheftPunishment;

public class UTestPersonActionFactory {

	@Test
	public void testCreatePersonActions() {
		Person person = new Person(18, Sex.FEMALE);
		PersonActions personActions =  PersonActionFactory.createSortedPersonActions(person, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(true, personActions.size() > 0);
	}
	
	@Test
	public void testCreatePersonActionsForLandAsset() {
		Person person = new Person(18, Sex.FEMALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 1));
		PersonActions personActions =  PersonActionFactory.createSortedPersonActions(person, new MockPublicAssets(), new PublicLocationsImpl());
		
		assertEquals(true, personActions.size() > 0);
	}
	
	@Test
	public void testLearnKnowledge() {
		Person person = new Person(18, Sex.FEMALE);
		PersonActionArgs args = new PersonActionArgs(null, Knowledge.STONE_TOOLING, null, OrganizationArgs.NONE, PersonDecisions.AI);
		PersonActionFactory.LEARN_KNOWLEDGE_PERSON_ACTION.perform(person, person.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations());
		
		assertEquals("1 / 16", person.getKnowledgeDescription().get(Knowledge.STONE_TOOLING));
	}
	
	@Test
	public void testShareKnowledge() {
		Person person = new Person(18, Sex.FEMALE);
		Person targetPerson = new Person(18, Sex.MALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.STONE_TOOLING);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.LANGUAGE);
		PersonKnowledgeUtils.learnKnowledge(targetPerson, Knowledge.LANGUAGE);
		PersonActionArgs args = new PersonActionArgs(targetPerson, Knowledge.STONE_TOOLING, null, OrganizationArgs.NONE, PersonDecisions.AI);
		PersonAction personAction = PersonActionFactory.SHARE_KNOWLEDGE_PERSON_ACTION;
		personAction.perform(person, person.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations());
		
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		assertEquals("16 / 16", person.getKnowledgeDescription().get(Knowledge.STONE_TOOLING));
		assertEquals("4 / 16", targetPerson.getKnowledgeDescription().get(Knowledge.STONE_TOOLING));
	}
	
	@Test
	public void testCanShareKnowledge() {
		Person person = new Person(18, Sex.FEMALE);
		PersonAction personAction = PersonActionFactory.SHARE_KNOWLEDGE_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.LANGUAGE);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
	}
	
	@Test
	public void testShareKnowledgeGetPossibleKnowledge() {
		Person person = new Person(18, Sex.FEMALE);
		Person targetPerson = new Person(18, Sex.MALE);
		PersonAction personAction = PersonActionFactory.SHARE_KNOWLEDGE_PERSON_ACTION;
		assertEquals(new ArrayList<>(), personAction.getPossibleKnowledge(person, targetPerson));
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.FARMING);
		
		assertEquals(Arrays.asList(Knowledge.FARMING), personAction.getPossibleKnowledge(person, targetPerson));
		
		PersonKnowledgeUtils.learnKnowledge(targetPerson, Knowledge.FARMING);
		assertEquals(new ArrayList<>(), personAction.getPossibleKnowledge(person, targetPerson));
	}
	
	@Test
	public void testShareKnowledgeGetPossibleTargetPersons() {
		Person person = new Person(18, Sex.FEMALE);
		Person targetPerson = new Person(18, Sex.MALE);
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		Society society = new Society();
		PersonAction personAction = PersonActionFactory.SHARE_KNOWLEDGE_PERSON_ACTION;
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
		
		society.addPerson(person);
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
		
		society.addPerson(targetPerson);
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
	
		PersonKnowledgeUtils.learnKnowledge(targetPerson, Knowledge.LANGUAGE);
		assertEquals(Arrays.asList(targetPerson), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
	}
	
	@Test
	public void testCreateOrganization() {
		Person person = new Person(18, Sex.FEMALE);
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		PersonAction personAction = PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.CENTRALIZED_GOVERNMENT);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, new MockPublicAssets(), new MockPublicKnowledge(), publicOrganizations);
		assertEquals(false, PersonActionFactory.SHARE_KNOWLEDGE_PERSON_ACTION.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	}
	
	@Test
	public void testAskOtherJoinOrganization() {
		Person person = new Person(18, Sex.FEMALE);
		Person targetPerson = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(person, DecisionCriteriaFactory.createOligarchy());
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
	
		PersonAction personAction = PersonActionFactory.ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.LANGUAGE);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		
		assertEquals(false, organization.contains(targetPerson));
		personAction.perform(person, person.getTimeRemaining(), new PersonActionArgs(targetPerson, null, null, OrganizationArgs.NONE, PersonDecisions.AI), new MockPublicAssets(), new MockPublicKnowledge(), publicOrganizations);
		assertEquals(true, organization.contains(targetPerson));
	}
	
	@Test
	public void testJoinOrganization() {
		Person person = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(new Person(18, Sex.MALE), DecisionCriteriaFactory.createOligarchy());
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
	
		PersonAction personAction = PersonActionFactory.JOIN_ORGANIZATION_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.LANGUAGE);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		
		assertEquals(false, organization.contains(person));
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, new MockPublicAssets(), new MockPublicKnowledge(), publicOrganizations);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		assertEquals(true, organization.contains(person));
	}
	
	@Test
	public void testTrade() {
		Person person = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(new Person(18, Sex.MALE), DecisionCriteriaFactory.createOligarchy());
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
	
		PersonAction personAction = PersonActionFactory.TRADE_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.TRADE);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	}
	
	@Test
	public void testTradeGetPossibleTargetPersons() {
		Person person = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(new Person(18, Sex.MALE), DecisionCriteriaFactory.createOligarchy());
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
	
		PersonAction personAction = PersonActionFactory.TRADE_PERSON_ACTION;
		Society society = new Society();
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
	
		society.addPerson(person);
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
		
		Person targetPerson = new Person(18, Sex.FEMALE);
		society.addPerson(targetPerson);
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
		
		PersonKnowledgeUtils.learnKnowledge(targetPerson, Knowledge.LANGUAGE);
		assertEquals(Arrays.asList(targetPerson), personAction.getPossibleTargetPersons(person, society, publicOrganizations, 1));
	}
	
	@Test
	public void testStealGetPossibleTargetPersons() {
		Person person = new Person(18, Sex.FEMALE);
		Person targetPerson = new Person(18, Sex.MALE);
		PersonAction personAction = PersonActionFactory.STEAL_PERSON_ACTION;
		Society society = new Society();
		assertEquals(new ArrayList<>(), personAction.getPossibleTargetPersons(person, society, new MockPublicOrganizations(), 1));
	
		society.addPerson(person);
		society.addPerson(targetPerson);
		assertEquals(Arrays.asList(targetPerson), personAction.getPossibleTargetPersons(person, society, new MockPublicOrganizations(), 1));
	}
	
	@Test
	public void testChangePolicyCanPerform() {
		Person leader = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(leader, Knowledge.CENTRALIZED_GOVERNMENT);
		Person commoner = new Person(18, Sex.MALE);
		Organization organization = new Organization(new Person(18, Sex.MALE), DecisionCriteriaFactory.createAutocracy(leader));
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
	
		PersonAction personAction = PersonActionFactory.CHANGE_POLICY_PERSON_ACTION;
		assertEquals(false, personAction.canPerform(commoner, commoner.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		assertEquals(true, personAction.canPerform(leader, leader.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));		
	}
	
	@Test
	public void testChangePolicyPerformAutocracy() {
		Person leader = new Person(18, Sex.FEMALE);
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createAutocracy(leader);
		Organization organization = new Organization(new Person(18, Sex.MALE), decisionCriteria);
		OrganizationArgs organizationArgs = new OrganizationArgs(decisionCriteria, new OrganizationPolicy(TheftPunishment.DEATH, null, null, 0));
		PersonActionArgs args = new PersonActionArgs(null, null, null, organizationArgs, new MockPersonDecisions());
		
		assertEquals(TheftPunishment.DEATH_FOR_KNOWN_THIEF, organization.getOrganizationPolicy().getTheftPunishment());
		
		PersonAction personAction = PersonActionFactory.CHANGE_POLICY_PERSON_ACTION;
		personAction.perform(leader, leader.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations(organization));
		
		assertEquals(TheftPunishment.DEATH, organization.getOrganizationPolicy().getTheftPunishment());
	}
	
	@Test
	public void testChangePolicyPerformRepublic() {
		Person leader = new Person(18, Sex.FEMALE);
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createRepublic();
		Organization organization = new Organization(new Person(18, Sex.MALE), decisionCriteria);
		MockPersonDecisions personBehaviour = new MockPersonDecisions();
		
		OrganizationArgs organizationArgs = new OrganizationArgs(decisionCriteria, new OrganizationPolicy(TheftPunishment.DEATH, null, null, 0));
		PersonActionArgs args = new PersonActionArgs(null, null, null, organizationArgs, personBehaviour);
		
		assertEquals(TheftPunishment.DEATH_FOR_KNOWN_THIEF, organization.getOrganizationPolicy().getTheftPunishment());
		
		PersonAction personAction = PersonActionFactory.CHANGE_POLICY_PERSON_ACTION;
		personAction.perform(leader, leader.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations(organization));
		
		assertEquals(TheftPunishment.DEATH, organization.getOrganizationPolicy().getTheftPunishment());
		
		organizationArgs = new OrganizationArgs(decisionCriteria, new OrganizationPolicy(TheftPunishment.DEATH_FOR_KNOWN_THIEF, null, null, 0));
		args = new PersonActionArgs(null, null, null, organizationArgs, personBehaviour);
		
		personBehaviour.setApprovePolicyChange(false);
		personAction.perform(leader, leader.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations(organization));
		assertEquals(TheftPunishment.DEATH, organization.getOrganizationPolicy().getTheftPunishment());		
	}
	
	@Test
	public void testHireCanPerform() {
		PersonAction personAction = PersonActionFactory.HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION;
		Person leader = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(leader, Knowledge.DIVISION_OF_LABOR);
		Person commoner = new Person(18, Sex.FEMALE);
		PublicOrganizations publicOrganizations = new MockPublicOrganizations();
		assertEquals(false, personAction.canPerform(leader, leader.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		
		Organization organization = new Organization(leader, DecisionCriteriaFactory.createAutocracy(leader));
		publicOrganizations = new MockPublicOrganizations(organization);
		assertEquals(true, personAction.canPerform(leader, leader.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
		assertEquals(false, personAction.canPerform(commoner, commoner.getTimeRemaining(), new MockPublicAssets(), publicOrganizations));
	}
	
	@Test
	public void testHirePerform() {
		PersonAction personAction = PersonActionFactory.HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION;
		Person leader = new Person(18, Sex.FEMALE);
		Person commoner = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(leader, DecisionCriteriaFactory.createAutocracy(leader));
		PublicOrganizations publicOrganizations = new MockPublicOrganizations(organization);
		
		PersonActionArgs args = new PersonActionArgs(commoner, null, null, OrganizationArgs.NONE, PersonDecisions.AI);
		personAction.perform(leader, leader.getTimeRemaining(), args, new MockPublicAssets(), new MockPublicKnowledge(), publicOrganizations);
	
		assertEquals(false, organization.isEmployee(leader));
		assertEquals(true, organization.isEmployee(commoner));
	}
}
