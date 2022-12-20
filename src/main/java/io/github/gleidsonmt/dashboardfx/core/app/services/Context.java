/*
 *
 *    Copyright (C) Gleidson Neves da Silveira
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.dashboardfx.core.app.services;

import io.github.gleidsonmt.dashboardfx.core.app.interfaces.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  02/10/2022
 */
public interface Context {

    Routes routes();

    ILayout layout();

    @Deprecated
    Properties getProperties();

    @Deprecated
    Logger getLogger();

    @Deprecated(since = "tets", forRemoval = true)
    IDecorator getDecorator();

    Wrapper getWrapper();

    ILayout getLayout();

    Root getRoot();

    @Deprecated(since = "1", forRemoval = true)
    Routes getRoutes();

    @Deprecated(since = "1", forRemoval = true)
    PathView getPaths();

    void openLink(String url);

    @Deprecated(forRemoval = true)
    Stage getStage();

    void icons(Image... icons);


}