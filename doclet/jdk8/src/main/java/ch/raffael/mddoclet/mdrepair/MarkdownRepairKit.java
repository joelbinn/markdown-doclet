/*
 * Copyright 2013-2016 Raffael Herzog, Marko Umek
 *
 * This file is part of markdown-doclet.
 *
 * markdown-doclet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * markdown-doclet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with markdown-doclet.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ch.raffael.mddoclet.mdrepair;

import java.util.ArrayList;
import java.util.List;

/**
 * MarkdownRepairKit provides a repair kit for Makdown/Javadoc integration obstacles.
 *
 *
 */
public final class MarkdownRepairKit implements MarkdownRepair {
    private final List<MarkdownRepair> before=new ArrayList<>();
    private final List<MarkdownRepair> after=new ArrayList<>();

    public MarkdownRepairKit(boolean dropLeadingSpace) {
        final MarkdownRepair spaceCharacterRepair = new SpaceCharacterRepair();
        final MarkdownRepair inlineTagletMarkdownRepair = new InlineTagletRepair();
        final MarkdownRepair atSymbolRepair=new AtSymbolRepair();
        final MarkdownRepair unescapeAtSymbolRepair=new UnescapeAtSymbolRepair();
        final MarkdownRepair htmlEntitiesRepair=new HtmlEntitiesRepair();

        before.add(unescapeAtSymbolRepair); // un-escape '@', then let atSymbolRepair do its job

        // before
        if( dropLeadingSpace ) {
            before.add(spaceCharacterRepair);
        }

        before.add(inlineTagletMarkdownRepair);
        before.add(atSymbolRepair);
        before.add(htmlEntitiesRepair);

        // after
        after.add(htmlEntitiesRepair);
        after.add(atSymbolRepair);
        after.add(inlineTagletMarkdownRepair);
        after.add(spaceCharacterRepair);
    }

    @Override
    public String beforeMarkdownTaglets(String markdown) {
        String result=markdown;
        for (MarkdownRepair markdownRepair : before) {
            result = markdownRepair.beforeMarkdownTaglets(result);
        }
        return result;
    }

    public String beforeMarkdownParser(String markdown) {
        String result=markdown;
        for (MarkdownRepair markdownRepair : before) {
            result = markdownRepair.beforeMarkdownParser(result);
        }
        return result;
    }

    @Override
    public String afterMarkdownParser(String markup) {
        String result=markup;
        for (MarkdownRepair markdownRepair : after) {
            result = markdownRepair.afterMarkdownParser(result);
        }
        return result;
    }
}
