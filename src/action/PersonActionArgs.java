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

import knowledge.Knowledge;
import organization.DecisionCriteria;
import organization.OrganizationPolicy;
import person.Person;
import person.PersonDecisions;

public class PersonActionArgs {
	public static final PersonActionArgs NONE = new PersonActionArgs();
	
	private Person targetPerson;
	private Knowledge knowledge;
	private TradeArgs tradeArgs;
	private OrganizationArgs organizationArgs;
	private PersonDecisions personDecisions;
	
	public PersonActionArgs() {
		this(null, null, null, OrganizationArgs.NONE, PersonDecisions.AI);
	}
	
	public PersonActionArgs(Person targetPerson, Knowledge knowledge, TradeArgs tradeArgs, OrganizationArgs organizationArgs, PersonDecisions personDecisions) {
		super();
		this.targetPerson = targetPerson;
		this.knowledge = knowledge;
		this.tradeArgs = tradeArgs;
		this.organizationArgs = organizationArgs;
		this.personDecisions = personDecisions;
	}

	public Person getTargetPerson() {
		return targetPerson;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public TradeArgs getTradeArgs() {
		return tradeArgs;
	}

	public DecisionCriteria getDecisionCriteria() {
		return organizationArgs.getDecisionCriteria();
	}

	public OrganizationPolicy getOrganizationPolicy() {
		return organizationArgs.getOrganizationPolicy();
	}

	public PersonDecisions getPersonDecisions() {
		return personDecisions;
	}
}
