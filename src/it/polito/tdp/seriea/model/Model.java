package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	SerieADAO dao;
	List<Season> seasons;
	List<Team> teams;
	List<DefaultWeightedEdge> bestAssoluto = new ArrayList<DefaultWeightedEdge>();
	
	WeightedGraph<Team, DefaultWeightedEdge> graph;
	
	Season stagione;
	
	public Model(){
		dao = new SerieADAO();
	}
	
	public List<Season> getSeasons(){
		
		if(seasons == null)
			this.seasons = dao.listSeasons();
		
		return this.seasons;
		
	}
	
	public List<Team> getTeams(){
		if(teams==null)
			this.teams=dao.listTeams();
		return this.teams;
	}

	public String doCarica(Season stagione) {
		
		this.creaGrafo(stagione);
		
		List<TeamAndPoints> classifica = new ArrayList <TeamAndPoints>();
		
		String ris = "";
		
		for(Team t : graph.vertexSet()){
			TeamAndPoints tp = new TeamAndPoints(t);
			for(DefaultWeightedEdge e : graph.edgesOf(t))
				if(graph.getEdgeSource(e).equals(t))
					if(graph.getEdgeWeight(e) == 1){
						tp.addPoints(3);
					}else if (graph.getEdgeWeight(e) == 0)
						tp.addPoints(1);
			classifica.add(tp);
		}
		
		Collections.sort(classifica);
		
		System.out.println(classifica);
		
		for(TeamAndPoints tp : classifica)
			ris += tp.toString();
		
		return ris.trim();
	}

	private void creaGrafo(Season stagione) {

		this.graph = new DirectedWeightedMultigraph<Team, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.stagione = stagione;
		
		Map<String,Team> mapTeams = new HashMap<String,Team>();
		
		for(Team t : this.getTeams())
			mapTeams.put(t.getTeam(), t);
		
		Graphs.addAllVertices(graph, dao.getTeamsForSeason(stagione,mapTeams));
		
		System.out.println(graph);
		
		for(Match m : dao.listMatchesForSeason(stagione,mapTeams)){
			DefaultWeightedEdge e;
			if(m.getFtr().equals("H")){
				e = graph.addEdge(m.getHomeTeam(), m.getAwayTeam());
				graph.setEdgeWeight(e, 1);
				e= graph.addEdge(m.getAwayTeam(), m.getHomeTeam());
				graph.setEdgeWeight(e, -1);
			}
			else if (m.getFtr().equals("D")){
				e = graph.addEdge(m.getHomeTeam(), m.getAwayTeam());
				graph.setEdgeWeight(e, 0);
				e= graph.addEdge(m.getAwayTeam(), m.getHomeTeam());
				graph.setEdgeWeight(e, 0);
			}
			else if (m.getFtr().equals("A")){
				e = graph.addEdge(m.getHomeTeam(), m.getAwayTeam());
				graph.setEdgeWeight(e, -1);
				e= graph.addEdge(m.getAwayTeam(), m.getHomeTeam());
				graph.setEdgeWeight(e, 1);
			}
		}
		
		System.out.println(graph);
		
	}

	public List<Team> getTeamsForSeason(Season stagione) {
		
		Map<String,Team> mapTeams = new HashMap<String,Team>();
		
		for(Team t : this.getTeams())
			mapTeams.put(t.getTeam(), t);
		
		return dao.getTeamsForSeason(stagione,mapTeams);
	}

	public String trovaCammino(Season stagione) {
		
		if(this.graph==null || !this.stagione.equals(stagione)){
			this.creaGrafo(stagione);
		}
		
		List <DefaultWeightedEdge> parziale = new ArrayList<DefaultWeightedEdge>();
		//List <DefaultWeightedEdge> best = new ArrayList<DefaultWeightedEdge>();
		
		for(Team teamp : this.getTeamsForSeason(stagione))
			recursive(parziale,/*best, */teamp);
		
		//System.out.println(best);
		
		System.out.println(bestAssoluto);
		
		String ris = "";
		
		for(DefaultWeightedEdge e : bestAssoluto)
			ris += graph.getEdgeSource(e) + "-" + graph.getEdgeTarget(e) + "\n";
		
		return ris.trim();
		
	}

	private void recursive(List<DefaultWeightedEdge> parziale,/* List<DefaultWeightedEdge> best, */Team team) {
		/*
		if(parziale.size() > best.size()){
			best.clear();
			best.addAll(parziale);
		}*/
		
		if(parziale.size() > bestAssoluto.size()){
			bestAssoluto.clear();
			bestAssoluto.addAll(parziale);
		}
		
			for(DefaultWeightedEdge e : graph.edgesOf(team))
				if(graph.getEdgeWeight(e) == 1 && team.equals(graph.getEdgeSource(e))){
					team = graph.getEdgeTarget(e);
					parziale.add(e);
					graph.setEdgeWeight(e, 0);
					recursive(parziale,/*best,*/team);
					graph.setEdgeWeight(e, 1);
					parziale.remove(e);
				}
		
	}

}
