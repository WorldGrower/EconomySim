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
package society;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import action.TradeArgs;
import asset.AssetType;
import asset.Assets;
import asset.LivingAsset;
import asset.LocatableAsset;
import asset.SimpleAsset;
import environment.Location;
import organization.DecisionCriteriaFactory;
import organization.Organization;
import person.CauseOfDeath;
import person.MockPersonBehaviour;
import person.Person;
import person.PersonDecisions;
import person.PlayerPersonActions;
import person.Sex;

public class UTestSociety {

	@Test
	public void testAddAndFindPerson() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE);
		
		society.addPerson(person);
		
		assertEquals(1, society.size());
		assertEquals(null, society.findPerson(p -> false));
		assertEquals(person, society.findPerson(p -> true));
	}
	
	@Test
	public void testAddPersons() {
		Society society = new Society();
		Person person1 = new Person(18, Sex.FEMALE);
		Person person2 = new Person(18, Sex.MALE);
		
		society.addPersons(Arrays.asList(person1, person2));
		assertEquals(2, society.size());
	}
	
	@Test
	public void testNextTurn() {
		Society society = new Society();
		society.addPerson(new Person(40, Sex.MALE));
		society.nextTurn(5, new PlayerPersonActions(null), PersonDecisions.AI);
		
		assertEquals(0, society.size());
	}
	
	@Test
	public void testGetCash() {
		Society society = new Society();
		assertEquals(0, society.getCash());
		
		society.addAsset(new SimpleAsset(AssetType.CASH, 100));
		assertEquals(100, society.getCash());
	}
	
	@Test
	public void testGetUnclaimedLand() {
		Society society = new Society();
		assertEquals(0, society.getUnclaimedLand());
		
		society.addAsset(new SimpleAsset(AssetType.LAND, 5));
		assertEquals(5, society.getUnclaimedLand());
	}
	
	@Test
	public void testGetPerson() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE);
		society.addPerson(person);
		assertEquals(person, society.getReadOnlyList().get(0));
	}
	
	@Test
	public void testGetAssetType() {
		Society society = new Society();
		society.addAsset(new SimpleAsset(AssetType.LAND, 6));
		assertEquals(AssetType.LAND, society.get(AssetType.LAND).getAssetType());
	}
	
	@Test
	public void testHandleControlledPersonDeath() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE);
		society.addPerson(person);
		society.setControlledPerson(person);
		
		assertEquals(person, society.getControlledPerson());
		
		PlayerPersonActions playerPersonActions = new PlayerPersonActions(person);
		society.nextTurn(1, playerPersonActions, PersonDecisions.AI);
		society.nextTurn(1, playerPersonActions, PersonDecisions.AI);
		society.nextTurn(1, playerPersonActions, PersonDecisions.AI);
		
		assertEquals(0, society.size());
		assertEquals(null, society.getControlledPerson());
	}
	
	@Test
	public void testPunishThief() {
		Society society = new Society();
		Person thief = new Person(18, Sex.FEMALE);
		Person victim = new Person(18, Sex.FEMALE);
		
		TradeArgs tradeArgs = TradeArgs.createTargetArgs(AssetType.FOOD, 100);
		thief.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 300));
		boolean success = true;
		
		society.punishThief(thief, victim, tradeArgs, success);
		
		assertEquals(100, victim.getAssets().getQuantityFor(AssetType.FOOD));
		assertEquals(200, thief.getAssets().getQuantityFor(AssetType.FOOD));
	}
	
	@Test
	public void testPunishThiefOrganization() {
		Society society = new Society();
		Person thief = new Person(18, Sex.FEMALE);
		Person victim = new Person(18, Sex.FEMALE);
		
		TradeArgs tradeArgs = TradeArgs.createTargetArgs(AssetType.FOOD, 100);
		thief.getAssets().addAsset(new SimpleAsset(AssetType.FOOD, 300));
		boolean success = true;
		
		Organization organization = new Organization(victim, DecisionCriteriaFactory.createOligarchy());
		society.setOrganization(organization);
		organization.add(thief);
		organization.add(victim);
		
		society.punishThief(thief, victim, tradeArgs, success);
		
		assertEquals(100, victim.getAssets().getQuantityFor(AssetType.FOOD));
		assertEquals(200, thief.getAssets().getQuantityFor(AssetType.FOOD));
	}
	
	@Test
	public void testThiefKilledDuringEndTurn() {
		Society society = new Society();
		Person thief = new Person(18, Sex.MALE, new MockPersonBehaviour());
		society.addPerson(thief);
		society.addPerson(new Person(18, Sex.FEMALE));
		
		society.nextTurn(1, null, null);
	}
	
	@Test
	public void testFindPersons() {
		Society society = new Society();
		Person person1 = new Person(18, Sex.FEMALE);
		Person person2 = new Person(18, Sex.MALE);
		
		society.addPerson(person1);
		society.addPerson(person2);
		
		List<Person> persons = society.findPersons(p -> p.getAge() > 10, 1);
		assertEquals(1, persons.size());
		assertEquals(true, (person1 == persons.get(0)) || (person2 == persons.get(0)));
		
		persons = society.findPersons(p -> p.getAge() > 10, 2);
		assertEquals(2, persons.size());
		assertEquals(true, persons.contains(person1));
		assertEquals(true, persons.contains(person2));
	}
	
	@Test
	public void testKillPerson() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE);
		society.addPerson(person);
		assertEquals(1, society.size());
		
		person.kill(CauseOfDeath.RETRIBUTION);
		
		society.nextTurn(1, null, null);
		assertEquals(0, society.size());
	}
	
	@Test
	public void testKillPersonOrganizationMember() {
		Society society = new Society();
		Person person = new Person(18, Sex.FEMALE);
		society.addPerson(person);
		society.setOrganization(new Organization(person, DecisionCriteriaFactory.createAutocracy(person)));
		assertEquals(1, society.size());
		
		person.kill(CauseOfDeath.RETRIBUTION);
		
		society.nextTurn(1, null, null);
		assertEquals(0, society.size());
		assertEquals(false, society.findOrganization().contains(person));
	}
	
	@Test
	public void testRetrieveAssetsForNewPerson() {
		Society society = new Society();
		society.addAsset(new SimpleAsset(AssetType.KILN, 1));
		society.addAsset(new LivingAsset(AssetType.DOG));
		society.addAsset(new SimpleAsset(AssetType.RIVER, 1));
		society.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		society.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, new Location(0)));
		
		Assets assets = society.retrieveAssetsForNewPerson();
		assertEquals(1, assets.getQuantityFor(AssetType.KILN));
		assertEquals(1, assets.getQuantityFor(AssetType.DOG));
		assertEquals(0, assets.getQuantityFor(AssetType.RIVER));
		assertEquals(1, assets.getQuantityFor(AssetType.LAND));
		assertEquals(1, assets.getQuantityFor(AssetType.WHEAT_FIELD));
	}
	
	@Test
	public void testAddAsset() {
		Society society = new Society();
		society.addAsset(new LocatableAsset(AssetType.LAND, new Location(0)));
		society.addAsset(new LivingAsset(AssetType.FOOD));
		
		assertEquals(1, society.get(AssetType.LAND).getQuantity());
		assertEquals(null, society.get(AssetType.FOOD));
	}
}
