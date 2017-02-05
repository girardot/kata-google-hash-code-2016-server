package google.hash.code.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import google.hash.code.MyGdxGame;

import java.util.ArrayList;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MyGdxGame(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                10,
                10,
                0
        );
    }
}
