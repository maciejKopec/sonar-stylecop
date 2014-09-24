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

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.apache.commons.io.IOUtils;
import org.sonar.api.rules.ActiveRule;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class StyleCopSettingsWriter {

  public void write(List<StyleCopRule> rules, Iterable<String> ignoredHungarianPrefixes, File file) {
    StringBuilder sb = new StringBuilder();

    appendLine(sb, "<StyleCopSettings Version=\"105\">");
    appendLine(sb, "  <Parsers>");
    appendLine(sb, "    <Parser ParserId=\"StyleCop.CSharp.CsParser\">");
    appendLine(sb, "      <ParserSettings>");
    appendLine(sb, "        <CollectionProperty Name=\"GeneratedFileFilters\">");
    appendLine(sb, "          <Value>\\.g\\.cs$</Value>");
    appendLine(sb, "          <Value>\\.generated\\.cs$</Value>");
    appendLine(sb, "          <Value>\\.g\\.i\\.cs$</Value>");
    appendLine(sb, "          <Value>TemporaryGeneratedFile_.*\\.cs$</Value>");
    appendLine(sb, "        </CollectionProperty>");
    appendLine(sb, "        <BooleanProperty Name=\"AnalyzeDesignerFiles\">False</BooleanProperty>");
    appendLine(sb, "      </ParserSettings>");
    appendLine(sb, "    </Parser>");
    appendLine(sb, "  </Parsers>");
    appendLine(sb, "  <Analyzers>");

    for (String analyzerId : getAnalyzers(rules)) {
      appendLine(sb, "    <Analyzer AnalyzerId=\"" + analyzerId + "\">");
      appendLine(sb, "      <Rules>");
      for (StyleCopRule rule : getAnalyzerRules(analyzerId, rules)) {
        appendLine(sb, "        <Rule Name=\"" + rule.getRuleName() + "\">");
        appendLine(sb, "          <RuleSettings>");
        appendLine(sb, "            <BooleanProperty Name=\"Enabled\">" + getBooleanString(rule.getIsEnabled()) + "</BooleanProperty>");
        appendLine(sb, "          </RuleSettings>");
        appendLine(sb, "        </Rule>");
      }
      appendLine(sb, "      </Rules>");
      if ("StyleCop.CSharp.NamingRules".equals(analyzerId)) {
        appendLine(sb, "      <AnalyzerSettings>");
        appendLine(sb, "        <CollectionProperty Name=\"Hungarian\">");
        for (String ignoredHungarianPrefix : ignoredHungarianPrefixes) {
          appendLine(sb, "          <Value>" + ignoredHungarianPrefix + "</Value>");
        }
        appendLine(sb, "        </CollectionProperty>");
        appendLine(sb, "      </AnalyzerSettings>");
      }
      appendLine(sb, "    </Analyzer>");
    }

    appendLine(sb, "  </Analyzers>");
    appendLine(sb, "</StyleCopSettings>");

    try {
      Files.write(sb.toString().getBytes(Charsets.UTF_8), file);
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }

  private static List<String> getAnalyzers(List<StyleCopRule> rules) {
    ImmutableList.Builder<String> builder = ImmutableList.builder();
    Set<String> alreadyAddedNamespaces = Sets.newHashSet();

    for (StyleCopRule rule : rules) {
      if (!alreadyAddedNamespaces.contains(rule.getAnalyzerId())) {
        builder.add(rule.getAnalyzerId());
        alreadyAddedNamespaces.add(rule.getAnalyzerId());
      }
    }

    return builder.build();
  }

  private static List<StyleCopRule> getAnalyzerRules(String analyzerId, List<StyleCopRule> rules) {
    ImmutableList.Builder<StyleCopRule> builder = ImmutableList.builder();

    for (StyleCopRule rule : rules) {
      if (rule.getAnalyzerId().equals(analyzerId)) {
        builder.add(rule);
      }
    }

    return builder.build();
  }

  private static void appendLine(StringBuilder sb, String s) {
    sb.append(s);
    sb.append(IOUtils.LINE_SEPARATOR);
  }

  private String getBooleanString(Boolean b) {
	String s = b.toString();
	return s.substring(0, 1).toUpperCase() + s.substring(1);
}

}
