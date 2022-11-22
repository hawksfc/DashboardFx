/*
 *    Copyright (C) Gleidson Neves da Silveira
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.gleidsonmt.dashboardfx.core.app;

import fr.brouillard.oss.cssfx.CSSFX;
import io.github.gleidsonmt.dashboardfx.core.app.controllers.LoaderController;
import io.github.gleidsonmt.dashboardfx.core.app.exceptions.NavigationException;
import io.github.gleidsonmt.dashboardfx.core.app.interfaces.*;
import io.github.gleidsonmt.dashboardfx.core.app.services.LoadViews;
import io.github.gleidsonmt.dashboardfx.core.app.services.View;
import io.github.gleidsonmt.dashboardfx.core.app.services.ViewComposer;
import io.github.gleidsonmt.dashboardfx.core.layout.Drawer;
import io.github.gleidsonmt.dashboardfx.core.layout.Root;
import io.github.gleidsonmt.gncontrols.Material;
import io.github.gleidsonmt.gncontrols.Theme;
import io.github.gleidsonmt.gndecorator.core.GNDecorator;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import org.jetbrains.annotations.NotNull;
import org.scenicview.ScenicView;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * This class represents the whole window that appear in application.
 * When this is starting all view receives the threads already.
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  03/10/2022
 */
public final class WindowDecorator extends GNDecorator implements IDecorator, Context {

    private final PathView pathView;
    private LoaderController loaderController;

    private IRotes routes;

    private final IRoot root;

    public WindowDecorator(@NotNull Properties _properties, @NotNull PathView _path) throws IOException {
        // setTheme and logo here
        this.pathView = _path;

        this.getIcons().add(new Image("/logo.png"));

        root = new Root(this);

        Scene scene = this.getWindow().getScene();

        // Theming by controls lib

        new Material(scene, Theme.SIMPLE_INFO);

//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/fonts/fonts.css")).toExternalForm());
        scene.getStylesheets().add("/dash.css");

        fullBody();

        // Getting default parameters for window
        setWidth(Integer.parseInt(_properties.getProperty("app.width")));
        setHeight(Integer.parseInt(_properties.getProperty("app.height")));

        setMinWidth(Integer.parseInt(_properties.getProperty("app.min.width")));
        setMinHeight(Integer.parseInt(_properties.getProperty("app.min.height")));


        setMaximized(true);

    }

    @Override
    public IRoot getRoot() {
        return root;
    }

    @Override
    public ObservableList<Node> controls() {
        return this.getCustomControls();
    }

    @Override
    public void show(HostServices hostServices) {


        initPreLoader();

        LoadViews loadViews = new LoadViews();

        loadViews.setOnReady(event -> {
            loaderController.info("Reading application..");
        });

        loadViews.setOnFailed(event -> {
            Logger.getLogger("app").severe("Error on loading preloader");
        });

        loadViews.setOnCancelled(event -> {
            Logger.getLogger("app").severe("Error on loading preloader");
        });

        loadViews.setOnRunning(event -> {
            loaderController.info("Reading application..");
        });

        loadViews.setOnSucceeded(event -> {
            initLayout();

            try {
                context.getRoutes().setContent("dash");
            } catch (NavigationException e) {
                e.printStackTrace();
            }

        });


        loadViews.start();
        show();

//        ScenicView.show(this.getWindow().getScene());
        CSSFX.start(this.getWindow());

    }

    private void initPreLoader() {

        try {

            Logger.getLogger("app").info("Initializing Pre Loader Application");

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/core.app/loader.fxml"));
            loader.load();

            loaderController = loader.getController();

            setContent(loader.getRoot());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initLayout() {
        setContent((Parent) root);

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/views/drawer.fxml"));

        Drawer drawer = context.getDecorator().getRoot().getWrapper().getDrawer();
        loader.setController(drawer);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ViewComposer viewComposer = new ViewComposer();
        viewComposer.setName("drawer");
        viewComposer.setFxml("drawer.fxml");

        View view = new View(viewComposer, loader);

        getRoot().getLayout().setDrawer(
                view
        );
    }

}
