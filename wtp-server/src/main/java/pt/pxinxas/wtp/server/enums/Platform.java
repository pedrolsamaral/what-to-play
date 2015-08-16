package pt.pxinxas.wtp.server.enums;

public enum Platform {

	PSX("PlayStation"),
	PS2("PlayStation_2"),
	PS3("PlayStation_3"),
	PS4("PlayStation_4"),
	PSVITA("PlayStation_Vita"),
	PSP("PSP"),
	XBOX("Xbox"),
	XBX360("Xbox_360"),
	XBOXONE("Xbox_One"),
	DOS("PC/MS-DOS"),
	WINDOWS_PRE2000("PC/Early_Windows", "PC/Windows_95", "PC/Windows_98"),
	WINDOWS_POS2000("PC/Windows_XP", "PC/Windows_Vista", "PC/Windows_7", "PC/Windows_8"),
	GB("Game_Boy"),
	GBC("Game_Boy_Color"),
	GBA("Game_Boy_Advance"),
	DS("Nintendo_DS"),
	DS3("Nintendo_3DS"),
	NES("NES"),
	SNES("SNES"),
	N64("Nintendo_64"),
	GC("Gamecube"),
	WII("Wii"),
	WIIU("Wii_U"),
	GG("Game_Gear"),
	MS("Master_System"),
	MD("Mega_Drive"),
	SS("Saturn"),
	DC("Dreamcast");

	private final String[] vrecUrls;

	private Platform(String... vrecUrls) {
		this.vrecUrls = vrecUrls;
	}

	public String[] getVrecUrls() {
		return vrecUrls;
	};

}
