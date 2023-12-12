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
package organization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.LocatableAsset;
import environment.Location;
import person.Person;
import person.Sex;

public class UTestDecisionCriteriaFactory {

	@Test
	public void testAutocracyPersonCanDecide() {
		Person leader = new Person(18, Sex.FEMALE);
		Person person = new Person(18, Sex.MALE);
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createAutocracy(leader);
		
		assertEquals(true, decisionCriteria.personCanDecide(leader));
		assertEquals(false, decisionCriteria.personCanDecide(person));
	}
	
	@Test
	public void testAutocracySuccession() {
		Person leader = new Person(18, Sex.FEMALE);
		Person child1 = new Person(18, Sex.MALE);
		Person child2 = new Person(18, Sex.MALE);
		Person partner = new Person(18, Sex.MALE);
		
		leader.getFamily().setPartner(leader, partner);
		leader.getFamily().addChild(leader, child1);
		leader.getFamily().addChild(leader, child2);
		
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createAutocracy(leader);
		
		assertEquals(true, decisionCriteria.personCanDecide(leader));
		assertEquals(false, decisionCriteria.personCanDecide(child1));
		
		decisionCriteria.onDeath(leader);
		assertEquals(false, decisionCriteria.personCanDecide(leader));
		assertEquals(true, decisionCriteria.personCanDecide(child1));
	}
	
	@Test
	public void testAutocracySuccessionNoChildren() {
		Person leader = new Person(18, Sex.FEMALE);
		Person partner = new Person(18, Sex.MALE);
		
		leader.getFamily().setPartner(leader, partner);
		
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createAutocracy(leader);
		
		assertEquals(true, decisionCriteria.personCanDecide(leader));
		assertEquals(false, decisionCriteria.personCanDecide(partner));
		
		decisionCriteria.onDeath(leader);
		assertEquals(false, decisionCriteria.personCanDecide(leader));
		assertEquals(true, decisionCriteria.personCanDecide(partner));
	}
	
	@Test
	public void testAutocracySuccessionNoFamily() {
		Person leader = new Person(18, Sex.FEMALE);
		Person commoner = new Person(18, Sex.MALE);
		
		
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createAutocracy(leader);
		
		assertEquals(true, decisionCriteria.personCanDecide(leader));
		assertEquals(false, decisionCriteria.personCanDecide(commoner));
		
		decisionCriteria.onDeath(leader);
		assertEquals(false, decisionCriteria.personCanDecide(leader));
		assertEquals(false, decisionCriteria.personCanDecide(commoner));
	}
	
	@Test
	public void testOligarchyPersonCanDecide() {
		Person person1 = new Person(18, Sex.FEMALE);
		person1.getAssets().addAsset(new LocatableAsset(AssetType.LAND, new Location(1)));
		Person person2 = new Person(18, Sex.MALE);
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createOligarchy();
		
		assertEquals(true, decisionCriteria.personCanDecide(person1));
		assertEquals(false, decisionCriteria.personCanDecide(person2));
	}
	
	@Test
	public void testRepublicPersonCanDecide() {
		Person person1 = new Person(18, Sex.FEMALE);
		Person person2 = new Person(14, Sex.MALE);
		Person person3 = new Person(18, Sex.MALE);
		DecisionCriteria decisionCriteria = DecisionCriteriaFactory.createRepublic();
		
		assertEquals(false, decisionCriteria.personCanDecide(person1));
		assertEquals(false, decisionCriteria.personCanDecide(person2));
		assertEquals(true, decisionCriteria.personCanDecide(person3));
	}
}
