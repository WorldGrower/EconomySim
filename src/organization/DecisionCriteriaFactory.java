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

import java.io.Serializable;
import java.util.List;

import asset.AssetType;
import person.Person;
import person.Sex;

public class DecisionCriteriaFactory {

	public static DecisionCriteria createAutocracy(Person leader) {
		return new AutocracyDecisionCriteria(leader);
	}
	
	public static DecisionCriteria createOligarchy() {
		return new OligarchyDecisionCriteria();
	}
	
	public static DecisionCriteria createRepublic() {
		return new RepublicDecisionCriteria();
	}
	
	private static abstract class AbstractDecisionCriteria implements DecisionCriteria, Serializable {
	}
	
	private static class AutocracyDecisionCriteria extends AbstractDecisionCriteria {
		private Person leader;
		
		public AutocracyDecisionCriteria(Person leader) {
			super();
			this.leader = leader;
		}

		@Override
		public boolean personCanDecide(Person person) {
			return person == leader;
		}

		@Override
		public void onDeath(Person person) {
			if (person == leader) {
				//TODO: add more succession strategies
				List<Person> children = person.getFamily().getChildren();
				if (children.size() > 0) {
					leader = children.get(0);
				} else if (person.getFamily().getPartner() != null) {
					leader = person.getFamily().getPartner();
				} else {
					leader = null;
				}
			}
		}

		@Override
		public DecisionCriteriaType getDecisionCriteriaType() {
			return DecisionCriteriaType.AUTOCRACY;
		}
	}
	
	private static class OligarchyDecisionCriteria extends AbstractDecisionCriteria {

		@Override
		public boolean personCanDecide(Person person) {
			//TODO: add more criteria
			return person.getAssets().getQuantityFor(AssetType.LAND) > 0;
		}

		@Override
		public void onDeath(Person person) {
		}
		
		@Override
		public DecisionCriteriaType getDecisionCriteriaType() {
			return DecisionCriteriaType.OLIGARCHY;
		}
	}
	
	private static class RepublicDecisionCriteria extends AbstractDecisionCriteria {

		@Override
		public boolean personCanDecide(Person person) {
			//TODO: add more criteria
			return person.isAdult() && person.getSex() == Sex.MALE;
		}

		@Override
		public void onDeath(Person person) {
		}
		
		@Override
		public DecisionCriteriaType getDecisionCriteriaType() {
			return DecisionCriteriaType.REPUBLIC;
		}
	}
}
