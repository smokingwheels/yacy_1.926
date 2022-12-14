// ContentScraperListener.java
// ---------------------------
// Copyright 2016 by luccioman; https://github.com/luccioman
//
// This is a part of YaCy, a peer-to-peer based web search engine
//
// LICENSE
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package net.yacy.document.parser.html;

/**
 * Listener interface to ContentScraper events
 */
public interface ContentScraperListener extends ScraperListener {
	/**
	 * Triggered by {@link ContentScraper#addAnchor(net.yacy.cora.document.id.AnchorURL)} implementations
	 * @param anchorURL the anchor normalized URL
	 */
    public void anchorAdded(String anchorURL);
}
