/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2022 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.content.actions;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.test.MockUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnit4.class)
public class SetBrightnessActionTest {

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	private static final float BRIGHTNESS = 91f;
	private Formula brightness = new Formula(BRIGHTNESS);
	private static final String NOT_NUMERICAL_STRING = "NOT_NUMERICAL_STRING";
	private Sprite sprite;

	@Before
	public void setUp() throws Exception {
		Project project = new Project(MockUtil.mockContextForProject(), "Project");
		sprite = new Sprite("testSprite");
		project.getDefaultScene().addSprite(sprite);
		ProjectManager.getInstance().setCurrentProject(project);
	}

	@Test
	public void testBrightnessEffect() {
		assertEquals(100f, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(), brightness).act(1.0f);
		assertEquals(BRIGHTNESS, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
	}

	@Test(expected = NullPointerException.class)
	public void testNullSprite() {
		Action action = sprite.getActionFactory().createSetBrightnessAction(null,
				null, brightness);
		action.act(1.0f);
	}

	@Test
	public void testNegativeBrightnessValue() {
		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(),
				new Formula(-BRIGHTNESS)).act(1.0f);
		assertEquals(0f, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
	}

	@Test
	public void testBrickWithStringFormula() {
		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(), new Formula(String.valueOf(BRIGHTNESS))).act(1.0f);
		assertEquals(BRIGHTNESS, sprite.look.getBrightnessInUserInterfaceDimensionUnit());

		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(), new Formula(NOT_NUMERICAL_STRING)).act(1.0f);
		assertEquals(BRIGHTNESS, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
	}

	@Test
	public void testNullFormula() {
		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(), null).act(1.0f);
		assertEquals(0f, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
	}

	@Test
	public void testNotANumberFormula() {
		sprite.getActionFactory().createSetBrightnessAction(sprite, new SequenceAction(),
				new Formula(Double.NaN)).act(1.0f);
		assertEquals(100f, sprite.look.getBrightnessInUserInterfaceDimensionUnit());
	}
}
