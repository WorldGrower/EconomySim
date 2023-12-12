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

public enum Profession {
	FARMER("farmer") {
		@Override
		public PersonBehaviour getPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
			return new FarmerProfessionPersonBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		}
	}, 
	ARTISAN("artisan") {
		@Override
		public PersonBehaviour getPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
			return new ArtisanProfessionPersonBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		}
	},
	
	NONE("none") {
		@Override
		public PersonBehaviour getPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
			return PersonBehaviour.NONE;
		}
	},
	// add irrigation canal builder, social maintenca
	GENERALIST("generalist") {
		@Override
		public PersonBehaviour getPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
			return new GeneralistProfessionPersonBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		}
	}
	;
	
	public static final Profession[] VALUES = values();

	public abstract PersonBehaviour getPersonBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations);

	private final String description;
	
	private Profession(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
