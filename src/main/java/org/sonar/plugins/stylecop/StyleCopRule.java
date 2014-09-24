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

import org.sonar.api.rules.Rule;

public class StyleCopRule {

	private final Rule rule;
	private final String analyzerId;
	private final Boolean isEnabled;
	private final String ruleName;
	
	public StyleCopRule(Rule rule, Boolean isEnabled) {
		this.rule = rule;
		String ruleConfigKey = rule.getConfigKey();
		this.analyzerId = ruleConfigKey.substring(0, ruleConfigKey.indexOf('#'));
		this.ruleName = ruleConfigKey.substring(analyzerId.length() + 1);
		this.isEnabled = isEnabled;
	}

	public StyleCopRule(Rule rule) {
		this(rule, true);
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

	    return rule.equals(that.rule);
	  }

	public String getAnalyzerId() {
		return analyzerId;
	}
	
	public String getConfigKey() {
		return rule.getConfigKey();
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
