package com.tool.greeting_tool.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Consumer;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Sceneform;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.tool.greeting_tool.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.tool.greeting_tool.common.constant.ArModelSet;
import com.tool.greeting_tool.pojo.vo.CardDisplayVO;


public class ArActivity extends AppCompatActivity implements BaseArFragment.OnTapArPlaneListener {

    private ArrayList<CardDisplayVO> greetingCards;
    private HashMap<CardDisplayVO,ArModelSet> arModelMap = new HashMap<>();
    private ArFragment arFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ar);
        greetingCards = new ArrayList<>();

        Intent intent = getIntent();
        greetingCards = (ArrayList<CardDisplayVO>) intent.getSerializableExtra("greetingCards");
        //for display and test
        /*CardDisplayVO card1 = new CardDisplayVO("getwellsoon", "heart", "staranimation");
        CardDisplayVO card2 = new CardDisplayVO("happynewyear","tongue","staranimation");
        CardDisplayVO card3 = new CardDisplayVO("haveaniceday","lovesmile","staranimation");
        greetingCards.add(card1);
        greetingCards.add(card2);
        greetingCards.add(card3);*/


        getSupportFragmentManager().addFragmentOnAttachListener((fragmentManager, fragment) -> {
            if (fragment.getId() == R.id.arFragment ) {
                arFragment = (ArFragment) fragment;
                arFragment.setOnTapArPlaneListener(ArActivity.this);
            }
        });

        if (savedInstanceState == null) {
            if (Sceneform.isSupported(this)) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.arFragment, ArFragment.class, null)
                        .commit();
            }
        }

        greetingCards.forEach(this::loadModelsFromCard);

    }

    /**
     * Get the card value from arraylist and put them to hashmap
     * @param card
     */
    private void loadModelsFromCard(CardDisplayVO card) {
        ArModelSet set = new ArModelSet();
        loadModel("Text/" + card.getTextId() + ".glb", model -> set.textModel = model);
        loadModel("Emojis/" + card.getEmojiId() + ".glb", model -> set.emojiModel = model);
        loadModel("Animations/" + card.getAnimationId() + ".glb", model -> set.animationModel = model);
        arModelMap.put(card,set);
    }

    /**
     * Load ar models from files
     * @param path
     * @param loadModel
     */
    private void loadModel(String path, Consumer<Renderable> loadModel) {

        WeakReference<ArActivity> weakActivity = new WeakReference<>(this);
        ModelRenderable.builder()
                .setSource(this, Uri.parse(path))
                .setIsFilamentGltf(true)
                .build()
                .thenAccept(model -> {
                    ArActivity activity = weakActivity.get();
                    if (activity != null) {
                        loadModel.accept(model);
                    }
                })
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load model from" + path, Toast.LENGTH_LONG).show();
                            return null;
                        });

    }

    /**
     * Help cards displayed in correct positions
     * @param hitResult   The ARCore hit result that occurred when tapping the plane
     * @param plane       The ARCore Plane that was tapped
     * @param motionEvent the motion event that triggered the tap
     */
    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        if (arModelMap.isEmpty()) {
            Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        int j = -1;
        float currentOffset = 0;
        int currentRotation = 0;
        for (CardDisplayVO card: greetingCards) {
            ArModelSet set = arModelMap.get(card);

            float cardGap = 8f;
            currentOffset += i * j * cardGap;
            currentRotation += (-30)*j*i;

            Vector3 modelLocation = new Vector3(currentOffset, 2f, -6f);
            Vector3 animationLocation =  new Vector3(currentOffset, 5f, -6f);

            if (set == null) {
                return;
            }

            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            Quaternion rotation = new Quaternion(Vector3.up(),-90 + currentRotation);

            createStaticTransformableNode(anchorNode, set.textModel, modelLocation, rotation);
            createStaticTransformableNode(anchorNode, set.emojiModel, modelLocation, rotation);
            createAnimationTransformableNode(anchorNode, set.animationModel, animationLocation, rotation);

            i++;
            j = j * (-1);


        }
    }

    /**
     * Method to display static model like text and emoji
     * @param anchorNode
     * @param model
     * @param position
     * @param rotation
     */
    private void createStaticTransformableNode(AnchorNode anchorNode, Renderable model, Vector3 position, Quaternion rotation ) {
        TransformableNode staticModelNode = new TransformableNode(arFragment.getTransformationSystem());
        staticModelNode.setParent(anchorNode);
        staticModelNode.setRenderable(model);
        staticModelNode.setLocalPosition(position);
        staticModelNode.setLocalRotation(rotation);
        staticModelNode.select();

    }

    /**
     * Display animation models
     * @param anchorNode
     * @param model
     * @param position
     * @param rotation
     */
    private void createAnimationTransformableNode(AnchorNode anchorNode, Renderable model, Vector3 position, Quaternion rotation) {
        TransformableNode animationModelNode = new TransformableNode(arFragment.getTransformationSystem());
        animationModelNode.setParent(anchorNode);
        animationModelNode.setRenderable(model).animate(true).start();
        animationModelNode.setLocalPosition(position);
        animationModelNode.setLocalRotation(rotation);
        animationModelNode.select();

    }

}
