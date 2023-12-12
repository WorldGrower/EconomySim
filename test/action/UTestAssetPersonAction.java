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

import org.junit.Test;

import asset.Asset;
import asset.AssetType;
import asset.LocatableAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.MockLightSourceEnvironment;
import environment.MockPublicLocations;
import environment.TimeRemaining;
import person.Person;
import person.Sex;
import society.MockPublicAssets;

public class UTestAssetPersonAction {

	@Test
	public void testCanPerform() {
		Asset asset = new LocatableAsset(AssetType.LAND, new Location(0));
		PersonAction personAction = createPersonAction(asset);
		
		Person person = new Person(18, Sex.MALE);
		assertEquals(true, personAction.canPerform(person, getTimeRemaining(), null, null));
	}

	private TimeRemaining getTimeRemaining() {
		return new TimeRemaining(new MockLightSourceEnvironment());
	}
	
	@Test
	public void testPerform() {
		Asset asset = new LocatableAsset(AssetType.LAND, new Location(0));
		PersonAction personAction = createPersonAction(asset);
		
		Person person = new Person(18, Sex.MALE);
		
		assertEquals(true, asset.hasRemainingProduce(AssetType.FOOD));		
		personAction.perform(person, getTimeRemaining(), null, null, null, null);
		assertEquals(false, asset.hasRemainingProduce(AssetType.FOOD));
	}

	private PersonAction createPersonAction(Asset asset) {
		Activity activity = Activity.GATHER_FOOD;
		return PersonActionFactory.createAssetPersonAction(asset, activity, new MockPublicAssets(), new MockPublicLocations());
	}
	
	@Test
	public void testPerformToolUsage() {
		Asset asset = new LocatableAsset(AssetType.LAND, new Location(0));
		PersonAction personAction = createPersonAction(asset);
		
		Person person = new Person(18, Sex.MALE);
		SimpleAsset stoneSickle = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		person.getAssets().addAsset(stoneSickle);
		
		assertEquals("tool: 20 / 20", stoneSickle.getRemainingProduceDescription());		
		personAction.perform(person, getTimeRemaining(), null, null, null, null);
		assertEquals("tool: 19 / 20", stoneSickle.getRemainingProduceDescription());
	}
	
	@Test
	public void testPerformToolUsageLastTool() {
		Asset asset = new LocatableAsset(AssetType.LAND, new Location(0));
		PersonAction personAction = createPersonAction(asset);
		
		Person person = new Person(18, Sex.MALE);
		SimpleAsset stoneSickle = new SimpleAsset(AssetType.STONE_SICKLE, 1);
		person.getAssets().addAsset(stoneSickle);
		
		assertEquals("tool: 20 / 20", stoneSickle.getRemainingProduceDescription());
		for(int i=0; i<30; i++) {
			asset.regenerate();
			personAction.perform(person, getTimeRemaining(), null, null, null, null);
		}
		assertEquals("tool: 0 / 20", stoneSickle.getRemainingProduceDescription());
	}
}
