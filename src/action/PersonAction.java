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

import java.util.ArrayList;
import java.util.List;

import environment.TimeRemaining;
import knowledge.Knowledge;
import person.Person;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

public abstract class PersonAction {
	public final void perform(Person person, TimeRemaining timeRemaining, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
		performInternal(person, args, publicAssets, publicKnowledge, publicOrganizations);
		timeRemaining.use(getTimeRequired());
		if (getRequiredKnowledge() != null) {
			publicKnowledge.addKnowledge(getRequiredKnowledge());
		}
	}
	
	public abstract void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations);
	
	public final boolean canPerform(Person person, TimeRemaining timeRemaining, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		final boolean hasKnowledge;
		Knowledge requiredKnowledge = getRequiredKnowledge();
		if (requiredKnowledge != null) {
			hasKnowledge = person.hasKnowledge(requiredKnowledge);
		} else {
			hasKnowledge = true;
		}
		return hasKnowledge && timeRemaining.hasRemainingTime(getTimeRequired()) && canPerformInternal(person, publicAssets, publicOrganizations);
	}
	protected abstract boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations);
	public abstract int getTimeRequired();
	public abstract String getName();
	public Knowledge getRequiredKnowledge() { return null; }
	
	public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) { return new ArrayList<>(); }
	public List<Knowledge> getPossibleKnowledge(Person person, Person targetPerson) { return new ArrayList<>(); }
	public boolean requiresTradeArgs() { return false; }
	public boolean requiresDecisionCriteria() { return false; }
	public boolean requiresOrganizationPolicy() { return false; }
	
	public final Person getFirstTargetPerson(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations) {
		Person targetPerson = null;
		List<Person> targetPersons = getPossibleTargetPersons(person, personFinder, publicOrganizations, 1);
		if (targetPersons.size() > 0) {
			targetPerson = targetPersons.get(0);
		}
		return targetPerson;
	}
	
	public final Knowledge getFirstKnowledge(Person person, Person targetPerson) {
		Knowledge knowledge = null;
		List<Knowledge> knowledgeList = getPossibleKnowledge(person, targetPerson);
		if (knowledgeList.size() > 0) {
			knowledge = knowledgeList.get(0);
		}
		return knowledge;
	}
}
