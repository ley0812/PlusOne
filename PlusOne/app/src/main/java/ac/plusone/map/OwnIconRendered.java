package ac.plusone.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import ac.plusone.R;

/**
 * Created by Admin on 2015-11-27.
 */
public class OwnIconRendered extends DefaultClusterRenderer<RealEstate> {

    private final IconGenerator mIconGenerator;
    private ShapeDrawable mColoredCircleBackground;
    private SparseArray<BitmapDescriptor> mIcons = new SparseArray();
    private final float mDensity;
    private Context mContext;

    public OwnIconRendered(Context context, GoogleMap map,
                           ClusterManager<RealEstate> clusterManager) {
        super(context, map, clusterManager);

        this.mContext = context;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mIconGenerator = new IconGenerator(context);
        this.mIconGenerator.setContentView(this.makeSquareTextView(context));
        this.mIconGenerator.setTextAppearance(
                com.google.maps.android.R.style.ClusterIcon_TextAppearance);
        this.mIconGenerator.setBackground(this.makeClusterBackground());
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<RealEstate> cluster,
                                           MarkerOptions markerOptions) {
        // Main color

        int bucket = this.getBucket(cluster);
        BitmapDescriptor descriptor = this.mIcons.get(bucket);
        if (descriptor == null) {
            this.mColoredCircleBackground.getPaint().setColor(0xFFF57F17);
            this.mColoredCircleBackground.getPaint().setStrokeWidth(0);
            descriptor = BitmapDescriptorFactory.fromBitmap(
                    this.mIconGenerator.makeIcon(this.getClusterText(bucket)));
            this.mIcons.put(bucket, descriptor);
        }
        markerOptions.icon(descriptor);
    }

    @Override
    protected void onBeforeClusterItemRendered(RealEstate item, MarkerOptions markerOptions){
        Drawable d = mContext.getResources().getDrawable(R.drawable.marker2);
        Bitmap bitmap = ((BitmapDrawable)d ).getBitmap();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        super.onBeforeClusterItemRendered(item, markerOptions);

    }


    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(com.google.maps.android.R.id.text);
        int twelveDpi = (int) (12.0F * this.mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }

    private LayerDrawable makeClusterBackground() {
        // Outline color
        int clusterOutlineColor = mContext.getResources().getColor(R.color.colorPrimary);

        this.mColoredCircleBackground = new ShapeDrawable(new OvalShape());
        ShapeDrawable outline = new ShapeDrawable(new OvalShape());
        outline.getPaint().setColor(clusterOutlineColor);
        LayerDrawable background = new LayerDrawable(
                new Drawable[]{outline, this.mColoredCircleBackground});
        int strokeWidth = (int) (this.mDensity * 0.0F);
        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return background;
    }
}