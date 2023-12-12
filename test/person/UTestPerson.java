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
import java.util.List;

import org.junit.Test;

import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import action.TradeArgs;
import asset.AssetType;
import asset.MockAssetContainer;
import asset.SimpleAsset;
import environment.PublicLocationsImpl;
import knowledge.InformationSource;
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;
import society.Society;

public class UTestPerson {

	@Test
	public void testProcessTurnPregnancyFemale() {
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl()));
		Person partner = new Person(18, Sex.MALE);
		person.getFamily().setPartner(person, partner);
		assertEquals(false, person.isPregnant());

		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(true, person.isPregnant());
		
		List<Person> personList = new ArrayList<>();
		for (int i=0; i<8; i++) { person.processTurn(personList, new MockPublicAssets(), new MockPublicOrganizations()); }
		
		assertEquals(false, person.isPregnant());
		assertEquals(1, personList.size());
	}
	
	@Test
	public void testProcessTurnPregnancyMale() {
		Person person = new Person(18, Sex.MALE);
		assertEquals(false, person.isPregnant());
		
		person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations());
		assertEquals(false, person.isPregnant());
	}
	
	@Test
	public void testProcessTurnPregnancyMinor() {
		Person person = new Person(5, Sex.FEMALE);
		assertEquals(false, person.isPregnant());
		
		person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations());
		assertEquals(false, person.isPregnant());
	}
	
	@Test
	public void testProcessTurnPartner() {
		Person partner = new Person(18, Sex.MALE);
		MockPersonFinder personFinder = new MockPersonFinder(partner);
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(personFinder, new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl()));
		assertEquals(null, person.getFamily().getPartner());
		
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(partner, person.getFamily().getPartner());
		
		Person partner2 = new Person(18, Sex.MALE);
		personFinder.setFoundPerson(partner2);
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(partner, person.getFamily().getPartner());
	}
	
	@Test
	public void testProcessTurnPartnerInSociety() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(society, society, society, society, new PublicLocationsImpl()));
		assertEquals(null, person.getFamily().getPartner());
		
		Person partner = new Person(18, Sex.MALE);
		
		society.addPerson(new Person(10, Sex.MALE));
		society.addPerson(new Person(18, Sex.FEMALE));
		Person partneredMale = new Person(18, Sex.MALE);
		partneredMale.getFamily().setPartner(partneredMale, new Person(18, Sex.FEMALE));
		society.addPerson(partneredMale);
		society.addPerson(partner);
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(partner, person.getFamily().getPartner());
	}
	
	@Test
	public void testProcessTurnPartnerMinor() {
		Person partner = new Person(18, Sex.MALE);
		Person person = new Person(5, Sex.FEMALE, new PersonBehaviourImpl(new MockPersonFinder(partner), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl()));
		assertEquals(null, person.getFamily().getPartner());
		
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations());
		assertEquals(null, person.getFamily().getPartner());
	}
	
	@Test
	public void testProcessTurnOldAge() {
		Person person = new Person(100, Sex.FEMALE);
		assertEquals(CauseOfDeath.OLD_AGE, person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations()));
	}
	
	@Test
	public void testProcessActionsWork() {
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl()));
		for(int i=0; i<20; i++) { person.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, new MockPublicKnowledge()); }
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 6));
		person.getAssets().addAsset(new SimpleAsset(AssetType.RIVER, 1));
		
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(6, person.getAssets().getQuantityFor(AssetType.LAND));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.RIVER));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.STONE_SICKLE));
	}
	
	@Test
	public void testProcessWorkUsingTool() {
		SimpleAsset riverAsset = new SimpleAsset(AssetType.RIVER, 1);
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(new MockPersonFinder(), new MockPublicOrganizations(), publicAssets, new MockPublicKnowledge(), new PublicLocationsImpl()));
		for(int i=0; i<20; i++) { person.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, new MockPublicKnowledge()); }
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 6));
		person.getAssets().addAsset(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(6, person.getAssets().getQuantityFor(AssetType.LAND));
		assertEquals(1, publicAssets.getQuantityFor(AssetType.RIVER));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.STONE_SICKLE));
		assertEquals("tool: 19 / 20", person.getAssets().get(AssetType.STONE_SICKLE).getRemainingProduceDescription());
	}
	
	@Test
	public void testProcessWorkUsingToolMultiple() {
		SimpleAsset riverAsset = new SimpleAsset(AssetType.RIVER, 1000);
		MockPublicAssets publicAssets = new MockPublicAssets(riverAsset);
		Person person = new Person(18, Sex.FEMALE, new PersonBehaviourImpl(new MockPersonFinder(), new MockPublicOrganizations(), publicAssets, new MockPublicKnowledge(), new PublicLocationsImpl()));
		for(int i=0; i<20; i++) { person.increaseKnowledge(Knowledge.STONE_TOOLING, InformationSource.PRACTICE, new MockPublicKnowledge()); }
		person.getAssets().addAsset(new SimpleAsset(AssetType.LAND, 100));
		person.getAssets().addAsset(new SimpleAsset(AssetType.STONE_SICKLE, 1));
		person.getAssets().addAsset(new SimpleAsset(AssetType.CLAY_POT, 10));
		
		while(person.getAssets().getQuantityFor(AssetType.STONE_SICKLE) > 0) {
			person.processActions(PersonDecisions.AI, new ProfessionStatistics());
			person.processTurn(new ArrayList<>(), publicAssets, new MockPublicOrganizations());
		}
		assertEquals(null, person.getAssets().get(AssetType.STONE_SICKLE));
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.STONE_SICKLE));
		
		person.processActions(PersonDecisions.AI, new ProfessionStatistics());
		person.processTurn(new ArrayList<>(), publicAssets, new MockPublicOrganizations());
		assertEquals("tool: 19 / 20", person.getAssets().get(AssetType.STONE_SICKLE).getRemainingProduceDescription());
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.STONE_SICKLE));
	}
	
	@Test
	public void testOnDeath() {
		Person person = new Person(18, Sex.FEMALE);
		person.increaseCash(100);
		MockAssetContainer cashContainer = new MockAssetContainer();
		person.onDeath(cashContainer);
		assertEquals(100, cashContainer.getCash());
	}
	
	@Test
	public void testOnDeathPartnerAndChildren() {
		Person person = new Person(18, Sex.FEMALE);
		person.increaseCash(101);
		Person partner = new Person(18, Sex.MALE);
		
		Person child1 = new Person(0, Sex.MALE);
		Person child2 = new Person(0, Sex.MALE);
		
		person.getFamily().setPartner(person, partner);
		person.getFamily().addChild(person, child1);
		person.getFamily().addChild(person, child2);
		
		MockAssetContainer cashContainer = new MockAssetContainer();
		person.onDeath(cashContainer);
		assertEquals(33, partner.getCash());
		assertEquals(33, child1.getCash());
		assertEquals(35, child2.getCash());
		assertEquals(0, cashContainer.getCash());
	}
	
	@Test
	public void testLightSourceIncreasesWorkingDay() {
		Person person = new Person(18, Sex.FEMALE);
		assertEquals(12, person.getTimeRemaining().get());
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.CAMP_FIRE, 1));
		assertEquals(18, person.getTimeRemaining().get());
	}
	
	@Test
	public void testImpregnate() {
		PersonAction personAction = PersonActionFactory.HAVE_SEX_PERSON_ACTION;
		Person person = new Person(18, Sex.FEMALE);
		Person partner = new Person(18, Sex.MALE);
		person.getFamily().setPartner(person, partner);
		assertEquals(false, person.isPregnant());
		
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations());
		assertEquals(true, person.isPregnant());
		assertEquals(false, partner.isPregnant());
	}
	
	@Test
	public void testImpregnateOther() {
		PersonAction personAction = PersonActionFactory.HAVE_SEX_PERSON_ACTION;
		Person person = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.FEMALE);
		person.getFamily().setPartner(person, partner);
		assertEquals(false, person.isPregnant());
		assertEquals(false, partner.isPregnant());
		
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, new MockPublicAssets(), new MockPublicKnowledge(), new MockPublicOrganizations());
		assertEquals(false, person.isPregnant());
		assertEquals(true, partner.isPregnant());
	}
	
	@Test
	public void testStealFrom() {
		Person person = new Person(18, Sex.MALE, 0, PersonBehaviour.NONE);
		Person target = new Person(18, Sex.FEMALE);
		target.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 100));
		
		TradeArgs tradeArgs = TradeArgs.createTargetArgs(AssetType.FOOD, 40);
		person.stealFrom(target, tradeArgs, new MockPublicOrganizations());

		assertEquals(40, person.getAssets().getQuantityFor(AssetType.FOOD));
		assertEquals(60, target.getAssets().getQuantityFor(AssetType.FOOD));
	}
	
	@Test
	public void testGetMaxQuantity() {
		Person person = new Person(18, Sex.MALE);
		assertEquals(150, person.getMaxQuantity());
		
		person.getAssets().addAsset(new SimpleAsset(AssetType.ACCOUNTING_SYSTEM, 1));
		assertEquals(300, person.getMaxQuantity());
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.WRITING);
		assertEquals(Integer.MAX_VALUE, person.getMaxQuantity());
	}
}
