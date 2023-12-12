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
package main;

import asset.AssetType;
import asset.LocatableAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.PublicLocations;
import environment.PublicLocationsImpl;
import person.Person;
import person.PersonBehaviour;
import person.PersonBehaviourImpl;
import person.Sex;
import society.Society;

public class SocietyFactory {

	public static Society createDefaultInstance() {
		int startingNumberOfPeople = 100;
		int locationIndex = 0;
		
		PublicLocations publicLocations = new PublicLocationsImpl();
		Society society = new Society(publicLocations);
		PersonBehaviour personBehaviour = new PersonBehaviourImpl(society, publicLocations);
		
		for(int i=0; i<startingNumberOfPeople; i++) {
			int cash = 100;
			int age = 18;
			Sex sex = (i % 2 == 0) ? Sex.MALE : Sex.FEMALE;
			Person person = new Person(age, sex, i, personBehaviour);
			person.increaseCash(cash);
			Location[] locations = {
					new Location(locationIndex++), 
					new Location(locationIndex++),
					new Location(locationIndex++),
					new Location(locationIndex++),
					new Location(locationIndex++),
					new Location(locationIndex++)
			};
			person.getAssets().addAsset(new LocatableAsset(AssetType.LAND, locations));
			society.addPerson(person);
		}
		
		society.addAsset(new SimpleAsset(AssetType.RIVER, 100000));
		
		return society;
	}
}
