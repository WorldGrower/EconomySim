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
import asset.OperationalAsset;
import asset.SimpleAsset;
import environment.Location;
import environment.MockCalendarSystem;
import knowledge.Knowledge;
import person.Person;
import person.PersonKnowledgeUtils;
import person.Sex;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;

public class UTestInfrastructurePersonActions {

	@Test
	public void testCanPerformBuildIrrigationCanal() {
		PersonAction personAction = InfrastructurePersonActions.BUILD_IRRIGATION_CANAL_PERSON_ACTION;
		Person person = new Person(18, Sex.FEMALE);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.IRRIGATION);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		
		OperationalAsset irrigationCanal = new OperationalAsset(AssetType.IRRIGATION_CANAL);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(irrigationCanal), new MockPublicOrganizations()));
	
		buildIrrigationCanalFully(irrigationCanal);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(irrigationCanal), new MockPublicOrganizations()));
	}
	
	@Test
	public void testPerformBuildIrrigationCanal() {
		PersonAction personAction = InfrastructurePersonActions.BUILD_IRRIGATION_CANAL_PERSON_ACTION;
		testPerformBuildIrrigationCanal(personAction);
	}
	
	private void testPerformBuildIrrigationCanal(PersonAction personAction) {
		Person person = new Person(18, Sex.FEMALE);
		OperationalAsset irrigationCanal = new OperationalAsset(AssetType.IRRIGATION_CANAL);
	
		assertEquals(false, irrigationCanal.getOperationalAssetAttribute(0).isFinished());
		
		for(int i=0; i<1000; i++) {
			personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, new MockPublicAssets(irrigationCanal), new MockPublicKnowledge(), new MockPublicOrganizations());
			person.getTimeRemaining().reset(new MockCalendarSystem(true), true);
		}
	
		assertEquals(true, irrigationCanal.getOperationalAssetAttribute(0).isFinished());	
	}
	
	@Test
	public void testCanPerformMaintainIrrigationCanal() {
		PersonAction personAction = InfrastructurePersonActions.MAINTAIN_IRRIGATION_CANAL_PERSON_ACTION;
		Person person = new Person(18, Sex.FEMALE);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.IRRIGATION);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(), new MockPublicOrganizations()));
		
		OperationalAsset irrigationCanal = new OperationalAsset(AssetType.IRRIGATION_CANAL);
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(irrigationCanal), new MockPublicOrganizations()));
	
		buildIrrigationCanalFully(irrigationCanal);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), new MockPublicAssets(irrigationCanal), new MockPublicOrganizations()));	
	}
	
	@Test
	public void testPerformMaintainIrrigationCanal() {
		PersonAction personAction = InfrastructurePersonActions.MAINTAIN_IRRIGATION_CANAL_PERSON_ACTION;
		testPerformBuildIrrigationCanal(personAction);
	}
	
	@Test
	public void testCanPerformIrrigateWheatFields() {
		PersonAction personAction = InfrastructurePersonActions.IRRIGATE_WHEAT_FIELDS_CANAL_PERSON_ACTION;
		Person person = new Person(18, Sex.FEMALE);
		MockPublicAssets publicAssets = new MockPublicAssets();
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), publicAssets, new MockPublicOrganizations()));

		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.IRRIGATION);
		
		OperationalAsset irrigationCanal = addRiverAndIrrigationCanal(publicAssets);
		Asset wheatFields = new LocatableAsset(AssetType.WHEAT_FIELD, new Location(0));
		person.getAssets().addAsset(wheatFields);
		
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), publicAssets, new MockPublicOrganizations()));
		
		buildIrrigationCanalFully(irrigationCanal);
		assertEquals(true, personAction.canPerform(person, person.getTimeRemaining(), publicAssets, new MockPublicOrganizations()));
	}

	private OperationalAsset addRiverAndIrrigationCanal(MockPublicAssets publicAssets) {
		Asset river = new SimpleAsset(AssetType.RIVER, 1000);
		publicAssets.addAsset(river);
		OperationalAsset irrigationCanal = new OperationalAsset(AssetType.IRRIGATION_CANAL);
		publicAssets.addAsset(irrigationCanal);
		return irrigationCanal;
	}

	private void buildIrrigationCanalFully(OperationalAsset irrigationCanal) {
		for(int i=0; i<1000; i++) { irrigationCanal.getOperationalAssetAttribute(0).increaseOperational(); }
	}
	
	@Test
	public void testPerformIrrigateWheatFields() {
		PersonAction personAction = InfrastructurePersonActions.IRRIGATE_WHEAT_FIELDS_CANAL_PERSON_ACTION;
		Person person = new Person(18, Sex.FEMALE);
		MockPublicAssets publicAssets = new MockPublicAssets();

		OperationalAsset irrigationCanal = addRiverAndIrrigationCanal(publicAssets);
		LocatableAsset wheatFields = new LocatableAsset(AssetType.WHEAT_FIELD, new Location(0));
		person.getAssets().addAsset(wheatFields);
		buildIrrigationCanalFully(irrigationCanal);
		
		assertEquals(100, wheatFields.getCapacity(0));
		
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, new MockPublicKnowledge(), new MockPublicOrganizations());
		assertEquals(150, wheatFields.getCapacity(0));
		
		assertEquals(false, personAction.canPerform(person, person.getTimeRemaining(), publicAssets, new MockPublicOrganizations()));
	
		personAction.perform(person, person.getTimeRemaining(), PersonActionArgs.NONE, publicAssets, new MockPublicKnowledge(), new MockPublicOrganizations());
		assertEquals(150, wheatFields.getCapacity(0));
	}
	
	/*
	@Test
	public void testProcessIrrigation() {
		Assets assets = new Assets();
		Location location = new Location(0);
		MockPublicAssets publicAssets = new MockPublicAssets();
		publicAssets.addAsset(new SimpleAsset(AssetType.RIVER, 150));
		assets.addAsset(new LocatableAsset(AssetType.IRRIGATION_CANAL, location));
		assets.addAsset(new LocatableAsset(AssetType.WHEAT_FIELD, location));
		
		Asset river = publicAssets.get(AssetType.RIVER);	
		assertEquals(150, river.getQuantity());
		
		LocatableAsset wheatField = (LocatableAsset) assets.get(AssetType.WHEAT_FIELD);
		assertEquals(100, wheatField.getCapacity(0));
		
		AssetType.processTurn(assets, publicAssets);
		assertEquals(150, river.getQuantity());
		assertEquals("water: 50 / 150", river.getRemainingProduceDescription());
		assertEquals(150, wheatField.getCapacity(0));
	}
	
	@Test
	public void testProcessIrrigationNoWheatField() {
		Assets assets = new Assets();
		Location location = new Location(0);
		MockPublicAssets publicAssets = new MockPublicAssets();
		publicAssets.addAsset(new SimpleAsset(AssetType.RIVER, 150));
		assets.addAsset(new LocatableAsset(AssetType.IRRIGATION_CANAL, location));
		
		Asset river = publicAssets.get(AssetType.RIVER);	
		assertEquals(150, river.getQuantity());
		
		AssetType.processTurn(assets, publicAssets);
		assertEquals(150, river.getQuantity());
		assertEquals("water: 150 / 150", river.getRemainingProduceDescription());
	}
	*/
}
