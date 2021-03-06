package com.ucla.jam.music.responses;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Style {
    BOOGIE_WOOGIE("Boogie Woogie"),
    PUB_ROCK("Pub Rock"),
    CHICAGO_BLUES("Chicago Blues"),
    ALTERNATIVE_ROCK("Alternative Rock"),
    COUNTRY_ROCK("Country Rock"),
    COUNTRY_BLUES("Country Blues"),
    SOUNDTRACK("Soundtrack"),
    DELTA_BLUES("Delta Blues"),
    BLUES_ROCK("Blues Rock"),
    POP_RAP("Pop Rap"),
    EAST_COAST_BLUES("East Coast Blues"),
    ELECTRIC_BLUES("Electric Blues"),
    HARMONICA_BLUES("Harmonica Blues"),
    FOLK_ROCK("Folk Rock"),
    ROCK_AND_ROLL("Rock & Roll"),
    AOR("AOR"),
    HILL_COUNTRY_BLUES("Hill Country Blues"),
    RHYTHM_AND_BLUES("Rhythm & Blues"),
    JUMP_BLUES("Jump Blues"),
    SOUL_JAZZ("Soul-Jazz"),
    LOUISIANA_BLUES("Louisiana Blues"),
    FUSION("Fusion"),
    BAYOU_FUNK("Bayou Funk"),
    MEMPHIS_BLUES("Memphis Blues"),
    SOUL("Soul"),
    FOLK("Folk"),
    MODERN_ELECTRIC_BLUES("Modern Electric Blues"),
    PROG_ROCK("Prog Rock"),
    PIANO_BLUES("Piano Blues"),
    SOFT_ROCK("Soft Rock"),
    RAGTIME("Ragtime"),
    CONTEMPORARY_JAZZ("Contemporary Jazz"),
    PIEDMONT_BLUES("Piedmont Blues"),
    CLASSIC_ROCK("Classic Rock"),
    TEXAS_BLUES("Texas Blues"),
    BRASS_BAND("Brass Band"),
    JAZZ_FUNK("Jazz-Funk"),
    INDUSTRIAL("Industrial"),
    MARCHES("Marches"),
    CONTEMPORARY("Contemporary"),
    NEO_CLASSICAL("Neo-Classical"),
    MILITARY("Military"),
    PIPE_AND_DRUM("Pipe & Drum"),
    EDUCATIONAL("Educational"),
    STORY("Story"),
    NURSERY_RHYMES("Nursery Rhymes"),
    BAROQUE("Baroque"),
    ABSTRACT("Abstract"),
    EXPERIMENTAL("Experimental"),
    AMBIENT("Ambient"),
    CHORAL("Choral"),
    HARD_ROCK("Hard Rock"),
    SYMPHONIC_ROCK("Symphonic Rock"),
    CLASSICAL("Classical"),
    EARLY("Early"),
    IMPRESSIONIST("Impressionist"),
    MODERN_CLASSICAL("Modern Classical"),
    MEDIEVAL("Medieval"),
    RENAISSANCE("Renaissance"),
    ABORIGINAL("Aboriginal"),
    MODERN("Modern"),
    DARKWAVE("Darkwave"),
    SCORE("Score"),
    NEO_ROMANTIC("Neo-Romantic"),
    OPERA("Opera"),
    EASY_LISTENING("Easy Listening"),
    MUSICAL("Musical"),
    OPERETTA("Operetta"),
    VOCAL("Vocal"),
    ORATORIO("Oratorio"),
    LOUNGE("Lounge"),
    POST_MODERN("Post-Modern"),
    ROMANTIC("Romantic"),
    SERIAL("Serial"),
    TWELVE_TONE("Twelve-tone"),
    ZARZUELA("Zarzuela"),
    IDM("IDM"),
    TECHNO("Techno"),
    ACID("Acid"),
    HOUSE("House"),
    ACID_HOUSE("Acid House"),
    ACID_JAZZ("Acid Jazz"),
    DOWNTEMPO("Downtempo"),
    BALLROOM("Ballroom"),
    RNB_SWING("RnB/Swing"),
    ROCKSTEADY("Rocksteady"),
    SKA("Ska"),
    BALTIMORE_CLUB("Baltimore Club"),
    UK_GARAGE("UK Garage"),
    BASSLINE("Bassline"),
    BEATDOWN("Beatdown"),
    BERLIN_SCHOOL("Berlin-School"),
    BREAKBEAT("Breakbeat"),
    BIG_BEAT("Big Beat"),
    BLEEP("Bleep"),
    HARDCORE("Hardcore"),
    JUNGLE("Jungle"),
    BREAKCORE("Breakcore"),
    BREAKS("Breaks"),
    BROKEN_BEAT("Broken Beat"),
    CHILLWAVE("Chillwave"),
    GLITCH("Glitch"),
    ELECTRO("Electro"),
    CHIPTUNE("Chiptune"),
    SYNTH_POP("Synth-pop"),
    DISCO("Disco"),
    DANCE_POP("Dance-pop"),
    BLACK_METAL("Black Metal"),
    DOOM_METAL("Doom Metal"),
    DRONE("Drone"),
    DARK_AMBIENT("Dark Ambient"),
    DEEP_HOUSE("Deep House"),
    FUTURE_JAZZ("Future Jazz"),
    DEEP_TECHNO("Deep Techno"),
    NEW_WAVE("New Wave"),
    POP_ROCK("Pop Rock"),
    PUNK("Punk"),
    DISCO_POLO("Disco Polo"),
    EURODANCE("Eurodance"),
    TRANCE("Trance"),
    DONK("Donk"),
    GABBER("Gabber"),
    DOOMCORE("Doomcore"),
    DRUM_N_BASS("Drum n Bass"),
    POST_PUNK("Post-Punk"),
    DUB("Dub"),
    AVANTGARDE("Avantgarde"),
    DUB_TECHNO("Dub Techno"),
    MINIMAL_TECHNO("Minimal Techno"),
    DUBSTEP("Dubstep"),
    DUNGEON_SYNTH("Dungeon Synth"),
    EBM("EBM"),
    ELECTRO_HOUSE("Electro House"),
    TRIP_HOP("Trip Hop"),
    ELECTRO_SWING("Electro Swing"),
    ACOUSTIC("Acoustic"),
    ELECTROACOUSTIC("Electroacoustic"),
    EUROPOP("Europop"),
    ELECTROCLASH("Electroclash"),
    EURO_HOUSE("Euro House"),
    EURO_DISCO("Euro-Disco"),
    EUROBEAT("Eurobeat"),
    HARD_TRANCE("Hard Trance"),
    PSYCHEDELIC_ROCK("Psychedelic Rock"),
    GHETTO("Ghetto"),
    BASS_MUSIC("Bass Music"),
    HIP_HOP("Hip Hop"),
    FOOTWORK("Footwork"),
    FREESTYLE("Freestyle"),
    J_CORE("J-Core"),
    TRIBAL("Tribal"),
    FUNKOT("Funkot"),
    CONTEMPORARY_RANDB("Contemporary R&B"),
    BALLAD("Ballad"),
    GARAGE_HOUSE("Garage House"),
    GHETTO_HOUSE("Ghetto House"),
    GHETTOTECH("Ghettotech"),
    GLITCH_HOP("Glitch Hop"),
    GOA_TRANCE("Goa Trance"),
    GRIME("Grime"),
    HALFTIME("Halftime"),
    HANDS_UP("Hands Up"),
    HAPPY_HARDCORE("Happy Hardcore"),
    NEW_BEAT("New Beat"),
    HARD_BEAT("Hard Beat"),
    PROGRESSIVE_HOUSE("Progressive House"),
    TECH_HOUSE("Tech House"),
    HARD_HOUSE("Hard House"),
    HARD_TECHNO("Hard Techno"),
    HARDSTYLE("Hardstyle"),
    HARSH_NOISE_WALL("Harsh Noise Wall"),
    HI_NRG("Hi NRG"),
    FUNK("Funk"),
    P_FUNK("P.Funk"),
    HIP_HOUSE("Hip-House"),
    ILLBIENT("Illbient"),
    SPOKEN_WORD("Spoken Word"),
    ITALO_HOUSE("Italo House"),
    ITALO_DISCO("Italo-Disco"),
    ITALODANCE("Italodance"),
    SPEEDCORE("Speedcore"),
    JAZZDANCE("Jazzdance"),
    JERSEY_CLUB("Jersey Club"),
    JUKE("Juke"),
    JUMPSTYLE("Jumpstyle"),
    LATIN("Latin"),
    JAZZY_HIP_HOP("Jazzy Hip-Hop"),
    LEFTFIELD("Leftfield"),
    LENTO_VIOLENTO("Lento Violento"),
    MAKINA("Makina"),
    MINIMAL("Minimal"),
    RAGGA_HIPHOP("Ragga HipHop"),
    DANCEHALL("Dancehall"),
    MOOMBAHTON("Moombahton"),
    MUSIQUE_CONCRETE("Musique Concr??te"),
    NEO_TRANCE("Neo Trance"),
    TECH_TRANCE("Tech Trance"),
    NEOFOLK("Neofolk"),
    NERDCORE_TECHNO("Nerdcore Techno"),
    NEW_AGE("New Age"),
    ART_ROCK("Art Rock"),
    NOISE("Noise"),
    INDIE_ROCK("Indie Rock"),
    NU_DISCO("Nu-Disco"),
    NEO_SOUL("Neo Soul"),
    POWER_ELECTRONICS("Power Electronics"),
    PROGRESSIVE_TRANCE("Progressive Trance"),
    PROGRESSIVE_BREAKS("Progressive Breaks"),
    TRIBAL_HOUSE("Tribal House"),
    PSY_TRANCE("Psy-Trance"),
    RHYTHMIC_NOISE("Rhythmic Noise"),
    SCHRANZ("Schranz"),
    SKWEEE("Skweee"),
    EDUCATION("Education"),
    SOUND_COLLAGE("Sound Collage"),
    SPEED_GARAGE("Speed Garage"),
    SYNTHWAVE("Synthwave"),
    POST_ROCK("Post Rock"),
    TROPICAL_HOUSE("Tropical House"),
    UK_FUNKY("UK Funky"),
    VAPORWAVE("Vaporwave"),
    WITCH_HOUSE("Witch House"),
    PSYCHEDELIC("Psychedelic"),
    LATIN_JAZZ("Latin Jazz"),
    AFRICAN("African"),
    ANDALUSIAN_CLASSICAL("Andalusian Classical"),
    ANDEAN_MUSIC("Andean Music"),
    APPALACHIAN_MUSIC("Appalachian Music"),
    HINDUSTANI("Hindustani"),
    GHAZAL("Ghazal"),
    BHANGRA("Bhangra"),
    BENGALI_MUSIC("Bengali Music"),
    RELIGIOUS("Religious"),
    BANGLADESHI_CLASSICAL("Bangladeshi Classical"),
    BASQUE_MUSIC("Basque Music"),
    INDIAN_CLASSICAL("Indian Classical"),
    RADIOPLAY("Radioplay"),
    SPEECH("Speech"),
    BLUEGRASS("Bluegrass"),
    CAJUN("Cajun"),
    CALYPSO("Calypso"),
    REGGAE("Reggae"),
    CAMBODIAN_CLASSICAL("Cambodian Classical"),
    JAZZ_ROCK("Jazz-Rock"),
    CANZONE_NAPOLETANA("Canzone Napoletana"),
    CARNATIC("Carnatic"),
    ETHEREAL("Ethereal"),
    CATALAN_MUSIC("Catalan Music"),
    CELTIC("Celtic"),
    CHACARERA("Chacarera"),
    ZAMBA("Zamba"),
    BOLERO("Bolero"),
    POLKA("Polka"),
    GUARANIA("Guarania"),
    CHAMAME("Chamam??"),
    CHINESE_CLASSICAL("Chinese Classical"),
    SALSA("Salsa"),
    KASEKO("Kaseko"),
    CHUTNEY("Chutney"),
    COBLA("Cobla"),
    COPLA("Copla"),
    COUNTRY("Country"),
    ENTEKHNO("??ntekhno"),
    CRETAN("Cretan"),
    DANGDUT("Dangdut"),
    LAIKO("La??k??"),
    FADO("Fado"),
    FILK("Filk"),
    FLAMENCO("Flamenco"),
    FUNANA("Funan??"),
    ZOUK("Zouk"),
    GAGAKU("Gagaku"),
    GAMELAN("Gamelan"),
    GALICIAN_TRADITIONAL("Galician Traditional"),
    KRAUTROCK("Krautrock"),
    GHANA("G??ana"),
    BOLLYWOOD("Bollywood"),
    QAWWALI("Qawwali"),
    GRIOT("Griot"),
    HIGHLIFE("Highlife"),
    ROOTS_REGGAE("Roots Reggae"),
    SOCA("Soca"),
    MPB("MPB"),
    GWO_KA("Gwo Ka"),
    HAWAIIAN("Hawaiian"),
    AFROBEAT("Afrobeat"),
    HONKY_TONK("Honky Tonk"),
    HILLBILLY("Hillbilly"),
    SOUTHERN_ROCK("Southern Rock"),
    HUAYNO("Huayno"),
    MARIACHI("Mariachi"),
    JOTA("Jota"),
    PASODOBLE("Pasodoble"),
    JUG_BAND("Jug Band"),
    KERONCONG("Keroncong"),
    DANZON("Danzon"),
    KIZOMBA("Kizomba"),
    KLASIK("Klasik"),
    MUSIC_HALL("Music Hall"),
    KLEZMER("Klezmer"),
    KOREAN_COURT_MUSIC("Korean Court Music"),
    LAO_MUSIC("Lao Music"),
    PERSIAN_CLASSICAL("Persian Classical"),
    TANGO("Tango"),
    LISCIO("Liscio"),
    LUK_KRUNG("Luk Krung"),
    BEAT("Beat"),
    LUK_THUNG("Luk Thung"),
    CHANSON("Chanson"),
    MALOYA("Maloya"),
    MBALAX("Mbalax"),
    MINYO("Min???y??"),
    MIZRAHI("Mizrahi"),
    MO_LAM("Mo Lam"),
    MORNA("Morna"),
    FIELD_RECORDING("Field Recording"),
    MOUTH_MUSIC("Mouth Music"),
    MUGHAM("Mugham"),
    NEPZENE("N??pzene"),
    GARAGE_ROCK("Garage Rock"),
    NHAC_VANG("Nh???c V??ng"),
    VIKING_METAL("Viking Metal"),
    NORDIC("Nordic"),
    OTTOMAN_CLASSICAL("Ottoman Classical"),
    OVERTONE_SINGING("Overtone Singing"),
    SPACE_AGE("Space-Age"),
    PACIFIC("Pacific"),
    NOVELTY("Novelty"),
    PHILIPPINE_CLASSICAL("Philippine Classical"),
    PHLENG_PHUEA_CHIWIT("Phleng Phuea Chiwit"),
    PIOBAIREACHD("Piobaireachd"),
    PARODY("Parody"),
    COMEDY("Comedy"),
    PROGRESSIVE_BLUEGRASS("Progressive Bluegrass"),
    RAI("Ra??"),
    REBETIKO("Rebetiko"),
    ROMANI("Romani"),
    RUNE_SINGING("Rune Singing"),
    SALEGY("Salegy"),
    SAMI_MUSIC("S??mi Music"),
    SCHLAGER("Schlager"),
    SEA_SHANTIES("Sea Shanties"),
    SEGA("S??ga"),
    SEPHARDIC("Sephardic"),
    SOUKOUS("Soukous"),
    RUMBA("Rumba"),
    TAARAB("Taarab"),
    TAMIL_FILM_MUSIC("Tamil Film Music"),
    THAI_CLASSICAL("Thai Classical"),
    VOLKSMUSIK("Volksmusik"),
    WAIATA("Waiata"),
    WESTERN_SWING("Western Swing"),
    YEMENITE_JEWISH("Yemenite Jewish"),
    ZEMER_IVRI("Zemer Ivri"),
    ZYDECO("Zydeco"),
    BOOGIE("Boogie"),
    CONSCIOUS("Conscious"),
    FREE_FUNK("Free Funk"),
    THEME("Theme"),
    COOL_JAZZ("Cool Jazz"),
    GOGO("Gogo"),
    SWINGBEAT("Swingbeat"),
    GOSPEL("Gospel"),
    MINNEAPOLIS_SOUND("Minneapolis Sound"),
    NEW_JACK_SWING("New Jack Swing"),
    UK_STREET_SOUL("UK Street Soul"),
    BEATBOX("Beatbox"),
    BONGO_FLAVA("Bongo Flava"),
    HIPLIFE("Hiplife"),
    KWAITO("Kwaito"),
    BOOM_BAP("Boom Bap"),
    BOUNCE("Bounce"),
    BRITCORE("Britcore"),
    CLOUD_RAP("Cloud Rap"),
    GANGSTA("Gangsta"),
    CRUNK("Crunk"),
    CUT_UP_DJ("Cut-up/DJ"),
    DJ_BATTLE_TOOL("DJ Battle Tool"),
    FAVELA_FUNK("Favela Funk"),
    G_FUNK("G-Funk"),
    GO_GO("Go-Go"),
    HARDCORE_HIP_HOP("Hardcore Hip-Hop"),
    HORRORCORE("Horrorcore"),
    HYPHY("Hyphy"),
    POST_BOP("Post Bop"),
    MODAL("Modal"),
    INSTRUMENTAL("Instrumental"),
    MIAMI_BASS("Miami Bass"),
    MOTSWAKO("Motswako"),
    PHONK("Phonk"),
    SCREW("Screw"),
    THUG_RAP("Thug Rap"),
    SPAZA("Spaza"),
    DREAM_POP("Dream Pop"),
    INDIE_POP("Indie Pop"),
    TRAP("Trap"),
    TURNTABLISM("Turntablism"),
    AFRO_CUBAN_JAZZ("Afro-Cuban Jazz"),
    AVANT_GARDE_JAZZ("Avant-garde Jazz"),
    HARD_BOP("Hard Bop"),
    BIG_BAND("Big Band"),
    BOP("Bop"),
    SMOOTH_JAZZ("Smooth Jazz"),
    BOSSA_NOVA("Bossa Nova"),
    JANGLE_POP("Jangle Pop"),
    CAPE_JAZZ("Cape Jazz"),
    DARK_JAZZ("Dark Jazz"),
    DIXIELAND("Dixieland"),
    FREE_IMPROVISATION("Free Improvisation"),
    FREE_JAZZ("Free Jazz"),
    GYPSY_JAZZ("Gypsy Jazz"),
    AFRO_CUBAN("Afro-Cuban"),
    ROCKABILLY("Rockabilly"),
    SWING("Swing"),
    STRIDE("Stride"),
    AGUINALDO("Aguinaldo"),
    SAMBA("Samba"),
    AXE("Ax??"),
    BACHATA("Bachata"),
    BAIAO("Bai??o"),
    FORRO("Forr??"),
    CHORO("Choro"),
    BAMBUCO("Bambuco"),
    BATUCADA("Batucada"),
    BEGUINE("Beguine"),
    CHA_CHA("Cha-Cha"),
    TWIST("Twist"),
    SON("Son"),
    TROVA("Trova"),
    GUAJIRA("Guajira"),
    GUAGUANCO("Guaguanc??"),
    BOMBA("Bomba"),
    BOOGALOO("Boogaloo"),
    BOSSANOVA("Bossanova"),
    CANDOMBE("Candombe"),
    CARIMBO("Carimb??"),
    CUMBIA("Cumbia"),
    DESCARGA("Descarga"),
    CHAMPETA("Champeta"),
    CHARANGA("Charanga"),
    MERENGUE("Merengue"),
    COMPAS("Compas"),
    CONJUNTO("Conjunto"),
    RANCHERA("Ranchera"),
    CORRIDO("Corrido"),
    MARIMBA("Marimba"),
    CUATRO("Cuatro"),
    MAMBO("Mambo"),
    CUBANO("Cubano"),
    GAITA("Gaita"),
    GUARACHA("Guaracha"),
    JIBARO("Jibaro"),
    JOROPO("Joropo"),
    LAMBADA("Lambada"),
    REGGAE_POP("Reggae-Pop"),
    MARCHA_CARNAVALESCA("Marcha Carnavalesca"),
    MUSETTE("Musette"),
    MUSICA_CRIOLLA("M??sica Criolla"),
    NORTENO("Norte??o"),
    NUEVA_CANCION("Nueva Cancion"),
    NUEVA_TROVA("Nueva Trova"),
    OCCITAN("Occitan"),
    PACHANGA("Pachanga"),
    PLENA("Plena"),
    PORRO("Porro"),
    QUECHUA("Quechua"),
    REGGAETON("Reggaeton"),
    SAMBA_CANCAO("Samba-Can????o"),
    SERESTA("Seresta"),
    SON_MONTUNO("Son Montuno"),
    SONERO("Sonero"),
    TEJANO("Tejano"),
    TIMBA("Timba"),
    VALLENATO("Vallenato"),
    AUDIOBOOK("Audiobook"),
    SURF("Surf"),
    DIALOGUE("Dialogue"),
    EROTIC("Erotic"),
    HEALTH_FITNESS("Health-Fitness"),
    INTERVIEW("Interview"),
    MONOLOG("Monolog"),
    MOVIE_EFFECTS("Movie Effects"),
    POETRY("Poetry"),
    POLITICAL("Political"),
    PROMOTIONAL("Promotional"),
    PUBLIC_BROADCAST("Public Broadcast"),
    SPEED_METAL("Speed Metal"),
    THRASH("Thrash"),
    HEAVY_METAL("Heavy Metal"),
    PUBLIC_SERVICE_ANNOUNCEMENT("Public Service Announcement"),
    SERMON("Sermon"),
    SOUND_ART("Sound Art"),
    GRUNGE("Grunge"),
    SOUND_POETRY("Sound Poetry"),
    SPECIAL_EFFECTS("Special Effects"),
    TECHNICAL("Technical"),
    SPACE_ROCK("Space Rock"),
    THERAPY("Therapy"),
    BARBERSHOP("Barbershop"),
    BREAK_IN("Break-In"),
    BUBBLEGUM("Bubblegum"),
    CANTOPOP("Cantopop"),
    CITY_POP("City Pop"),
    ENKA("Enka"),
    KAYOKYOKU("Kay??kyoku"),
    ETHNO_POP("Ethno-pop"),
    HOKKIEN_POP("Hokkien Pop"),
    INDO_POP("Indo-Pop"),
    J_POP("J-pop"),
    K_POP("K-pop"),
    KARAOKE("Karaoke"),
    LEVENSLIED("Levenslied"),
    LIGHT_MUSIC("Light Music"),
    MANDOPOP("Mandopop"),
    NEO_KYMA("N??o Kyma"),
    RYUKOKA("Ry??k??ka"),
    VILLANCICOS("Villancicos"),
    AZONTO("Azonto"),
    BUBBLING("Bubbling"),
    LOVERS_ROCK("Lovers Rock"),
    DUB_POETRY("Dub Poetry"),
    JUNKANOO("Junkanoo"),
    MENTO("Mento"),
    RAGGA("Ragga"),
    RAPSO("Rapso"),
    REGGAE_GOSPEL("Reggae Gospel"),
    STEEL_BAND("Steel Band"),
    ACID_ROCK("Acid Rock"),
    ARENA_ROCK("Arena Rock"),
    GLAM("Glam"),
    ATMOSPHERIC_BLACK_METAL("Atmospheric Black Metal"),
    BRIT_POP("Brit Pop"),
    COLDWAVE("Coldwave"),
    CRUST("Crust"),
    DEATH_METAL("Death Metal"),
    DEATHCORE("Deathcore"),
    GOTH_ROCK("Goth Rock"),
    DEATHROCK("Deathrock"),
    DEPRESSIVE_BLACK_METAL("Depressive Black Metal"),
    DOO_WOP("Doo Wop"),
    EMO("Emo"),
    FOLK_METAL("Folk Metal"),
    FUNERAL_DOOM_METAL("Funeral Doom Metal"),
    FUNK_METAL("Funk Metal"),
    GRINDCORE("Grindcore"),
    GOREGRIND("Goregrind"),
    GOTHIC_METAL("Gothic Metal"),
    GROOVE_METAL("Groove Metal"),
    HORROR_ROCK("Horror Rock"),
    INDUSTRIAL_METAL("Industrial Metal"),
    J_ROCK("J-Rock"),
    K_ROCK("K-Rock"),
    LO_FI("Lo-Fi"),
    MATH_ROCK("Math Rock"),
    PROGRESSIVE_METAL("Progressive Metal"),
    MELODIC_DEATH_METAL("Melodic Death Metal"),
    MELODIC_HARDCORE("Melodic Hardcore"),
    METALCORE("Metalcore"),
    MOD("Mod"),
    NDW("NDW"),
    NO_WAVE("No Wave"),
    NOISECORE("Noisecore"),
    NU_METAL("Nu Metal"),
    OI("Oi"),
    POP_PUNK("Pop Punk"),
    PORNOGRIND("Pornogrind"),
    POST_HARDCORE("Post-Hardcore"),
    POST_METAL("Post-Metal"),
    POWER_METAL("Power Metal"),
    POWER_POP("Power Pop"),
    POWER_VIOLENCE("Power Violence"),
    SLUDGE_METAL("Sludge Metal"),
    PSYCHOBILLY("Psychobilly"),
    ROCK_OPERA("Rock Opera"),
    SHOEGAZE("Shoegaze"),
    SKIFFLE("Skiffle"),
    STONER_ROCK("Stoner Rock"),
    SWAMP_POP("Swamp Pop"),
    SYMPHONIC_METAL("Symphonic Metal"),
    TECHNICAL_DEATH_METAL("Technical Death Metal"),
    YE_YE("Y??-Y??"),
    CABARET("Cabaret"),
    VAUDEVILLE("Vaudeville"),
    VIDEO_GAME_MUSIC("Video Game Music");

    @JsonValue
    public final String name;
}
