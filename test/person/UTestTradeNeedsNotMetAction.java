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

import asset.AssetType;
import asset.LocatableAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestTradeNeedsNotMetAction {

	@Test
	public void testPerform() {
		Person person = new Person(18, Sex.MALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.TRADE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.CLAY_POT, 100));
		
		Person targetPerson = new Person(18, Sex.FEMALE);
		targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.WATER, 300));
		
		TradeNeedsNotMetAction tradeNeedsNotMetAction = new TradeNeedsNotMetAction(new MockPersonFinder(targetPerson), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		tradeNeedsNotMetAction.perform(person, AssetType.WATER);
		
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(99, person.getAssets().getQuantityFor(AssetType.CLAY_POT));
		
		assertEquals(200, targetPerson.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(1, targetPerson.getAssets().getQuantityFor(AssetType.CLAY_POT));
	}
	
	@Test
	public void testPerformIrrigationCanal() {
		Person person = new Person(18, Sex.MALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.TRADE);
		person.getAssets().addAsset(new LocatableAsset(AssetType.IRRIGATION_CANAL, new Location(0)));
		
		Person targetPerson = new Person(18, Sex.FEMALE);
		targetPerson.getAssets().addAsset(new SimpleAsset(AssetType.WATER, 300));
		
		TradeNeedsNotMetAction tradeNeedsNotMetAction = new TradeNeedsNotMetAction(new MockPersonFinder(targetPerson), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		tradeNeedsNotMetAction.perform(person, AssetType.WATER);
		
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.IRRIGATION_CANAL));
		
		assertEquals(300, targetPerson.getAssets().getQuantityFor(AssetType.WATER));
		assertEquals(0, targetPerson.getAssets().getQuantityFor(AssetType.IRRIGATION_CANAL));
	}
}
