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

import static person.DurationConstants.AGE_INCREASE;
import static person.DurationConstants.PREGNANCY_DURATION;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import organization.Organization;
import society.PublicAssets;
import society.PublicOrganizations;

class Pregnancy implements Serializable {

	private final Person parent;
	private final Random random;
	private final PersonBehaviour personBehaviour;
	
	private Integer pregnant;
	
	public Pregnancy(Person parent, Random random, PersonBehaviour personBehaviour) {
		super();
		this.parent = parent;
		this.random = random;
		this.personBehaviour = personBehaviour;
		this.pregnant = null;
	}
	
	public void processTurn(List<Person> personList, PublicOrganizations publicOrganizations, PublicAssets publicAssets) {
		checkPregnancy(personList, publicOrganizations, publicAssets);
	}
	
	private void checkPregnancy(List<Person> personList, PublicOrganizations publicOrganizations, PublicAssets publicAssets) {
		if (pregnant != null) {
			pregnant += AGE_INCREASE;
			if (pregnant > PREGNANCY_DURATION) {
				Family family = parent.getFamily();
				Sex newSex = random.nextInt() % 2 == 0 ? Sex.MALE : Sex.FEMALE;
				int newRandomSeed = random.nextInt();
				Person person = new Person(0, newSex, newRandomSeed, personBehaviour);
				
				Organization organization = publicOrganizations.findOrganization();
				if (organization != null
						&& organization.childrenJoinAtBirth()
						&& (family.parentIsMemberOf(parent, organization))) {
					organization.add(person);
				}
				
				person.getAssets().addAssets(publicAssets.retrieveAssetsForNewPerson());
				
				personList.add(person);
				
				family.addChild(parent, person);
				pregnant = null;
			}
		}
	}
	
	public boolean isPregnant() {
		return pregnant != null;
	}
	
	public void impregnate() {
		pregnant = 0;
	}
}
