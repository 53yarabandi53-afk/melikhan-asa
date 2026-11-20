package com.melikhan.wand;

/**
 * Asanin butun ayarlari burada. Sayilari degistirip tekrar derleyerek denemeler yapabilirsin.
 */
public final class WandConfig {
	private WandConfig() {}

	// ---- Hiz (blok / tick, 20 tick = 1 saniye) ----
	public static final double GLIDE_SPEED = 0.6;   // normal suzulme (saniyede 12 blok)
	public static final double DIVE_SPEED = 1.2;    // dalis (saniyede 24 blok)

	// ---- Dalis ----
	/** Bu aciya (derece) veya daha asagi bakinca DALIS baslar. 90 = tam asagi. */
	public static final float DIVE_ENTER_PITCH = 80.0f;
	/** Dalistan cikis esigi (titremeyi onlemek icin biraz daha dusuk). */
	public static final float DIVE_EXIT_PITCH = 72.0f;
	/** Yukari bakarken asanin burnu en fazla bu kadar kalkar (negatif = yukari). */
	public static final float MAX_PITCH_UP = -60.0f;

	// ---- Oturma ----
	/** Oyuncunun oturdugu nokta asanin govde ekseninden ne kadar yukarida (blok). Oyuncu havada/yerde ise bunu ayarla. */
	public static final double SEAT_HEIGHT = 0.12;
	/** Oturan oyuncunun "kalca" noktasinin ayaklarindan yuksekligi. Oyuncu asaya gomuluyorsa/yukseliyorsa bunu ayarla. */
	public static final double RIDER_HIP_HEIGHT = 0.6;
	/** Ucustan inince verilen yavas dusme suresi (tick). 0 = kapali. */
	public static final int SLOW_FALL_TICKS = 100;

	// ---- Model yonu (asa yan yatirilip oyuncunun altina yerlestirilir) ----
	/** true: asanin goz/kanat kismi ileri bakar. false: geriye bakar. */
	public static final boolean HEAD_POINTS_FORWARD = true;
	/** Asayi kendi ekseni etrafinda cevirir (0 veya 180 dene). 180: goz yukari bakar. */
	public static final float MODEL_ROLL_DEGREES = 0.0f;
	/** Modelde oyuncunun oturdugu nokta (Blockbench birimi). Govde mili x=5, z=7'de. */
	public static final float PIVOT_X = 5.0f;
	public static final float PIVOT_Y = -3.0f;
	public static final float PIVOT_Z = 7.0f;

	public static boolean nextDiveState(boolean diving, float pitch) {
		return diving ? pitch >= DIVE_EXIT_PITCH : pitch >= DIVE_ENTER_PITCH;
	}
}
