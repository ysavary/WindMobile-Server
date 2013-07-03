/*******************************************************************************
 * Copyright (c) 2011 epyx SA.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ch.windmobile.server.ffvl;

import java.util.ArrayList;
import java.util.List;

import ch.windmobile.server.mongo.MongoDataSource;

public class FfvlDataSource extends MongoDataSource {

    @Override
    protected String getProvider() {
        return "ffvl.fr";
    }

    private static final List<String> filter = new ArrayList<String>();
    static {
        filter.add("ffvl-38");
    }

    @Override
    protected List<String> getStationsFilter() {
        return filter;
    }
}
