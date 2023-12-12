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

import environment.PublicLocations;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class ArtisanProfessionPersonBehaviour implements PersonBehaviour {
	
	private final CreateClothingBehaviour createClothingBehaviour;
	private final CreateStorageBehaviour createStorageBehaviour;
	
	public ArtisanProfessionPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		this.createClothingBehaviour = new CreateClothingBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations, 10);
		this.createStorageBehaviour = new CreateStorageBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations, StorageCalculatorArtisan.INSTANCE);
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		createClothingBehaviour.processActions(person, personDecisions, professionStatistics);
		createStorageBehaviour.processActions(person, personDecisions, professionStatistics);
	}
}
