package business;

public class Catalog {
	private int id;
	private String name;
	public Catalog(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return String.format("[%d, %s]", id, name);
	}
	
}
