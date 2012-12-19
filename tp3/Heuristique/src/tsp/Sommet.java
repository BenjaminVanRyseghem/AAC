package tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Sommet {

	private static Map<Integer,Sommet> sommets = new HashMap<Integer,Sommet>();
	private int id;
	private List<Arrete> arretes_sortantes;
	private List<Arrete> arretes_entrantes;
	
	private Sommet(int id){
		this.id = id;
		this.sommets.put(id, this);
		this.arretes_sortantes = new ArrayList<Arrete>();
		this.arretes_entrantes = new ArrayList<Arrete>();
	}
	
	static public Sommet getSommet(int id){
		Sommet retrievedSommet = sommets.get(id);
		if(retrievedSommet == null){
			retrievedSommet = new Sommet(id);
		}
		return retrievedSommet;
	}

	public List<Arrete> getArretes_sortantes() {
		return arretes_sortantes;
	}

	public List<Arrete> getArretes_entrantes() {
		return arretes_entrantes;
	}
	
	public boolean addArreteEntrante(Arrete a){
		return this.arretes_entrantes.add(a);
	}
	
	public boolean addArreteSortante(Arrete a){
		return this.arretes_sortantes.add(a);
	}
	
	public boolean isArretesEntrantesEmpty(){
		return this.arretes_entrantes.isEmpty();
	}
	
	public boolean isArretesSortantesEmpty(){
		return this.arretes_sortantes.isEmpty();
	}

	public int getId() {
		return id;
	}
	
}
