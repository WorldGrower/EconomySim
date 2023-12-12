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
import asset.OperationalAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestWheatFarmingBehaviour {

	@Test
	public void testperformWheatFarming() {
		Person person = new Person(18, Sex.FEMALE);
		person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, new Location(0), new Location(1)));
		MockPublicOrganizations publicOrganizations = new MockPublicOrganizations();
		MockPublicKnowledge publicKnowledge = new MockPublicKnowledge();
		MockPublicAssets publicAssets = new MockPublicAssets();
		publicAssets.addAsset(new SimpleAsset(AssetType.RIVER, 1000));
		PersonBehaviour wheatFarmingBehaviour = new WheatFarmingBehaviour(new MockPersonFinder(), publicOrganizations, publicAssets, publicKnowledge, new PublicLocationsImpl());
		
		assertEquals(false, person.hasKnowledge(Knowledge.PLOUGHING));
		
		while (person.getAssets().getQuantityFor(AssetType.PLOUGH) == 0) {
			performWheatFarming(person, publicOrganizations, wheatFarmingBehaviour);
		}
		assertEquals(125, person.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.BREAD));
		assertEquals(2, person.getAssets().getQuantityFor(AssetType.WHEAT_FIELD));
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.IRRIGATION_CANAL));
		assertEquals(1, publicAssets.getQuantityFor(AssetType.IRRIGATION_CANAL));
		
		OperationalAsset irrigationCanal = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
		assertEquals(false, irrigationCanal.getOperationalAssetAttribute(0).isFinished());
		assertEquals(false, irrigationCanal.getOperationalAssetAttribute(0).isOperational());
		assertEquals(1, person.getAssets().getQuantityFor(AssetType.PLOUGH));
		
		performWheatFarming(person, publicOrganizations, wheatFarmingBehaviour);
		assertEquals(125, person.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.BREAD));
		assertEquals(2, person.getAssets().getQuantityFor(AssetType.WHEAT_FIELD));
		
		performWheatFarming(person, publicOrganizations, wheatFarmingBehaviour);
		assertEquals(125, person.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.FLOUR));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.BREAD));
		assertEquals(2, person.getAssets().getQuantityFor(AssetType.WHEAT_FIELD));
		
		assertEquals(true, publicKnowledge.canObserveKnowledge(Knowledge.IRRIGATION));
	
		while (!irrigationCanal.getOperationalAssetAttribute(0).isFinished()) {
			performWheatFarming(person, publicOrganizations, wheatFarmingBehaviour);
		}
		assertEquals(true, irrigationCanal.getOperationalAssetAttribute(0).isFinished());
		assertEquals(true, irrigationCanal.getOperationalAssetAttribute(0).isOperational());
		
		LocatableAsset wheatFields = (LocatableAsset) person.getAssets().get(AssetType.WHEAT_FIELD);
		assertEquals(150, wheatFields.getCapacity(0));
		assertEquals(150, wheatFields.getCapacity(1));
		
		performWheatFarming(person, publicOrganizations, wheatFarmingBehaviour);
		assertEquals(200, wheatFields.getCapacity(0));
		assertEquals(200, wheatFields.getCapacity(1));
	}

	private void performWheatFarming(Person person, MockPublicOrganizations publicOrganizations, PersonBehaviour wheatFarmingBehaviour) {
		wheatFarmingBehaviour.processActions(person, PersonDecisions.AI, new ProfessionStatistics());
		person.getTimeRemaining().reset(publicOrganizations, true);
		person.getAssets().regenerate();
	}
}
