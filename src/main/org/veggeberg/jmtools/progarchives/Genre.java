package org.veggeberg.jmtools.progarchives;

public enum Genre {
	CANTEBURY_SCENE(12, "Canterbury Scene", "CanterburyScene"),
	CROSSOVER_PROG(3, "Crossover Prog", "CrossoverProg"),
	ECLECTIC_PROG(42, "Eclectic Prog", "EclecticProg"),
	EXPERIMENTAL_POST_METAL(44, "Experimental/Post Metal", "ExperimentalPostMetal"),
	HEAVY_PROG(41, "Heavy Prog", "HeavyProg"),
	INDO_PROG_RAGA_ROCK(35, "Indo-Prog/Raga Rock", "Indo-ProgRagaRock"),
	JAZZ_ROCK_FUSION(30, "Jazz Rock/Fusion", "JazzRockFusion"),
	KRAUTROCK(17, "Krautrock", "Krautrock"),
	NEO_PROG(18, "Neo-Prog", "Neo-Prog"),
	POST_ROCK_MATH_ROCK(32, "Post Rock/Math Rock", "PostRockMathRock"),
	PROG_FOLK(6, "Prog Folk", "ProgFolk"),
	PROGRESSIVE_ELECTRONIC(33, "Progressive Electronic", "Progressive Electronic"),
	PROGRESSIVE_METAL(19, "Progressive Metal", "ProgressiveMetal"),
	PSYCEDLIC_SPACE_ROCK(15, "Psychedelic/Space Rock", "PsychedelicSpaceRock"),
	RIO_AVANT_PROG(36, "RIO/Avant-Prog", "RIOAvant-Prog"),
	ROCK_PROGRESSIVO_ITALIANO(28, "Rock Progressivo Italiano", "RockProgressivoItaliano"),
	SYMPHONIC_PROG(4, "Symphonic Prog", "SymphonicProg"),
	TECH_EXTREME_PROG_METAL(43, "Tech/Extreme Prog Metal", "TechExtremeProgMetal"),
	ZEUHL(11, "Zeuhl", "Zeuhl"),
	VARIOUS_GENRES(29, "Various Genres/Artists", "VariousGenresArtists"),
	PROG_RELATED(38, "Prog Related", "ProgRelated"),
	PROTO_PROG(37, "Proto-Prog", "Proto-Prog");
	
	Genre(int id, String name, String dirName) {
		this.id = id;
		this.name = name;
		this.dirName = dirName;
	}
	private int id;
	private String name;
	private String dirName;
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getDirName() {
		return dirName;
	}
}
