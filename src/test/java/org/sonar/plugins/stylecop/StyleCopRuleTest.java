/*
 * SonarQube StyleCop Plugin
 * Copyright (C) 2014 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.plugins.stylecop;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;
import org.sonar.api.rules.ActiveRule;

public class StyleCopRuleTest {

	@Test
	public void testStyleCopRuleActiveRule() {
	    ActiveRule activeRule = mockActiveRule("a#b", "b", true);

		StyleCopRule rule = new StyleCopRule(activeRule);
		assertThat(rule.getAnalyzerId()).isEqualTo("a");
		assertThat(rule.getRuleName()).isEqualTo("b");
		assertThat(rule.getIsEnabled()).isTrue();
	}

	@Test
	public void testStyleCopRuleString() {
		StyleCopRule rule = new StyleCopRule("a#b");
		assertThat(rule.getAnalyzerId()).isEqualTo("a");
		assertThat(rule.getRuleName()).isEqualTo("b");
		assertThat(rule.getIsEnabled()).isTrue();
	}

	@Test
	public void testStyleCopRuleStringBoolean() {
		StyleCopRule rule = new StyleCopRule("a#b", false);
		assertThat(rule.getAnalyzerId()).isEqualTo("a");
		assertThat(rule.getRuleName()).isEqualTo("b");
		assertThat(rule.getIsEnabled()).isFalse();
	}

	ActiveRule mockActiveRule(String configKey, String ruleKey, Boolean isEnabled){
	    ActiveRule activeRule = mock(ActiveRule.class);
	    when(activeRule.getRuleKey()).thenReturn(ruleKey);
	    when(activeRule.getConfigKey()).thenReturn(configKey);
	    when(activeRule.isEnabled()).thenReturn(isEnabled);
        return activeRule;
	}
}
