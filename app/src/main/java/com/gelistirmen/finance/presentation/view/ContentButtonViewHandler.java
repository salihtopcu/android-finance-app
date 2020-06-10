package com.gelistirmen.finance.presentation.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gelistirmen.finance.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContentButtonViewHandler {

    public enum Type {
        Light, Dark
    }

    private FrameLayout view;

    @BindView(R.id.buttonContentLinearLayout) LinearLayout buttonContentLinearLayout;
    @BindView(R.id.iconImageView) ImageView iconImageView;
    @BindView(R.id.miniTitleTextView) TextView miniTitleTextView;
    @BindView(R.id.titleTextView) TextView titleTextView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.arrowImageView) ImageView arrowImageView;

//    public ContentButtonViewHandler(Context context, String miniTitleText, String titleText, String descriptionText, @DrawableRes Integer iconId, Integer backgroundColor, Integer tintColor, View.OnClickListener onClickListener) {
    @SuppressLint("ResourceAsColor")
    public ContentButtonViewHandler(Context context, String miniTitleText, String titleText, String descriptionText, @DrawableRes Integer iconId, Type type, View.OnClickListener onClickListener) {
        if (type == Type.Light)
            this.view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_content_button, null, false);
        else
            this.view = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.view_content_button_dark, null, false);
        ButterKnife.bind(this, this.view);


        if (miniTitleText == null || miniTitleText.isEmpty())
            this.miniTitleTextView.setVisibility(View.GONE);
        else
            this.miniTitleTextView.setText(miniTitleText.toString());

        if (titleText == null || titleText.isEmpty())
            this.titleTextView.setVisibility(View.GONE);
        else
            this.titleTextView.setText(titleText.toString());

        if (descriptionText == null || descriptionText.isEmpty())
            this.descriptionTextView.setVisibility(View.GONE);
        else
            this.descriptionTextView.setText(descriptionText.toString());

        if (iconId == null)
            this.iconImageView.setVisibility(View.GONE);
        else
            this.iconImageView.setImageResource(iconId);
    }

    public ContentButtonViewHandler(Context context, String miniTitleText, String titleText, String descriptionText, @DrawableRes int iconId, View.OnClickListener onClickListener) {
        this(context, miniTitleText, titleText, descriptionText, iconId, Type.Light, onClickListener);
    }

    public FrameLayout getView() {
        return this.view;
    }
}
