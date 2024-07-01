package com.tool.greeting_tool.ui.home;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.tool.greeting_tool.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import com.tool.greeting_tool.pojo.vo.CardDisplayVO;


public class ArActivity extends AppCompatActivity implements BaseArFragment.OnTapArPlaneListener {

    private ArrayList<CardDisplayVO> greetingCards;


    boolean isArFragmentInitialized = false;
    private ArFragment arFragment;
    private Renderable textModel;
    private Renderable emojiModel;
    private Renderable animationModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);

        /*Intent intent = getIntent();
        greetingCards = (ArrayList<GreetingCard>) intent.getSerializableExtra("greetingCards");*/
        CardDisplayVO card1 = new CardDisplayVO("getwellsoon", "heart", "staranimation");
       // CardDisplayVO card2 = new CardDisplayVO("happynewyear","tongue","heartanimation");
        //CardDisplayVO card3 = new CardDisplayVO("haveaniceday","lovesmile","");
       // greetingCards.add(card1);
        //greetingCards.add(card2);
        //greetingCards.add(card3);


        getSupportFragmentManager().addFragmentOnAttachListener((fragmentManager, fragment) -> {
            if (fragment.getId() == R.id.arFragment && !isArFragmentInitialized) {
                arFragment = (ArFragment) fragment;
                arFragment.setOnTapArPlaneListener(ArActivity.this);
                isArFragmentInitialized=true;
            }
        });

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }


        //for (CardDisplayVO card : greetingCards) {
            loadTextModel(card1.getTextId());
            loadEmojiModel(card1.getEmojiId());
            loadAnimationModel(card1.getAnimationId());
       // }

    }

    private void loadTextModel(String textId) {

        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse("Text/" + textId +".glb"))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.textModel = model;
                    }
                })
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load text model", Toast.LENGTH_LONG).show();
                            return null;
                        });
    }

    private void loadEmojiModel(String emojiId) {
        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse("Emojis/" + emojiId +".glb"))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.emojiModel = model;
                    }
                })
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load emoji model", Toast.LENGTH_LONG).show();
                            return null;
                        });
    }

    private void loadAnimationModel(String animationId) {
        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse("Animations/" + animationId + ".glb"))
                .setIsFilamentGltf(true)
                .setAsyncLoadEnabled(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.animationModel = model;
                    }
                })
                .exceptionally(throwable -> {
                    Toast.makeText(
                            this, "Unable to load animation model", Toast.LENGTH_LONG).show();
                    return null;
                });
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (textModel == null && animationModel == null && emojiModel == null) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        Quaternion rotation = new Quaternion(Vector3.up(),-90);

        // Create the transformable model and add it to the anchor.
        //Currently only show one model for test.
        TransformableNode textModelNode = new TransformableNode(arFragment.getTransformationSystem());
        textModelNode.setParent(anchorNode);
        RenderableInstance modelInstance = textModelNode.setRenderable(this.textModel);
        textModelNode.setLocalPosition(new Vector3(0, 2f, -6f));
        //modelNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
        textModelNode.setLocalRotation(rotation);
        textModelNode.select();

        TransformableNode emojiModelNode = new TransformableNode(arFragment.getTransformationSystem());
        emojiModelNode.setParent(anchorNode);
        emojiModelNode.setRenderable(this.emojiModel);
        //modelNode.setLocalPosition(new Vector3(1f, 2f, 1f));
        emojiModelNode.setLocalPosition(new Vector3(0, 2f, -6f));
        emojiModelNode.setLocalRotation(rotation);
        emojiModelNode.select();

        TransformableNode animationModelNode = new TransformableNode(arFragment.getTransformationSystem());
        animationModelNode.setParent(anchorNode);
        animationModelNode.setRenderable(this.animationModel).animate(true).start();
        animationModelNode.setLocalPosition(new Vector3(0, 5f, -18f));
        //modelNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));
        animationModelNode.select();
    }
}
