package com.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.common.X;
import com.common.XGame;

public class LinearAlgebra extends XGame {
  private Matrix4 mat = new Matrix4(); // ma trận chiếu (projection matrix)
  private TextureRegion tg;
  private InputMultiplexer imx = new InputMultiplexer();
  private float PPM = 1;
  @Override
  public void _create() {
    float ratio = Gdx.graphics.getHeight()/(Gdx.graphics.getWidth()*1f);
    float w = 20,h; // chiều dài + cao game world, mình định nghĩa

    //ở đây mình định nghĩa phép chiếu từ thế giới [20, 11.25] ra màn hình [1280, 720]
    //ma trận chiếu sẽ đứng giữa 2 hệ tọa độ này.
    // khi batch(vẽ ra màn hình) thì lấy (vector)đọa độ game nhân với NGHỊCH ĐẢO ma trận chiếu để có tọa độ màn hình
    // khi xử lí input thì lấy vector tọa độ màn hình nhân với ma trận chiếu để về tọa đó thế giới game
    mat.setToOrtho2D(0, 0, w, h = ratio* w);//h = w/1.777777

    /*[0.1|0.0        |0.0  |-1.0 ]
      [0.0|0.17777778 |0.0  |-1.0 ]  x [0.8, 0] = [900, 0] * A
      [0.0|0.0        |-2.0 |-1.0 ]
      [0.0|0.0        |0.0  | 1.0 ]*/

    PPM = w/Gdx.graphics.getWidth(); // 20 chia 1280 = 0.01562
    tg = X.getTextureRegion("1");

    //CHỖ NÀY TEST CHUYỂN ĐỔI TỌA ĐỘ TỪ MÀN HÌNH VẬT LÝ SANG TỌA ĐỘ GAME WORLD
    //KHI CLICK THÌ GDX TRẢ VỀ TỌA ĐỘ MÀN HÌNH VẬT LÝ
    InputProcessor ip = new InputAdapter() {
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.app.log("Toa do man hinh ", "X : " + screenX + " - Y: " + screenY);
        // ở đây là lấy tọa độ màn hình nhân ma trận chiếu để ra tọa độ game world
        Vector3 c = (new Vector3(screenX, screenY, 0)).prj(mat);
        Gdx.app.log("Toa do the gioi game ", "X: " + c.x + " - Y: " + c.y);
        return super.touchDown(screenX, screenY, pointer, button);
      }
    };
    imx.addProcessor(ip);
    batch.setProjectionMatrix(mat); //<------------ SET MA TRẬN CHIẾU CHO BATCH, COMMENT CHỖ NÀY THỬ :)
  }

  @Override
  public void render() {
    batch.begin();
    //Ở ĐÂY LÀ CHIẾU TỪ TỌA ĐỘ GAME WORLD RA TỌA ĐỘ MÀN HÌNH
    /*lấy đọa độ gameworld nhân nghịch đảo ma trận chiếu để ra tọa độ màn hình*/
    batch.draw(tg, 0f, 0,tg.getRegionWidth()*PPM, tg.getRegionHeight()*PPM);
    batch.end();
  }

  @Override
  public InputMultiplexer getInput() {
    return imx;
  }

  @Override
  public void dispose() {
    super.dispose();
  }
}
