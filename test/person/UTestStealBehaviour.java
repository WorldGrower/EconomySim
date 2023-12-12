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

import org.junit.Test;

import asset.AssetType;
import asset.SimpleAsset;
import environment.PublicLocationsImpl;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestStealBehaviour {

	@Test
	public void testStealNoTarget() {
		Person person = new Person(18, Sex.FEMALE);
		PersonBehaviour stealBehaviour = new StealBehaviour(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		
		stealBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(null, person.getAssets().get(AssetType.FOOD));
	}
	
	@Test
	public void testStealTarget() {
		Person person = new Person(18, Sex.FEMALE, 2, null);
		Person target = new Person(18, Sex.MALE);
		target.getAssets().addAsset(new SimpleAsset(AssetType.WATER, 200));
		MockPersonFinder personFinder = new MockPersonFinder(target);
		PersonBehaviour stealBehaviour = new StealBehaviour(personFinder, new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(200, target.getAssets().getQuantityFor(AssetType.WATER));
		
		person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations());
		person.processTurn(new ArrayList<>(), new MockPublicAssets(), new MockPublicOrganizations());
		stealBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(100, target.getAssets().getQuantityFor(AssetType.WATER));
	}
}
