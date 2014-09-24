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

import org.sonar.api.rules.ActiveRule;

public class StyleCopRule {

	private final String analyzerId;

	private final Boolean isEnabled;

	private final String ruleName;

	public StyleCopRule(ActiveRule activeRule) {
		this(activeRule.getConfigKey(), activeRule.isEnabled());
	}

	public StyleCopRule(String ruleConfigKey)  {
		this(ruleConfigKey, true);
	}
	
	public StyleCopRule(String ruleConfigKey, Boolean isEnabled) {
		this.analyzerId = ruleConfigKey.substring(0, ruleConfigKey.indexOf('#'));
		this.ruleName = ruleConfigKey.substring(analyzerId.length() + 1);
		this.isEnabled = isEnabled;
	}

	@Override
	  public boolean equals(Object o) {
	    if (this == o) {
	      return true;
	    }
	    if (o == null || getClass() != o.getClass()) {
	      return false;
	    }

	    StyleCopRule that = (StyleCopRule) o;

	    return analyzerId.equals(that.getAnalyzerId()) && ruleName.equals(that.getRuleName());
	  }

	public String getAnalyzerId() {
		return analyzerId;
	}
	
	public String getConfigKey() {
		return analyzerId + "#" + ruleName;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public String getRuleName() {
		return ruleName;
	}
	@Override
	  public int hashCode() {
	    return getConfigKey().hashCode();
	  }

	  @Override
	public String toString() {
		return getConfigKey();
	}
}
