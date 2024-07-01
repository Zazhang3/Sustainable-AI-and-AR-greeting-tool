package com.tool.greeting_tool.ui.home;

import android.content.Intent;
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
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.tool.greeting_tool.R;
import com.tool.greeting_tool.pojo.dto.GreetingCard;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import com.google.ar.sceneform.ux.ArFragment;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ArActivity extends AppCompatActivity implements BaseArFragment.OnTapArPlaneListener {

    private ArrayList<GreetingCard> greetingCards;

    boolean isArFragmentInitialized = false;
    private ArFragment arFragment;
    private Renderable model;
    private Renderable emojiModel;
    private ModelRenderable AnimationModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);

        Intent intent = getIntent();
        greetingCards = (ArrayList<GreetingCard>) intent.getSerializableExtra("greetingCards");


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


        for (GreetingCard card : greetingCards) {
            loadTextModel(card.getCardId());
        }


    }

    private void loadTextModel(String textId) {

        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse("Emojis/cube.glb"))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.model = model;
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
                .setSource(this, Uri.parse("Emoji/happy_face.glb"))  
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
                .setSource(this, Uri.parse("Animation/happy_face.glb"))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        activity.AnimationModel = model;
                    }
                })
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load emoji model", Toast.LENGTH_LONG).show();
                            return null;
                        });
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (model == null ) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create the Anchor.
        Anchor anchor = hitResult.createAnchor();
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());

        // Create the transformable model and add it to the anchor.
        //Currently only show one model for test.
        TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
        modelNode.setParent(anchorNode);
        RenderableInstance modelInstance = modelNode.setRenderable(this.model);
        modelInstance.getMaterial().setInt("baseColorIndex", 0);
        //modelInstance.getMaterial().setTexture("baseColorMap", texture);
        //modelNode.setLocalPosition(new Vector3(1f, 2f, 1f));
        //modelNode.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));

        modelNode.select();

    }


}
