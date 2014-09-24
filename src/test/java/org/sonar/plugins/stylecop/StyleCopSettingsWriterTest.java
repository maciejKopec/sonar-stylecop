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

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;
import java.util.Collections;

import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.rules.Rule;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;

public class StyleCopSettingsWriterTest {

  @org.junit.Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void test() throws Exception {
    File file1 = tmp.newFile();

    new StyleCopSettingsWriter().write(ImmutableList.of(new StyleCopRule(mockRule("Foo#A")), new StyleCopRule(mockRule("StyleCop.CSharp.NamingRules#C")), new StyleCopRule(mockRule("Foo#B")),
    		new StyleCopRule(mockRule("Foo#Inactive"), false)), ImmutableList.of("aa", "bb"), file1);
    String contents1 = Files.toString(file1, Charsets.UTF_8);

    assertThat(contents1.replace("\r", "").replace("\n", ""))
      .isEqualTo(
        "<StyleCopSettings Version=\"105\">"
          + "  <Parsers>"
          + "    <Parser ParserId=\"StyleCop.CSharp.CsParser\">"
          + "      <ParserSettings>"
          + "        <CollectionProperty Name=\"GeneratedFileFilters\">"
          + "          <Value>\\.g\\.cs$</Value>"
          + "          <Value>\\.generated\\.cs$</Value>"
          + "          <Value>\\.g\\.i\\.cs$</Value>"
          + "          <Value>TemporaryGeneratedFile_.*\\.cs$</Value>"
          + "        </CollectionProperty>"
          + "        <BooleanProperty Name=\"AnalyzeDesignerFiles\">False</BooleanProperty>"
          + "      </ParserSettings>"
          + "    </Parser>"
          + "  </Parsers>"
          + "  <Analyzers>"
          + "    <Analyzer AnalyzerId=\"Foo\">"
          + "      <Rules>"
          + "        <Rule Name=\"A\">"
          + "          <RuleSettings>"
          + "            <BooleanProperty Name=\"Enabled\">True</BooleanProperty>"
          + "          </RuleSettings>"
          + "        </Rule>"
          + "        <Rule Name=\"B\">"
          + "          <RuleSettings>"
          + "            <BooleanProperty Name=\"Enabled\">True</BooleanProperty>"
          + "          </RuleSettings>"
          + "        </Rule>"
          + "        <Rule Name=\"Inactive\">"
          + "          <RuleSettings>"
          + "            <BooleanProperty Name=\"Enabled\">False</BooleanProperty>"
          + "          </RuleSettings>"
          + "        </Rule>"
          + "      </Rules>"
          + "    </Analyzer>"
          + "    <Analyzer AnalyzerId=\"StyleCop.CSharp.NamingRules\">"
          + "      <Rules>"
          + "        <Rule Name=\"C\">"
          + "          <RuleSettings>"
          + "            <BooleanProperty Name=\"Enabled\">True</BooleanProperty>"
          + "          </RuleSettings>"
          + "        </Rule>"
          + "      </Rules>"
          + "      <AnalyzerSettings>"
          + "        <CollectionProperty Name=\"Hungarian\">"
          + "          <Value>aa</Value>"
          + "          <Value>bb</Value>"
          + "        </CollectionProperty>"
          + "      </AnalyzerSettings>"
          + "    </Analyzer>"
          + "  </Analyzers>"
          + "</StyleCopSettings>");

    File file2 = tmp.newFile();
    new StyleCopSettingsWriter().write(ImmutableList.of(new StyleCopRule(mockRule("baz#SomeRuleKey"))), Collections.<String>emptyList(), file2);
    String contents2 = Files.toString(file2, Charsets.UTF_8);

    assertThat(contents2)
      .contains("baz")
      .contains("SomeRuleKey")
      .doesNotContain("foo")
      .doesNotContain("Hungarian");
  }

  private static Rule mockRule(String configKey){
		return Rule.create("stylecop", configKey);
	}

}
