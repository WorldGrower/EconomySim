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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import person.Person;

public class SocialObligations implements Serializable {
	private final List<SocialObligation> socialObligations = new ArrayList<>();
	
	public void add(SocialObligation socialObligation) {
		this.socialObligations.add(socialObligation);
	}

	public boolean hasSocialObligation(SocialObligationType type) {
		for(SocialObligation socialObligation : socialObligations) {
			if (socialObligation.getType() == type) {
				return true;
			}
		}
		return false;
	}

	public void endTurn() {
		final Iterator<SocialObligation> socialObligationIterator = socialObligations.iterator();
		while (socialObligationIterator.hasNext()) {
			SocialObligation socialObligation = socialObligationIterator.next();
			socialObligation.endTurn();
			if (socialObligation.isDone()) {
				socialObligationIterator.remove();
			}
		}
	}
	
	public int size() {
		return socialObligations.size();
	}
	
	public boolean canFullfill(int index, Person person) {
		return socialObligations.get(index).canFullfill(person);
	}

	public void fullfill(int index, Person person) {
		socialObligations.get(index).fullfill(person);
		socialObligations.remove(index);
	}

	public String getDescription(int index) {
		return socialObligations.get(index).getDescription();
	}

	public void remove(SocialObligationType type) {
		final Iterator<SocialObligation> socialObligationIterator = socialObligations.iterator();
		while (socialObligationIterator.hasNext()) {
			SocialObligation socialObligation = socialObligationIterator.next();
			if (socialObligation.getType() == type) {
				socialObligationIterator.remove();
			}
		}
	}
}
