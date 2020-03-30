package org.mozilla.vrbrowser.ui.widgets.menus.library;

import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.IntDef;

import org.mozilla.vrbrowser.R;
import org.mozilla.vrbrowser.browser.SettingsStore;
import org.mozilla.vrbrowser.ui.widgets.WidgetPlacement;
import org.mozilla.vrbrowser.ui.widgets.menus.MenuWidget;
import org.mozilla.vrbrowser.utils.AnimationHelper;

import java.util.ArrayList;

public class SortingContextMenuWidget extends MenuWidget {

    @IntDef(value = {SORT_FILENAME_AZ, SORT_FILENAME_ZA, SORT_DATE_ASC, SORT_DATE_DESC})
    public @interface Order {}
    public static final int SORT_FILENAME_AZ = 0;
    public static final int SORT_FILENAME_ZA = 1;
    public static final int SORT_DATE_ASC = 2;
    public static final int SORT_DATE_DESC = 3;

    public interface SortingContextDelegate {
        void onItemSelected(@Order int item);
    }

    ArrayList<MenuItem> mItems;
    SortingContextDelegate mItemDelegate;

    public SortingContextMenuWidget(Context aContext) {
        super(aContext, R.layout.library_menu);
        initialize();

        createMenuItems();
    }

    private void initialize() {
        updateUI();
    }

    @Override
    public void updateUI() {
        super.updateUI();

        mAdapter.updateBackgrounds(
                R.drawable.library_context_menu_item_background_top,
                R.drawable.library_context_menu_item_background_bottom,
                R.drawable.library_context_menu_item_background_middle,
                R.drawable.library_context_menu_item_background_single);
        mAdapter.updateLayoutId(R.layout.library_menu_item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        updateUI();
    }

    @Override
    public void show(int aShowFlags) {
        super.show(aShowFlags);

        AnimationHelper.scaleIn(findViewById(R.id.menuContainer), 100, 0, null);
    }

    @Override
    public void hide(int aHideFlags) {
        AnimationHelper.scaleOut(findViewById(R.id.menuContainer), 100, 0, () -> SortingContextMenuWidget.super.hide(aHideFlags));
    }

    @Override
    protected void initializeWidgetPlacement(WidgetPlacement aPlacement) {
        aPlacement.visible = false;
        aPlacement.width =  WidgetPlacement.dpDimension(getContext(), R.dimen.library_menu_width);
        aPlacement.parentAnchorX = 0.0f;
        aPlacement.parentAnchorY = 1.0f;
        aPlacement.anchorX = 1.0f;
        aPlacement.anchorY = 1.0f;
        aPlacement.opaque = false;
        aPlacement.cylinder = true;
        aPlacement.translationZ = WidgetPlacement.unitFromMeters(getContext(), R.dimen.context_menu_z_distance);
    }

    public void setItemDelegate(SortingContextDelegate delegate) {
        mItemDelegate = delegate;;
    }

    private void createMenuItems() {
        mItems = new ArrayList<>();

        @Order int order = SettingsStore.getInstance(getContext()).getDownloadsSortingOrder();
        mItems.add(new MenuItem(
                getResources().getString(R.string.downloads_filename_sort_az),
                order == SORT_FILENAME_AZ ? R.drawable.ic_icon_check : -1,
                () -> {
                    SettingsStore.getInstance(getContext()).setDownloadsSortingOrder(SORT_FILENAME_AZ);
                    if (mItemDelegate != null) {
                        mItemDelegate.onItemSelected(SORT_FILENAME_AZ);
                    }
        }));

        mItems.add(new MenuItem(
                getResources().getString(R.string.downloads_filename_sort_za),
                order == SORT_FILENAME_ZA ? R.drawable.ic_icon_check : -1,
                () -> {
                    SettingsStore.getInstance(getContext()).setDownloadsSortingOrder(SORT_FILENAME_ZA);
                    if (mItemDelegate != null) {
                        mItemDelegate.onItemSelected(SORT_FILENAME_ZA);
                    }
        }));

        mItems.add(new MenuItem(
                getResources().getString(R.string.downloads_sort_download_date_asc),
                order == SORT_DATE_ASC ? R.drawable.ic_icon_check : -1,
                () -> {
                    SettingsStore.getInstance(getContext()).setDownloadsSortingOrder(SORT_DATE_ASC);
                    if (mItemDelegate != null) {
                        mItemDelegate.onItemSelected(SORT_DATE_ASC);
                    }
                }));

        mItems.add(new MenuItem(
                getResources().getString(R.string.downloads_sort_download_date_desc),
                order == SORT_DATE_DESC ? R.drawable.ic_icon_check : -1,
                () -> {
                    SettingsStore.getInstance(getContext()).setDownloadsSortingOrder(SORT_DATE_DESC);
                    if (mItemDelegate != null) {
                        mItemDelegate.onItemSelected(SORT_DATE_DESC);
                    }
                }));

        super.updateMenuItems(mItems);

        mWidgetPlacement.height = mItems.size() * WidgetPlacement.dpDimension(getContext(), R.dimen.library_menu_item_height);
        mWidgetPlacement.height += mBorderWidth * 2;
    }

}
