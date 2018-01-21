package ru.rpuxa.arkanoid.Main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.collision.Ray;

import ru.rpuxa.arkanoid.Account.Server.Connection;
import ru.rpuxa.arkanoid.Cache.CacheMaster;
import ru.rpuxa.arkanoid.Cache.TextureBank;

public class Visual extends ApplicationAdapter {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	public static Game game;
	public static Message message;
	public static String imei;
	public static TextureBank textureBank = new TextureBank();

	public static final boolean ONLINE_MODE = true;

	public static final int WIDTH = 1080;
	public static final int HEIGHT = 1920;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		textureBank.fill(null);
		CacheMaster.loadUpdates();
		game = new Game();
		Gdx.input.setInputProcessor(getInput());
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		game.render(batch, Gdx.graphics.getDeltaTime());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	private InputProcessor getInput() {
		return new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Ray ray = camera.getPickRay(screenX, screenY);
				if (pointer == 0)
					game.controller.touchDown((int) ray.origin.x, (int) ray.origin.y);
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				if (pointer == 0)
					game.controller.touchUp();
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				Ray ray = camera.getPickRay(screenX, screenY);
				if (pointer == 0)
					game.controller.touchDragged((int) ray.origin.x, (int) ray.origin.y);
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
		};
	}

	public interface Message {
		void send(String text);
	}
}
