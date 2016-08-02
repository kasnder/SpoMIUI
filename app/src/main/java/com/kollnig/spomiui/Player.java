package com.kollnig.spomiui;

import android.content.res.XModuleResources;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Player implements IXposedHookInitPackageResources, IXposedHookZygoteInit {
	/**
	 * Color matrix that flips the components (<code>-1.0f * c + 255 = 255 - c</code>)
	 * and keeps the alpha intact.
	 */
	private static final float[] NEGATIVE = {
			-1.0f,     0,     0,    0, 255, // red
			0, -1.0f,     0,    0, 255, // green
			0,     0, -1.0f,    0, 255, // blue
			0,     0,     0, 1.0f,   0  // alpha
	};

	private static String MODULE_PATH = null;

	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		MODULE_PATH = startupParam.modulePath;
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		if (!resparam.packageName.equals("com.spotify.music"))
			return;

		XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
		resparam.res.setReplacement("com.spotify.music", "drawable", "icn_notification_add_to_disabled", modRes.fwd(R.drawable.icn_notification_add_to_disabled));
		resparam.res.setReplacement("com.spotify.music", "drawable", "icn_notification_prev_disabled", modRes.fwd(R.drawable.icn_notification_prev_disabled));
		resparam.res.setReplacement("com.spotify.music", "drawable", "btn_player_in_collection_checked", modRes.fwd(R.drawable.btn_player_in_collection_checked));
		resparam.res.setReplacement("com.spotify.music", "drawable", "icn_notification_dismiss", modRes.fwd(R.drawable.icn_notification_dismiss));

		// resparam.res.setReplacement("com.spotify.music", "color", "notification_bg_color", Color.TRANSPARENT);

		resparam.res.hookLayout("com.spotify.music", "layout", "notification_small_player", new XC_LayoutInflated() {
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				liparam.view.setBackgroundColor(Color.TRANSPARENT); // notification_bg_color

				TextView artist = (TextView) liparam.view.findViewById(
						liparam.res.getIdentifier("title", "id", "com.spotify.music"));
				artist.setTextColor(Color.BLACK);

				ImageButton play = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("play", "id", "com.spotify.music"));
				play.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton pause = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("pause", "id", "com.spotify.music"));
				pause.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton next = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("next", "id", "com.spotify.music"));
				next.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton next_disabled = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("next_disabled", "id", "com.spotify.music"));
				next_disabled.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
			}
		});

		resparam.res.hookLayout("com.spotify.music", "layout", "notification_big_player", new XC_LayoutInflated() {
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				liparam.view.setBackgroundColor(Color.TRANSPARENT); // notification_bg_color

				TextView artist = (TextView) liparam.view.findViewById(
						liparam.res.getIdentifier("firstLine", "id", "com.spotify.music"));
				artist.setTextColor(Color.WHITE);

				ImageButton add_to = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("add_to", "id", "com.spotify.music"));
				add_to.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton prev = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("prev", "id", "com.spotify.music"));
				prev.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
			}
		});

		resparam.res.hookLayout("com.spotify.music", "layout", "notification_big_player_cluster", new XC_LayoutInflated() {
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				liparam.view.setBackgroundColor(Color.TRANSPARENT); // notification_bg_color

				TextView artist = (TextView) liparam.view.findViewById(
						liparam.res.getIdentifier("firstLine", "id", "com.spotify.music"));
				artist.setTextColor(Color.BLACK);

				ImageButton notification_positive_feedback = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_positive_feedback", "id", "com.spotify.music"));
				notification_positive_feedback.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageView notification_positive_feedback_disabled = (ImageView) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_positive_feedback_disabled", "id", "com.spotify.music"));
				notification_positive_feedback_disabled.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton notification_negative_feedback = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_negative_feedback", "id", "com.spotify.music"));
				notification_negative_feedback.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton notification_negative_feedback_disabled = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_negative_feedback_disabled", "id", "com.spotify.music"));
				notification_negative_feedback_disabled.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
			}
		});

		resparam.res.hookLayout("com.spotify.music", "layout", "notification_big_player_radio", new XC_LayoutInflated() {
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				liparam.view.setBackgroundColor(Color.TRANSPARENT); // notification_bg_color

				TextView artist = (TextView) liparam.view.findViewById(
						liparam.res.getIdentifier("firstLine", "id", "com.spotify.music"));
				artist.setTextColor(Color.BLACK);

				ImageButton notification_radio_thumb_down = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_radio_thumb_down", "id", "com.spotify.music"));
				notification_radio_thumb_down.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageView notification_radio_thumb_down_disabled = (ImageView) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_radio_thumb_down_disabled", "id", "com.spotify.music"));
				notification_radio_thumb_down_disabled.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton notification_radio_thumb_up = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_radio_thumb_up", "id", "com.spotify.music"));
				notification_radio_thumb_up.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));

				ImageButton notification_radio_thumb_up_disabled = (ImageButton) liparam.view.findViewById(
						liparam.res.getIdentifier("notification_radio_thumb_up_disabled", "id", "com.spotify.music"));
				notification_radio_thumb_up_disabled.getDrawable().setColorFilter(new ColorMatrixColorFilter(NEGATIVE));
			}
		});
	}
}