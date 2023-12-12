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

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import asset.AssetType;
import asset.SimpleAsset;
import environment.MockPublicLocations;
import person.Person;
import person.Sex;
import society.MockPublicAssets;

public class UTestAssetPersonActions {

	@Test
	public void testCreateAssetPersonActionsNoDuplicates() {
		Person person = new Person(18, Sex.FEMALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.WHEAT, 1));
		person.getAssets().addAsset(new SimpleAsset(AssetType.FLOUR, 1));
		person.getAssets().addAsset(new SimpleAsset(AssetType.BREAD, 10));
		MockPublicAssets publicAssets = new MockPublicAssets(new SimpleAsset(AssetType.WHEAT, 1));
		PersonActions personActions = AssetPersonActions.createAssetPersonActions(person, publicAssets, new MockPublicLocations());
		
		List<String> personActionNames = personActions.getNames();
	
		for(String personActionName : personActionNames) {
			if (Collections.frequency(personActionNames, personActionName) > 1) {
				throw new IllegalStateException("personActionName has duplicate " + personActionName);
			}
		}
	}
}
